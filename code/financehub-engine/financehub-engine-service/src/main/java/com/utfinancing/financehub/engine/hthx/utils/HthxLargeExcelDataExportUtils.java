package com.utfinancing.financehub.engine.hthx.utils;

import com.utfinancing.financehub.common.core.annotation.Excel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HthxLargeExcelDataExportUtils {
    private static final Logger log = LoggerFactory.getLogger(HthxExcelExportUtils.class);
    private static final int BUFFER_SIZE = 1000; // 内存缓存行数
    private static final int FLUSH_INTERVAL = 500; // 刷新到磁盘的间隔

    /**
     * 优化后的大数据量导出
     */
    public static <T> void exportLargeExcel(
            HttpServletResponse response,
            List<T> dataList,
            String fileName,
            String sheetName,
            Class<T> clazz) throws IOException {

        // 0. 设置临时文件存储位置（可选）
        // System.setProperty("org.apache.poi.util.POITempFile.tempdir", "/path/to/large/disk");

        // 1. 初始化工作簿
        SXSSFWorkbook workbook = null;
        try {
            workbook = new SXSSFWorkbook(BUFFER_SIZE);
            workbook.setCompressTempFiles(true); // 压缩临时文件

            SXSSFSheet sheet = workbook.createSheet(sheetName);
            sheet.setRandomAccessWindowSize(BUFFER_SIZE);

            // 2. 创建样式（避免重复创建）
            CellStyle numberStyle = createNumberStyle(workbook);
            createHeaderRow(sheet, clazz);

            // 3. 批量写入数据（分批次刷新）
            int rowNum = 1;
            for (T data : dataList) {
                Row row = sheet.createRow(rowNum++);
                populateDataRow(data, row, clazz, numberStyle);

                // 定期刷新到磁盘
                if (rowNum % FLUSH_INTERVAL == 0) {
                    sheet.flushRows(FLUSH_INTERVAL);
                }
            }

            // 4. 设置响应头
            setResponseHeaders(response, fileName);

            // 5. 写入响应流（确保在资源关闭前完成）
            workbook.write(response.getOutputStream());

        } catch (Exception e) {
            log.error("Excel导出失败: {}", e.getMessage(), e);
            throw new IOException("导出Excel失败: " + e.getMessage(), e);
        } finally {
            // 6. 确保资源释放（关键！）
            if (workbook != null) {
                try {
                    workbook.dispose(); // 清理临时文件
                } catch (Exception disposeEx) {
                    log.warn("清理临时文件失败: {}", disposeEx.getMessage());
                }
                try {
                    workbook.close();
                } catch (IOException closeEx) {
                    log.warn("关闭工作簿失败: {}", closeEx.getMessage());
                }
            }
        }
    }

    // 创建数值样式（移除了ThreadLocal缓存）
    private static CellStyle createNumberStyle(SXSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        DataFormat dataFormat = workbook.createDataFormat();
        style.setDataFormat(dataFormat.getFormat("#,##0.00"));
        return style;
    }

    // 优化后的表头创建
    private static <T> void createHeaderRow(SXSSFSheet sheet, Class<T> clazz) {
        Row headerRow = sheet.createRow(0);
        List<Field> excelFields = getExcelFields(clazz);

        int cellIndex = 0;
        for (Field field : excelFields) {
            Excel excelAnnotation = field.getAnnotation(Excel.class);
            Cell cell = headerRow.createCell(cellIndex++);
            cell.setCellValue(excelAnnotation.name());

            // 设置列宽自适应（提升性能）
            sheet.trackColumnForAutoSizing(cellIndex - 1);
        }
    }

    // 数据填充优化
    private static <T> void populateDataRow(T data, Row row, Class<T> clazz, CellStyle numberStyle) {
        List<Field> excelFields = getExcelFields(clazz);
        int cellIndex = 0;

        for (Field field : excelFields) {
            Cell cell = row.createCell(cellIndex++);
            try {
                field.setAccessible(true);
                Object value = field.get(data);

                // 安全处理文本长度
                String stringValue = value == null ? "" : value.toString();
                if (stringValue.length() > 32700) {
                    stringValue = stringValue.substring(0, 32700) + "...";
                }

                if (value instanceof BigDecimal) {
                    cell.setCellValue(((BigDecimal) value).doubleValue());
                    cell.setCellStyle(numberStyle);
                } else {
                    cell.setCellValue(stringValue);
                }
            } catch (IllegalAccessException e) {
                cell.setCellValue("N/A");
                log.warn("字段访问失败: {}", field.getName());
            }
        }
    }

    // 提取字段获取逻辑
    private static <T> List<Field> getExcelFields(Class<T> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field ->
                        field.getAnnotation(Excel.class) != null &&
                                !Modifier.isStatic(field.getModifiers()))
                .collect(Collectors.toList());
    }

    // 响应头设置（不变）
    private static void setResponseHeaders(HttpServletResponse response, String fileName) throws IOException {
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName + ".xlsx");
        response.setCharacterEncoding("UTF-8");
    }
}