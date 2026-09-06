package com.utfinancing.financehub.common.core.utils.poi;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
//import com.ruoyi.common.annotation.Excel;
//import com.ruoyi.common.annotation.Excel.ColumnType;
//import com.ruoyi.common.annotation.Excel.Type;
//import com.ruoyi.common.annotation.Excels;
//import com.ruoyi.common.config.RuoYiConfig;
//import com.ruoyi.common.core.domain.AjaxResult;
//import com.ruoyi.common.exception.CustomException;
//import com.ruoyi.common.utils.DateUtils;
//import com.ruoyi.common.utils.DeptUtils;
//import com.ruoyi.common.utils.DictUtils;
//import com.ruoyi.common.utils.file.FileTypeUtils;
//import com.ruoyi.common.utils.file.ImageUtils;
import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.common.core.annotation.Excel.Type;
import com.utfinancing.financehub.common.core.annotation.Excel.ColumnType;
import com.utfinancing.financehub.common.core.annotation.Excels;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.file.FileTypeUtils;
import com.utfinancing.financehub.common.core.utils.file.ImageUtils;
import com.utfinancing.financehub.common.core.web.domain.AjaxResult;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFDrawing;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Excel多sheet导出工具
 *
 * @author ruoyi
 */
public class ExcelManySheetUtil
{
    private static final Logger log = LoggerFactory.getLogger(ExcelManySheetUtil.class);

    /**
     * Excel sheet最大行数，默认65536
     */
    public static final int sheetSize = 65535;

    /**
     * 工作表名称
     */
    private String fileName;

    /**
     * 导出类型（EXPORT:导出数据；IMPORT：导入模板）
     */
    private Excel.Type type;

    /**
     * 工作薄对象
     */
    private Workbook wb;

    /**
     * 工作表对象
     */
    private Sheet sheet;

    /**
     * 样式列表
     */
    private Map<String, CellStyle> styles;

    /**
     * 多sheet导出数据列表
     */
    Map<String, List<?>> map;

    /**
     * 注解列表
     */
    private List<Object[]> fields;

    /**
     * 最大高度
     */
    private short maxHeight;

    /**
     * 统计列表
     */
    private Map<Integer, Double> statistics = new HashMap<Integer, Double>();

    /**
     * 数字格式
     */
    private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("######0.00");

    /**
     * 实体对象
     */
    public Class clazz;


    /**
     * 初始化，并创建Workbook
     * @param map
     * @param fileName
     */
    public ExcelManySheetUtil(Map<String, List<?>> map, String fileName, Excel.Type type) {

        if(map == null){
            map = new LinkedHashMap<>();
        }
        this.map = map;
        this.fileName = fileName;
        this.type = type;
        createWorkbook();
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @param response
     * @param sheetNameMap 导出数据集合
     * @param classMap     工作表的名称
     * @return 结果
     */
    public AjaxResult exportManySheetExcel(HttpServletResponse response, Map<String, String> sheetNameMap, Map<String, Class> classMap)
    {
        AtomicInteger no = new AtomicInteger();
        map.forEach((k, v)->{
            this.clazz = classMap.get(k);
            createExcelField();
            // 取出一共有多少个sheet.
            double sheetNo = Math.ceil(v.size() / sheetSize);
            for (int index = 0; index <= sheetNo; index++)
            {
                createSheet(sheetNo, no.get(), sheetNameMap.get(k) + (index==0?"":index));
                no.getAndIncrement();
                // 产生一行
                Row row = sheet.createRow(0);
                SXSSFDrawing patr = (SXSSFDrawing) sheet.createDrawingPatriarch();
                int column = 0;
                // 写入各个字段的列头名称
                for (Object[] os : fields)
                {
                    Excel excel = (Excel) os[1];
                    if (Type.IMPORT.equals(type)) {
//                        if (ColumnType.STRING == excel.cellType()) {
//                            CellStyle cellStyle = wb.createCellStyle();
//                            DataFormat dataFormat = wb.createDataFormat();
//                            cellStyle.setDataFormat(dataFormat.getFormat("@"));// 文本类型
//                            sheet.setDefaultColumnStyle(column, cellStyle);
//                        }  else
                        if (ColumnType.DATE == excel.cellType()) {
                            CellStyle cellStyle = wb.createCellStyle();
                            DataFormat dataFormat = wb.createDataFormat();
                            cellStyle.setDataFormat(dataFormat.getFormat("yyyy-MM-dd"));// 日期格式
                            sheet.setDefaultColumnStyle(column, cellStyle);
                        }
                    }
                    this.createCell(excel, row, column++,patr);
                }
                if (Excel.Type.EXPORT.equals(type))
                {
                    fillExcelData(index, row, v);
                    addStatisticsRow();
                }
            }
        });

//        OutputStream out = null;
        try
        {
//            String filename = encodingFilename(fileName);
//            out = new FileOutputStream(getAbsoluteFile(filename));
            wb.write(response.getOutputStream());
            return AjaxResult.success();
        }
        catch (Exception e)
        {
            log.error("导出Excel异常{}", e.getMessage());
            throw new ServiceException("导出Excel失败，请联系网站管理员！");
        }
        finally
        {
            IOUtils.closeQuietly(wb);
//            if (wb != null)
//            {
//                try
//                {
//                    wb.close();
//                }
//                catch (IOException e1)
//                {
//                    e1.printStackTrace();
//                }
//            }
//            if (out != null)
//            {
//                try
//                {
//                    out.close();
//                }
//                catch (IOException e1)
//                {
//                    e1.printStackTrace();
//                }
//            }
        }
    }

    /**
     * 填充excel数据
     *
     * @param index 序号
     * @param row 单元格行
     */
    public void fillExcelData(int index, Row row, List<?> list)
    {
        int startNo = index * sheetSize;
        int endNo = Math.min(startNo + sheetSize, list.size());
        for (int i = startNo; i < endNo; i++)
        {
            row = sheet.createRow(i + 1 - startNo);
            // 得到导出对象.
            Object vo = list.get(i);
            int column = 0;
            for (Object[] os : fields)
            {
                Field field = (Field) os[0];
                Excel excel = (Excel) os[1];
                // 设置实体类私有属性可访问
                field.setAccessible(true);
                this.addCell(excel, row, vo, field, column++);
            }
        }
    }

    /**
     * 创建表格样式
     *
     * @param wb 工作薄对象
     * @return 样式列表
     */
    private Map<String, CellStyle> createStyles(Workbook wb)
    {
        // 写入各条记录,每条记录对应excel表中的一行
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        CellStyle style = getBaseCellStyle(wb);
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        style.setFont(dataFont);
        styles.put("data", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(headerFont);
        styles.put("header", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        Font totalFont = wb.createFont();
        totalFont.setFontName("Arial");
        totalFont.setFontHeightInPoints((short) 10);
        style.setFont(totalFont);
        styles.put("total", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.LEFT);
        styles.put("data1", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.CENTER);
        styles.put("data2", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.RIGHT);
        styles.put("data3", style);

        return styles;
    }

    /**
     * 创建单元格
     */
    public Cell createCell(Excel attr, Row row, int column, SXSSFDrawing patr)
    {
        // 创建列
        Cell cell = row.createCell(column);
        // 写入列信息
        cell.setCellValue(attr.name());
        if (row.getRowNum()==0 && StringUtils.isNotEmpty(attr.comment())) {
            XSSFComment comment = (XSSFComment) patr.createCellComment(
                    new XSSFClientAnchor(0, 0, 0, 0, (short)column, row.getRowNum(), (short)column+2, row.getRowNum()+2));
            comment.setString(new XSSFRichTextString(attr.comment()));
            cell.setCellComment(comment);
        }
        setDataValidation(attr, row, column);
        cell.setCellStyle(styles.get("header"));
        return cell;
    }

    /**
     * 设置单元格信息
     *
     * @param value 单元格值
     * @param attr 注解相关
     * @param cell 单元格信息
     */
    public void setCellVo(Object value, Excel attr, Cell cell)
    {
        if (ColumnType.STRING == attr.cellType())
        {
            cell.setCellValue(Validator.isNull(value) ? attr.defaultValue() : value + attr.suffix());
        }
        else if (ColumnType.NUMERIC == attr.cellType())
        {
            if (Validator.isNotNull(value))
            {
                cell.setCellValue(StrUtil.contains(Convert.toStr(value), ".") ? Convert.toDouble(value) : Convert.toInt(value));
            }
        }
        else if (ColumnType.DATE == attr.cellType()) {
            if (StringUtils.isNotNull(value)) {
                CellStyle style = getDateFormatCellStyle();
                cell.setCellStyle(style);
                cell.setCellValue((Date) value);
            }
        }
        else if (ColumnType.IMAGE == attr.cellType())
        {
            ClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) cell.getColumnIndex(), cell.getRow().getRowNum(), (short) (cell.getColumnIndex() + 1),
                    cell.getRow().getRowNum() + 1);
            String imagePath = Convert.toStr(value);
            if (Validator.isNotEmpty(imagePath))
            {
                byte[] data = ImageUtils.getImage(imagePath);
                getDrawingPatriarch(cell.getSheet()).createPicture(anchor,
                        cell.getSheet().getWorkbook().addPicture(data, getImageType(data)));
            }
        }
    }

    private CellStyle getDateFormatCellStyle() {
        CellStyle style = getBaseCellStyle(wb);
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        dataFont.setColor(IndexedColors.BLACK.index);
        DataFormat dataFormat = wb.createDataFormat();
        style.setDataFormat(dataFormat.getFormat("yyyy-MM-dd"));// 日期格式
        style.setFont(dataFont);
        return style;
    }

    private CellStyle getBaseCellStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        return style;
    }

    /**
     * 获取画布
     */
    public static Drawing<?> getDrawingPatriarch(Sheet sheet)
    {
        if (sheet.getDrawingPatriarch() == null)
        {
            sheet.createDrawingPatriarch();
        }
        return sheet.getDrawingPatriarch();
    }

    /**
     * 获取图片类型,设置图片插入类型
     */
    public int getImageType(byte[] value)
    {
        String type = FileTypeUtils.getFileExtendName(value);
        if ("JPG".equalsIgnoreCase(type))
        {
            return Workbook.PICTURE_TYPE_JPEG;
        }
        else if ("PNG".equalsIgnoreCase(type))
        {
            return Workbook.PICTURE_TYPE_PNG;
        }
        return Workbook.PICTURE_TYPE_JPEG;
    }

    /**
     * 创建表格样式
     */
    public void setDataValidation(Excel attr, Row row, int column)
    {
        if (attr.name().indexOf("注：") >= 0)
        {
            sheet.setColumnWidth(column, 6000);
        }
        else
        {
            // 设置列宽
            sheet.setColumnWidth(column, (int) ((attr.width() + 0.72) * 256));
        }
        // 如果设置了提示信息则鼠标放上去提示.
        if (Validator.isNotEmpty(attr.prompt()))
        {
            // 这里默认设了2-101列提示.
            setXSSFPrompt(sheet, "", attr.prompt(), 1, 100, column, column);
        }
        // 如果设置了combo属性则本列只能选择不能输入
        if (attr.combo().length > 0)
        {
            // 这里默认设了2-101列只能选择不能输入.
            setXSSFValidation(sheet, attr.combo(), 1, 100, column, column);
        }
    }

    /**
     * 添加单元格
     */
    public Cell addCell(Excel attr, Row row, Object vo, Field field, int column)
    {
        Cell cell = null;
        try
        {
            // 设置行高
            row.setHeight(maxHeight);
            // 根据Excel中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
            if (attr.isExport())
            {
                // 创建cell
                cell = row.createCell(column);
                int align = attr.align().getCode();
                cell.setCellStyle(styles.get("data" + (align >= 1 && align <= 3 ? align : "")));

                // 用于读取对象中的属性
                Object value = getTargetValue(vo, field, attr);
                String dateFormat = attr.dateFormat();
                String readConverterExp = attr.readConverterExp();
                String separator = attr.separator();
//                String dictType = attr.dictType();
//                String deptTra = attr.deptTra();
                if (Validator.isNotEmpty(dateFormat) && Validator.isNotNull(value))
                {
                    cell.setCellValue(parseDateToStr(dateFormat, value));
                }
                else if (Validator.isNotEmpty(readConverterExp) && Validator.isNotNull(value))
                {
                    cell.setCellValue(convertByExp(Convert.toStr(value), readConverterExp, separator));
                }
//                else if (Validator.isNotEmpty(dictType) && Validator.isNotNull(value))
//                {
//                    cell.setCellValue(convertDictByExp(Convert.toStr(value), dictType, separator));
//                }
//                else if (Validator.isNotEmpty(deptTra) && Validator.isNotNull(value))
//                {
////                    cell.setCellValue(DeptUtils.getDeptNameByDeptId(Convert.toStr(value)));
//                }
                else if (value instanceof BigDecimal && -1 != attr.scale())
                {
                    cell.setCellValue((((BigDecimal) value).setScale(attr.scale(), attr.roundingMode())).toString());
                }
                else
                {
                    // 设置单元格类型
                    setCellVo(value, attr, cell);
                }
                addStatisticsData(column, Convert.toStr(value), attr);
            }
        }
        catch (Exception e)
        {
            log.error("导出Excel失败{}", e);
        }
        return cell;
    }

    /**
     * 格式化不同类型的日期对象
     *
     * @param dateFormat 日期格式
     * @param val        被格式化的日期对象
     * @return 格式化后的日期字符
     */
    public String parseDateToStr(String dateFormat, Object val) {
        if (val == null) {
            return "";
        }
        String str;
        if (val instanceof Date) {
            str = DateUtils.parseDateToStr(dateFormat, (Date) val);
        } else if (val instanceof LocalDateTime) {
            str = DateUtils.parseDateToStr(dateFormat, DateUtils.toDate((LocalDateTime) val));
        } else if (val instanceof LocalDate) {
            str = DateUtils.parseDateToStr(dateFormat, DateUtils.toDate((LocalDate) val));
        } else {
            str = val.toString();
        }
        return str;
    }

    /**
     * 设置 POI XSSFSheet 单元格提示
     *
     * @param sheet 表单
     * @param promptTitle 提示标题
     * @param promptContent 提示内容
     * @param firstRow 开始行
     * @param endRow 结束行
     * @param firstCol 开始列
     * @param endCol 结束列
     */
    public void setXSSFPrompt(Sheet sheet, String promptTitle, String promptContent, int firstRow, int endRow,
                              int firstCol, int endCol)
    {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = helper.createCustomConstraint("DD1");
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        dataValidation.createPromptBox(promptTitle, promptContent);
        dataValidation.setShowPromptBox(true);
        sheet.addValidationData(dataValidation);
    }

    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框.
     *
     * @param sheet 要设置的sheet.
     * @param textlist 下拉框显示的内容
     * @param firstRow 开始行
     * @param endRow 结束行
     * @param firstCol 开始列
     * @param endCol 结束列
     * @return 设置好的sheet.
     */
    public void setXSSFValidation(Sheet sheet, String[] textlist, int firstRow, int endRow, int firstCol, int endCol)
    {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // 加载下拉列表内容
        DataValidationConstraint constraint = helper.createExplicitListConstraint(textlist);
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        // 处理Excel兼容性问题
        if (dataValidation instanceof XSSFDataValidation)
        {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        }
        else
        {
            dataValidation.setSuppressDropDownArrow(false);
        }

        sheet.addValidationData(dataValidation);
    }

    /**
     * 解析导出值 0=男,1=女,2=未知
     *
     * @param propertyValue 参数值
     * @param converterExp 翻译注解
     * @param separator 分隔符
     * @return 解析后值
     */
    public static String convertByExp(String propertyValue, String converterExp, String separator)
    {
        StringBuilder propertyString = new StringBuilder();
        String[] convertSource = converterExp.split(",");
        for (String item : convertSource)
        {
            String[] itemArray = item.split("=");
            if (StrUtil.containsAny(separator, propertyValue))
            {
                for (String value : propertyValue.split(separator))
                {
                    if (itemArray[0].equals(value))
                    {
                        propertyString.append(itemArray[1] + separator);
                        break;
                    }
                }
            }
            else
            {
                if (itemArray[0].equals(propertyValue))
                {
                    return itemArray[1];
                }
            }
        }
        return StrUtil.strip(propertyString.toString(), null,separator);
    }

    /**
     * 反向解析值 男=0,女=1,未知=2
     *
     * @param propertyValue 参数值
     * @param converterExp 翻译注解
     * @param separator 分隔符
     * @return 解析后值
     */
    public static String reverseByExp(String propertyValue, String converterExp, String separator)
    {
        StringBuilder propertyString = new StringBuilder();
        String[] convertSource = converterExp.split(",");
        for (String item : convertSource)
        {
            String[] itemArray = item.split("=");
            if (StrUtil.containsAny(separator, propertyValue))
            {
                for (String value : propertyValue.split(separator))
                {
                    if (itemArray[1].equals(value))
                    {
                        propertyString.append(itemArray[0] + separator);
                        break;
                    }
                }
            }
            else
            {
                if (itemArray[1].equals(propertyValue))
                {
                    return itemArray[0];
                }
            }
        }
        return StrUtil.strip(propertyString.toString(), null,separator);
    }

    /**
     * 解析字典值
     *
     * @param dictValue 字典值
     * @param dictType 字典类型
     * @param separator 分隔符
     * @return 字典标签
     */
    public static String convertDictByExp(String dictValue, String dictType, String separator)
    {
        return "DictUtils.getDictLabel(dictType, dictValue, separator)";
    }

    /**
     * 反向解析值字典值
     *
     * @param dictLabel 字典标签
     * @param dictType 字典类型
     * @param separator 分隔符
     * @return 字典值
     */
    public static String reverseDictByExp(String dictLabel, String dictType, String separator)
    {
        return "DictUtils.getDictValue(dictType, dictLabel, separator)";
    }

    /**
     * 合计统计信息
     */
    private void addStatisticsData(Integer index, String text, Excel entity)
    {
        if (entity != null && entity.isStatistics())
        {
            Double temp = 0D;
            if (!statistics.containsKey(index))
            {
                statistics.put(index, temp);
            }
            try
            {
                temp = Double.valueOf(text);
            }
            catch (NumberFormatException e)
            {
            }
            statistics.put(index, statistics.get(index) + temp);
        }
    }

    /**
     * 创建统计行
     */
    public void addStatisticsRow()
    {
        if (statistics.size() > 0)
        {
            Cell cell = null;
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            Set<Integer> keys = statistics.keySet();
            cell = row.createCell(0);
            cell.setCellStyle(styles.get("total"));
            cell.setCellValue("合计");

            for (Integer key : keys)
            {
                cell = row.createCell(key);
                cell.setCellStyle(styles.get("total"));
                cell.setCellValue(DOUBLE_FORMAT.format(statistics.get(key)));
            }
            statistics.clear();
        }
    }

    /**
     * 编码文件名
     */
    public String encodingFilename(String filename)
    {
        filename = UUID.randomUUID().toString() + "_" + filename + ".xlsx";
        return filename;
    }

    /**
     * 获取下载路径
     *
     * @param filename 文件名称
     */
//    public String getAbsoluteFile(String filename)
//    {
//        String downloadPath = RuoYiConfig.getDownloadPath() + filename;
//        File desc = new File(downloadPath);
//        if (!desc.getParentFile().exists())
//        {
//            desc.getParentFile().mkdirs();
//        }
//        return downloadPath;
//    }

    /**
     * 获取bean中的属性值
     *
     * @param vo 实体对象
     * @param field 字段
     * @param excel 注解
     * @return 最终的属性值
     * @throws Exception
     */
    private Object getTargetValue(Object vo, Field field, Excel excel) throws Exception
    {
        Object o = field.get(vo);
        if (Validator.isNotEmpty(excel.targetAttr()))
        {
            String target = excel.targetAttr();
            if (target.contains("."))
            {
                String[] targets = target.split("[.]");
                for (String name : targets)
                {
                    o = getValue(o, name);
                }
            }
            else
            {
                o = getValue(o, target);
            }
        }
        return o;
    }

    /**
     * 以类的属性的get方法方法形式获取值
     *
     * @param o
     * @param name
     * @return value
     * @throws Exception
     */
    private Object getValue(Object o, String name) throws Exception
    {
        if (Validator.isNotNull(o) && Validator.isNotEmpty(name))
        {
            Class<?> clazz = o.getClass();
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            o = field.get(o);
        }
        return o;
    }

    /**
     * 得到所有定义字段
     */
    private void createExcelField()
    {
        this.fields = new ArrayList<Object[]>();
        List<Field> tempFields = new ArrayList<>();
        tempFields.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
        tempFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        for (Field field : tempFields)
        {
            // 单注解
            if (field.isAnnotationPresent(Excel.class))
            {
                putToField(field, field.getAnnotation(Excel.class));
            }

            // 多注解
            if (field.isAnnotationPresent(Excels.class))
            {
                Excels attrs = field.getAnnotation(Excels.class);
                Excel[] excels = attrs.value();
                for (Excel excel : excels)
                {
                    putToField(field, excel);
                }
            }
        }
        this.fields = this.fields.stream().sorted(Comparator.comparing(objects -> ((Excel) objects[1]).sort())).collect(Collectors.toList());
        this.maxHeight = getRowHeight();
    }

    /**
     * 根据注解获取最大行高
     */
    public short getRowHeight()
    {
        double maxHeight = 0;
        for (Object[] os : this.fields)
        {
            Excel excel = (Excel) os[1];
            maxHeight = maxHeight > excel.height() ? maxHeight : excel.height();
        }
        return (short) (maxHeight * 20);
    }

    /**
     * 放到字段集合中
     */
    private void putToField(Field field, Excel attr)
    {
        if (attr != null && (attr.type() == Type.ALL || attr.type() == type))
        {
            this.fields.add(new Object[] { field, attr });
        }
    }

    /**
     * 创建一个工作簿
     */
    public void createWorkbook()
    {
        this.wb = new SXSSFWorkbook(500);
    }

    /**
     * 创建工作表
     *
     * @param sheetNo sheet数量
     * @param index 序号
     */
    public void createSheet(double sheetNo, int index, String sheetName)
    {
        this.sheet = wb.createSheet();
        this.styles = createStyles(wb);
        // 设置工作表的名称.
        if (sheetNo == 0)
        {
            wb.setSheetName(index, sheetName);
        }
        else
        {
            wb.setSheetName(index, sheetName + index);
        }
    }

    /**
     * 获取单元格值
     *
     * @param row 获取的行
     * @param column 获取单元格列号
     * @return 单元格值
     */
    public Object getCellValue(Row row, int column)
    {
        if (row == null)
        {
            return row;
        }
        Object val = "";
        try
        {
            Cell cell = row.getCell(column);
            if (Validator.isNotNull(cell))
            {
                if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA)
                {
                    val = cell.getNumericCellValue();
                    if (DateUtil.isCellDateFormatted(cell))
                    {
                        val = DateUtil.getJavaDate((Double) val); // POI Excel 日期格式转换
                    }
                    else
                    {
                        if ((Double) val % 1 != 0)
                        {
                            val = new BigDecimal(val.toString());
                        }
                        else
                        {
                            val = new DecimalFormat("0").format(val);
                        }
                    }
                }
                else if (cell.getCellType() == CellType.STRING)
                {
                    val = cell.getStringCellValue();
                }
                else if (cell.getCellType() == CellType.BOOLEAN)
                {
                    val = cell.getBooleanCellValue();
                }
                else if (cell.getCellType() == CellType.ERROR)
                {
                    val = cell.getErrorCellValue();
                }

            }
        }
        catch (Exception e)
        {
            return val;
        }
        return val;
    }

    /**
     * 判断是否是EXCEL文件
     * @param inputStream
     * @return
     */
    public static boolean isExcelFile(InputStream inputStream) {
        boolean result = false;
        try {
            FileMagic fileMagic = FileMagic.valueOf(inputStream);
            if (Objects.equals(fileMagic, FileMagic.OLE2)
                    || Objects.equals(fileMagic, FileMagic.OOXML)) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }


}
