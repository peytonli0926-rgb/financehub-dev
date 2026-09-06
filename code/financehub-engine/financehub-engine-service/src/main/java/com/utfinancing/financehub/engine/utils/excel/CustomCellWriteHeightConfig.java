package com.utfinancing.financehub.engine.utils.excel;

import com.alibaba.excel.write.handler.context.RowWriteHandlerContext;
import com.alibaba.excel.write.style.row.AbstractRowHeightStyleStrategy;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.util.ObjectUtils;

import java.util.Iterator;

/**
 * 自定义设置Excel表格行高的配置类。
 * 该类继承了AbstractRowHeightStyleStrategy，通过内容的长度来动态调整行高。
 *
 * <p>默认行高为40。对于字符串内容，按每10个字符添加换行，以计算所需的行数并设置相应的高度。
 *
 */
public class CustomCellWriteHeightConfig extends AbstractRowHeightStyleStrategy {


    /**
     * 设置表头的行高
     *
     * @param row              当前行对象
     * @param relativeRowIndex 当前行的相对索引
     */
    @Override
    protected void setHeadColumnHeight(Row row, int relativeRowIndex) {
        // 默认实现为空，可根据需要自定义表头行高
        row.setHeight((short) (400));
    }

    /**
     * 根据内容设置数据行的行高。
     *
     * @param row              当前行对象
     * @param relativeRowIndex 当前行的相对索引
     */
    @Override
    protected void setContentColumnHeight(Row row, int relativeRowIndex) {
//        Iterator<Cell> cellIterator = row.cellIterator();
//        if (!cellIterator.hasNext()) {
//            return;
//        }
//
//        // 初始化最大行高为1行高度
//        int maxHeight = 1;
//
//        // 遍历行中的每个单元格
//        while (cellIterator.hasNext()) {
//            Cell cell = cellIterator.next();
//            if (cell.getCellType() == CellType.STRING) {
//                // 处理字符串类型的单元格内容
//                String value = cell.getStringCellValue();
//
//                // 每10个字符添加换行符，以便计算所需行数
//                for (int i = 0; i < value.length(); i += 10) {
//                    if (i + 10 < value.length()) {
//                        value = value.substring(0, i) + "\n" + value.substring(i, i + 10) + value.substring(i + 10);
//                    } else {
//                        value = value.substring(0, i) + "\n" + value.substring(i);
//                    }
//                }
//
//                // 计算换行后的行数，以确定行高
//                if (value.contains("\n")) {
//                    int length = value.split("\n").length;
//                    maxHeight = Math.max(maxHeight, length);
//                }
//            }
//        }
        // 根据最大行高设置当前行的高度
        row.setHeight((short) (400));
    }

    /**
     * 在行处理完成后根据行类型设置行高。
     *
     * @param context 行写入的上下文，包含行和相对索引信息
     */
    @Override
    public void afterRowDispose(RowWriteHandlerContext context) {
        if (context.getHead() != null) {
            if (ObjectUtils.isEmpty(context.getRelativeRowIndex())) {
                return;
            }
            // 根据行是否为表头调用相应的行高设置方法
            if (Boolean.TRUE.equals(context.getHead())) {
                this.setHeadColumnHeight(context.getRow(), context.getRelativeRowIndex());
            } else {
                this.setContentColumnHeight(context.getRow(), context.getRelativeRowIndex());
            }
        }
    }
}
