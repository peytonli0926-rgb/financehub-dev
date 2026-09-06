package com.utfinancing.financehub.engine.finance.constant;

import java.math.BigDecimal;

public interface ExceptionConstant {

    /**
     * //1、开票主体、开票金额、开票税率、开票对象不一致
     */
    String EXCEPTION_1 = "开票主体、开票金额、开票税率、开票对象不一致; ";

    /**
     * //2、累计已开金额大于已收款金额
     */
    String EXCEPTION_2 = "累计已开金额大于已收款金额; ";

    /**
     * //3、同一合同同一科目在两个签约主体有余额
     */
    String EXCEPTION_3 = "同一合同同一科目在两个签约主体有余额; ";

    /**
     * //4、特殊合同状态有应收销项税余额
     */
    String EXCEPTION_4 = "特殊合同状态有应收销项税余额; ";

    /**
     * //5、实际剩余≠合同余额
     */
    String EXCEPTION_5 = "实际剩余≠科目余额; ";

    /**
     * //6、合同余额≠报表余额
     */
    String EXCEPTION_6 = "科目余额≠报表余额; ";

    /**
     * 对比税额、开票明细数据，若不相等则提示差异
     */
    String EXCEPTION_TAX_DETAIL_DIFFERENT = "税额与开票明细数据存在差异; ";


}
