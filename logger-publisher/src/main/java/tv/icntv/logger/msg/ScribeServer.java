package tv.icntv.logger.msg;/*
* Copyright 2014 Future TV, Inc.
*
* The contents of this file are subject to the terms
* of the Common Development and Distribution License
* (the License). You may not use this file except in
* compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.icntv.tv/licenses/LICENSE-1.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

import com.facebook.fb303.fb_status;
import com.facebook.generate.LogEntry;
import com.facebook.generate.ResultCode;
import com.facebook.generate.scribe;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.icntv.logger.msg.receive.IConnection;

import java.util.List;
import java.util.Map;

/**
* Created by leixw
 * flush num config of scribe server ,then the method  Log of scribe.Iface received .
* <p/>
* Author: leixw
* Date: 2014/09/24
* Time: 14:10
*/
public class ScribeServer implements IConnection {
    private static final Logger LOG = LoggerFactory.getLogger(ScribeServer.class);


    private String splitter="|";
    private TServer server;

    @Override
    public void start() {
        Startup startupThread = new Startup();
        startupThread.start();
    }

    private int workers;
    private int port;


    public ScribeServer(@Named("port")Integer port,@Named("workers")Integer workers){
        this.workers=workers;
        this.port=port;
    }
    class Receiver implements scribe.Iface {

        @Inject
        private IReceiverSender receiverAndSender;
        @Override
        public ResultCode Log(List<LogEntry> messages) throws TException {
            if(null == messages||messages.isEmpty()){
                return ResultCode.TRY_LATER;
            }
            //parser scribe log and receive
            boolean result = receiverAndSender.receiveAndSend(messages);
            return result?ResultCode.OK:ResultCode.TRY_LATER;
        }

        @Override
        public String getName() throws TException {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public String getVersion() throws TException {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public fb_status getStatus() throws TException {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public String getStatusDetails() throws TException {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Map<String, Long> getCounters() throws TException {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public long getCounter(String key) throws TException {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void setOption(String key, String value) throws TException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public String getOption(String key) throws TException {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Map<String, String> getOptions() throws TException {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public String getCpuProfile(int profileDurationInSec) throws TException {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public long aliveSince() throws TException {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void reinitialize() throws TException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void shutdown() throws TException {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
    private class Startup extends Thread {

        public void run() {
            try {
                TProcessor processor = new scribe.Processor(new Receiver());
                TNonblockingServerTransport transport = new TNonblockingServerSocket(port);
                THsHaServer.Args args = new THsHaServer.Args(transport);

                args.workerThreads(workers);
                args.processor(processor);
                args.transportFactory(new TFramedTransport.Factory());
                args.protocolFactory(new TBinaryProtocol.Factory());

                server = new THsHaServer(args);

                LOG.info("Starting Scribe Source on port ={},start thread size={}" , port,workers);

                server.serve();
            } catch (Exception e) {
                LOG.warn("Scribe failed", e);
            }
        }
    }

}
