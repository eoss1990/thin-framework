package thin.framework.aop.proxy;

/**
 * Created by yangyu on 2017/2/10.
 */
public interface Proxy {

    public Object doProxy(ProxyChain proxyChain) throws Throwable;
}
