package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferThirdPartDetailEntity;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferCheckVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferThirdPartCheckVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ConvertTransferThirdPartDetailMapper extends BaseMapper<ConvertTransferThirdPartDetailEntity> {
    IPage<ConvertTransferThirdPartCheckVO> selectCheckPage(Page<ConvertTransferCheckVO> page, @Param("orgIdContractCodeMap") Map<String, List<String>> transferContractCodeMap);
}
