package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.MarginContractBalanceEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.MarginContractBalanceQueryDTO;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 保证金合同余额表 Mapper 接口
 * </p>
 *
 * @author hzhao
 * @since 2023-09-24
 */
public interface MarginContractBalanceMapper extends BaseMapper<MarginContractBalanceEntity> {

    List<MarginContractBalanceEntity> selectSummaryInfo(Page page, @Param("param") MarginContractBalanceQueryDTO param);

    List<MarginContractBalanceEntity> selectSummaryInfo(@Param("param") MarginContractBalanceQueryDTO param);

    MarginContractBalanceEntity getLastMonth( @Param("param") MarginContractBalanceQueryDTO param);

    void initLastMonthBalance( @Param("param") MarginContractBalanceQueryDTO param);

    void initContractBalance( @Param("param") MarginContractBalanceQueryDTO param);

    void clearMarginTemp();
    List<Map<String, Object>> selectMarginContractTemp(@Param("param") MarginContractBalanceQueryDTO param);

    void updateSpecialContractBalance(@Param("list")List<String> contracts, @Param("businessCode") String businessCode);
}
