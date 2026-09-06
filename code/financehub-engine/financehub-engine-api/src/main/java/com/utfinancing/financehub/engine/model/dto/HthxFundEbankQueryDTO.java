package com.utfinancing.financehub.engine.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 应用模块名称:
 * 代码描述:
 *
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/8/12 9:25
 */
@Data
public class HthxFundEbankQueryDTO {

    private List<String> list;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
