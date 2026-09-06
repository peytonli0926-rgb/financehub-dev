package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.ContractBalanceEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ContractBalanceCheckQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractBalanceLastQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractBalanceQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.MarginContractBalanceQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TransferContractBalanceQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractAccountBalanceVO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractBalanceVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 合同余额表 Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-09-23
 */
public interface ContractBalanceMapper extends BaseMapper<ContractBalanceEntity> {

    boolean insertContractBalance(@Param("columns")List<String> columns, @Param("values")List<Object> values);

    List<Map<String, Object>> selectContractMap(Page page, @Param("param") MarginContractBalanceQueryDTO param);

    List<Map<String, Object>> selectContractMap(@Param("param") MarginContractBalanceQueryDTO param);

    IPage<ContractBalanceVO> selectLatestBalance(Page page,@Param("param") ContractBalanceQueryDTO queryDTO);

    IPage<ContractBalanceVO> selectCheckPage(Page page,@Param("param") ContractBalanceCheckQueryDTO queryDTO);

    List<ContractBalanceVO> selectLatestBalanceByCondition(@Param("param") ContractBalanceQueryDTO queryDTO);

    IPage<ContractAccountBalanceVO> sumAccountBalancePage(Page page, @Param("param") ContractBalanceQueryDTO queryDTO);

    List<ContractBalanceEntity> getLastContract(@Param("param") ContractBalanceLastQueryDTO param);

    Map<String, BigDecimal> sumContractBalanceJYJGBG(@Param("periodCode") int periodCode, @Param("orgId") String orgId, @Param("voucherDate") LocalDateTime voucherDate, @Param("contractCode") String contractCode);

    Map<String, BigDecimal> sumContractBalanceZLSK(@Param("periodCode") int periodCode, @Param("orgId") String orgId, @Param("voucherDate") LocalDateTime voucherDate, @Param("contractCode") String contractCode);

    Map<String, BigDecimal> sumContractBalanceKJFP(@Param("periodCode") int periodCode, @Param("orgId") String orgId, @Param("voucherDate") LocalDateTime voucherDate, @Param("contractCode") String contractCode);

    List<ContractBalanceVO> selectLatestBalanceByOrgIdContractCodeList(@Param("param") TransferContractBalanceQueryDTO queryDTO);

    /**
     * 资产转让 - 第三方 上传后 按照org_id,contract_code,scene_code统计基准日后数据
     * @param orgContractMap
     * @param sceneCode
     * @param localDate
     * @return
     */
    List<ContractBalanceEntity> selectContractBalanceAfterJZRGroupByOrgIdContractScene(
            @Param("orgContractMap") Map<String, List<String>> orgContractMap,
            @Param("sceneCode") List<String> sceneCode,
            @Param("voucherDate") LocalDate localDate,
            @Param("periodCode") int periodCode
    );
}
