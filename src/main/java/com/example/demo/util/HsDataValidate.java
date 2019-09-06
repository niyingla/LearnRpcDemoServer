package com.example.demo.util;

import com.example.demo.dto.ExcelData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * @program: continutiy-parent
 * @description:
 * @author: xiaoye
 * @create: 2019-07-01 14:38
 **/
@Slf4j
public class HsDataValidate {

    /**
     * 存储购销计划上线日期
     */
    static Map<String,String> contractOnLine = new LinkedHashMap();

    private static String preContract = "9907";


    public static void validate(String filePathFrom, String sheetNameFrom,  Integer startLineFrom, List<String> filedList) throws Exception {
        Workbook sheets = WorkbookFactory.create(new FileInputStream(filePathFrom));
        Sheet sheetFrom = sheets.getSheet(sheetNameFrom);
        //-----------------------主excel-------------------------
        //字段列  在第几行
        //获取每段标题行
        Row sheetRowFrom = sheetFrom.getRow(startLineFrom);
        //主excel需要检查的列名级在几行
        Map<String, Integer> filedMap = getValidateFiledMap(filedList, sheetRowFrom);
        //key 日期  vulue list
        Map<String, List<Map<String,String>>> fieldValue = getFieldValue(sheetFrom, filedList, startLineFrom, filedMap);
        //按循序比较得到当日成交量最大的合约 只能前进不能后退
        LinkedHashMap<String, Map<String,String>> mainContract = getMainContract(fieldValue);
        ExcelData excelData = ExportExcelUtils.parseToExcelData(filedList, mainContract);
        //得到主力合约
        ExportExcelUtils.exportExcel(excelData);

    }

    private static LinkedHashMap<String, Map<String,String>> getMainContract(Map<String, List<Map<String,String>>> fieldValue) {
        //上一次预留的数据
        Map<String, String> hashMapPre = null;
        LinkedHashMap<String, Map<String,String>> linkedHashMap = new LinkedHashMap<>();
        int count = 0;
        //循环所有日期数据
        for (String 日期 : fieldValue.keySet()) {
            count++;
            if(日期.equals("20190319") ){
                System.out.println(1);
            }
            List<Map<String,String>> maps = fieldValue.get(日期);
            //过滤之前的购销计划
            List<Map<String, String>> 购销计划去循序 = maps.stream().filter(item -> {
                String 购销计划 = item.get("购销计划");
                return Integer.valueOf(contractOnLine.get(购销计划)) >= Integer.valueOf(contractOnLine.get(preContract));
            }).collect(Collectors.toList());

            //获取满足条件成交量最高的
            Collections.sort(购销计划去循序, new Comparator<Map<String, String>>() {
                @Override
                public int compare(Map<String, String> o1, Map<String, String> o2) {
                    return Integer.valueOf(o2.get("成交")) - Integer.valueOf(o1.get("成交"));
                }
            });
            hashMapPre = 购销计划去循序.get(0);
            preContract = hashMapPre.get("购销计划");
            linkedHashMap.put(日期 + "------------" + preContract, hashMapPre);

        }
        System.out.println(count);
        return linkedHashMap;
    }

    /**
     *
     * @param sheetFrom
     * @param filedList 需要校验字段
     * @param startLine 字段开始行
     * @param filedMap 字段和行数对应的map
     * @return
     */
    private static Map<String, List<Map<String,String>>> getFieldValue(Sheet sheetFrom, List<String> filedList, int startLine, Map<String, Integer> filedMap) throws Exception{

        LinkedHashMap<String, List<Map<String,String>>> linkedHashMap = new LinkedHashMap<>();
        int rows = sheetFrom.getPhysicalNumberOfRows();
        //循环所有数据行
        for (int i = startLine + 3452; i < rows; i++) {
            Row row = sheetFrom.getRow(i);
            Map<String,String> valueMap = new HashMap<>();

            //循环数据列
            for (int j = 0; j < filedList.size(); j++) {
                if (j == 0) {
                    Date dateCellValue = row.getCell(j).getDateCellValue();
                    String date = com.example.demo.util.StringUtils.dateToString(dateCellValue);
                    valueMap.put(filedList.get(j),date);
                    continue;
                }
                String filedValue = filedList.get(j);
                Integer col = filedMap.get(filedValue);
                Cell cell = row.getCell(col);
                cell.setCellType(CellType.STRING);
                valueMap.put(filedValue, cell.getStringCellValue());
            }

            if(valueMap.get("购销计划").equals("YS19043")){
                System.out.println(1);
            }
            //过滤购销计划  包含小写字母  包含 NULL YS开头不是以3结尾的不要
            String replace = valueMap.get("购销计划").replace("YS", "");
            if (!Pattern.matches("^[0-9]*$", replace) || !(valueMap.get("购销计划").startsWith("YS") ? replace.endsWith("3") : true)) {
                System.out.println(replace);
                continue;
            }


            //放入购销计划 生效日期列表
            if(!contractOnLine.containsKey(valueMap.get("购销计划"))){
                contractOnLine.put(valueMap.get("购销计划"), valueMap.get("日期"));
            }

            List<Map<String,String>> mapList;
            if (linkedHashMap.containsKey(valueMap.get("日期"))) {
                mapList = linkedHashMap.get(valueMap.get("日期"));
            } else {
                mapList = new LinkedList<>();
                linkedHashMap.put(valueMap.get("日期"), mapList);
            }
            mapList.add(valueMap);
        }
        return linkedHashMap;
    }

    /**
     * 获取
     * @param filedList 需要校验的字段数据
     * @param sheetRow excel字段列
     * @return
     */
    private static Map<String, Integer> getValidateFiledMap(List<String> filedList, Row sheetRow) {
        //获取列数
        int physicalNumberOfCells = sheetRow.getPhysicalNumberOfCells();
        //key 字段名 value 在第几列
        Map<String, Integer> filedMap = new HashMap<>(30);
        //虚幻所有字段
        for (int i = 0; i < physicalNumberOfCells; i++) {
            Cell cell = sheetRow.getCell(i);

            cell.setCellType(CellType.STRING);

            String rawValue = cell.getStringCellValue();

            if (StringUtils.isBlank(rawValue)) {
                continue;
            }
            //字段存在需要校验的list中
            if (filedList.indexOf(rawValue.trim()) >= 0) {
                filedMap.put(rawValue, i);
            }
        }
        return filedMap;
    }

    public static void main(String[] args)throws Exception {
        List<String> list = Arrays.asList( "日期","购销计划", "高", "开", "低", "收", "涨跌", "成交", "订货", "平均价");
        validate("C:\\Users\\10479\\Desktop\\主力连续合约\\昆糖行情.xls", "昆糖行情", 0, list);
    }
}
