package com.utfinancing.financehub.engine.hthx.utils;

import com.utfinancing.financehub.common.core.annotation.Excel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 应用模块名称: Excel导出工具类封装
 * 代码描述:
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/4/23 21:13
 */


public class HthxExcelExportUtils {

    // 数值格式样式缓存（线程安全）
    private static final ThreadLocal<CellStyle> NUMBER_STYLE = new ThreadLocal<>();

    /**
     * 大数据量 Excel 导出（支持自定义数值格式）
     * @param response      HttpServletResponse
     * @param dataList      数据列表
     * @param fileName      文件名（不含后缀）
     * @param sheetName     Sheet名称
     * @param clazz         导出VO的Class类型（用于反射获取字段注解）
     */
    public static <T> void exportLargeExcel(
            HttpServletResponse response,
            List<T> dataList,
            String fileName,
            String sheetName,
            Class<T> clazz) throws IOException {

        // 1. 初始化 SXSSFWorkbook（内存缓存1000行）
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(1000)) {
            Sheet sheet = workbook.createSheet(sheetName);

            // 2. 创建表头
            createHeaderRow(workbook, sheet, clazz);

            // 3. 创建数值格式样式
            CellStyle numberStyle = createNumberStyle(workbook);

            // 4. 批量写入数据
            int rowNum = 1;
            for (T data : dataList) {
                Row row = sheet.createRow(rowNum++);
                populateDataRow(data, row, clazz, numberStyle);
            }

            // 5. 设置响应头
            setResponseHeaders(response, fileName);

            // 6. 流式写入响应
            workbook.write(response.getOutputStream());
            workbook.dispose(); // 清理临时文件
        }
    }

    private static <T> void createHeaderRow(SXSSFWorkbook workbook, Sheet sheet, Class<T> clazz) {
        Row headerRow = sheet.createRow(0);
        // 过滤有效字段（同上）
        List<Field> excelFields = Arrays.stream(clazz.getDeclaredFields())
                .filter(field ->
                        field.getAnnotation(Excel.class) != null &&
                                !Modifier.isStatic(field.getModifiers())
                ).collect(Collectors.toList());
        int cellIndex = 0;
        for (Field field : excelFields) {
            Excel excelAnnotation = field.getAnnotation(Excel.class);
            Cell cell = headerRow.createCell(cellIndex++);
            cell.setCellValue(excelAnnotation.name());
        }
    }

    private static CellStyle createNumberStyle(SXSSFWorkbook workbook) {
        if (NUMBER_STYLE.get() == null) {
            CellStyle style = workbook.createCellStyle();
            DataFormat dataFormat = workbook.createDataFormat();
            style.setDataFormat(dataFormat.getFormat("#,##0.00")); // 千分位两位小数
            NUMBER_STYLE.set(style);
        }
        return NUMBER_STYLE.get();
    }

    private static <T> void populateDataRow(T data, Row row, Class<T> clazz, CellStyle numberStyle) {
        // 获取所有字段并过滤出带有 @Excel 注解的非静态字段
        List<Field> excelFields = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> {
                    // 过滤条件：有 @Excel 注解且非静态字段
                    boolean hasAnnotation = field.getAnnotation(Excel.class) != null;
                    boolean isStatic = Modifier.isStatic(field.getModifiers());
                    return hasAnnotation && !isStatic;
                })
                .collect(Collectors.toList());
        int cellIndex = 0;
        for (Field field : excelFields) { // 仅遍历有效字段
            try {
                field.setAccessible(true);
                Object value = field.get(data);
                Cell cell = row.createCell(cellIndex++); // 列索引从0开始
                if (value instanceof BigDecimal) {
                    cell.setCellValue(((BigDecimal) value).doubleValue());
                    cell.setCellStyle(numberStyle);
                } else if (value != null) {
                    cell.setCellValue(value.toString());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("反射访问字段失败", e);
            }
        }
    }

    private static void setResponseHeaders(HttpServletResponse response, String fileName) throws IOException {
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName + ".xlsx");
        response.setCharacterEncoding("UTF-8");
    }

}
