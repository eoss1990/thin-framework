package thin.framework.util;

import com.alibaba.fastjson.JSON;

/**
 * Created by yangyu on 2017/2/7.
 */
public final class JsonUtil {

    /**
     * 对象转json
     * @param obj
     * @return
     */
    public static String toJson(Object obj){
        return JSON.toJSONString(obj);
    }

    public static <T> T fromJson(String json)
    {
        return (T) JSON.parse(json);
    }
}
