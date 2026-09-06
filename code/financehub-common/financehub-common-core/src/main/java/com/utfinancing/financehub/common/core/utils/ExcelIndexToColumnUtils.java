package com.utfinancing.financehub.common.core.utils;

import cn.hutool.core.util.ObjectUtil;

/**
 * <ul>
 * <li>Project : financehub-common</li>
 * <li>ClassName : com.utfinancing.financehub.common.core.utils.ExcelIndexToColumnUtils</li>
 * <li>CreateTime : 2024/02/04 14:54</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
public class ExcelIndexToColumnUtils {

   public static String excelIndexToColumn(int index) {
       if (ObjectUtil.isNull(index)) {
           return "";
       }
       int high = index / 26;
       int low = index % 26;
       String transStr = String.valueOf((char) (low + 65));
       if (high > 0) {
           transStr = excelIndexToColumn(high) + transStr;
       }
       return transStr;
   }
}
