package com.utfinancing.financehub.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.admin.model.dto.SysInternalSubUserQueryDTO;
import com.utfinancing.financehub.admin.model.dto.SysInternalSubUserDTO;
import com.utfinancing.financehub.admin.model.vo.SysInternalSubUserVO;
import com.utfinancing.financehub.admin.entity.SysInternalSubUserEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-17
 * @Description : SysInternalSubUser服务类接口
 * @Modified :
 */
public interface ISysInternalSubUserService extends IService<SysInternalSubUserEntity> {

    Long saveSysInternalSubUser(SysInternalSubUserDTO dto);

    Long updateSysInternalSubUser(Long id, SysInternalSubUserDTO dto);

    SysInternalSubUserDTO getSysInternalSubUserDTOById(Long id);

    IPage<SysInternalSubUserVO> selectPage(SysInternalSubUserQueryDTO queryDTO);

}
