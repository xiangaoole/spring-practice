package com.haroldgao.projects.user.management;

import com.haroldgao.projects.log.Logger;
import com.haroldgao.projects.user.domain.User;
import com.sun.jmx.mbeanserver.Introspector;
import sun.rmi.runtime.Log;

import javax.management.*;
import java.lang.management.ManagementFactory;

public class TestMBean {
    public static void main(String[] args) throws Exception {
        testAuthorMXBean();
    }

    public static void testStandardMBean() throws Exception {
        Introspector.checkCompliance(UserManager.class);
        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("com.haroldgao.projects.user.management:type=User");
        User user = new User();
        platformMBeanServer.registerMBean(new UserManager(user), objectName);
        MBeanInfo mBeanInfo = platformMBeanServer.getMBeanInfo(objectName);
        Logger.info(mBeanInfo.toString());
        while (true) {
            Thread.sleep(2000);
            Logger.info(user.toString());
        }
    }

    public static void testAuthorMXBean() throws Exception  {
        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("com.haroldgao.projects.user.management:type=Author");
        Author author = Author.getInstance();
        platformMBeanServer.registerMBean(author, objectName);
        while (true) {
            Thread.sleep(5000);
            Logger.info(author.toString());
        }
    }
}
