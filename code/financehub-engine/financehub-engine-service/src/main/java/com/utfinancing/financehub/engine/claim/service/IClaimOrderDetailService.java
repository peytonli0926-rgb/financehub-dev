package com.utfinancing.financehub.engine.claim.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderDetailQueryDTO;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderDetailDTO;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderDetailVO;
import com.utfinancing.financehub.engine.claim.entity.ClaimOrderDetailEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-10-23
 * @Description : ClaimOrderDetail服务类接口
 * @Modified :
 */
public interface IClaimOrderDetailService extends IService<ClaimOrderDetailEntity> {

    Long saveClaimOrderDetail(ClaimOrderDetailDTO dto);

    Long updateClaimOrderDetail(Long id, ClaimOrderDetailDTO dto);

    ClaimOrderDetailDTO getClaimOrderDetailDTOById(Long id);

    IPage<ClaimOrderDetailVO> selectPage(ClaimOrderDetailQueryDTO queryDTO);

    List<ClaimOrderDetailVO> selectByCondition(ClaimOrderDetailQueryDTO queryDTO);

    /**
     * 取得未下载文件的费用明细
     */
    public List<ClaimOrderDetailEntity> selectByIsDownloadFile();
}
