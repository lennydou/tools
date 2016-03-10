package com.lendou.test;

public class HelloWorld {

    private static int count = 0;

    private String name;
    private String alias;

    public HelloWorld() {
        System.out.println("construct helloworld()");
        ++count;
    }

    public HelloWorld(String alias) {
        System.out.println("construct helloworld(alias)");
        this.alias = alias;
        ++count;
    }

    public void init() {
        System.out.println("call init method");
    }

    public void setName(String name) {
        this.name = name;
    }

    public void sayHello(String value) {
        System.out.println("hello " + value);
        System.out.println("name: " + name);
        System.out.println("alias: " + alias);
        System.out.println("Count: " + count);
    }

    public void destroy() {
        System.out.println("destroy");
    }
}
