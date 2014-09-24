package tv.icntv.logger.common;/*
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


import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/09/16
 * Time: 13:44
 */
public class PropertiesLoaderUtils {
    /**
     * Load all properties from the specified class path resource
     * (in ISO-8859-1 encoding), using the default class loader.
     * <p>Merges properties if more than one resource of the same name
     * found in the class path.
     * @param resourceName the name of the class path resource
     * @return the populated Properties instance
     * @throws java.io.IOException if loading failed
     */
    public static Properties loadAllProperties(String resourceName) throws IOException {
        return loadAllProperties(resourceName, null);
    }

    /**
     * Load all properties from the specified class path resource
     * (in ISO-8859-1 encoding), using the given class loader.
     * <p>Merges properties if more than one resource of the same name
     * found in the class path.
     * @param resourceName the name of the class path resource
     * @param classLoader the ClassLoader to use for loading
     * (or {@code null} to use the default class loader)
     * @return the populated Properties instance
     * @throws java.io.IOException if loading failed
     */
    public static Properties loadAllProperties(String resourceName, ClassLoader classLoader) throws IOException {
        Preconditions.checkNotNull(resourceName,"resource null");
        ClassLoader classLoaderToUse = classLoader;
        if (classLoaderToUse == null) {
            classLoaderToUse = Thread.currentThread().getContextClassLoader();
        }
        Enumeration<URL> urls = (classLoaderToUse != null ? classLoaderToUse.getResources(resourceName) :
                ClassLoader.getSystemResources(resourceName));
        Properties props = new Properties();
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            URLConnection con = url.openConnection();

            InputStream is = con.getInputStream();
            try {
                props.load(is);
            }
            finally {
                is.close();
            }
        }
        return props;
    }

}
