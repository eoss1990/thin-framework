package thin.framework.helper;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 每个线程独立的ServletHelper，可以调用Servlet API并且与ServletAPI解耦
 * Created by yangyu on 2017/2/15.
 */
public final class ServletHelper {

    /**
     * 每个线程都独有的ServletHelper
     */
    private static final ThreadLocal<ServletHelper> SERVLET_HELPER_HOLDER = new ThreadLocal<ServletHelper>();

    private HttpServletRequest request;

    private HttpServletResponse response;

    private ServletHelper(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    /**
     * 初始化
     *
     * @param request
     * @param response
     */
    public static void init(HttpServletRequest request, HttpServletResponse response) {
        SERVLET_HELPER_HOLDER.set(new ServletHelper(request, response));
    }

    /**
     * 销毁
     */
    public static void destroy() {
        SERVLET_HELPER_HOLDER.remove();
    }

    /**
     * 获取request
     *
     * @return
     */
    private static HttpServletRequest getRequest() {
        return SERVLET_HELPER_HOLDER.get().request;
    }

    /**
     * 获取response
     *
     * @return
     */
    private static HttpServletResponse getResponse() {
        return SERVLET_HELPER_HOLDER.get().response;
    }

    private static HttpSession getSession() {
        return getRequest().getSession();
    }

    private static ServletContext getServletContext() {
        return getRequest().getServletContext();
    }

    /**
     * 将属性放入request中
     *
     * @param key
     * @param value
     */
    public static void setRequestAttribute(String key, Object value) {
        getRequest().setAttribute(key, value);
    }

    /**
     * 从request中获取属性
     *
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getRequsetAttribute(String key) {
        return (T) getRequest().getAttribute(key);
    }

    /**
     * 从request中移出属性
     *
     * @param key
     */
    public static void removeRequestAttribute(String key) {
        getRequest().removeAttribute(key);
    }

    /**
     * 重定向
     *
     * @param location
     * @throws IOException
     */
    public static void sendRedirect(String location) throws IOException {
        getResponse().sendRedirect(getRequest().getContextPath() + location);
    }

    /**
     * 转发
     *
     * @param location
     * @throws ServletException
     * @throws IOException
     */
    public static void forward(String location) throws ServletException, IOException {
        getRequest().getRequestDispatcher(getRequest().getContextPath() + location).forward(getRequest(), getResponse());
    }

    /**
     * 将属性放入session中
     * @param key
     * @param value
     */
    public static void setSessionAttribute(String key, Object value) {
        getSession().setAttribute(key, value);
    }

    /**
     * 获取session中的属性
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getSessionAttribute(String key) {
        return (T) getSession().getAttribute(key);
    }

    /**
     * 移出session中的属性
     * @param key
     */
    public static void removeSessionAttribute(String key) {
        getSession().removeAttribute(key);
    }

    /**
     * 使session失效
     */
    public static void invalidateSession(){
        getSession().invalidate();
    }
}
