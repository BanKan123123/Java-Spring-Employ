package com.example.utils;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class ExcelExporter {
    private XSSFWorkbook workbook = new XSSFWorkbook();
    private XSSFSheet sheet;

    public ExcelExporter() {
    }

    private void writeHeaderLine(List<String> headerStr) {
        sheet = workbook.createSheet("Users");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        for (int i = 0; i < headerStr.size(); i++) {
            createCell(row, i, headerStr.get(i), style);
        }
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private <T> void writeDataLines(List<T> objects, List<Function<T, Object>> propertyGetters) {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (T object : objects) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            for (Function<T, Object> propertyGetter : propertyGetters) {
                Object value = propertyGetter.apply(object);
                createCell(row, columnCount++, value, style);
            }
        }
    }

    public <T> void export(HttpServletResponse response, List<String> headerString, List<T> objects, List<Function<T, Object>> propertyGetters) throws IOException {
        writeHeaderLine(headerString);
        writeDataLines(objects, propertyGetters);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }
}
