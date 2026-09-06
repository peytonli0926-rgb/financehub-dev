package com.utfinancing.financehub.engine.approve.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveQueryDTO;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.model.vo.ApproveVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

import com.utfinancing.financehub.engine.approve.service.IApproveService;


/**
 * @Author : bruyang
 * @Date : Create in 2024-01-08
 * @Description :   Approve控制器实现类
 * @Modified :
 */
@Api(tags = "审批相关接口")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/approve/approve")
public class ApproveController {

    private final IApproveService  approveService;

    @ApiOperation(value = "待我审批分页查询")
    @PostMapping("/todoApproveByPage")
    public R<IPage<ApproveVO>> todoApproveByPage(@RequestBody @Valid ApproveQueryDTO queryDTO) {
        return R.ok(approveService.todoApproveByPage(queryDTO));
    }

    @ApiOperation(value = "我的单据分页查询")
    @PostMapping("/myDocumentByPage")
    public R<IPage<ApproveVO>> myDocumentByPage(@RequestBody @Valid ApproveQueryDTO queryDTO) {
        return R.ok(approveService.myDocumentByPage(queryDTO));
    }

    @ApiOperation(value = "已审批分页查询")
    @PostMapping("/approvedByPage")
    public R<IPage<ApproveVO>> approvedByPage(@RequestBody @Valid ApproveQueryDTO queryDTO) {
        return R.ok(approveService.approvedByPage(queryDTO));
    }

    @ApiOperation(value = "审批通过接口")
    @PostMapping("/pass")
    public R<Boolean> pass(@RequestBody @Valid List<Long> idList) {
        return R.ok(approveService.pass(idList));
    }

    @ApiOperation(value = "审批拒绝接口")
    @PostMapping("/refuse")
    public R<Boolean> refuse(@RequestBody @Valid List<Long> idList,@RequestParam(value = "remark",required = false) String remark) {
        return R.ok(approveService.refuse(idList,remark));
    }

    @ApiOperation(value = "已审批单据退回接口")
    @PostMapping("/returnReviewed")
    public R<Boolean> returnReviewed(@RequestBody @Valid List<Long> idList,@RequestParam(value = "remark",required = false) String remark) {
        return R.ok(approveService.returnReviewed(idList, remark));
    }

    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Map<Long,Long>> submit(@RequestBody @Valid List<ApproveDTO> approveDTOList) {
        return R.ok(approveService.submit(approveDTOList));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @Valid List<Long> idList) {
        return R.ok(approveService.withdraw(idList));
    }


    @ApiOperation(value = "批量同意全部")
    @PostMapping("/passAll")
    public R<Boolean> passAll() {
        return R.ok(approveService.passAll());
    }

    @ApiOperation(value = "批量拒绝全部")
    @PostMapping("/refuseAll")
    public R<Boolean> refuseAll() {
        return R.ok(approveService.refuseAll());
    }


}



