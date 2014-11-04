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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/10/31
 * Time: 15:28
 */
public class JdbcUtils {
    public static String getResultForSql(String sql) {
        System.out.println(sql);
        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet set = null;
        try {
            connection = Connections.getConnection();
            pst = connection.prepareStatement(sql);
            set = pst.executeQuery();
            if (set.next()) {
                return set.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != set) {
                    set.close();
                }
                if (null != pst) {
                    pst.close();
                }
                Connections.returnConnection(connection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
