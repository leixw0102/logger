package tv.icntv.logger.msg.send;/*
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

import tv.icntv.logger.exception.SendExpetion;

import java.nio.charset.Charset;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/09/24
 * Time: 13:52
 */
public interface ISender {
    /**
     *
     * @param msg
     * @return
     */
    public boolean send(String msg) throws SendExpetion;
    /**
     *
     * @param msg
     * @param charset
     * @return
     */
    public boolean send(String msg,Charset charset) throws SendExpetion;

    /**
     *
     * @param msg
     * @param charset
     * @param compressed
     * @return
     */
    public boolean send(String msg,Charset charset,int compressed) throws SendExpetion;

    /**
     *
     * @param msg
     * @return
     * @throws SendExpetion
     */
    public boolean send(byte[] msg) throws SendExpetion;


    /**
     *
     * @param msg
     * @param compressed
     * @return
     */
    public boolean send(byte[] msg,int compressed) throws SendExpetion;
}
