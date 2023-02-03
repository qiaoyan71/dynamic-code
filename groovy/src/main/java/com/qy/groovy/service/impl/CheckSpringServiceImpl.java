package com.qy.groovy.service.impl;

import com.qy.common.dto.ReturnResult;
import com.qy.groovy.service.ICheckSpringService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author qiaoyan
 */
@Service
public class CheckSpringServiceImpl implements ICheckSpringService {

    @Resource
    private RestTemplate restTemplate;

    @Override
    public String doCheck() {
        HttpEntity<Object> entity = new HttpEntity<>(null);
        ResponseEntity<ReturnResult<String>> exchange = restTemplate.exchange(
                "http://127.0.0.1:8080/groovy/check",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<ReturnResult<String>>(){}
        );
        return exchange.getBody().getData();
    }
}
