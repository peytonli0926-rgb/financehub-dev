package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferDetailEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferCheckVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 折价转让-详情 Mapper 接口
 * </p>
 *
 * @author wenbin
 * @since 2024-04-22
 */
public interface ConvertTransferDetailMapper extends BaseMapper<ConvertTransferDetailEntity> {

    IPage<ConvertTransferCheckVO> selectCheckPage(PageDTO<Object> page, @Param("orgIdContractCodeMap") Map<String, List<String>> orgIdContractCodeMap);
}
