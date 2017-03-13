package thin.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by yangyu on 2017/2/6.
 */
public final class ReflectionUtil {

    /**
     * 创建实例
     *
     * @param cls
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static Object newInstance(Class cls) throws IllegalAccessException, InstantiationException {
        return cls.newInstance();
    }

    public static Object invokeMethod(Object object, Method method, Object... args){
        method.setAccessible(true);
        try {
            return method.invoke(object,args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void setField(Object object, Field field,Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(object,value);
    }


}
