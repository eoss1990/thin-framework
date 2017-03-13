package thin.framework.aop.proxy;

import java.lang.reflect.Method;

/**
 * Created by yangyu on 2017/2/10.
 */
public abstract class AspectProxy implements Proxy {

    public final Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;
        Class cls = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();

        begin();
        try {
            if (intercept(cls,method,params)){
                before(cls,method,params);
                result = proxyChain.doProxyChain();
                after(cls,method,params,result);
            }else {
                result = proxyChain.doProxyChain();
            }
        } catch (Throwable e) {
            error(cls,method,params,e);
            throw e;
        }finally {
            end();
        }

        return result;
    }

    /**
     * 开始增强
     */
    public void begin(){}

    public boolean intercept(Class cls,Method method,Object[] params) throws Throwable{
        return true;
    }

    public void before(Class cls,Method method,Object[] params) throws Throwable{}

    public void after(Class cls,Method method,Object[] params,Object result) throws Throwable{}

    public void error(Class cls,Method method,Object[] params,Throwable e){}

    public void end(){};
}
