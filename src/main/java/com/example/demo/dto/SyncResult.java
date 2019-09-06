package com.example.demo.dto;

import lombok.Data;

/**
 * @program: demo
 * @description:
 * @author: xiaoye
 * @create: 2019-08-12 16:29
 **/
@Data
public class SyncResult {
    volatile boolean isRead = false;
    Object Data = null;

    public Object getData() {
        while (!isRead) {
        }
        return Data;
    }
}