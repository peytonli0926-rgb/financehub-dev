package com.utfinancing.financehub.engine.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 应用模块名称:
 * 代码描述:
 *
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/8/11 19:20
 */
@ToString
@Data
public class HthxOnlineBankCrossCheckDataDTO implements Serializable {

    /**
     * @description: 恒运网银编号
     **/
    String hyWybh;

    /**
     * @description: 勾稽关系
     **/
    HthxOnlineBankCrossCheckDetailDTO gjInfo;

    /**
     * @description: 资金网银信息
     **/
    List<HthxFundEbankTransactionDataDTO> zjWyList;

    public String getHyWybh() {
        return hyWybh;
    }

    public void setHyWybh(String hyWybh) {
        this.hyWybh = hyWybh;
    }

    public HthxOnlineBankCrossCheckDetailDTO getGjInfo() {
        return gjInfo;
    }

    public void setGjInfo(HthxOnlineBankCrossCheckDetailDTO gjInfo) {
        this.gjInfo = gjInfo;
    }

    public List<HthxFundEbankTransactionDataDTO> getZjWyList() {
        return zjWyList;
    }

    public void setZjWyList(List<HthxFundEbankTransactionDataDTO> zjWyList) {
        this.zjWyList = zjWyList;
    }
}
