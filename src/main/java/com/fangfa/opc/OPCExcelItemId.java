package com.fangfa.opc;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class OPCExcelItemId {
    public List<String> opcExcelItemId() throws Exception{
        File file = new File("D:\\输气处20180622导出1.xlsx");
        InputStream inputStream = new FileInputStream(file);
        List<String> list = new LinkedList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();

        for (int i = 1; i <= lastRowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            //用户名
//            row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
            if (row.getCell(2).getStringCellValue() != null && !row.getCell(2).getStringCellValue().equals("")) {
                list.add(row.getCell(2).getStringCellValue());
            }
        }
        return list;
    }
}
