package com.haroldgao.projects.user.web.servlet;

import com.haroldgao.projects.log.Logger;
import com.haroldgao.projects.user.context.ComponentContext;
import com.haroldgao.projects.user.domain.User;
import com.haroldgao.projects.user.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

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

        UserService userService = ComponentContext.getInstance().getComponent("bean/UserService");
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        StringBuilder sb = new StringBuilder();
        sb.append("<ul>");
        violations.forEach(v -> {
            sb.append("<li>").append(v.getMessage()).append("</li>");
        });
        sb.append("</ul>");
        if (violations.size() > 0) {
            out.println("<html><body>" +
                    "<h1>Format Error!</h1>" +
                    sb.toString() +
                    "</body></html>");
            return;
        }


        if (userService.register(user)) {
            out.println("<html><body><h1>Hello</h1><p>" +
                    "Welcome to Spring, " + user.getName() +
                    ".</p></body></html>");
            Logger.info(user.toString());
        } else {
            out.println("<html><body><p>" +
                    "Register failed!" +
                    "</p></body></html>");

            out.flush();
            Logger.warning(user.toString());
        }
    }
}
