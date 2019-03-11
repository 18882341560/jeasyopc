package com.fangfa.opc;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author green
 * @date 2018/6/22/022
 */
public class ImportExcel {

    public static List<String> getItemsAll() throws IOException {
        InputStream inputStream = ImportExcel.class.getClassLoader().getResourceAsStream("items.xlsx");
        List<String> list = new LinkedList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();

        for (int i = 1; i <= lastRowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            //用户名
            row.getCell(1).setCellType(CellType.STRING);
            if (row.getCell(1).getStringCellValue() != null && !row.getCell(1).getStringCellValue().equals("")) {
                String tag = "\\\\10.89.6.6\\"+ row.getCell(1).getStringCellValue();
                list.add(tag);
            }
        }
        return list;
    }
}
