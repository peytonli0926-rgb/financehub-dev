package com.utfinancing.financehub.engine.rule.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.rule.model.dto.MqErrorMessageQueryDTO;
import com.utfinancing.financehub.engine.rule.model.dto.MqErrorMessageDTO;
import com.utfinancing.financehub.engine.rule.model.dto.MqErrorMessageRePushReqDTO;
import com.utfinancing.financehub.engine.rule.model.vo.MqErrorMessageVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.rule.service.IMqErrorMessageService;


/**
 * @Author : lixin
 * @Date : Create in 2024-01-08
 * @Description :   MqErrorMessage控制器实现类
 * @Modified :
 */
@Api(tags = "MQ异常消息记录")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/rule/mq-error-message")
public class MqErrorMessageController {

    private final IMqErrorMessageService  mqErrorMessageService;

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(mqErrorMessageService.removeById(id));
    }

    @ApiOperation(value = "获取详情")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<MqErrorMessageDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(mqErrorMessageService.getMqErrorMessageDTOById(id));
    }

    @ApiOperation(value = "重新推送")
    @PostMapping("/rePush")
    public R<Boolean> rePushMessage(@RequestBody @Valid MqErrorMessageRePushReqDTO queryDTO) {
        return R.ok(mqErrorMessageService.rePushMessage(queryDTO.getIds()));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<MqErrorMessageVO>> page(@RequestBody @Valid MqErrorMessageQueryDTO queryDTO) {
        return R.ok(mqErrorMessageService.selectPage(queryDTO));
    }

}



