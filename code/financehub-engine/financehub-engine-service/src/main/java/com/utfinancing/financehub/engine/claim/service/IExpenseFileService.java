package com.utfinancing.financehub.engine.claim.service;

import com.utfinancing.financehub.common.core.dto.R;

import javax.servlet.http.HttpServletResponse;

public interface IExpenseFileService {

    public R<String> expenseTypeDataSyncTest() throws Exception;

    /**
     * 费用类型数据同步
     */
    public R<String> expenseTypeDataSync() throws Exception;
}
