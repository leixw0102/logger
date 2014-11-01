package tv.icntv.logger.common.jdbc;/*
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

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import tv.icntv.logger.common.PropertiesLoaderUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/10/31
 * Time: 15:01
 */
public class Connections {

    static class ShutDownConnections extends Thread{
        @Override
        public void run() {

            if(null != connectionPool){
                connectionPool.shutdown();
            }
        }
    }

    static BoneCP connectionPool = null;
    static {

        Properties properties =null;
        try {
            properties=PropertiesLoaderUtils.loadAllProperties("jdbc.properties");
            Class.forName(properties.getProperty("jdbc.driverClass"));
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(properties.getProperty("jdbc.jdbcUrl"));
            config.setUsername(properties.getProperty("jdbc.name"));
            config.setPassword(properties.getProperty("jdbc.pwd"));
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config);
            Connection test=  connectionPool.getConnection();
            test.close();

            Runtime.getRuntime().addShutdownHook(new ShutDownConnections());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }
    public static void returnConnection(Connection connection){
        if(null != connection){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}
