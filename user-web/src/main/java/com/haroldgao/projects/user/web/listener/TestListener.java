package com.haroldgao.projects.user.web.listener;

import com.haroldgao.projects.log.Logger;
import com.haroldgao.projects.user.context.ComponentContext;
import com.haroldgao.projects.user.domain.User;
import com.haroldgao.projects.user.sql.DBConnectionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Test in Web global context
 *
 * @since 4.1
 */
public class TestListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ComponentContext componentContext = ComponentContext.getInstance();
        DBConnectionManager dbConnectionManager = componentContext.getComponent("bean/DBConnectionManager");
        dbConnectionManager.getConnection();
        testDomain(dbConnectionManager.getEntityManager());
    }

    private void testDomain(EntityManager entityManager) {
        User user = new User();
        user.setName("Harold Gao");
        user.setPassword("123456");
        user.setEmail("abc@outlook.com");
        user.setPhoneNumber("10010001000");

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(user);
        transaction.commit();
        Logger.info(entityManager.find(User.class, user.getId()).toString());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
