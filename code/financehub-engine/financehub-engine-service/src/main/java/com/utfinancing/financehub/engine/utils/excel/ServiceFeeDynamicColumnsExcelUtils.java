package com.utfinancing.financehub.engine.utils.excel;

import cn.hutool.core.date.DateUtil;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeAllocationExcelVo;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeMonthlyData;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@Slf4j
public class ServiceFeeDynamicColumnsExcelUtils {

    // 导出Excel文件的方法
    public static void exportToExcel(List<ServiceFeeAllocationExcelVo> datas, HttpServletResponse response) throws IOException {
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里可以自定义文件名，例如使用当前时间
        String fileName = URLEncoder.encode("咨询服务费分摊计划.xlsx", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("咨询服务费分摊计划");

            // 获取所有月份
            Set<String> allMonths = new TreeSet<>();
            for (ServiceFeeAllocationExcelVo contract : datas) {
                for (ServiceFeeMonthlyData monthlyData : contract.getMonthlyDatas()) {
                    allMonths.add(monthlyData.getMonth());
                }
            }

            // 创建表头
            Row headerRow = sheet.createRow(0);
            createHeaderRow(headerRow,allMonths);

            // 填充数据
            for (int i = 0; i < datas.size(); i++) {
                Row dataRow = sheet.createRow(i + 1);
                fillDataRow(allMonths,dataRow, datas.get(i));
            }

            // 自动调整列宽
            int columnCount = headerRow.getLastCellNum();
            for (int i = 0; i < columnCount; i++) {
                sheet.autoSizeColumn(i);
            }

            // 写入文件
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            // 记录日志，可根据实际情况添加日志框架
            log.error("Excel 导出失败", e);
            // 可以根据需求抛出更具体的异常
            throw new IOException("Excel 导出失败", e);
        }
    }

    // 创建表头行
    private static void createHeaderRow(Row headerRow, Set<String> allMonths) {


        // 创建表头单元格
        List<String> headers = new ArrayList<>();
        headers.add("合同号");
        headers.add("合同主体");
        headers.add("服务费协议编号");
        headers.add("服务费主体");
        headers.add("实收服务费比例");
        headers.add("承租人");
        headers.add("会计起租日");
        headers.add("结束日");
        headers.add("设备金额");
        headers.add("服务费实收（税前）");
        headers.add("服务费实收（税后）");
        headers.add("应分摊的服务费收入（税前）");
        headers.add("应分摊的服务费收入（税后）");
        headers.add("分摊比例");
        headers.addAll(allMonths);
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
            // 设置表头样式
            Workbook workbook = headerRow.getSheet().getWorkbook();
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }
    }

    // 填充数据行
    private static void fillDataRow(Set<String> allMonths, Row dataRow, ServiceFeeAllocationExcelVo contract) {
        Workbook workbook = dataRow.getSheet().getWorkbook();
        // 创建千分位金额格式
        DataFormat format = workbook.createDataFormat();
        CellStyle amountCellStyle = workbook.createCellStyle();
        amountCellStyle.setDataFormat(format.getFormat("#,##0.00"));
        // 创建百分比格式样式
        CellStyle percentStyle = workbook.createCellStyle();
        DataFormat format1 = workbook.createDataFormat();
        percentStyle.setDataFormat(format1.getFormat("0.00%"));

        Cell cell;

        // 填充基本信息
        cell = dataRow.createCell(0);
        cell.setCellValue(contract.getContractCode());
        cell = dataRow.createCell(1);
        cell.setCellValue(contract.getOrgId());
        cell = dataRow.createCell(2);
        cell.setCellValue(contract.getServiceFeeNo());
        cell = dataRow.createCell(3);
        cell.setCellValue(contract.getServiceOrgId());

        cell = dataRow.createCell(4);
        cell.setCellValue(contract.getActualReceiveRatio().doubleValue());
        cell.setCellStyle(percentStyle);
        cell = dataRow.createCell(5);
        cell.setCellValue(contract.getLesseeName());
        cell = dataRow.createCell(6);
        cell.setCellValue(DateUtil.format(DateUtil.parse(contract.getLeaseDateStart()),"yyyy-MM-dd"));
        cell = dataRow.createCell(7);
        cell.setCellValue(DateUtil.format(DateUtil.parse(contract.getLeaseDateEnd()),"yyyy-MM-dd"));

        cell = dataRow.createCell(8);
        cell.setCellValue(contract.getPayableDeviceAmount().doubleValue());
        cell = dataRow.createCell(9);
        cell.setCellValue(contract.getActualReceive().doubleValue());
        cell = dataRow.createCell(10);
        cell.setCellValue(contract.getActualReceiveNoTax().doubleValue());
        cell = dataRow.createCell(11);
        cell.setCellValue(contract.getPlanApportionAmount().doubleValue());
        cell = dataRow.createCell(12);
        cell.setCellValue(contract.getPlanApportionNoTax().doubleValue());

        cell = dataRow.createCell(13);
        cell.setCellValue(contract.getAllocationRatio().doubleValue());

        // 获取月份数据并按月份排序
        Map<String, ServiceFeeMonthlyData> monthlyDataMap = new TreeMap<>();
        for (ServiceFeeMonthlyData monthlyData : contract.getMonthlyDatas()) {
            monthlyDataMap.put(monthlyData.getMonth(), monthlyData);
        }

        // 填充月份数据
        int cellIndex = 14;
        for (String month : allMonths) {
            ServiceFeeMonthlyData data = monthlyDataMap.get(month);
            cell = dataRow.createCell(cellIndex++);
            if (data!=null && data.getAmount() != null) {
                cell.setCellValue(data.getAmount().doubleValue());
            } else {
                cell.setCellValue(0);
            }
            // 设置单元格样式为千分位金额格式
            cell.setCellStyle(amountCellStyle);
        }
    }
}
