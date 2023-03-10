/*
 * Copyright (c) 2017, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.qy.javassist.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JavassistProxyHandler implements InvocationHandler {

	//被代理类的对象  
    private Object target;  
      
    public JavassistProxyHandler(Object target) {  
        this.target = target;  
    }  
      
    /*  
     * @see cc.lixiaohui.demo.javassist.proxy.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[]) 
     */  
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {  
        System.out.println("------- intercept before --------");  
        // 调用原来的方法  
        Object result = method.invoke(target, args);  
        System.out.println("--------intercept after ---------");  
        return result;  
    }  
   

}
