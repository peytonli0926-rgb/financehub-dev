package com.utfinancing.financehub.engine.constants;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Constants {

    // 生成偿还计划每次处理数据量
    public static int GENERATE_PAYMENT_PLAN_PROCESS_NUMBER = 100;

    // 删除标识
    public static String DEL_FLAG_0 = "0";
    public static String DEL_FLAG_1 = "1";

    public static String FILE_DOWNLOAD_ERRS_FLAG = "Errs";

    //未确认收款科目编码
    public static String RECEIVABLE_UNCONFIRM_RECEIPT = "1531.02";
    public static String RECEIVABLE_UNCONFIRM_RECEIPT_NAME = "未确认收款";

    //其他应收款款_应收转让后收款
    public static String OHTER_RECEIVABLE_TRANSFER_ACCOUNT = "1221.15";

    // 其他应付款_代收转让款项
    public static String OTHER_PAYABLES_PROXY_MAKE_OVER_ACCOUNT = "2241.17";

    // 其他应付款_关联公司往来
    public static String OTHER_PAYABLES_CONNECT_COMPANY_ACCOUNT = "2241.02";

    // 其他应收款款_关联公司往来
    public static String OTHER_RECEIVABLE_CONNECT_COMPANY_ACCOUNT = "1221.01";

    // 其他应付款_代收款项科目
    public static String OTHER_PAYABLE_PROXY_RECEIVE_ACCOUNT = "2241.09";

    public static String RECYCLE_PRINCIPAL_AMOUNT = "1531.01.01";
    public static String RECEIVE_FIRST_AMOUNT = "1531.01.02";

    public static String RECEIVE_RETAINED_PRICE = "1531.01.03";

    public static String RECEIVE_PROCEDURE_AMOUNT = "1531.01.04";

    public static String RECEIVE_FIRM_REBATE = "1531.01.05";

    public static String RECEIVABLE_INSURANCE_AMOUNT = "1531.01.06";

    public static String RECEIVE_OTHER_REVENUES_LIST = "1531.01.08";

    public static String RECEIVE_SERVICE_AMOUNT = "1122.06";
    public static String RECEIVE_MARGIN_AMOUNT = "2701.01";
    public static String RECEIVE_VOUCHER_MARGIN_AMOUNT = "2701.02";
    public static String RECEIVE_INSURANCE_DIFFER_AMOUNT = "6051.01";
    public static String RECYCLE_DEFAULT_INTEREST_AMOUNT = "7002.01";
    public static String RECEIVE_TERMINATE_PROCEDURE_AMOUNT = "7002.02";
    public static String RECEIVE_PENAL = "7002.04";

    /**
     * 合同编码包含-A
     */
    public static String CONTRACT_CODE_A = "-A";

    /**
     * 合同编码包含-1
     */
    public static String CONTRACT_CODE_1 = "-1";

    /**
     * 借款合同编号默认值
     */
    public static String BILL_CONTRACT_CODE_DEFAULT = "XN001";

    String CNY = "CNY";

    String RMB = "RMB";

    // 操作系统名称定义
    public static final String OPERATION_SYSTEM_NAME_WINDOWS = "windows";
    public static final String OPERATION_SYSTEM_NAME_LINUX = "linux";
    public static final String OPERATION_SYSTEM_NAME_UNIX = "unix";

    /**
     * 进项税科目
     */
    public static String INPUT_TAX_ACCOUNT_CODE = "2221.01.01";

    /**
     * 销项税科目
     */
    public static String OUT_TAX_ACCOUNT_CODE = "2221.01.05";

    /**
     * 计提
     */
    public static String INVOICING_FLAG = "计提";

    /**
     * 开票（原计提）
     */
    public static String NEW_INVOICING_FLAG = "开票（原计提）";

    /**
     * 直租税率
     */
    public static BigDecimal DIRECT_RATE = new BigDecimal(1.13);

    /**
     * 回租税率
     */
    public static BigDecimal LEASEBACK_RATE = new BigDecimal(1.06);

    public static String PAY_METHOD_PERIOD_INIT = "期初";
    public static String PAY_METHOD_PERIOD_LAST = "期末";

    public static List<String> CONTRACT_STATUS_CLOSE = Arrays.asList("合同结束", "合同结清", "买断结清", "亏损结清", "内部结清", "处置结清", "提前结清", "结清");

    /**
     * 虚拟合同开头
     */
    String VIRTUAL_CONTRACT_START = "VL";

    /**
     * 软提示-特殊头信息
     */
    public static String X_CONFIRM_KEY = "X-Confirm-Key";

    /**
     * 1.开票主体、开票金额、开票税率、开票对象不一致。
     */
    String INVOICE_ERROR_COMMENT = "开票主体、开票金额、开票税率、开票对象不一致。";


    /**
     * 2.累计已开金额大于已收款金额
     */
    String INVOICE_AMOUNT_ERROR_COMMENT = "累计已开金额大于已收款金额";

    public String NON_OBSERVED = "1"; // 非观察期
    public String OBSERVED_TIME = "2"; // 观察期
    public String OBSERVED_EXPIRED = "3"; // 观察期退出
    public String IN_OBSERVED = "4"; // 进入观察期
}
