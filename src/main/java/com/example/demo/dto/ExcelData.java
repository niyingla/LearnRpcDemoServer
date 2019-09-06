package com.example.demo.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class ExcelData implements Serializable {

	private static final long serialVersionUID = 4444017239100620999L;

    /**
     * 标题
     */
    private String theme;

    /**
     * 表头
     */
    private List<String> titles;

    /**
     * 数据
     */
    private List<List<Object>> rows;

    /**
     * 页签名称
     */
    private String name;

    /**
     * 用户信息
     */
    private String userInfo;
}
