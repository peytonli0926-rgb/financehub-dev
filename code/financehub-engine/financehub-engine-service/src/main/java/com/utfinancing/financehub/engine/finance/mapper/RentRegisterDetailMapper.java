package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.RentRegisterDetailEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.RentRegisterDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.RentRegisterDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 出租登记-租金计划 Mapper 接口
 * </p>
 *
 * @author wenbin
 * @since 2024-04-08
 */
public interface RentRegisterDetailMapper extends BaseMapper<RentRegisterDetailEntity> {

    /**
     * 分页查询
     * @param page
     * @param queryDTO
     * @return
     */
    IPage<RentRegisterDetailVO> selectPageByMapper(Page page, @Param("queryDTO") RentRegisterDetailQueryDTO queryDTO);

    /**
     * 查询数据
     * @param queryDTO
     * @return
     */
    List<RentRegisterDetailVO> selectPageByMapper(@Param("queryDTO") RentRegisterDetailQueryDTO queryDTO);
}
