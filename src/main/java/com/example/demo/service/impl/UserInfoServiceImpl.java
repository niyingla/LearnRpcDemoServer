package com.example.demo.service.impl;

import com.example.demo.dto.CompareDto;
import com.example.demo.service.UserInfoService;

/**
 * @author pikaqiu
 */

//@Service
public class UserInfoServiceImpl implements UserInfoService  {

    @Override
    public CompareDto getCompareDto(String type) {
        CompareDto compareDto = new CompareDto();
        compareDto.setType("xiao");
        compareDto.setWay("ye");
        return compareDto;
    }

    @Override
    public CompareDto getCompareTest(String type) {
        return null;
    }
}
