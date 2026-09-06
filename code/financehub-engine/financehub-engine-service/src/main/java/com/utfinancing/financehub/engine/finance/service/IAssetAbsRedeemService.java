package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsRedeemDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsRedeemQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsRedeemDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CheckPageQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.AssetAbsRedeemDetailExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.AssetAbsRedeemDetailVO;
import com.utfinancing.financehub.engine.finance.model.vo.AssetAbsRedeemVO;
import com.utfinancing.financehub.engine.finance.entity.AssetAbsRedeemEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.model.vo.ContractBalanceVO;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * @Author : bruyang
 * @Date : Create in 2024-03-15
 * @Description : AssetAbsRedeem服务类接口
 * @Modified :
 */
public interface IAssetAbsRedeemService extends IService<AssetAbsRedeemEntity> {

    Long saveAssetAbsRedeem(AssetAbsRedeemDTO dto);

    Long updateAssetAbsRedeem(Long id, AssetAbsRedeemDTO dto);

    AssetAbsRedeemDTO getAssetAbsRedeemDTOById(Long id);

    IPage<AssetAbsRedeemVO> selectPage(AssetAbsRedeemQueryDTO queryDTO);

    Boolean importTemplate(MultipartFile file);

    IPage<AssetAbsRedeemDetailVO> detailPage(AssetAbsRedeemDetailQueryDTO queryDTO);

    Boolean deleteByIds(List<Long> idList);

    /**
     * 批量撤回
     * @param idList
     * @return
     */
    Boolean withdraw(List<Long> idList);

    /**
     * 批量提交
     * @param idList
     * @return
     */
    Boolean submit(List<Long> idList);

    /**
     * 批量生成凭证
     * @param idList
     * @param isSubmit
     * @return
     */
    Boolean generateVoucher(List<Long> idList,String isSubmit);

    List<AssetAbsRedeemVO> selectByCondition(AssetAbsRedeemQueryDTO queryDTO);

    Boolean updateProcessStatus(CommonApproveDTO approveDTO);

    List<AssetAbsRedeemDetailExcelVO> selectDetailByRedeemId(Long assetAbsRedeemId);

    Boolean detailImport(MultipartFile file,Long assetAbsRedeemId);

    IPage<ContractBalanceVO> selectCheckPage(CheckPageQueryDTO queryDTO);

    List<AssetAbsRedeemDetailExcelVO> selectDetailByRedeemIdList(List<Long> assetAbsRedeemIdList);

}
