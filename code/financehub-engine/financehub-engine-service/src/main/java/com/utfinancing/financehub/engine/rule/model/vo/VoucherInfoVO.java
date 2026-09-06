package com.utfinancing.financehub.engine.rule.model.vo;

import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO</li>
 * <li>CreateTime : 2024/02/22 14:58</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Api(value = "生成凭证信息VO")
@Data
public class VoucherInfoVO {

    @ApiModelProperty("凭证数据唯一id")
    private String orderId;

    @ApiModelProperty("生成凭证信息")
    private List<VoucherDTO> voucherDTOList;

    @ApiModelProperty("报错信息")
    private String errorInfo;

    @ApiModelProperty("场景编码")
    private String sceneCode;
    @ApiModelProperty("合同编码")
    private String contractCode;
    @ApiModelProperty("签约主体")
    private String orgId;
}
