package com.me.learn;

import com.me.learn.domain.Message;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

public class MethodHandleDemo {

    public static void main(String[] args) throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        Message message = new Message("hello");
        message.setSource("wangsenyuan");
        testPublicMethod(lookup, message);
        testGetterMethod(lookup, message);
        testGetterViaReflection(lookup, message);
    }

    private static void testGetterViaReflection(MethodHandles.Lookup lookup, Message message) throws Throwable {
        System.out.println("in test getter via reflection");
        Field field = Message.class.getDeclaredField("message");
        field.setAccessible(true);
        MethodHandle methodHandle = lookup.unreflectGetter(field);
        Object ret = methodHandle.invoke(message);
        System.out.println(ret);
    }

    private static void testGetterMethod(MethodHandles.Lookup lookup, Message message) {
        try {
            System.out.println("in test getter method");
            MethodHandle methodHandle = lookup.findGetter(Message.class, "message", String.class);
            Object ret = methodHandle.invoke(message);
            System.out.println(ret);
        } catch (final Throwable e) {
            System.err.println("direct invoke private field will not work");
        }
    }

    private static void testPublicMethod(MethodHandles.Lookup lookup, Message message) throws Throwable {
        System.out.println("in test public method");
        MethodType methodType = MethodType.methodType(String.class);
        MethodHandle methodHandle = lookup.findVirtual(Message.class, "completeMessage", methodType);
        Object ret = methodHandle.invoke(message);
        System.out.println(ret);
    }
}
