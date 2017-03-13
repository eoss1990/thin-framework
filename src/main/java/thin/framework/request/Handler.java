package thin.framework.request;

import java.lang.reflect.Method;

/**
 * Created by yangyu on 2017/2/6.
 */
public class Handler {

    /**
     * Controller类
     */
    private Class controllerClass;

    /**
     * Action个方法
     */
    private Method actionMethod;

    public Handler(Class controllerClass,Method actionMethod){
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Class getControllerClass() {
        return controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }
}
