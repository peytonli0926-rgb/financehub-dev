package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.entity.BatchQueryUploadRecordEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.entity.FileRecordEntity;
import com.utfinancing.financehub.engine.finance.model.dto.BatchQueryUploadQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FileRecordQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.BatchQueryDataVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Author : jnc
 * @Date : Create in 2024-05-14
 * @Description : BatchQueryUploadRecord服务类接口
 * @Modified :
 */
public interface IBatchQueryUploadRecordService extends IService<BatchQueryUploadRecordEntity> {


    Boolean importTemplate(MultipartFile file);

    IPage<Map<String, String>> selectPage(BatchQueryUploadQueryDTO queryDTO);

    Map<String, String> generateBatchQueryReportExcel(BatchQueryUploadQueryDTO queryDTO);

    IPage<FileRecordEntity> selectBatchQueryFileList(FileRecordQueryDTO queryDTO);

    List<Map<String, String>> selectHeaderList();
}
