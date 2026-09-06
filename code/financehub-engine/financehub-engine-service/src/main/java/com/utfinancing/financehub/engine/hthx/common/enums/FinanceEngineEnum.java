package com.utfinancing.financehub.engine.hthx.common.enums;

import com.utfinancing.financehub.engine.hthx.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 财务引擎枚举值汇总
 * @author: zhangli.chen
 * @date 2024/12/30 14:00
 * @param: null
 * @return null
 **/
public interface FinanceEngineEnum {


    @Getter
    @AllArgsConstructor
    enum WorkDayParam implements FinanceEngineEnum {
        PARAM_WORK_DAY("workDay", "2"),
        PARAM_ENABLE("enable", "Y");
        private String key;
        private String value;
    }

    /**
     * @description: 有效标志(Y：有效；N：无效)
     **/
    @Getter
    @AllArgsConstructor
    enum ValidFlag implements FinanceEngineEnum {
        YES("Y", "有效"),
        NO("N", "无效");
        private String key;
        private String value;
    }

    /**
     * @description: 任务结果汇总状态
     **/
    @Getter
    @AllArgsConstructor
    enum TaskState implements FinanceEngineEnum{
        TaskState_10("10", "新建"),
        TaskState_20("20", "执行中"),
        TaskState_30("30", "执行成功"),
        TaskState_40("40", "执行失败"),
        TaskState_50("50", "文件生成中")
        ;

        private String key;
        private String value;

        public static String getValue(String key) {
            for (TaskState o : values()) {
                if (o.key.equalsIgnoreCase(key)) {
                    return o.getValue();
                }
            }
            return null;
        }
    }

    /**
     * @description: 任务执行明细状态
     **/
    @Getter
    @AllArgsConstructor
    enum TaskStateDetail implements FinanceEngineEnum{
        TaskStateDetail_41("41", "业务系统异步生成凭证任务执行出现异常。"),
        TaskStateDetail_42("42", "开票异步生成凭证任务执行出现异常。"),
        TaskStateDetail_43("43", "资金异步生成凭证任务执行出现异常。"),
        TaskStateDetail_44("44", "异步生成凭证任务执行失败，具体报错原因请参见行表。"),
        TaskStateDetail_51("51", "生成凭证比对excel文件路径为空。"),
        TaskStateDetail_52("52", "生成凭证比对excel文件生成前，生成具体凭证的任务失败了。"),
        ;

        private String key;
        private String value;

        public static String getValue(String key) {
            for (TaskStateDetail o : values()) {
                if (o.key.equalsIgnoreCase(key)) {
                    return o.getValue();
                }
            }
            return null;
        }
    }


    /**
     * @description: 系统来源
     **/
    @Getter
    @AllArgsConstructor
    enum SystemNameExecuteSystemDataJob implements FinanceEngineEnum{
        XWXT("XWXT", "小微系统"),
        SYCXT("SYCXT", "商用车系统"),
        CYCXT("CYCXT", "乘用车系统"),
        TYPT("TYPT", "统一平台");

        private String key;
        private String value;

        public static String getValue(String key) {
            for (SystemNameExecuteSystemDataJob o : values()) {
                if (o.key.equalsIgnoreCase(key)) {
                    return o.getValue();
                }
            }
            return null;
        }
    }

    /**
     * @description: 新凭证方向
     **/
    @Getter
    @AllArgsConstructor
    enum NewVoucherDirection implements FinanceEngineEnum{
        NEW_VOUCHER_DEBIT("DR", "新凭证借方分录"),
        NEW_VOUCHER_CREDIT("CR", "新凭证贷方分录");

        private String key;
        private String value;

        public static String getValue(String key) {
            for (NewVoucherDirection o : values()) {
                if (o.key.equalsIgnoreCase(key)) {
                    return o.getValue();
                }
            }
            return null;
        }
    }



    /**
     * @description: 旧凭证方向
     **/
    @Getter
    @AllArgsConstructor
    enum OlderVoucherDirection implements FinanceEngineEnum{
        OLDER_VOUCHER_DEBIT("DR", "原凭证借方分录"),
        OLDER_VOUCHER_CREDIT("CR", "原凭证贷方分录");

        private String key;
        private String value;

        public static String getValue(String key) {
            for (OlderVoucherDirection o : values()) {
                if (o.key.equalsIgnoreCase(key)) {
                    return o.getValue();
                }
            }
            return null;
        }
    }


    /**
     * @description: 数字0-10
     **/
    @Getter
    @AllArgsConstructor
    enum Numbers implements FinanceEngineEnum{
        ZERO(0, "0"),
        ONE(1, "1"),
        TWO(2, "2"),
        THREE(3, "3"),
        FOUR(4, "4"),
        FIVE(5, "5"),
        SIX(6, "6"),
        SEVEN(7, "7"),
        EIGHT(8, "8"),
        NINE(9, "9"),
        TEN(10, "10"),
        HUNDRED(100,"100"),
        FIVE_HUNDRED(500,"500"),
        THOUSAND(1000,"1000"),
        TEN_THOUSAND(10000,"10000"),
        ONE_HUNDRED_THOUSAND(100000,"100000");

        private int key;
        private String value;

        public static String getValue(int key) {
            for (Numbers o : values()) {
                if (o.key == (key)) {
                    return o.getValue();
                }
            }
            return null;
        }
    }

    /**
     * @description: excel填充字段
     **/
    @Getter
    @AllArgsConstructor
    enum ExcelFillField implements FinanceEngineEnum{
        VOUCHER_RULE_TOTAL_NUMBER("totalNumber", "事件总笔数"),
        VOUCHER_RULE_SAME_NUMBER("sameNumber", "结果一致笔数"),
        VOUCHER_RULE_DIFFERENT_NUMBER("differentNumber", "结果不一致笔数");

        private String key;
        private String value;

        public static String getValue(String key) {
            for (ExcelFillField o : values()) {
                if (o.key.equalsIgnoreCase(key)) {
                    return o.getValue();
                }
            }
            return null;
        }
    }

    /**
     * @description: 不参与凭证分录属性值比对的数据字典配置
     **/
    @Getter
    @AllArgsConstructor
    enum NotCompareAttribute implements FinanceEngineEnum{
        DICTIONARY_CATEGORY("VoucherRule", "VoucherRule"),
        DICTIONARY_TYPE("RuleNotCompare", "RuleNotCompare");

        private String key;
        private String value;

        public static String getValue(String key) {
            for (NotCompareAttribute o : values()) {
                if (o.key.equalsIgnoreCase(key)) {
                    return o.getValue();
                }
            }
            return null;
        }
    }



    /**
     * @description: 不存于属性值比对的列名
     **/
    @Getter
    @AllArgsConstructor
    enum ExcelColumnNames implements FinanceEngineEnum{
        id(0, "id"),
        sceneName(1, "sceneName"),
        systemName(2, "systemName"),
        voucherDirection(3, "voucherDirection"),
        orgId(4, "orgId"),
        orgName(5, "orgName"),
        periodCode(6, "periodCode"),
        voucherType(7, "voucherType"),
        voucherTypeName(8, "voucherTypeName"),
        voucherDate(9, "voucherDate"),
        businessDate(10, "businessDate"),
        createUserNo(11, "createUserNo"),
        currency(12, "currency"),
        accountCode(13, "accountCode"),
        accountName(14, "accountName"),
        voucherSummary(15, "voucherSummary"),
        debitAmount(16, "debitAmount"),
        creditAmount(17, "creditAmount");
        private Integer columnNumber;
        private String columnName;

        /**
         * @description: 通过列名获取列号
         **/
        public static Integer getColumnNumber(String columnName) {
            if (StringUtils.isEmpty(columnName)) {
                return null;
            }
            for (ExcelColumnNames o : values()) {
                if (o.columnName.equalsIgnoreCase(columnName)) {
                    return o.getColumnNumber();
                }
            }
            return null;
        }

        /**
         * @description: 通过列号获取列名
         **/
        public static String getColumnName(Integer columnNumber) {
            if (StringUtils.isEmpty(columnNumber)) {
                return null;
            }
            for (ExcelColumnNames enums : values()) {
                if (columnNumber.equals(enums.getColumnNumber())) {
                    return enums.getColumnName();
                }
            }
            return null;
        }

        /**
         * @description: 通过列号获取列名
         **/
        public static List<String> getAllColumnName() {
            List<String> columnNameList = new ArrayList<>();
            for (ExcelColumnNames enums : values()) {
                columnNameList.add(enums.getColumnName());
            }
            return columnNameList;
        }
    }


    /**
     * @description: 业务分类
     **/
    @Getter
    @AllArgsConstructor
    enum TaskType implements FinanceEngineEnum{
        YWXT("YWXT", "业务系统"),
        KP("KP", "开票"),
        ZJXT("ZJXT", "资金系统");

        private String key;
        private String value;

        public static String getValue(String key) {
            for (TaskType o : values()) {
                if (o.key.equalsIgnoreCase(key)) {
                    return o.getValue();
                }
            }
            return null;
        }
    }

    /**
     * @description: 符号
     **/
    @Getter
    @AllArgsConstructor
    enum Symbol implements FinanceEngineEnum{
        UNDERLINE("underline", "_"),
        LINE("line", "-"),
        NULL("null",""),
        COMMA(",",","),
        SEMICOLON("；","；"),
        COLON("：","：");


        private String key;
        private String value;

        public static String getValue(String key) {
            for (Symbol o : values()) {
                if (o.key.equalsIgnoreCase(key)) {
                    return o.getValue();
                }
            }
            return null;
        }
    }

    /**
     * @description: 相关中英文转换的KEY
     * @author: zhangli.chen
     **/
    @Getter
    @AllArgsConstructor
    enum KeyValueTextMapping implements FinanceEngineEnum {
        ORG_ID("org_id", "org_name");

        private String key;
        private String value;

        public static String getValue(String key) {
            for (KeyValueTextMapping o : values()) {
                if (o.key.equalsIgnoreCase(key)) {
                    return o.getValue();
                }
            }
            return null;
        }
    }

    /**
     * @description: 6类资产转让状态
     **/
    @Getter
    @AllArgsConstructor
    enum ContractAssetTransferStatus implements FinanceEngineEnum{
        HX_ZR("1", "正常核销、资产处置结束（第三方转让）"),
        HX_ZR_FWF("2", "正常核销、资产处置结束（第三方转让）、服务费核销"),
        KS_ZR("3", "亏损结清、资产处置结束（第三方转让）"),
        CZ_ZR("4", "资产处置结束（第三方转让）"),
        ZR_HX("5", "资产处置结束（第三方转让）、服务费核销"),
        KS_HX_ZR("6", "亏损结清、服务费核销、资产处置结束（第三方转让）");

        private String key;
        private String value;

        /**
         * @description: 通过key获取value
         **/
        public static String getValue(String key) {
            for (ContractAssetTransferStatus o : values()) {
                if (o.key.equalsIgnoreCase(key)) {
                    return o.getValue();
                }
            }
            return null;
        }

        /**
         * @description: 通过value获取key
         **/
        public static String getKey(String value) {
            for (ContractAssetTransferStatus o : values()) {
                if (o.value.equalsIgnoreCase(value)) {
                    return o.getKey();
                }
            }
            return null;
        }
    }

    /**
     * @description: 结束的财务合同状态
     * @author: zhangli.chen
     **/
    @Getter
    @AllArgsConstructor
    enum FinishFinancialContractStatus implements FinanceEngineEnum {
        KS("1", "亏损结清"),
        ZCHX("2", "正常核销"),
        ZCCZ_SF_ZR("3", "正常核销、资产处置结束（第三方转让）"),
        ZCCB_ABS("4", "资产出表（ABS）"),
        CZJS_BL("5", "资产处置结束（保理出表）"),
        CZJS_ZR("6", "资产处置结束（第三方转让）"),
        //CZJS_NBZR("7", "资产处置结束（内部转让）"),
        CZJS_ZCJY("8", "资产处置结束（资产交易）"),
        KSJQ("9", "亏损结清、资产处置结束（第三方转让）"),
        ZWCZ("10", "债务重组")
        ;

        private String columnNumber;
        private String columnName;

        /**
         * @description: 通过列名获取列号
         **/
        public static String getColumnNumber(String columnName) {
            if (StringUtils.isEmpty(columnName)) {
                return null;
            }
            for (FinishFinancialContractStatus o : values()) {
                if (o.columnName.equalsIgnoreCase(columnName)) {
                    return o.getColumnNumber();
                }
            }
            return null;
        }

        /**
         * @description: 通过列号获取列名
         **/
        public static String getColumnName(Integer columnNumber) {
            if (StringUtils.isEmpty(columnNumber)) {
                return null;
            }
            for (FinishFinancialContractStatus enums : values()) {
                if (columnNumber.equals(enums.getColumnNumber())) {
                    return enums.getColumnName();
                }
            }
            return null;
        }

        /**
         * @description: 通过列号获取列名
         **/
        public static List<String> getAllColumnName() {
            List<String> columnNameList = new ArrayList<>();
            for (FinishFinancialContractStatus enums : values()) {
                columnNameList.add(enums.getColumnName());
            }
            return columnNameList;
        }

    }

    /**
     * @description: 内部转让-结束的财务合同状态
     * @author: zhangli.chen
     **/
    @Getter
    @AllArgsConstructor
    enum InnerFinishFinancialContractStatus implements FinanceEngineEnum {
        CZJS_NBZR("7", "资产处置结束（内部转让）");

        private String columnNumber;
        private String columnName;

        /**
         * @description: 通过列名获取列号
         **/
        public static String getColumnNumber(String columnName) {
            if (StringUtils.isEmpty(columnName)) {
                return null;
            }
            for (InnerFinishFinancialContractStatus o : values()) {
                if (o.columnName.equalsIgnoreCase(columnName)) {
                    return o.getColumnNumber();
                }
            }
            return null;
        }

        /**
         * @description: 通过列号获取列名
         **/
        public static String getColumnName(Integer columnNumber) {
            if (StringUtils.isEmpty(columnNumber)) {
                return null;
            }
            for (InnerFinishFinancialContractStatus enums : values()) {
                if (columnNumber.equals(enums.getColumnNumber())) {
                    return enums.getColumnName();
                }
            }
            return null;
        }

        /**
         * @description: 通过列号获取列名
         **/
        public static List<String> getAllColumnName() {
            List<String> columnNameList = new ArrayList<>();
            for (InnerFinishFinancialContractStatus enums : values()) {
                columnNameList.add(enums.getColumnName());
            }
            return columnNameList;
        }
    }

    /**
     * @description: 内部转让-结束的财务合同状态
     * @author: zhangli.chen
     **/
    @Getter
    @AllArgsConstructor
    enum TrueOrFalse implements FinanceEngineEnum {
        TRUE("TRUE", true),
        FALSE("FALSE",false);
        private String key;
        private boolean value;


    }


    /**
     * @description: 小微业务系统-保证金池类型
     * @author: zhangli.chen
     **/
    @Getter
    @AllArgsConstructor
    enum XwClientType implements FinanceEngineEnum {
        SU("1", "供应商"),
        GU("2","担保方");
        private String key;
        private String value;

        /**
         * @description: 通过key获取value
         **/
        public static String getValue(String key) {
            for (XwClientType o : values()) {
                if (o.key.equalsIgnoreCase(key)) {
                    return o.getValue();
                }
            }
            return null;
        }

        /**
         * @description: 通过value获取key
         **/
        public static String getKey(String value) {
            for (XwClientType o : values()) {
                if (o.value.equalsIgnoreCase(value)) {
                    return o.getKey();
                }
            }
            return null;
        }


    }



    /**
     * @description: 资金系统查询参数
     * @author: zhangli.chen
     **/
    @Getter
    @AllArgsConstructor
    enum FundSystemQueryParameters implements FinanceEngineEnum {
        START_DATE("startDate", "时间开始"),
        END_DATE("endDate","时间结束");
        private String key;
        private String value;

        /**
         * @description: 通过key获取value
         **/
        public static String getValue(String key) {
            for (FundSystemQueryParameters o : values()) {
                if (o.key.equalsIgnoreCase(key)) {
                    return o.getValue();
                }
            }
            return null;
        }

        /**
         * @description: 通过value获取key
         **/
        public static String getKey(String value) {
            for (FundSystemQueryParameters o : values()) {
                if (o.value.equalsIgnoreCase(value)) {
                    return o.getKey();
                }
            }
            return null;
        }
    }

    /**
     * @description: 未确认收款-线下网银导入状态
     * @author: zhangli.chen
     **/
    @Getter
    @AllArgsConstructor
    enum offlineOnlineBankState implements FinanceEngineEnum {
        NOT_EXIST("1", "资金系统不存在"),
        EXIST_CURRENCY_IS_NOT_RMB("2","资金系统存在但网银币种不为人民币"),
        EXIST_CURRENCY_IS_RMB("3","资金系统存在且网银币种为人民币"),
        ;
        private String key;
        private String value;

        /**
         * @description: 通过key获取value
         **/
        public static String getValue(String key) {
            for (offlineOnlineBankState o : values()) {
                if (o.key.equalsIgnoreCase(key)) {
                    return o.getValue();
                }
            }
            return null;
        }

        /**
         * @description: 通过value获取key
         **/
        public static String getKey(String value) {
            for (offlineOnlineBankState o : values()) {
                if (o.value.equalsIgnoreCase(value)) {
                    return o.getKey();
                }
            }
            return null;
        }
    }






}
