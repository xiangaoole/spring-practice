package com.haroldgao.projects.user.web.servlet;

import com.haroldgao.projects.user.domain.User;
import com.haroldgao.projects.user.repository.DatabaseUserRepository;
import com.haroldgao.projects.user.sql.DBConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        PrintWriter out = resp.getWriter();

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String phoneNumber = req.getParameter("phone_number");
        String email = req.getParameter("email");

        User user = new User();
        user.setPhoneNumber(phoneNumber);
        user.setPassword(password);
        user.setEmail(email);
        user.setName(username);

        System.out.println(user.toString());

        DatabaseUserRepository databaseUserRepository = new DatabaseUserRepository();
        if (databaseUserRepository.save(user)) {
            out.println("<html><body><h1>Hello</h1><p>" +
                    "Welcome to Spring, " + user.getName() +
                    ".</p></body></html>");
        } else {
            out.println("<html><body><p>" +
                    "Register failed!" +
                    "</p></body></html>");

            out.flush();
        }
    }
}
