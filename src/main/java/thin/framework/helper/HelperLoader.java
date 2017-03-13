package thin.framework.helper;

import javax.servlet.ServletContext;

/**
 * Created by yangyu on 2017/2/6.
 */
public final class HelperLoader {

    public static void init(ServletContext servletContext){
        //加载properties文件
        ConfigHelper.init();

        //查找并加载应用包下的class
        ClassHelper.init();

        //将带有注解的class实例化
        BeanHelper.init();

        //数据库连接初始化
        DatabaseHelper.init();

        //生成代理类
        AopHelper.init();

        //进行依赖注入
        IocHelper.init();

        //建立Mapping映射关系
        ControllerHelper.init();

        //上传组件初始化
        UploadHelper.init(servletContext);
    }
}
