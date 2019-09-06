package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: demo
 * @description:
 * @author: xiaoye
 * @create: 2019-08-12 11:55
 **/
@Data
@AllArgsConstructor
public class RpcRequestDto implements Serializable {

    private String requestId;

    private String classPath;

    private String methodName;

    private Object[] args;

    private Object result;
}
