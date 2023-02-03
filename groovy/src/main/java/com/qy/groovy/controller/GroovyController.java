package com.qy.groovy.controller;

import com.qy.common.config.SpringContextHolder;
import com.qy.common.dto.ReturnResult;
import com.qy.groovy.dto.GroovyDTO;
import com.qy.groovy.service.ICheckSpringService;
import com.qy.groovy.util.GroovyUtils;
import groovy.lang.GroovyClassLoader;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
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

    @Resource
    private ICheckSpringService checkSpringService;

    @GetMapping("/check")
    public ReturnResult<String> check(){
        return ReturnResult.success("SERVICE OK !!");
    }

    @GetMapping("/doCheck")
    public ReturnResult<String> doCheck(){
        return ReturnResult.success(checkSpringService.doCheck());
    }

    /**
     * 使用groovy执行字符串代码
     * <pre>
        {
        "body":"public class TestRunA {\n\n    public String invoke(String name,Integer count){\n        for (int i = 0; i < count; i++) {\n            name += (\"@@_\"+count);\n        }\n        return name;\n    }\n\n}",
        "params" : ["xiaohei",5],
        "classNames" : ["java.lang.String","java.lang.Integer"]
         }
     * </pre>
     * @param groovyDTO
     * @author qiaoyan
     * @date 2023年02月02日 18:02:16
     * @return com.qy.common.dto.ReturnResult<?>
     */
    @PostMapping( "/exeStringCode")
    public ReturnResult<?> exeStringCode(@RequestBody GroovyDTO<String> groovyDTO) throws Exception {
        String result = GroovyUtils.executeJavaCode(groovyDTO,String.class);
        return ReturnResult.success(result);
    }


    /**
     * 在spring中获取对象
     * <pre>
     *  示例: 即为 com.qy.groovy.service.impl.CheckSpringServiceImpl 的逻辑,包含 注解依赖注入的属性
    {
        "params": [],
        "classNames": [],
        "methodName": "doCheck",
        "body": "package com.qy.groovy.service.impl;\n\nimport com.qy.common.dto.ReturnResult;\nimport com.qy.groovy.service.ICheckSpringService;\nimport org.springframework.core.ParameterizedTypeReference;\nimport org.springframework.http.HttpEntity;\nimport org.springframework.http.HttpMethod;\nimport org.springframework.http.ResponseEntity;\nimport org.springframework.stereotype.Service;\nimport org.springframework.web.client.RestTemplate;\n\nimport javax.annotation.Resource;\n\npublic class CheckSpringServiceImpl implements ICheckSpringService {\n\n    @Resource\n    private RestTemplate restTemplate;\n\n    @Override\n    public String doCheck() {\n        HttpEntity<Object> entity = new HttpEntity<>(null);\n        ResponseEntity<ReturnResult<String>> exchange = restTemplate.exchange(\n                \"http://127.0.0.1:8080/groovy/check\",\n                HttpMethod.GET,\n                entity,\n                new ParameterizedTypeReference<ReturnResult<String>>(){}\n        );\n        return exchange.getBody().getData();\n    }\n}"
    }
     * </pre>
     * @param groovyDTO groovy dto
     * @return {@code ReturnResult<?>}
     * @throws Exception 异常
     */
    @PostMapping( "/exeToSpring")
    public ReturnResult<?> exeToSpring(@RequestBody GroovyDTO<String> groovyDTO) throws Exception {
        String result = GroovyUtils.executeJavaCodeAsSpring(
                groovyDTO,
                (GenericApplicationContext) SpringContextHolder.getApplicationContext(),
                String.class
        );
        return ReturnResult.success(result);
    }

}

