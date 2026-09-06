package com.utfinancing.financehub.engine.hthx.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 应用模块名称: 批量生成凭证结果返回类
 * 代码描述:
 *
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/4/22 20:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HthxBatchVoucherResultVO {

    /**
     * @description: 传入凭证生成逻辑数据总数
     **/
    private int totalSize;

    /**
     * @description: 凭证生成成功记录条数
     **/
    private int successSize;

    /**
     * @description: 凭证生成失败记录条数
     **/
    private int failSize ;

    /**
     * @description: 错误信息
     **/
    private StringBuffer errorInfo;

    /**
     * @description: 凭证生成结果 1:是-成功 0:否-不成功 2:部分成功
     **/
    String generateResult;

    /**
     * @description: 生成的凭证头列表
     **/
    private List<String> voucherIdList;

    /**
     * @description: 财务日期
     **/
    private LocalDateTime accountDate;

    /**
     * @description: 例外记录条数
     **/
    private int exceptionRecords;



}
