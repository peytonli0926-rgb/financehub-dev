package com.utfinancing.financehub.engine.rule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.model.dto.ContractDetailQueryInputDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractDetailQueryOutputDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractTransactionVO;
import com.utfinancing.financehub.engine.rule.entity.InterfaceDataEntity;
import com.utfinancing.financehub.engine.rule.model.dto.InterfaceDataQueryDTO;
import com.utfinancing.financehub.engine.rule.model.dto.SelectHtqzSceneInputDTO;
import com.utfinancing.financehub.engine.verification.model.dto.VerificationPaybackQueryDTO;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationPaybackDetailsVO;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationPaybackVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 接口数据表 Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-09-14
 */
public interface InterfaceDataMapper extends BaseMapper<InterfaceDataEntity> {

    IPage<VerificationPaybackVO> selectPaybackPage(@Param("pager") final Page pager,
                                                   @Param("condition") final VerificationPaybackQueryDTO condition);

    IPage<VerificationPaybackDetailsVO> selectPaybackDetailsPage(@Param("pager") final Page pager,
                                                                 @Param("condition") final VerificationPaybackQueryDTO condition);

    List<VerificationPaybackDetailsVO> selectPayBackAmount(@Param("condition") final VerificationPaybackQueryDTO queryDTO);

    List<VerificationPaybackDetailsVO> listPaybackDetails(@Param("condition") final VerificationPaybackQueryDTO queryDTO);

    /**
     * 查询网银编号不为空的数据-排除已经取过的数据
     */
    List<InterfaceDataEntity> selectEbankSerialNumberNotNullData();

    IPage<ContractTransactionVO> contractTransactionByPage(Page page,@Param("param") InterfaceDataQueryDTO queryDTO);

    /**
     * 查询合同起租场景
     * @return
     */
    public List<InterfaceDataEntity> selectHtqzScene(@Param("params") SelectHtqzSceneInputDTO params);

    List<VerificationPaybackVO> selectPaybackList(@Param("condition") VerificationPaybackQueryDTO queryDTO);

    List<VerificationPaybackDetailsVO> selectPaybackDetailsList(@Param("condition") final VerificationPaybackQueryDTO condition);
    List<ContractDetailQueryOutputDTO> contractDetailQuery(@Param("param") ContractDetailQueryInputDTO queryDTO);
}
