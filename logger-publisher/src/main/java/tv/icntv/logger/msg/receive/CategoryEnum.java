package tv.icntv.logger.msg.receive;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/09/25
 * Time: 10:56
 */
public enum CategoryEnum {
//    private static Map<String,String> maps = Maps.newHashMap();

    REALTIME {
        @Override
        public String getKafkaTopic() {
            return maps.get(name());  //To change body of implemented methods use File | Settings | File Templates.
        }
    },NOREALTIME {
        @Override
        public String getKafkaTopic() {
            return maps.get(name());  //To change body of implemented methods use File | Settings | File Templates.
        }
    },DEFAULT {
        @Override
        public String getKafkaTopic() {
            return maps.get(name());  //To change body of implemented methods use File | Settings | File Templates.
        }
    };
    private static Map<String,String> maps = Maps.newHashMap();
    static {
        maps.put(DEFAULT.toString(),"icntv.default");
        maps.put(NOREALTIME.toString(),"icntv.no.real.time");
        maps.put(REALTIME.toString(),"icntv.real.time");
    }
    public abstract String getKafkaTopic();
}
