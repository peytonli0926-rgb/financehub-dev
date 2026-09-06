package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.engine.enums.CheckExecuteStatusEnum;
import com.utfinancing.financehub.engine.finance.entity.FileRecordEntity;
import com.utfinancing.financehub.engine.finance.entity.NonConfirmCollectionFileUploadRecordEntity;
import com.utfinancing.financehub.engine.finance.model.dto.FileRecordQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ReportFinanceInOutQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ReportLeaseTableQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ReportFinanceInOutVO;
import com.utfinancing.financehub.engine.finance.model.vo.ReportLeaseTableVO;
import com.utfinancing.financehub.engine.finance.service.IFileRecordService;
import com.utfinancing.financehub.engine.finance.service.IReportCommonService;
import com.utfinancing.financehub.engine.finance.service.IReportFinanceInOutService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.net.URLEncoder;
import java.util.Map;


/**
 * @Author : bruyang
 * @Date : Create in 2024-02-27
 * @Description :   报表模块控制器实现类
 * @Modified :
 */
@Api(tags = "报表模块")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/report")
public class ReportCommonController {

    private final IReportCommonService reportCommonService;

    @Resource
    private IReportFinanceInOutService reportFinanceInOutService;

    @Resource
    private IFileRecordService fileRecordService;


    @ApiOperation(value = "租赁大表报表查询")
    @PostMapping("/leaseTable/report")
    public R<IPage<ReportLeaseTableVO>> selectLeaseTable(@RequestBody @Valid ReportLeaseTableQueryDTO queryDTO) {
        return R.ok(reportCommonService.selectLeaseTable(queryDTO));
    }

    @ApiOperation(value = "判断当前选择日期是查数据还是生成文件提供下载")
    @PostMapping("/leaseTable/check")
    public R<Map<String, String>> checkLeaseTableDate(@RequestBody @Valid ReportLeaseTableQueryDTO queryDTO) {
        return R.ok(reportCommonService.checkLeaseTableDate(queryDTO));
    }

    @ApiOperation(value = "生成租赁大表报表数据excel")
    @PostMapping("/leaseTable/export")
    public R<Map<String, String>> generateLeaseTableReportExcel(@RequestBody @Valid ReportLeaseTableQueryDTO queryDTO) {
        return R.ok(reportCommonService.generateLeaseTableReportExcel(queryDTO));
    }

    /**
     * @description: 统计报表-财务出库入库报表-列表查询
     * @author: zhangli.chen
     **/
    @ApiOperation(value = "财务入库出库报表查询")
    @PostMapping("/inbound-outbound/report")
    public R selectLeaseTable(@RequestBody @Valid ReportFinanceInOutQueryDTO queryDTO) {
        return R.ok(reportFinanceInOutService.selectPage(queryDTO));
    }

    @ApiOperation(value = "财务入库出库报表数据固化")
    @PostMapping("/reportInboundAndOutboundJob")
    public R<String> reportInboundAndOutboundJob() {
        return R.ok(reportFinanceInOutService.syncFinanceInboundOutboundData());
    }

    @ApiOperation(value = "租赁大表-上传文件查询")
    @PostMapping("/leaseTable/fileList")
    public R<IPage<FileRecordEntity>> selectLeaseTableFileList(@RequestBody @Valid FileRecordQueryDTO queryDTO) {
        return R.ok(reportCommonService.selectLeaseTableFileList(queryDTO));
    }

    /**
     * @description: 统计报表-财务出库入库报表-下载按钮
     * @author: zhangli.chen
     **/
    @ApiOperation(value = "生成财务入库出库报表数据excel")
    @PostMapping("/inbound-outbound/export")
    public R<Map<String, String>> generateInboundOutboundReportExcel(@RequestBody @Valid ReportFinanceInOutQueryDTO queryDTO) {
        return R.ok(reportFinanceInOutService.generateInboundOutboundReportExcel(queryDTO));
    }

    @ApiOperation(value = "根据文件记录ID下载文件")
    @PostMapping("/downloadFile/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<String> downloadFile(HttpServletResponse response,@PathVariable("id") @Valid @NotNull Long id) throws Exception {

        FileRecordEntity fileRecord = fileRecordService.getById(id);
        if (ObjectUtil.isEmpty(fileRecord)) {
            return R.fail("未查询到上传的文件记录!");
        }
        if(!StringUtils.equals(fileRecord.getExecuteStatus(), CheckExecuteStatusEnum.FINISH.getCode())){
            return R.fail("只能下载已生成的文件");
        }

        File file = new File(fileRecord.getFileLocation());
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStream fis = new BufferedInputStream(fileInputStream);
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        // 清空response
        response.reset();
        // 设置response的Header
        response.setCharacterEncoding("UTF-8");
        //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
        //attachment表示以附件方式下载 inline表示在线打开 "Content-Disposition: inline; filename=文件名.mp3"
        // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
        // 告知浏览器文件的大小
        response.addHeader("Content-Length", "" + file.length());
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        outputStream.write(buffer);
        outputStream.flush();
        return R.ok();
    }

    @ApiOperation(value = "财务入库出库-上传文件查询")
    @PostMapping("/inbound-outbound/fileList")
    public R<IPage<FileRecordEntity>> selectInboundOutboundFileList(@RequestBody @Valid FileRecordQueryDTO queryDTO) {
        return R.ok(reportFinanceInOutService.selectInboundOutboundFileList(queryDTO));
    }
}



