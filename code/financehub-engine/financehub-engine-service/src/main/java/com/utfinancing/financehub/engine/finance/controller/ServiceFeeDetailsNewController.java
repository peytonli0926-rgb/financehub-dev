package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeDetailsNewQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeDetailsNewDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeDetailsQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeDetailsNewVO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeDetailsVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import com.utfinancing.financehub.engine.finance.service.IServiceFeeDetailsNewService;


/**
 * @Author : le
 * @Date : Create in 2025-11-10
 * @Description :   ServiceFeeDetailsNew控制器实现类
 * @Modified :
 */
@Api(tags = "服务费分摊表详情-新")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/service-fee-details-new")
public class ServiceFeeDetailsNewController {

    private final IServiceFeeDetailsNewService serviceFeeDetailsNewService;

    @ApiOperation(value = "单月分页查询")
    @PostMapping("/detail/page")
    public R<IPage<ServiceFeeDetailsNewVO>> selectDetailPage(@RequestBody @Valid ServiceFeeDetailsQueryDTO queryDTO) {
        return R.ok(serviceFeeDetailsNewService.selectDetailPage(queryDTO));
    }


}



