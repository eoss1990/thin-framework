package thin.framework.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by yangyu on 2017/2/6.
 */
public final class ClassUtil {

    /**
     * 获取ClassLoader
     *
     * @return
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载类
     *
     * @param className
     * @param isInitialized
     * @return
     * @throws ClassNotFoundException
     */
    public static Class loadClass(String className, boolean isInitialized) throws ClassNotFoundException {
        return Class.forName(className, isInitialized, getClassLoader());
    }

    /**
     * 获取指定包名下的所有类
     *
     * @param packageName
     * @return
     * @throws IOException
     */
    public static Set<Class> getClassSet(String packageName) throws IOException, ClassNotFoundException {
        Set<Class> classSet = new HashSet<Class>();
        Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            if (url != null) {
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String packagePath = url.getPath().replaceAll("%20", "");
                    addClass(classSet, packagePath, packageName);
                } else if ("jar".equals(protocol)) {
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    if (jarURLConnection != null) {
                        JarFile jarFile = jarURLConnection.getJarFile();
                        if (jarFile != null) {
                            Enumeration<JarEntry> jarEntries = jarFile.entries();
                            while (jarEntries.hasMoreElements()) {
                                JarEntry jarEntry = jarEntries.nextElement();
                                String jarEntryName = jarEntry.getName();
                                if (jarEntryName != null && jarEntryName.endsWith(".class")) {
                                    String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                                    doAddClass(classSet, className);
                                }
                            }
                        }
                    }
                }
            }
        }
        return classSet;
    }

    private static void addClass(Set<Class> classSet, String packagePath, String packageName) throws ClassNotFoundException {
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return (pathname.isFile() && pathname.getName().endsWith(".class")) || pathname.isDirectory();
            }
        });

        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                //如果是文件
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (StringUtils.isNotEmpty(packageName))
                    className = packageName + "." + className;
                doAddClass(classSet, className);
            } else {
                //如果是目录,则解析出子目录并且递归
                String subPackagePath = fileName;
                if (StringUtils.isNotEmpty(packageName))
                    subPackagePath = packagePath + "/" + subPackagePath;
                String subPackageName = fileName;
                if (StringUtils.isNotEmpty(packageName))
                    subPackageName = packageName + "." + subPackageName;
                addClass(classSet,subPackagePath,subPackageName);
            }
        }
    }

    private static void doAddClass(Set<Class> classSet, String className) throws ClassNotFoundException {
        Class cls = loadClass(className,false);
        classSet.add(cls);
    }

}
