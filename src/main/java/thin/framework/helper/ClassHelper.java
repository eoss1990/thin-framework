package thin.framework.helper;

import thin.framework.annotation.Controller;
import thin.framework.annotation.Service;
import thin.framework.util.ClassUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yangyu on 2017/2/6.
 */
public final class ClassHelper{

    /**
     * 定义类集合，用于存放所有加载的类
     */
    private static Set<Class> CLASS_SET;

    public static void init() {
        System.out.println("加载ClassHelper...");
        String basePackage = ConfigHelper.getAppBasePackage();
        try {
            CLASS_SET = ClassUtil.getClassSet(basePackage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取应用包名下所有类
     * @return
     */
    public static Set<Class> getClassSet(){
        return CLASS_SET;
    }

    /**
     * 获取应用包名下所有service类
     * @return
     */
    public static Set<Class> getServiceClassSet(){
        return getClassSetByAnnotation(Service.class);
    }

    /**
     * 获取应用包名下所有controller类
     * @return
     */
    public static Set<Class> getControllerClassSet(){
        return getClassSetByAnnotation(Controller.class);
    }

    /**
     * 获取所有Bean类（controller和service类）
     * @return
     */
    public static Set<Class> getBeanClassSet(){
        Set<Class> classSet = new HashSet<Class>();
        classSet.addAll(getServiceClassSet());
        classSet.addAll(getControllerClassSet());
        return classSet;
    }

    /**
     * 获取应用包下父类（或接口）的所有子类
     * @param superClass
     * @return
     */
    public static Set<Class> getClassSetBySuper(Class superClass){
        Set<Class> classSet = new HashSet<Class>();
        for (Class cls : CLASS_SET){
            if (superClass.isAssignableFrom(cls)&&!superClass.equals(cls)){
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取应用包下带某注解的所有类
     * @param annotationClass
     * @return
     */
    public static Set<Class> getClassSetByAnnotation(Class<? extends Annotation> annotationClass){
        Set<Class> classSet = new HashSet<Class>();
        for (Class cls : CLASS_SET){
            if (cls.isAnnotationPresent(annotationClass)){
                classSet.add(cls);
            }
        }
        return classSet;
    }


}
