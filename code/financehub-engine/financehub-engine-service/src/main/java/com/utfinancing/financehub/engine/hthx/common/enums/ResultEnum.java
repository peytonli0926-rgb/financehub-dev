package com.utfinancing.financehub.engine.hthx.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 应用模块名称:
 * 代码描述:
 *
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2024/12/30 13:37
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {

    //**************************200-默认返回**********************//
    /**
     * 成功
     */
    SUCCESS(200,"请求成功！"),

    /**
     * 失败
     */
    ERROR(-200, "请求失败！"),

    /**
     * 程序运行出现异常
     */
    PROGRAM_RUN_EXCEPTION(201,"程序运行出现异常！"),



    //**************************300-通用提示**********************//
    COMMON_NO_DATA_SELECTED(300,"请至少勾选一条数据提交！"),
    COMMON_ONLY_SELECTED_ONE(301,"只能选择一条数据提交！"),
    COMMON_SUBMIT_STATUS_ERROR(302,"只有处理状态为已录入或者已拒绝的才可以提交！"),
    COMMON_VOUCHER_STATUS_ERROR(303,"处理状态为已录入或者已拒绝的才可以生成凭证！"),
    COMMON_IMPORT_EMPTY_ERROR(304,"导入数据为空，没有数据需要上传！"),

    COMMON_QUERY_SUBMIT_RESULT(305,"导入数据为空，没有数据需要上传！"),
    COMMON_SUBMIT_DATA_INVALID(306,"提交数据已失效，请重新提交！"),
    COMMON_FILE_UPLOAD_HAS_TIMED_OUT(307,"您已操作超时（${}分钟），烦请重新上传文件后再次提交！"),

    COMMON_THE_UPLOAD_FILE_EMPTY(308,"上传文件数据为空！"),
    COMMON_FILE_UPLOAD_SUCCESS(309,"文件上传成功！"),






    //**************************400-客户端错误**********************//
    /**
     * 参数缺失校验报错
     */
    PARAM_ERROR(409,"必传参数不能为空！"),

    /**
     * 参数值校验报错
     */
    PARAM_VALUE_ERROR(410,"参数值传值不规范校验报错！"),


    //**************************600-业务端错误**********************//
    /**
     * 规则集比对
     */
    SUBMIT_ERROR(506,"规则集比对运行报错！"),

    /**
     * 规则集比对-请勿重复提交
     */
    RESUBMIT_ERROR(507,"已有运行中的提交任务，请勿重复提交！"),

    /**
     * 规则集比对-数据未找到
     */
    DATA_NOT_FOUND(508,"未查询到该任务，请核查！"),

    /**
     * 规则集比对-文件生成失败
     */
    FILE_CREATE_ERROR(509,"文件生成失败！"),

    /**
     * 规则集比对-文件生成失败
     */
    TASK_EXE_ERROR(510,"文件还未生成成功，请稍后再下载！"),

    /**
     * 规则集比对-文件已被删除
     */
    FILE_DELETE(608,"文件已被删除!"),

    /**
     * 规则集比对-只有选择全量运行数据的任务才允许更新比对凭证
     */
    FULL_FLAG_CHECK_FAIL(609,"只有选择全量运行数据的任务才允许更新比对凭证！"),

    /**
     * 规则集比对-该条运行任务的数据未找到，烦请刷新页面后再次选择
     */
    TASK_NOT_FOUND(610,"该条运行任务的数据未找到，烦请刷新页面后再次选择！"),

    /**
     * 规则集比对-数据已失效，烦请刷新页面后再次选择
     */
    DATA_IS_INVALID(611,"数据已失效，烦请刷新页面后再次选择！"),

    /**
     * 规则集比对-只有选择执行成功状态的任务才允许更新凭证比对样本
     */
    STATE_CHECK_FAIL(612,"只有选择执行成功状态的任务才允许更新凭证比对样本！"),

    /**
     * 规则集比对-已限制只能选择最后生成的一条任务才能更新凭证比对样本
     */
    UPDATE_LAST_CHECK(613,"已限制只能选择最后生成的一条任务才能更新凭证比对样本！"),

    /**
     * 规则集比对-更新凭证比对样本失败
     */
    UPDATE_LIBRARY_FAIL(614,"更新凭证比对样本失败！"),

    /**
     * 节假日任务执行-校验不通过，烦请再次核查
     */
    CHECK_NO_PASS(615,"校验不通过，烦请再次核查！"),

    //**************************债务重组业务-回收设备**********************//
    /**
     * 导入数据为空，本次没有数据需要上传！
     */
    DR_RE_NO_DATA_UPLOAD(700,"导入数据为空，本次没有数据需要上传！"),

    /**
     * 不可以重复导入！
     */
    DR_RE_CONTRACT_HAS_BEEN_UPLOADED(701,"该合同号${}已存在入库日期${}的入库数据，不可以重复导入！"),


    //**************************减值计提-8--************************//
    IP_SUBMIT_BEFORE_VOUCHER_ERROR(801,"生成凭证后才可以提交！"),
    IP_FILE_UPLOAD_WAIT_TIME(802,"文件上传成功，请${}分钟后查看数据！"),
    IP_VOUCHER_PROMPT(803,"凭证生成中，请${}分钟后查看凭证生成结果！"),
    IP_SIGNING_PARTY_EMPTY(804,"合同编码:${}，根据签约主体编码:${}未查询到对应的签约主体！"),
    IP_COST_CENTER_EMPTY(805,"合同编码:${},根据签约主体名称:${}未查询到对应的成本中心！"),
    IP_FINANCIAL_EMPTY(806,"合同编码:${},根据金融机构名称:${}未查询到对应的金融机构！"),
    IP_ABROAD_NEED_TO_GENERATE_VOUCHERS(807,"合同编码:${},属于境外主体${}，无需生成凭证！"),
    IP_LATER_QUERY_RESULTS(808,"流程提交成功，请${}分钟后查看流程提交结果！"),
    IP_ONLY_REVIEWED_CAN_PUSH_VOUCHERS(809,"只有已复核状态才能推送凭证至金蝶！"),
    IP_VOUCHER_IS_NULL(810,"还未生成凭证，请先点击生成凭证！"),
    IP_VOUCHER_LATER_QUERY_RESULTS(811,"凭证推送中，请${}分钟后通过查看任务来查看推送结果！"),
    IP_CAN_NOT_REPEATEDLY_PUSH(812,"该笔减值计提明细凭证已推送至金蝶，不可重复推送！"),
    IP_NO_SUMMARY_VOUCHER_NEED_GENERATE(813,"该笔减值计提没有汇总凭证需要生成！"),
    IP_THERE_IS_A_SUMMARY_VOUCHER_BEING_GENERATED(814,"该笔单据存在正在生成中的汇总凭证，请生成完后再重试！"),
    IP_VERIFY_VOUCHER_BEFORE_SUBMIT(815,"凭证校验失败：${}，请重新生成凭证！"),






    //**************************未确认收款-9--************************//
    NC_FINANCIAL_TRANSFER_STATUS(900,"财务合同状态为资产转让状态时，请确认是否已录入其他应付款_代收款项科目(2241.09)！"),
    NC_REQUEST_FUNDS_SYSTEM_ERROR(901,"请求资金系统查询网银信息报错，请联系管理员！"),
    NC_OFFLINE_ONLINE_BANK_CURRENCY_IS_RMB(902,"网银${}在资金系统中已存在，请核实是否继续插入这些网银？"),
    NC_OFFLINE_ONLINE_BANK_CURRENCY_IS_NOT_RMB(903,"网银${}在资金系统不存在或其为外币网银，请使用【上传线下网银】功能进行上传"),



    ;

    private String message;

    private int code;


    ResultEnum(int code, String message) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public static ResultEnum parse(Integer status) {
        for (ResultEnum value : values()) {
            if (value.getCode() == status) {
                return value;
            }
        }
        return null;
    }

}
