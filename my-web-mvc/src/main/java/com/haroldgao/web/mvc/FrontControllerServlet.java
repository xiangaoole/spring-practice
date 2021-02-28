package com.haroldgao.web.mvc;

import com.haroldgao.web.mvc.controller.Controller;
import com.haroldgao.web.mvc.controller.PageController;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class FrontControllerServlet extends HttpServlet {

    /**
     * 请求路径和 {@link Controller} 映射关系的缓存
     */
    private Map<String, Controller> controllerMapping = new HashMap<>();

    /**
     * 请求路径和 {@link HandlerMethodInfo} 映射关系的缓存
     */
    private Map<String, HandlerMethodInfo> handlerMethodInfoMapping = new HashMap<>();

    @Override
    public void init(ServletConfig servletConfig) {
        initialHandlerMethod();
    }

    /**
     * 读取所有的 RestController 的注解元信息 @Path
     * 利用 ServiceLoader 技术（Java SPI）
     */
    private void initialHandlerMethod() {
        for (Controller controller : ServiceLoader.load(Controller.class)) {
            Class<?> controllerClass = controller.getClass();
            Path pathFromClass = controllerClass.getAnnotation(Path.class);
            String requestPath = pathFromClass.value();
            Method[] publicMethods = controllerClass.getMethods();
            // 缓存 {请求路径 -> HandlerMethod} {请求路径 -> Controller}
            for (Method method : publicMethods) {
                Set<String> supportedHttpMethods = findSupportedHttpMethods(method);
                Path pathFromMethod = method.getAnnotation(Path.class);
                if (pathFromMethod != null) {
                    requestPath += pathFromMethod.value();
                }
                handlerMethodInfoMapping.put(requestPath,
                        new HandlerMethodInfo(requestPath, method, supportedHttpMethods));
            }
            controllerMapping.put(requestPath, controller);
            log(requestPath + controller);
        }
    }

    /**
     * 返回方法中标记的所支持的 HttpMethod 集合
     *
     * @param method
     * @return
     */
    private Set<String> findSupportedHttpMethods(Method method) {
        Set<String> supportedHttpMethods = new LinkedHashSet<>();
        for (Annotation annotationFromMethod : method.getAnnotations()) {
            HttpMethod m = annotationFromMethod.annotationType().getAnnotation(HttpMethod.class);
            if (m != null) {
                supportedHttpMethods.add(m.value());
            }
        }

        if (supportedHttpMethods.isEmpty()) {
            supportedHttpMethods.addAll(Arrays.asList(HttpMethod.GET, HttpMethod.POST,
                    HttpMethod.PUT, HttpMethod.OPTIONS, HttpMethod.DELETE, HttpMethod.HEAD));
        }

        return supportedHttpMethods;
    }

    /**
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestPath = req.getRequestURI();
        String contextPath = req.getContextPath();
        String requestMappingPath = contextPath + requestPath;
        log(requestPath + " + " + contextPath + " => " + requestMappingPath);

        Controller controller = controllerMapping.get(requestMappingPath);

        if (controller != null) {
            HandlerMethodInfo handlerMethodInfo = handlerMethodInfoMapping.get(requestMappingPath);
            if (handlerMethodInfo != null) {
                String httpMethod = req.getMethod();
                if (!handlerMethodInfo.getSupportedHttpMethods().contains(httpMethod)) {
                    resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    return;
                }

                try {
                    if (controller instanceof PageController) {
                        PageController pageController = (PageController) controller;
                        String viewPath = pageController.execute(req, resp);
                        ServletContext servletContext = req.getServletContext();
                        if (!viewPath.startsWith("/")) {
                            viewPath = "/" + viewPath;
                        }
                        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(viewPath);
                        requestDispatcher.forward(req, resp);
                        return;
                    }
                } catch (Throwable t) {
                    throw new ServletException(t.getCause());
                }

            }
        }
    }

    public void log(String msg) {
        System.out.println("=> " + msg);
    }

}
