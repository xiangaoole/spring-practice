# Java Web Application From 0 to 100

## Versions

- v1: simple Tomcat 7.0 web: only an index.jsp
- v2: add a simple servlet page
- v3: add a simple mvc structure
- v4: make JDBC using DriverManager or JNDI

## v3 : MVC

Tomcat 7.x Documentation : http://tomcat.apache.org/tomcat-7.0-doc/index.html

Read java.util.ServiceLoader for how ServiceLoader load Service Providers.
- Service: my-web-mvc/src/main/java/com/haroldgao/web/mvc/controller/*
- Service Providers: user-web/src/main/java/com/haroldgao/projects/user/web/controller/*

## v4 : JDBC using DriverManager or JNDI

> **Requirement**: 
> Apache Derby installed in your server.

JDBC: Java DataBase Connectivity

### Use DriverManager

Read java.sql.DriverManager for how JDBC DriverManager load Drivers:
- read from dbc.drivers system property; then,
- use ServiceLoader load java.sql.Driver Service Providers

**NOTE**: Derby@10.15.2.0.jar auto provide dbc.drivers service, so no need to deploy in App; But Derby@10. 1.3.1.jar don't auto provide dbc.drivers service, so you need do it yourself.

### or Use DataSource and JNDI

Read javax.sql.DataSource for how DataSource load DataBase drivers.

JNDI: [Java Naming Directory Interface](http://tomcat.apache.org/tomcat-7.0-doc/jndi-resources-howto.html),

Naming Directory structure is analogue to:

```text
ENV -> java:comp/env
                    /bean
                         /DBConnectionManager

=      java:comp/env/bean/DBConnectionManager
```

Use javax.servlet.ServletContextListener and JNDI context lookup to keep the object unique in global context.

## v4.1 JPA

### JPA

JPA : [Java Persistence API](https://www.oracle.com/technical-resources/articles/java/jpa.html). We use hibernate-entitymanager in the maven dependency. (==> "JSR 317: Java Persistence API, Version 2.0")

We use [Hibernate ORM](https://docs.jboss.org/hibernate/orm/5.4/quickstart/html_single/#tutorial_jpa) for JPA. 

### Interpretation of Annotation

Using java.lang.reflect.* API, inject Components' field (javax.annotation.Resource) and invoke Components' method (javax.annotation.PostConstruct)

### Validation

javax.validation.Validator can validate Bean fields with javax.validation.constraints.* annotations. (==> "JSR 303: Bean Validation 1.0 Final Release Specification")

You can also add customized Constraint(liken UserValid in this project) on JavaBean. (==> "JSR 303"#Chapter 2)



## v4.2

JMX : Java Management Extensions

jconsole

Com

