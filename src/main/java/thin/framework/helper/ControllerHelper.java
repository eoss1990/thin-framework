package thin.framework.helper;

import org.apache.commons.lang3.ArrayUtils;
import thin.framework.annotation.Action;
import thin.framework.request.Handler;
import thin.framework.request.Request;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by yangyu on 2017/2/6.
 */
public final class ControllerHelper {

    private static final Map<Request,Handler> ACTION_MAP = new HashMap<Request, Handler>();

    public static void init(){
        System.out.println("加载ControllerHelper...");
        //获取所有controller
        Set<Class> controllerClassSet = ClassHelper.getControllerClassSet();
        //遍历controller
        if (!controllerClassSet.isEmpty()){
            for (Class cls : controllerClassSet){
                //获取所有方法
                Method[] methods = cls.getMethods();
                if (ArrayUtils.isNotEmpty(methods)){
                    for (Method method : methods){
                        if (method.isAnnotationPresent(Action.class)){
                            Action action = method.getAnnotation(Action.class);
                            String mapping = action.value();
                            //验证URL映射规则
                            if (mapping.matches("\\w+:/\\w*")){
                                String[] array = mapping.split(":");
                                if (ArrayUtils.isNotEmpty(array)&&array.length==2){
                                    String requestMethod=array[0];
                                    String requestPath=array[1];
                                    Request request = new Request(requestMethod,requestPath);
                                    Handler handler = new Handler(cls,method);
                                    ACTION_MAP.put(request,handler);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public static Handler getHandler(String requestMethod,String requestPath){
        Request request = new Request(requestMethod,requestPath);
        return ACTION_MAP.get(request);
    }
}
