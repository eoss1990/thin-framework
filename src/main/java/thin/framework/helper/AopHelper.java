package thin.framework.helper;

import thin.framework.aop.annotation.Aspect;
import thin.framework.aop.proxy.AspectProxy;
import thin.framework.aop.proxy.Proxy;
import thin.framework.aop.proxy.ProxyManager;
import thin.framework.transaction.TransactionProxy;
import thin.framework.transaction.annotation.Transaction;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Created by yangyu on 2017/2/10.
 */
public final class AopHelper {

    public static void init(){

        Map<Class,Set<Class>> proxyMap = createProxyMap();
        Map<Class,List<Proxy>> targetMap = createTargetMap(proxyMap);
        for (Map.Entry<Class,List<Proxy>> targetEntry : targetMap.entrySet()){
            Class targetClass = targetEntry.getKey();
            List<Proxy> proxyList = targetEntry.getValue();
            Object proxy = ProxyManager.createProxy(targetClass,proxyList);
            BeanHelper.setBean(targetClass,proxy);
        }
    }
    /**
     * 根据Aspect注解的value值（也是注解），获取所有class
     * @param aspect
     * @return
     */
    private static Set<Class> createTargetClassSet(Aspect aspect){
        Set<Class> targetClassSet = new HashSet<Class>();
        Class<? extends Annotation> annotation = aspect.value();
        if (annotation != null && !annotation.equals(Aspect.class)){
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClassSet;
    }

    /**
     * 获取所有AspectProxy子类，并且建立切面与被代理类的映射关系
     * @return
     */
    private static Map<Class,Set<Class>> createProxyMap(){
        Map<Class,Set<Class>> proxyMap = new HashMap<Class, Set<Class>>();
        //增加Aspect代理
        addAspectProxy(proxyMap);
        addTransactionProxy(proxyMap);
        return proxyMap;
    }

    /**
     * 增加Aspect代理，查找带有Aspect注解类
     * @return
     */
    private static Map<Class,Set<Class>> addAspectProxy(Map<Class,Set<Class>> proxyMap){
        Set<Class> aspectClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
        for (Class aspectClass : aspectClassSet){
            if (aspectClass.isAnnotationPresent(Aspect.class)){
                Aspect aspect = (Aspect) aspectClass.getAnnotation(Aspect.class);
                Set<Class> targetClassSet = createTargetClassSet(aspect);
                proxyMap.put(aspectClass,targetClassSet);
            }
        }
        return proxyMap;
    }

    /**
     * 增加Transaction代理，查找带有Service注解的类
     * @param proxyMap
     * @return
     */
    private static Map<Class,Set<Class>> addTransactionProxy(Map<Class,Set<Class>> proxyMap){
        Set<Class> serviceClassSet = ClassHelper.getServiceClassSet();
        proxyMap.put(TransactionProxy.class,serviceClassSet);
        return proxyMap;
    }

    /**
     * 以被代理类为key，切面类为value，反应一个被代理类可以有多个切面与之关联
     * @param proxyMap
     * @return
     * @throws Exception
     */
    private static Map<Class,List<Proxy>> createTargetMap(Map<Class,Set<Class>> proxyMap){

        Map<Class,List<Proxy>> targetMap = new HashMap<Class, List<Proxy>>();
        for (Map.Entry<Class,Set<Class>> proxyEntry : proxyMap.entrySet()){
            Class aspectClass = proxyEntry.getKey();
            Set<Class> targetClassSet = proxyEntry.getValue();
            for (Class targetClass : targetClassSet){
                Proxy proxy = null;
                try {
                    proxy = (Proxy) aspectClass.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if (targetMap.containsKey(targetClass)){
                    targetMap.get(targetClass).add(proxy);
                }else {
                    List<Proxy> proxyList = new ArrayList<Proxy>();
                    proxyList.add(proxy);
                    targetMap.put(targetClass,proxyList);
                }
            }
        }
        return targetMap;
    }
}
