package com.utfinancing.financehub.etl.kingdee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.etl.kingdee.entity.TGLAssistBalanceEntity;
import com.utfinancing.financehub.etl.kingdee.entity.TGlVoucherEntity;
import com.utfinancing.financehub.etl.kingdee.model.dto.TGLVoucherInitDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface TGLAssistBalanceMapper extends BaseMapper<TGLAssistBalanceEntity> {

    public List<TGLAssistBalanceEntity> selectOutstandingData(@Param("period") String period);

    public List<TGLVoucherInitDTO> selectVoucherDataInit(TGLVoucherInitDTO dto);
}
