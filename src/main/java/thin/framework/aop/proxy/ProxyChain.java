package thin.framework.aop.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyu on 2017/2/10.
 */
public class ProxyChain {

    //目标类
    private final Class targetClass;
    //目标对象
    private final Object targetObject;
    //目标方法
    private final Method targetMethod;
    //方法代理
    private final MethodProxy methodProxy;
    //方法参数
    private final Object[] methodParams;
    //代理列表
    private List<Proxy> proxyList = new ArrayList<Proxy>();
    //代理索引
    private int proxyIndex = 0;

    public ProxyChain(Class targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParms, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodParams = methodParms;
        this.proxyList = proxyList;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public MethodProxy getMethodProxy() {
        return methodProxy;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    public List<Proxy> getProxyList() {
        return proxyList;
    }

    public Object doProxyChain() throws Throwable {
        Object methedResult;
        if (proxyIndex < proxyList.size()) {
            methedResult = proxyList.get(proxyIndex++).doProxy(this);
        } else {
            methedResult = methodProxy.invokeSuper(targetObject, methodParams);
        }
        return methedResult;
    }
}
