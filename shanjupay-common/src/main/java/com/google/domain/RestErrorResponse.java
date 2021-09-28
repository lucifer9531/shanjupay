package com.google.domain;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Administrator
 * @version 1.0
 **/
@ApiModel(value = "RestErrorResponse", description = "错误响应参数包装")
@Data
@AllArgsConstructor
public class RestErrorResponse {

    private String errCode;

    private String errMessage;
}
