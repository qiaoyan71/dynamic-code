package com.qy.groovy.util;

import com.qy.groovy.dto.GroovyDTO;
import groovy.lang.GroovyClassLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.GenericApplicationContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Function;

/**
 * GroovyUtils
 *
 * @author qiaoyan
 * @date 2023-02-03 18:06:43
 */
@Slf4j
public class GroovyUtils {

    /**
     * 执行java代码
     * <pre>
     *     注意: class只能被加载一次,多次执行仅第一次生效
     * </pre>
     * @param groovyDTO 对象
     * @param function  获取实例function
     * @param rType     返回类型
     * @return {@code T}
     * @throws Exception 异常
     */
    public static <T> T executeJavaCode(GroovyDTO<String> groovyDTO, Function<Class,Object> function, Class<T> rType) throws Exception {
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
        Class clazz = groovyClassLoader.parseClass(groovyDTO.getBody());
        Object instance = function.apply(clazz);
        Class<?>[] parameterTypes = getClasses(groovyDTO);
        Method method = clazz.getDeclaredMethod(groovyDTO.getMethodName(), parameterTypes);
        return (T) method.invoke(instance,groovyDTO.getParams());
    }

    /**
     * 执行java代码(直接反射new对象)
     * <pre>
     *     注意: 每次都是新的GroovyClassLoader,所以class可以多次生效
     * </pre>
     * @param groovyDTO 对象
     * @param rType     返回类型
     * @return {@code T}
     * @throws Exception 异常
     */
    public static <T> T executeJavaCode(GroovyDTO<String> groovyDTO,Class<T> rType) throws Exception {
        return executeJavaCode(groovyDTO,clazz-> {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                log.info("初始化错误:",e);
                throw new RuntimeException("初始化错误");
            }
        },rType);
    }

    /**
     * 执行java代码(使用spring容器,可以解析其中的注入属性等)
     * <pre>
     *     注意: 每次都是新的GroovyClassLoader,所以class可以多次生效,并且要保证sprig容器有先删除
     * </pre>
     * @param groovyDTO          groovy dto
     * @param applicationContext 应用程序上下文
     * @param rType              返回类型
     * @return {@code T}
     * @throws Exception 异常
     */
    public static <T> T executeJavaCodeAsSpring(GroovyDTO<String> groovyDTO,GenericApplicationContext applicationContext,Class<T> rType) throws Exception {
        return executeJavaCode(groovyDTO,clazz-> {
            String clazzName = clazz.getName();
            // 注入到spring中,使自定注入注解生效
            // 存在了先从容器中删除
            if(applicationContext.containsBean(clazzName)){
                applicationContext.removeBeanDefinition(clazzName);
            }
            applicationContext.registerBean(clazzName,clazz);
            return applicationContext.getBean(clazzName);
        },rType);
    }

    private static Class<?>[] getClasses(GroovyDTO<String> groovyDTO) {
        String[] classNames = groovyDTO.getClassNames();
        Class[] classes = Arrays.stream(classNames).map(name -> {
            try {
                return Class.forName(name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }).toArray(Class[]::new);
        return classes;
    }

}
