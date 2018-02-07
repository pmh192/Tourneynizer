package com.tourneynizer.tourneynizer.helper;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ContextHelper {

    private static ApplicationContext context;

    synchronized
    public static ApplicationContext getContext() {
        if (context == null) {
            context = new ClassPathXmlApplicationContext("DevSpringModule.xml");
        }
        return context;
    }
}
