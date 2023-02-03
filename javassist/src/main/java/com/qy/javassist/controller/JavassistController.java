package com.qy.javassist.controller;

import cn.hutool.core.date.DateUtil;
import com.qy.common.config.SpringContextHolder;
import com.qy.common.dto.RequestVO;
import com.qy.common.dto.ReturnResult;
import com.qy.javassist.bytecode.ClassGenerator;
import com.qy.javassist.bytecode.CtAnnotationBuilder;
import com.qy.javassist.bytecode.CtFieldBuilder;
import com.qy.javassist.bytecode.CtMethodBuilder;
import com.qy.javassist.utils.ClassPoolFactory;
import com.qy.javassist.utils.JavassistUtils;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.ParameterAnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

@RestController
@RequestMapping("/javassist")
public class JavassistController {

    @Resource
    private Environment environment;

    @PostMapping("test01")
    public ReturnResult<?> test01(@RequestBody RequestVO<String> requestVO) throws Exception {
        ClassPool pool = ClassPoolFactory.getDefaultPool();
//        String classname = "com.test.TestA_" + DateUtil.format(new Date(), "HHmmsss");
        String classname = "com.test.TestController01";
        CtClass cc = JavassistUtils.getCtClass(classname);
        ConstPool constPool = cc.getClassFile().getConstPool();
        // 类注释
        CtAnnotationBuilder
                .create(RestController.class,constPool)
                .markClass(cc);
        CtAnnotationBuilder
                .create(RequestMapping.class,constPool)
                .addStringMember("value",new String[]{"/testa"})
                .markClass(cc);
        // 属性
        CtField field = CtFieldBuilder
                .create(cc,JavassistUtils.getCtClass(Environment.class),"environment")
                .build();
        // 属性注释
        CtAnnotationBuilder
                .create(Autowired.class,constPool)
                .markField(field);

        // 方法
        CtMethod method = CtMethodBuilder
                .create(
                        JavassistUtils.getCtClass(ReturnResult.class),
                        "start",
                        JavassistUtils.getCtClass(new Class[]{String.class, RequestVO.class}),
                        JavassistUtils.getCtClass(new Class[]{NotFoundException.class, RuntimeException.class}),
                          "{ " +
                                    "return com.qy.common.dto.ReturnResult.success($1 + $2.getBody());" +
                                "}",
                        cc
                )
                .markClass(cc)
                .build();
        // 方法设置
        CtAnnotationBuilder
                .create(RequestMapping.class,constPool)
                .addStringMember("value", new String[]{"/start"})
                .addEnumMember("method", new Enum[]{RequestMethod.POST})
                .markMethod(method);
        // 方法参数注解
        Annotation param01 = CtAnnotationBuilder
                .create(RequestParam.class, constPool)
                .addStringMember("value", "type")
                .build();
        Annotation param02 = CtAnnotationBuilder
                .create(RequestBody.class, constPool)
                .build();
        ParameterAnnotationsAttribute parameterAnnotationsAttribute = new ParameterAnnotationsAttribute(constPool, ParameterAnnotationsAttribute.visibleTag);
        parameterAnnotationsAttribute.setAnnotations(new Annotation[][]{{param01},{param02}});
        method.getMethodInfo().addAttribute(parameterAnnotationsAttribute);

        CtMethod method1 = CtNewMethod.make(
                "public String testA1(){\n" +
                "        String property = $0.environment.getProperty(\"server.port\");\n" +
                "        System.out.println(property);\n" +
                "        return property;\n" +
                "    }" +
                "", cc);
        cc.addMethod(method1);

        // 设置到spring容器中
        AnnotationConfigServletWebServerApplicationContext applicationContext = (AnnotationConfigServletWebServerApplicationContext)SpringContextHolder.getApplicationContext();
        applicationContext.registerBean(classname,cc.toClass());

        cc.writeFile();
        return ReturnResult.success();
    }

    /**
     * test02
     * 测试从spring容器中获取bean对象,并测试 @Autowired 注入是否生效
     * 测试结果为都生效
     * @return {@code ReturnResult<?>}
     * @throws Exception 异常
     */
    @PostMapping("/test02")
    public ReturnResult<?> test02() throws Exception {
        String classname = "com.test.TestController01";
        AnnotationConfigServletWebServerApplicationContext applicationContext = (AnnotationConfigServletWebServerApplicationContext)SpringContextHolder.getApplicationContext();
        Object bean = applicationContext.getBean(classname);
        Method[] methods = bean.getClass().getDeclaredMethods();
        String result = null;
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if(method.getName().equals("testA1")){
                result = (String)method.invoke(bean);
                break;
            }
        }
        return ReturnResult.success(result);
    }
}
