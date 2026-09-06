package com.utfinancing.financehub.engine.finance.service;


import com.utfinancing.financehub.engine.finance.model.dto.BatchQueryUploadQueryDTO;
import com.utfinancing.financehub.engine.hthx.base.MyBaseService;
import com.utfinancing.financehub.engine.hthx.page.PageResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IBatchQueryUploadNewService extends MyBaseService {

    /**
     * @description: 批量查询功能列表-支持分页查询
     **/
    PageResult selectPage(BatchQueryUploadQueryDTO queryDTO) throws Exception;

    /**
     * @description:文件上传导入
     **/
    Boolean importTemplate(MultipartFile file);

    /**
     * @description:批量查询表单头
     **/
    List<Map<String, String>> selectHeaderList();

    /**
     * @description:下载原表+详情
     **/
    Map<String, String> generateBatchQueryReportExcel(BatchQueryUploadQueryDTO queryDTO);

}
