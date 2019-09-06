package com.example.demo.excelcontinu;

import com.example.demo.dto.ExcelData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;

import java.awt.Color;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author lucky
 */
public class ExportExcelUtils {


    /**
     * 导出一页数据
     * @param data
     * @throws Exception
     */
    public static void exportExcel(ExcelData data) throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();
        try {
            String sheetName = "Sheet1";
            //创建一个页脚
            XSSFSheet sheet = wb.createSheet(sheetName);
            //写入该页每项
            writeExcel(wb, sheet, data, 0);

            FileOutputStream fout = new FileOutputStream("C:\\Users\\10479\\Desktop\\连续合约.xlsx");
            wb.write(fout);
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            wb.close();
        }
    }



    /**
     * 写入excel数据
     * @param wb
     * @param sheet
     * @param data
     */
    private static void writeExcel(XSSFWorkbook wb, Sheet sheet, ExcelData data, int rowIndex) {
            //写入标题
            int titleIndex = writeTitlesToExcel(wb, sheet, data.getTitles(), rowIndex);
            //写入行数据内容
           writeRowsToExcel(wb, sheet, data.getRows(), titleIndex);
    }


    /**
     * 写入excel标题
     * @param wb
     * @param sheet
     * @param titles
     * @return
     */
    private static int writeTitlesToExcel(XSSFWorkbook wb, Sheet sheet, List<String> titles, int rowIndex) {
        int colIndex = 0;

        //获取字体
        Font titleFont = getFont(wb, null);

        //获取单元格风格
        XSSFCellStyle titleStyle = getXssfCellStyle(wb, titleFont);

        //设置边框
        setBorder(titleStyle, BorderStyle.THIN, new XSSFColor(new Color(0, 0, 0)));

        Row titleRow = sheet.createRow(rowIndex);

        titleRow.setHeightInPoints(26);

        //循环创建标题
        for (String field : titles) {
            sheet.setColumnWidth(colIndex, 256*15+184);
            Cell cell = titleRow.createCell(colIndex);
            cell.setCellValue(field);
            cell.setCellStyle(titleStyle);
            colIndex++;
        }

        rowIndex++;
        return rowIndex;
    }

    /**
     * 创建默认单元格央视
     * @param wb
     * @param titleFont
     * @return
     */
    private static XSSFCellStyle getXssfCellStyle(XSSFWorkbook wb, Font titleFont) {
        XSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        titleStyle.setFillForegroundColor(new XSSFColor(new Color(182, 184, 192)));
        titleStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        titleStyle.setFont(titleFont);
        return titleStyle;
    }

    /**
     * 获取默认字体
     * @param wb
     * @param fontHeight 不传获取 默认14
     * @return
     */
    private static Font getFont(XSSFWorkbook wb, Short fontHeight) {
        Font titleFont = wb.createFont();
        titleFont.setFontName("simsun");
        titleFont.setBold(true);
        //设置字体高度
        if (fontHeight != null) {
            titleFont.setFontHeightInPoints(fontHeight);
        } else {
            titleFont.setFontHeightInPoints((short) 14);
        }
        titleFont.setColor(IndexedColors.BLACK.index);
        return titleFont;
    }

    /**
     * 写入行数据
     * @param wb
     * @param sheet
     * @param rows
     * @param rowIndex
     * @return
     */
    private static int writeRowsToExcel(XSSFWorkbook wb, Sheet sheet, List<List<Object>> rows, int rowIndex) {

        //创建字体
        Font dataFont = wb.createFont();
        dataFont.setFontName("simsun");
        dataFont.setColor(IndexedColors.BLACK.index);

        //创建单元格样式
        XSSFCellStyle dataStyle = wb.createCellStyle();
        dataStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        dataStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        dataStyle.setFont(dataFont);
        //设置边框
        setBorder(dataStyle, BorderStyle.THIN, new XSSFColor(new Color(0, 0, 0)));

        //循环写入数据
        for (List<Object> rowData : rows) {
            Row dataRow = sheet.createRow(rowIndex);
            dataRow.setHeightInPoints(22);
            int colIndex = 0;
            //写入倒每个单元格
            for (Object cellData : rowData) {
                Cell cell = dataRow.createCell(colIndex);
                if (cellData != null) {
                    cell.setCellValue(cellData.toString());
                } else {
                    cell.setCellValue("");
                }

                cell.setCellStyle(dataStyle);
                colIndex++;
            }
            rowIndex++;
        }
        return rowIndex;
    }

    /**
     * 设置边框
     * @param style
     * @param border
     * @param color
     */
    private static void setBorder(XSSFCellStyle style, BorderStyle border, XSSFColor color) {
        style.setBorderTop(border);
        style.setBorderLeft(border);
        style.setBorderRight(border);
        style.setBorderBottom(border);
        style.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
        style.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
        style.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);
        style.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
    }

    public static ExcelData parseToExcelData(List<String> filedList, Map<String,Map<String,String>> valueMap) {
        //创建导出数据对象
        ExcelData data = new ExcelData();
        //获取列名作为标题
        data.setTitles(filedList);

        List<List<Object>> rowsList = new ArrayList<>();
        //设置字段值
        for (Map<String, String> currentValueMap : valueMap.values()) {
            List valueList = new ArrayList<>();
            filedList.forEach(item-> {
                Object value = currentValueMap.get(item);
                valueList.add(value);

            });
            rowsList.add(valueList);
        }
        data.setRows(rowsList);
        return data;
    }
}

