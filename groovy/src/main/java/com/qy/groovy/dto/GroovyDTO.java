package com.qy.groovy.dto;

import com.qy.common.dto.RequestVO;
import lombok.Data;

/**
 * groovy dto
 *
 * @author qiaoyan
 * @date 2023-02-02 17:15:53
 */
@Data
public class GroovyDTO<T> extends RequestVO<T> {

    private Object[] params;

    private String[] classNames;

    private String methodName = "invoke";

}
