package com.qy.javassist.proxy;

import com.qy.javassist.JavassistWebserviceGenerator;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * https://www.cnblogs.com/coshaho/p/5105545.html
 */
public class JavassistProxyExample3 {

	@Test
	public void testDynamicInterface() throws Exception {
		
		JavassistWebserviceGenerator javassistLearn = new JavassistWebserviceGenerator();
		
		Class<?> webservice = javassistLearn.createDynamicClazz();
		
		// Javassist Proxy
		Object obj = JavassistProxy.getProxy(webservice);
				
		Class<?> clazz = obj.getClass();
		
		System.err.println("=========Type Annotations======================");
		for (Annotation element : clazz.getAnnotations()) {
			System.out.println(element.toString());
		}
		
		System.err.println("=========Fields======================");
		for (Field element : clazz.getDeclaredFields()) {
			System.out.println(element.getName());
			for (Annotation anno : element.getAnnotations()) {
				System.out.println(anno.toString());
			}
		}
		System.err.println("=========Methods======================");
		for (Method element : clazz.getDeclaredMethods()) {
			System.out.println(element.getName());
			for (Annotation anno : element.getAnnotations()) {
				System.out.println(anno.toString());
			}
		}
		System.err.println("=========sayHello======================");
		
		Method mt =  obj.getClass().getDeclaredMethod("sayHello", String.class);
		mt.invoke(obj, "xxx");
		
	}
 
}