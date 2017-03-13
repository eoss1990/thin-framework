package thin.framework.helper;

import org.apache.commons.lang3.ArrayUtils;
import thin.framework.annotation.Inject;
import thin.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by yangyu on 2017/2/6.
 */
public final class IocHelper {

    public static void init(){
        System.out.println("加载IocHelper...");
        Map<Class,Object> beanMap = BeanHelper.getBeanMap();
        if (!beanMap.isEmpty()){
            //从beanMap中获取所有class的实例
            for (Map.Entry<Class,Object> beanEntry : beanMap.entrySet()){
                Class beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                //获取bean所有的成员变量field
                Field[] beanFields = beanClass.getDeclaredFields();
                //遍历所有field，判断是否有@Inject注解
                if (ArrayUtils.isNotEmpty(beanFields)){
                    for (Field field : beanFields)
                    {
                        if (field.isAnnotationPresent(Inject.class)){
                            Class fieldClass = field.getType();
                            Object fieldInstance = beanMap.get(fieldClass);
                            //进行依赖注入
                            if (fieldInstance!=null){
                                try {
                                    ReflectionUtil.setField(beanInstance,field,fieldInstance);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
