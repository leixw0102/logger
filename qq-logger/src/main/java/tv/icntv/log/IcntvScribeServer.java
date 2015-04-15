package tv.icntv.log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.facebook.fb303.fb_status;
import scribe.thrift.LogEntry;
import scribe.thrift.ResultCode;
import scribe.thrift.scribe;


public class IcntvScribeServer {
    private static final Logger LOG = LoggerFactory.getLogger(IcntvScribeServer.class);

    public static final String SCRIBE_CATEGORY = "category";

    private static final int DEFAULT_WORKERS = 10;
    private boolean isTest=false;

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }

    private String splitter="\r\n";
    private TServer server;
    private int port = 14630;
    private int workers = DEFAULT_WORKERS;


    private class Startup extends Thread {

        public void run() {
            try {
                TProcessor processor = new scribe.AsyncProcessor(new Receiver1() );
                TNonblockingServerTransport transport = new TNonblockingServerSocket(port);
                THsHaServer.Args args = new THsHaServer.Args(transport);

                args.workerThreads(workers);
                args.processor(processor);
                args.transportFactory(new TFramedTransport.Factory());
                args.protocolFactory(new TCompactProtocol.Factory());
                
                server = new THsHaServer(args);
                
                LOG.info("Starting Scribe Source on port " + port);

                server.serve();
            } catch (Exception e) {
                LOG.warn("Scribe failed", e);
            }
        }
    }


    public void start() {

        Startup startupThread = new Startup();
        startupThread.start();

    }
    FileUtils fileUtils=null;

    FileChannel channel = null;
    public IcntvScribeServer(int port,int worker){
    	this(port, worker,false);
    }

    public IcntvScribeServer( int port, int workers,boolean test) {

        this.port = port;
        this.workers = workers;
        isTest = test;
        fileUtils=new FileUtils();
    }

    public IcntvScribeServer(){
    	this(14630,10,false);
    }

    class Receiver1 implements scribe.AsyncIface{

        @Override
        public void Log(List<LogEntry> messages, AsyncMethodCallback resultHandler) throws TException {
            if (messages != null) {
                List<String> msgs=Lists.transform(messages, new Function<LogEntry, String>() {

                    @Override
                    public String apply(LogEntry arg0) {
                        try {
                            return URLDecoder.decode(arg0.getMessage(), "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            return arg0.getMessage();

                        }
                    }
                });
                String message=Joiner.on(splitter).join(msgs);
                if(isTest()){
                    LOG.info(message+splitter);
                }else{
                    fileUtils.write(message+splitter);
                    resultHandler.onComplete(ResultCode.OK);
                }
            }else {
                LOG.error(".error");
                resultHandler.onError(new NullPointerException(ResultCode.TRY_LATER+""));
            }
        }

        @Override
        public void getName(AsyncMethodCallback resultHandler) throws TException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void getVersion(AsyncMethodCallback resultHandler) throws TException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void getStatus(AsyncMethodCallback resultHandler) throws TException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void getStatusDetails(AsyncMethodCallback resultHandler) throws TException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void getCounters(AsyncMethodCallback resultHandler) throws TException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void getCounter(String key, AsyncMethodCallback resultHandler) throws TException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void setOption(String key, String value, AsyncMethodCallback resultHandler) throws TException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void getOption(String key, AsyncMethodCallback resultHandler) throws TException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void getOptions(AsyncMethodCallback resultHandler) throws TException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void getCpuProfile(int profileDurationInSec, AsyncMethodCallback resultHandler) throws TException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void aliveSince(AsyncMethodCallback resultHandler) throws TException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void reinitialize(AsyncMethodCallback resultHandler) throws TException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void shutdown(AsyncMethodCallback resultHandler) throws TException {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    class Receiver implements scribe.Iface {
    	

        @Override
        public ResultCode Log(
                List<LogEntry> messages)
                throws TException {
            if (messages != null) {
            	List<String> msgs=Lists.transform(messages, new Function<LogEntry, String>() {

					@Override
					public String apply(LogEntry arg0) {
                       return arg0.getMessage();
					}
				});
                if(null == msgs || msgs.isEmpty()){
                    return ResultCode.OK;
                }
            	String message=Joiner.on(splitter).join(msgs);
//            	System.out.println("receive msg = "+message);
//            	try {
//					Thread.sleep(500L);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//            	LOG.info("received msg = {}",message);
            	fileUtils.write(message+splitter);

                return ResultCode.OK;
            }
            return ResultCode.TRY_LATER;
        }

        @Override
        public String getName() throws TException {
            // TODO Auto-generated method stub
            return "icntv";
        }

        @Override
        public String getVersion() throws TException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public fb_status getStatus() throws TException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getStatusDetails() throws TException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Map<String, Long> getCounters() throws TException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getCounter(String key) throws TException {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void setOption(String key, String value) throws TException {
            // TODO Auto-generated method stub

        }

        @Override
        public String getOption(String key) throws TException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Map<String, String> getOptions() throws TException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getCpuProfile(int profileDurationInSec) throws TException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long aliveSince() throws TException {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void reinitialize() throws TException {
            // TODO Auto-generated method stub

        }

        @Override
        public void shutdown() throws TException {
            // TODO Auto-generated method stub

        }

    }

    public static void main(String[] args) {

        if(args.length == 2){
            new IcntvScribeServer(Integer.parseInt(args[0]),Integer.parseInt(args[1])).start();
        }else if(args.length == 3){
            new IcntvScribeServer(Integer.parseInt(args[0]),Integer.parseInt(args[1]),Boolean.parseBoolean(args[2])).start();
        }else{
            new IcntvScribeServer().start();
        }
    }
}
