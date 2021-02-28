package com.haroldgao.projects.user.web.controller;

import com.haroldgao.web.mvc.controller.PageController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/hello")
public class HelloWorldController implements PageController {

    @Override
    @GET
    @POST
    @Path("/world") // /hello/world -> HelloWorldController
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Throwable {
        return "welcome.jsp";
    }
}
