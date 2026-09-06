package com.utfinancing.financehub.etl.middle.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 28/11/2023
 */
public class VoucherTypeMapUtil {

    private static Map<String, String> voucherTypeMap;

    static {
        voucherTypeMap = new HashMap<>();
        voucherTypeMap.put("现金", "01");
        voucherTypeMap.put("银行", "02");
        voucherTypeMap.put("转账", "03");
        voucherTypeMap.put("自动银行", "04");
        voucherTypeMap.put("自动转账", "05");
    }

    public static String getVoucherTypeCode(String voucherTypeName){
        return voucherTypeMap.get(voucherTypeName);
    }

}
