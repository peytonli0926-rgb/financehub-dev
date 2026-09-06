package com.utfinancing.financehub.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.admin.api.model.SysInternalUser;
import com.utfinancing.financehub.admin.model.dto.SysInternalUserQueryDTO;
import com.utfinancing.financehub.admin.model.dto.SysInternalUserDTO;
import com.utfinancing.financehub.admin.model.vo.SysInternalUserVO;
import com.utfinancing.financehub.admin.entity.SysInternalUserEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-17
 * @Description : SysInternalUser服务类接口
 * @Modified :
 */
public interface ISysInternalUserService extends IService<SysInternalUserEntity> {

    Long saveSysInternalUser(SysInternalUserDTO dto);

    Long updateSysInternalUser(Long id, SysInternalUserDTO dto);

    SysInternalUserDTO getSysInternalUserDTOById(Long id);

    IPage<SysInternalUserVO> selectPage(SysInternalUserQueryDTO queryDTO);

    List<SysInternalUserVO> listByCondition(SysInternalUserQueryDTO queryDTO);

    List<SysInternalUser> getReviewSubUserByUserCode(String userCode);

}
