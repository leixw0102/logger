package tv.icntv.log.common;

import com.google.common.base.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {
	public static Properties getProperties(String file){
		InputStream in=PropertiesUtils.class.getClassLoader().getResourceAsStream(file);
		Properties pro=new Properties();
		try {
			pro.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pro;
	}
	private static String defaultPro=System.getProperty("qq-config");
	public static Properties getProperties(){
        if(Strings.isNullOrEmpty(defaultPro)){
            System.out.println("......");
            return getProperties("icntvStb.properties");
        }
		return getProperties(defaultPro);
	}

}
