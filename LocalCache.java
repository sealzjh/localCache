import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service("localCache")
public class LocalCache {

    private final Integer defaultTimeOutDay = 30;

    private static Map<String, StringNode> strData = new ConcurrentHashMap<String, StringNode>();
    private static Map<String, HashNode> hashData = new ConcurrentHashMap<String, HashNode>();

    public Map<String, StringNode> getStrData() {
        return strData;
    }

    public Map<String, HashNode> getHashData() {
        return hashData;
    }

    public String get(String key) {
        String value = null;
        if(strData.containsKey(key)) {
            Integer nowTime = (int) (System.currentTimeMillis() / 1000);
            StringNode rs = strData.get(key);

            if(rs.getCreateTime() + rs.getTimeOut() > nowTime) {
                rs.setLastUseTime(nowTime);
                value = rs.getValue();
            } else {
                strData.remove(key);
            }
        }

        Random random = new Random();
        int s = random.nextInt(86400);
        if (s == 1) {
            gc();
        }

        return value;
    }

    public Integer del(String key) {
        if(strData.containsKey(key)) {
            strData.remove(key);
        }

        return 1;
    }

    public Integer set(String key, String value) {
        set(key, value, defaultTimeOut());
        return 1;
    }

    public Integer set(String key, String value, Integer timeOut) {
        Integer createTime = (int) (System.currentTimeMillis() / 1000);
        strData.put(key, new StringNode(value, createTime, timeOut));

        return 1;
    }

    public Integer incr(String key) {
        Integer value = 0;

        if(strData.containsKey(key)) {
            value = Integer.valueOf(strData.get(key).getValue());
        }

        value++;
        set(key, String.valueOf(value));

        return 1;
    }

    public Integer hset(String key, String fieldKey, String value) {
        hset(key, fieldKey, value, defaultTimeOut());
        return 1;
    }

    public Integer hset(String key, String fieldKey, String value, Integer timeOut) {
        Integer createTime = (int) (System.currentTimeMillis() / 1000);
        if(hashData.containsKey(key)) {
            HashNode rs = hashData.get(key);
            Map<String, String> rsValue = rs.getValue();
            rs.setCreateTime(createTime);
            rs.setLastUseTime(createTime);
            rs.setTimeOut(timeOut);
            rsValue.put(fieldKey, value);
        } else {
            Map<String, String> rsValue = new HashMap<String, String>();
            rsValue.put(fieldKey, value);
            hashData.put(key, new HashNode(rsValue, createTime, timeOut));
        }

        return 1;
    }

    public String hget(String key, String fieldKey) {
        String value = null;
        if(hashData.containsKey(key)) {
            HashNode rs = hashData.get(key);

            Integer nowTime = (int) (System.currentTimeMillis() / 1000);
            if(rs.getCreateTime() + rs.getTimeOut() > nowTime) {
                rs.setLastUseTime(nowTime);
                value = rs.getValue().get(fieldKey);
            } else {
                hashData.remove(key);
            }
        }

        return value;
    }

    public Map<String, String> hgetAll(String key) {
        Map<String, String> value = null;
        if(hashData.containsKey(key)) {
            HashNode rs = hashData.get(key);

            Integer nowTime = (int) (System.currentTimeMillis() / 1000);
            if(rs.getCreateTime() + rs.getTimeOut() > nowTime) {
                rs.setLastUseTime(nowTime);
                value = rs.getValue();
            } else {
                hashData.remove(key);
            }
        }

        return value;
    }

    public Integer hdelAll(String key) {
        if(hashData.containsKey(key)) {
            hashData.remove(key);
        }

        return 1;
    }

    public Integer hdel(String key, String fieldKey) {
        if(hashData.containsKey(key)) {
            hashData.get(key).getValue().remove(fieldKey);
        }

        return 1;
    }

    private Integer defaultTimeOut() {
        return defaultTimeOutDay * 24 * 60 * 60;
    }

    public void gc() {
        Integer nowTime = (int) (System.currentTimeMillis() / 1000);

        if (strData.size() > 0) {
            Iterator entries = strData.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                StringNode value = (StringNode) entry.getValue();

                if(value.getCreateTime() + value.getTimeOut() < nowTime) {
                    entries.remove();
                }
            }
        }

        if (hashData.size() > 0) {
            Iterator entries = hashData.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                HashNode value = (HashNode) entry.getValue();

                if(value.getCreateTime() + value.getTimeOut() < nowTime) {
                    entries.remove();
                }
            }
        }

    }

}
