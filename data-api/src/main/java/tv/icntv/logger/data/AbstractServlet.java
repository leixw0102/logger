package tv.icntv.logger.data;/*
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/11/01
 * Time: 14:12
 */
public class AbstractServlet extends HttpServlet{
    protected Logger logger = LoggerFactory.getLogger(getClass());
      protected void setJSONType(HttpServletResponse response){
          response.setCharacterEncoding("UTF-8");
          response.setContentType("application/json; charset=utf-8");
      }

}
