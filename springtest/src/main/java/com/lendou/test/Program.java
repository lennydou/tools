package com.lendou.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Program {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");

        HelloWorld obj = (HelloWorld) context.getBean("helloBean", "lendou");
        obj.sayHello("lenny dou");

        HelloWorld obj2 = (HelloWorld) context.getBean("helloBean");
        obj2.sayHello("douyunliang");
    }
}
