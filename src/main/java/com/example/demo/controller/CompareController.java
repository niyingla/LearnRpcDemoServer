package com.example.demo.controller;

import com.example.demo.dto.CompareDto;
import com.example.demo.enums.ExeclEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * @Description
 * @Author toms
 * @Date 2019/7/1
 */
@RequestMapping("/hs")
@Slf4j
@RestController
public class CompareController {

    private static List<String> filedList;

//    public static void main(String[] args)throws Exception {
//        //从excel
//        String filePathFrom = "E:\\延期数据核对\\1\\持仓.xls";
//        //从excel
//        String filePathTo = "E:\\延期数据核对\\1\\持仓汇总_采购.xls";
//        //主excel sheet名
//        String sheetNameTo = "当日订货查询(采购)";
//        //从excel sheet名
//        String sheetNameFrom = sheetNameTo;
//        //数量	均价	补充担保履约资金	百分比	客户区域
//        //需要校验的字段 第一个字段请填入唯一标识列 如 userId
//        filedList = Arrays.asList("客户","客户名称","数量","均价","补充担保履约资金","客户区域");
//        HsDataValidate.validate(filePathFrom, filePathTo, sheetNameTo, sheetNameFrom, 2, 0,filedList);
//    }

    @RequestMapping("/compareData")
    public String compareData(@Valid CompareDto dto){
        ExeclEnums execlEnums = ExeclEnums.getByType(dto.getType());
        if(null==execlEnums){
            return "类型非法";
        }
        String fileds = execlEnums.getFileds();
        filedList = Arrays.asList(fileds.split(","));
                //主excel sheet名
        String sheetNameTo = execlEnums.getSheetName();
        //从excel sheet名
        String sheetNameFrom = sheetNameTo;
        //数量	均价	补充担保履约资金	百分比	客户区域
        //需要校验的字段 第一个字段请填入唯一标识列 如 userId
        try{
//            HsDataValidate.validate(dto.getExcelPathFrom(), dto.getExcelPathTo(), sheetNameTo, sheetNameFrom, execlEnums.getStartLineTo(),execlEnums.getStartLineFrom(),filedList);
            return "ok";
        }catch (Exception e){
            e.printStackTrace();
             log.error("核对异常，异常信息：[{}]",e.getMessage());
            return "exception";
        }
    }
}
