package thin.framework.helper;

import thin.framework.constant.ConfigConstant;
import thin.framework.util.PropsUtil;

import java.util.Properties;

/**
 * Created by yangyu on 2017/1/25.
 */
public final class ConfigHelper{

    private static Properties CONFIG_PROPS;

    public static String getJdbcDriver(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_DRIVER);
    }

    public static String getJdbcUrl(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_URL);
    }

    public static String getJdbcUsername(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_USERNAME);
    }

    public static String getJdbcPassword(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_PASSWORD);
    }

    public static String getAppBasePackage(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.APP_BASE_PACKAGE);
    }

    public static String getAppJspPath(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.APP_JSP_PATH);
    }

    public static String getAppStaticPath(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.APP_STATIC_PATH);
    }

    public static int getAppUploadLimit(){
        return PropsUtil.getInt(CONFIG_PROPS,ConfigConstant.APP_UPLOAD_LIMIT,10);
    }
    public static void init() {
        System.out.println("加载ConfigHelper...");
        CONFIG_PROPS=PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);
    }
}
