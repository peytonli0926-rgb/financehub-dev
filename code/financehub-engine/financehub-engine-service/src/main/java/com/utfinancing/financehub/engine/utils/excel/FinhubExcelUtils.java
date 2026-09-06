package com.utfinancing.financehub.engine.utils.excel;

import com.alibaba.excel.EasyExcel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class FinhubExcelUtils {

    public static <T> void downloadExcel(HttpServletResponse response, String sheetName, List<T> datas, Class<T> cla) throws IOException {
        EasyExcel.write(response.getOutputStream(), cla)
                .registerWriteHandler(new CustomCellStyleStrategy())
                .registerWriteHandler(new CustomCellWriteHeightConfig())
                .registerWriteHandler(new CustomCellWriteWidthConfig())
                .sheet(sheetName)
                .doWrite(datas);
    }
}
