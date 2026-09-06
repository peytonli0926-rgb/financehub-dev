package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 应用模块名称: 金蝶凭证推送结果类
 *
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/5/8 11:30
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HthxPushVoucherResultDTO implements Serializable {

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
     * @description: 凭证推送结果 1:是-成功 0:否-不成功 2:部分成功
     **/
    String pushResult;

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

    /**
     * @description: 推送批次ID
     **/
    private List<Long> batchIdList;



}