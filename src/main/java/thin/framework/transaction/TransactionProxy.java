package thin.framework.transaction;

import thin.framework.aop.proxy.Proxy;
import thin.framework.aop.proxy.ProxyChain;
import thin.framework.helper.DatabaseHelper;
import thin.framework.transaction.annotation.Transaction;

import java.lang.reflect.Method;

/**
 * Created by yangyu on 2017/2/12.
 */
public class TransactionProxy implements Proxy{

    private static final ThreadLocal<Boolean> FLAG = new ThreadLocal<Boolean>(){
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };


    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        boolean flag = FLAG.get();
        Method method = proxyChain.getTargetMethod();
        if (!flag && method.isAnnotationPresent(Transaction.class)){
            FLAG.set(true);
            try {
                DatabaseHelper.beginTransaction();
                result = proxyChain.doProxyChain();
                DatabaseHelper.commitTransaction();
            } catch (Throwable throwable) {
                DatabaseHelper.rollbackTransaction();
                throwable.printStackTrace();
                throw throwable;
            }finally {
                FLAG.remove();
            }
        }else {
            result = proxyChain.doProxyChain();
        }
        return result;
    }
}
