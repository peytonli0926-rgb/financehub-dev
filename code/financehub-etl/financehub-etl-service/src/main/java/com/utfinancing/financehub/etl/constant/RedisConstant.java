package com.utfinancing.financehub.etl.constant;
/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.constants.RedisConstant</li>
 * <li>CreateTime : 2023/12/07 14:29</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
public interface RedisConstant {

    /**
     * 凭证场景code key %s = 场景编码
     */
     String V_SCENE_CODE_KEY = "_scene_code_%s";

    /**
     * 凭证业务code key %s = 业务编码
     */
    String V_BUSINESS_CODE_KEY = "_business_code_%s";

    /**
     * 凭证业务阈值code key %s = 场景编码
     */
    String V_FIELD_SCENE_CODE_KEY = "_field_business_code_%s";

    /**
     * 凭证场景阈值code key
     */
    String V_ALL_BUSINESS = "_all_business";

    /**
     * 凭证场景阈值code key
     */
    String V_ALL_TAX_RATE = "_all_tax_rate";

    /**
     * 凭证场景规则key %s = 场景编码
     */
    String V_SCENE_CODE_RULE = "_scene_code_rule_%s";

    /**
     * 字典金额类型 KEY %s = 字典类型
     */
    String V_DICT_SYS_CASH_TYPE = "_sys_dict:type_%s";

    /**
     * 数仓数据字典
     */
    String V_DW_DICT_DATA = "_dw_dict_data";

    /**
     * 系统关账期间
     */
    String CLOSE_ACCOUNT_PERIOD = "_close_account_period";

    String ACCOUNT_DATA = "_account_data";

    /**
     * 过期时间
     */
     Long TIME_OUT = 30L;

    /**
     * 币种
     */
    String V_ALL_CURRENCY = "_all_currency";
}
