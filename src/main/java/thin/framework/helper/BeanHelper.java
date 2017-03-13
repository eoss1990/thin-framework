package thin.framework.helper;

import thin.framework.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by yangyu on 2017/2/6.
 */
public final class BeanHelper{

    /**
     * 存放bean实例
     */
    private static final Map<Class,Object> BEAN_MAP = new HashMap();

    public static void init() {
        System.out.println("加载BeanHelper...");
        Set<Class> beanClassSet = ClassHelper.getBeanClassSet();
        for (Class beanClass : beanClassSet){
            try {
                Object object = ReflectionUtil.newInstance(beanClass);
                BEAN_MAP.put(beanClass,object);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Map<Class,Object> getBeanMap(){
        return BEAN_MAP;
    }

    public static <T> T getBean(Class<T> cls){
        if (!BEAN_MAP.containsKey(cls))
            throw new RuntimeException("can not get bean by class:"+ cls);
        return (T) BEAN_MAP.get(cls);
    }

    public static void setBean(Class cls,Object object){
        BEAN_MAP.put(cls,object);
    }

}
