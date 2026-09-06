package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.PostalStorageFeeDetailsVO;
import com.utfinancing.financehub.engine.finance.model.vo.PostalStorageFeeVO;
import com.utfinancing.financehub.engine.finance.entity.PostalStorageFeeEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-17
 * @Description : PostalStorageFee服务类接口
 * @Modified :
 */
public interface IPostalStorageFeeService extends IService<PostalStorageFeeEntity> {

    Long savePostalStorageFee(PostalStorageFeeDTO dto);

    Long updatePostalStorageFee(Long id, PostalStorageFeeDTO dto);

    PostalStorageFeeDTO getPostalStorageFeeDTOById(Long id);

    IPage<PostalStorageFeeVO> selectPage(PostalStorageFeeQueryDTO queryDTO);

    IPage<PostalStorageFeeDetailsVO> selectDetailPage(PostalStorageFeeDetailsQueryDTO queryDTO);

    void detailUpdate(PostalStorageFeeDetailsUpdateDTO saveDTO);

    void submit(List<Long> ids);

    void withdraw(List<Long> ids);

    void pass(List<Long> ids);

    void fail(List<Long> ids);

    void generate(PostalStorageFeeQueryDTO queryDTO);

    void voucher(List<Long> ids);
}
