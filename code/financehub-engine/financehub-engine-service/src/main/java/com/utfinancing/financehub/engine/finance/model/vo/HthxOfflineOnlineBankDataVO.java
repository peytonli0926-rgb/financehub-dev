package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.engine.model.dto.HthxFundEbankTransactionDataDTO;
import com.utfinancing.financehub.engine.model.dto.HthxOnlineBankCrossCheckDetailDTO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 应用模块名称:
 * 代码描述:
 *
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/8/12 13:31
 */
@Data
public class HthxOfflineOnlineBankDataVO implements Serializable {

    /**
     * @description: 网银编号
     **/
    private String ebankNumber;

    /**
     * @description: 网银状态
     **/
    private String state;

    /**
     * @description: 勾稽关系
     **/
    HthxOnlineBankCrossCheckDetailDTO gjInfo;

    /**
     * @description: 资金网银信息
     **/
    List<HthxFundEbankTransactionDataDTO> zjWyList;

    /**
     * @description: 业务系统网银编号-小网银
     **/
    private String ebankSerialNumber;

    /**
     * @description: 业务系统网银编号
     **/
    private String businessEbankNumber;

    /**
     * @description: 借方发生额
     **/
    private BigDecimal claimAmount;

    /**
     * @description: 认领日期(yyyy-MM-dd)
     **/
    private String claimDate;

    /**
     * @description: 主体ID
     **/
    private String orgId;

    /**
     * @description: 网银归属主体
     **/
    private String orgName;


}
