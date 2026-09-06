package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.RentRegisterQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.RentRegisterDTO;
import com.utfinancing.financehub.engine.finance.model.vo.RentRegisterExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.RentRegisterVO;
import com.utfinancing.financehub.engine.finance.entity.RentRegisterEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-07
 * @Description : RentRegister服务类接口
 * @Modified :
 */
public interface IRentRegisterService extends IService<RentRegisterEntity> {

    Long saveRentRegister(RentRegisterDTO dto);

    Long updateRentRegister(Long id, RentRegisterDTO dto);

    RentRegisterDTO getRentRegisterDTOById(Long id);

    IPage<RentRegisterVO> selectPage(RentRegisterQueryDTO queryDTO);

    List<RentRegisterVO> selectList(RentRegisterQueryDTO queryDTO);

    /**
     * 上传基本信息
     *
     * @param file
     * @return
     */
    Boolean importFile(MultipartFile file);

    /**
     * 上传租金计划表
     *
     * @param file
     * @return
     */
    Boolean importFileForDetail(MultipartFile file);

    /**
     * 批量提交
     *
     * @param ids
     * @return
     */
    Boolean submit(List<Long> ids);

    /**
     * 批量撤回
     *
     * @param ids
     * @return
     */
    Boolean withdraw(List<Long> ids);

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    Boolean delete(List<Long> ids);

    /**
     * 根据资产编号查询出租登记
     *
     * @param assetNumber
     * @param contractCode
     * @return
     */
    RentRegisterVO getByAssetNumber(String assetNumber, String contractCode);

    /**
     * 分摊
     *
     * @param ids
     * @return
     */
    Boolean apportion(List<Long> ids);

    void updateProcessStatus(CommonApproveDTO approveDTO);
}
