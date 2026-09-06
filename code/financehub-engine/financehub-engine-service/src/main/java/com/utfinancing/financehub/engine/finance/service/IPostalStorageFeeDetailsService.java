package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.PostalStorageFeeDetailsQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.PostalStorageFeeDetailsDTO;
import com.utfinancing.financehub.engine.finance.model.vo.PostalStorageFeeDetailsVO;
import com.utfinancing.financehub.engine.finance.entity.PostalStorageFeeDetailsEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-17
 * @Description : PostalStorageFeeDetails服务类接口
 * @Modified :
 */
public interface IPostalStorageFeeDetailsService extends IService<PostalStorageFeeDetailsEntity> {

    Long savePostalStorageFeeDetails(PostalStorageFeeDetailsDTO dto);

    Long updatePostalStorageFeeDetails(Long id, PostalStorageFeeDetailsDTO dto);

    PostalStorageFeeDetailsDTO getPostalStorageFeeDetailsDTOById(Long id);

    IPage<PostalStorageFeeDetailsVO> selectPage(PostalStorageFeeDetailsQueryDTO queryDTO);

}
