package thin.framework.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * Created by yangyu on 2017/1/25.
 */
public final class PropsUtil {

    public static Properties loadProps(String path){
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(PathUtil.getClassPath()+path));
        } catch (IOException e) {
            if (e instanceof FileNotFoundException)
                try {
                    properties.load(new FileInputStream(PathUtil.getWebappPath()+path));
                } catch (IOException e1) {
                    e1.printStackTrace();
                    throw new RuntimeException(e1);
                }

        }
        return properties;
    }

    public static String getString(Properties properties,String key){
        return (String) properties.get(key);
    }

    public static int getInt(Properties properties,String key,int defaultValue){
        Object object = properties.get(key);
        return object == null ? defaultValue:(Integer) object;
    }
}
