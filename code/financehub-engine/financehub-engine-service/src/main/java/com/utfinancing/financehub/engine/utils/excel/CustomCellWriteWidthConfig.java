package com.utfinancing.financehub.engine.utils.excel;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomCellWriteWidthConfig extends AbstractColumnWidthStyleStrategy {
    // 缓存每个工作表和列的最大宽度，以优化性能
    private final Map<Integer, Map<Integer, Integer>> CACHE = new HashMap<>();

    /**
     * 根据内容长度设置列宽，最大为20字符。
     *
     * @param writeSheetHolder 工作表的元数据
     * @param cellDataList     单元格数据对象列表
     * @param cell             当前处理的单元格
     * @param head             列的头部信息
     * @param integer          未使用的参数
     * @param isHead           布尔值，指示当前单元格是否为表头
     */
    @Override
    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer integer, Boolean isHead) {
        // 判断是否需要设置列宽
        boolean needSetWidth = isHead || !CollectionUtils.isEmpty(cellDataList);
        if (needSetWidth) {
            // 获取或初始化当前工作表的宽度缓存
            Map<Integer, Integer> maxColumnWidthMap = CACHE.computeIfAbsent(writeSheetHolder.getSheetNo(), k -> new HashMap<>());
            Integer columnWidth = this.dataLength(cellDataList, cell, isHead);

            // 在指定范围内设置列宽
            if (columnWidth > 0) {
                if (columnWidth > 10) {
                    columnWidth = 20;  // 超过阈值时设置为最大宽度
                } else {
                    columnWidth = 10;  // 设置最小宽度
                }

                // 获取当前列的最大宽度（如果已经设置）
                Integer maxColumnWidth = maxColumnWidthMap.get(cell.getColumnIndex());
                // 如果新宽度超过当前最大值或尚未设置最大宽度，则更新
                if (maxColumnWidth == null || columnWidth > maxColumnWidth) {
                    maxColumnWidthMap.put(cell.getColumnIndex(), columnWidth);
                    // 在工作表中设置宽度，字符间距进行适当调整
                    Sheet sheet = writeSheetHolder.getSheet();
                    sheet.setColumnWidth(cell.getColumnIndex(), 256 * columnWidth + 184);
                }
            }
        }
    }

    /**
     * 计算单元格内容的长度，用于确定列宽。
     *
     * @param cellDataList 单元格数据对象列表（用于非表头单元格）
     * @param cell         当前处理的单元格
     * @param isHead       布尔值，指示当前单元格是否为表头
     * @return 单元格内容的计算长度
     */
    private Integer dataLength(List<WriteCellData<?>> cellDataList, Cell cell, Boolean isHead) {
        if (isHead) {
            // 返回表头单元格内容的字节长度
            return cell.getStringCellValue().getBytes().length;
        } else {
            CellData<?> cellData = cellDataList.get(0); // 获取列表中的第一个数据项
            CellDataTypeEnum type = cellData.getType(); // 获取单元格数据类型

            if (type == null) {
                return -1;
            } else {
                switch (type) {
                    case STRING:
                        // 处理换行符，计算到第一个换行符为止的长度
                        int index = cellData.getStringValue().indexOf("\n");
                        return index != -1 ?
                                cellData.getStringValue().substring(0, index).getBytes().length + 1
                                : cellData.getStringValue().getBytes().length + 1;
                    case BOOLEAN:
                        // 布尔类型内容的长度
                        return cellData.getBooleanValue().toString().getBytes().length;
                    case NUMBER:
                        // 数字类型内容的长度
                        return cellData.getNumberValue().toString().getBytes().length;
                    default:
                        return -1;
                }
            }
        }
    }
}