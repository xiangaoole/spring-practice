package com.haroldgao.projects.user.web.servlet;

import com.haroldgao.log.Logger;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TestConfigServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

        Config config = ConfigProvider.getConfig(getServletContext().getClassLoader());
        StringBuilder sb = new StringBuilder();

        sb.append("<ol>");

        config.getPropertyNames().forEach(name -> {
            String configItem = name + "=" + config.getValue(name, String.class);
            sb.append("<li>").append(configItem).append("</li>");
        });

        sb.append("</ol>");

        ServletOutputStream out = resp.getOutputStream();
        out.println("<html><body>" +
                "<h1>Configs: </h1>" +
                sb.toString() +
                "</body></html>");
    }
}
