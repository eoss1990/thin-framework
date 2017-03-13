package thin.framework.util;

/**
 * Created by yangyu on 2017/2/7.
 */
public final class PathUtil {

    private static String webappPath;

    private final static String classPath;

    static {
        classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    }

    public static String getWebappPath() {
        return webappPath;
    }

    public static void setWebappPath(String webappPath) {
        PathUtil.webappPath = webappPath;
    }

    public static String getClassPath() {
        return classPath;
    }
}
