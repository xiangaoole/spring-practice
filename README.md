# Java Web Application Demo

## Versions

- v1: simple Tomcat 7.0 web: only an index.jsp
- v2: add a simple servlet page
- v3: add a simple mvc structure

### v3 : MVC

Read java.util.ServiceLoader for how ServiceLoader load Service Providers.
- Service: my-web-mvc/src/main/java/com/haroldgao/web/mvc/controller/*
- Service Providers: user-web/src/main/java/com/haroldgao/projects/user/web/controller/*

### v4 : JDBC

> **Requirement**: 
> Apache Derby installed in your server.

JDBC: Java DataBase Connectivity

Read java.sql.DriverManager for how JDBC DriverManager load Drivers:
- read from dbc.drivers system property; then,
- use ServiceLoader load java.sql.Driver Service Providers

**NOTE**: Derby@10.15.2.0.jar auto provide dbc.drivers service, so no need to deploy in App; But Derby@10. 1.3.1.jar don't auto provide dbc.drivers service, so you need do it yourself.