package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.ReceiveTaxEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ReceiveTaxQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ReceiveTaxVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2024-03-20
 */
public interface ReceiveTaxMapper extends BaseMapper<ReceiveTaxEntity> {

    /**
     * 分页查询
     *
     * @param page
     * @param queryDTO
     * @return
     */
    IPage<ReceiveTaxVO> selectPageByMapper(Page page, @Param("queryDTO") ReceiveTaxQueryDTO queryDTO);

    /**
     * 查询所有数据
     *
     * @param queryDTO
     * @return
     */
    List<ReceiveTaxVO> selectPageByMapper(@Param("queryDTO") ReceiveTaxQueryDTO queryDTO);
}
