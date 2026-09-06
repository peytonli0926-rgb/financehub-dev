package com.utfinancing.financehub.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.admin.api.model.SysInternalUser;
import com.utfinancing.financehub.admin.entity.SysInternalUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.admin.model.dto.SysInternalUserDTO;
import com.utfinancing.financehub.admin.model.dto.SysInternalUserQueryDTO;
import com.utfinancing.financehub.admin.model.vo.SysInternalUserVO;
import org.apache.ibatis.annotations.Param;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

/**
 * <p>
 * 中台角色下的用户表 Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2023-11-17
 */
public interface SysInternalUserMapper extends BaseMapper<SysInternalUserEntity> {

    List<SysInternalUserVO> selectByCondition(@Param("condition") SysInternalUserDTO dto);

    List<SysInternalUser> getReviewSubUser(@Param("condition") SysInternalUserDTO dto);
}
