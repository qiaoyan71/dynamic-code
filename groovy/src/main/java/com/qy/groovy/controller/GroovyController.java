package com.qy.groovy.controller;

import com.qy.common.dto.ReturnResult;
import com.qy.groovy.dto.GroovyDTO;
import groovy.lang.GroovyClassLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * groovy控制器
 *
 * @author qiaoyan
 * @date 2023-02-02 16:39:00
 */
@RestController
@RequestMapping("/groovy")
public class GroovyController {

    /**
     * 使用groovy执行字符串代码
     * <pre>
     *     {
     *      "body":"public class TestRun {\n\n    public String invoke(String name,Integer count){\n        for (int i = 0; i < count; i++) {\n            name += (\"_\"+count);\n        }\n        return name;\n    }\n\n}",
     *      "params" : ["xiaohei",5],
     *      "classNames" : ["java.lang.String","java.lang.Integer"]
     *     }
     * </pre>
     * @param groovyDTO
     * @author qiaoyan
     * @date 2023年02月02日 18:02:16
     * @return com.qy.common.dto.ReturnResult<?>
     */
    @PostMapping( "/exeStringCode")
    public ReturnResult<?> exeStringCode(@RequestBody GroovyDTO<String> groovyDTO) throws Exception {
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
        Class clazz = groovyClassLoader.parseClass(groovyDTO.getBody());
        Object instance = clazz.newInstance();
        Class<?>[] parameterTypes = getClasses(groovyDTO);
        Method method = clazz.getDeclaredMethod(groovyDTO.getMethodName(), parameterTypes);
        return ReturnResult.success(method.invoke(instance,groovyDTO.getParams()));
    }

    private Class<?>[] getClasses(GroovyDTO<String> groovyDTO) {
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

