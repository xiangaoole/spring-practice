package com.haroldgao.projects.user.web.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HelloWorldServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter pw = resp.getWriter();

        pw.println("<html><body>");
        pw.println("<h1>Hi</h1>");
        pw.println("<p>Hello, this is a simple servlet page.</p>");
        pw.println("</body></html>");

        pw.close();
    }
}
