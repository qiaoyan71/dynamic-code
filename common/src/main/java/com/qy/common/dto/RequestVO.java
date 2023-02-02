package com.qy.common.dto;

import lombok.Data;

/**
 * 请求VO
 *
 * @author qiaoyan
 * @date 2023-02-02 16:59:52
 */
@Data
public class RequestVO<T>{

    private T body;

}
