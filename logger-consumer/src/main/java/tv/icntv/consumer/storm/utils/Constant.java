package tv.icntv.consumer.storm.utils;/*
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

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/10/16
 * Time: 15:50
 */
public interface Constant {
    public String SOURCE_PATH="icntv.stb.log.path";
    public String SOURCE_FILE_NAME="icntv.stb.log.file.format";
    public String ROLL_INTERVAL="icntv.stb.log.rollInterval";
    public String SOURCE_TYPE="icntv.stb.decompress";

    public String COMPRESSED_PATH="icntv.stb.compress.log.path";
    public String COMPRESSED_SUFFIX="icntv.stb.compress.log.suffix";
    public String COMPRESS_TYPE="icntv.stb.compress";

    public String READ_CHARSET="icntv.stb.charset";
    public String HDFS_PATH="icntv.stb.hdfs.url";
}
