package com.utfinancing.financehub.engine.utils;

import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.common.core.utils.poi.ExcelHandlerAdapter;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.enums.ProcessStatusEnum;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class ExcelExportUtil {

    public static void setResponse(HttpServletResponse response, String fileName) {

        response.setContentType(XSSFWorkbookType.XLSX.getContentType());

        try {
            fileName = URLEncoder.encode(fileName, "utf8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
    }

    public static <T> void export(HttpServletResponse response, List<T> data, Class<T> title, String sheetName) {
        setResponse(response, sheetName + ".xlsx");
        ExcelUtil<T> util = new ExcelUtil<>(title);
        util.init(data, sheetName, "", Excel.Type.EXPORT);
        util.exportExcel(response);
    }

    public static class ProcessStatusExcelHandlerAdapter implements ExcelHandlerAdapter {
        @Override
        public Object format(Object o, String[] strings) {
            return Optional.ofNullable(o)
                    .map(Objects::toString)
                    .map(ProcessStatusEnum::getDescByCode)
                    .orElse(null);
        }
    }

    public static class LocalDateExcelHandlerAdapter implements ExcelHandlerAdapter {

        @Override
        public Object format(Object value, String[] args) {
            return Optional.ofNullable(value)
                    .map(Objects::toString)
                    .map(LocalDate::parse)
                    .orElse(null);
        }
    }
}
