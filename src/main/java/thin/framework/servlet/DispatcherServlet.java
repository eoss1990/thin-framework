package thin.framework.servlet;

import org.apache.commons.lang3.StringUtils;
import thin.framework.helper.*;
import thin.framework.request.Handler;
import thin.framework.request.Param;
import thin.framework.response.Data;
import thin.framework.response.View;
import thin.framework.util.JsonUtil;
import thin.framework.util.PathUtil;
import thin.framework.util.ReflectionUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by yangyu on 2017/2/7.
 */
@WebServlet(urlPatterns = "/thin/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        //获取servletContext对象
        ServletContext servletContext = config.getServletContext();
        PathUtil.setWebappPath(servletContext.getRealPath("/"));

        //初始化
        HelperLoader.init(servletContext);

        //注册处理jsp的servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");

        //注册处理静态资源的servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppStaticPath() + "*");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //初始化ServletHelper
        ServletHelper.init(req, resp);

        try {
            String requestMethod = req.getMethod().toLowerCase();
            String requestPath = req.getPathInfo();

            if (requestPath.equals("/favicon.ico"))
                return;

            //获取handler处理器
            Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
            if (handler != null) {
                //获取controller实例
                Class controllerClass = handler.getControllerClass();
                Object controllerBean = BeanHelper.getBean(controllerClass);
                //创建请求参数对象
                Param param;
                if (UploadHelper.isMultipart(req))
                    param = UploadHelper.createParam(req);
                else
                    param = RequestHelper.createParam(req);

                Method method = handler.getActionMethod();

                Object result;
                //判断param是否为空，空就调用不带参数的方法，不为空就调用带参数的方法
                if (param.isEmpty())
                    result = ReflectionUtil.invokeMethod(controllerBean, method);
                else
                    result = ReflectionUtil.invokeMethod(controllerBean, method, param);

                //处理返回值
                if (result instanceof View) {
                    handleViewResult((View) result, req, resp);
                } else if (result instanceof Data) {
                    handleDataResult((Data) result, resp);
                }
            }
        } finally {
            ServletHelper.destroy();
        }
    }

    /**
     * 处理View类型的返回值
     * @param view
     * @param req
     * @param resp
     * @throws IOException
     * @throws ServletException
     */
    private void handleViewResult(View view, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String path = view.getPath();
        if (StringUtils.isNotEmpty(path)) {
            if (path.startsWith("/")) {
                resp.sendRedirect(req.getContextPath() + path);
            } else {
                Map<String, Object> model = view.getModel();
                if (model != null) {
                    for (Map.Entry<String, Object> entry : model.entrySet()) {
                        req.setAttribute(entry.getKey(), entry.getValue());
                    }
                }
                req.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(req, resp);
            }
        }
    }

    /**
     * 处理Data类型的返回值
     * @param data
     * @param resp
     * @throws IOException
     */
    private void handleDataResult(Data data, HttpServletResponse resp) throws IOException {
        Object model = data.getModel();
        if (model != null) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter printWriter = resp.getWriter();
            String json = JsonUtil.toJson(model);
            printWriter.write(json);
            printWriter.flush();
            printWriter.close();
        }

    }
}
