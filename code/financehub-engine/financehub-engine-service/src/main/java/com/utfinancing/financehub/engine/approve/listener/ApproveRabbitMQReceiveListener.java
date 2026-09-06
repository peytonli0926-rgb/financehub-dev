package com.utfinancing.financehub.engine.approve.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.engine.config.ApproveRabbitmqConfig;
import com.utfinancing.financehub.engine.enums.ProcessStatusEnum;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.verification.service.ICourtCostService;
import com.utfinancing.financehub.engine.verification.service.IVerificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.approve.listener.ApproveRabbitMQReceiveListener</li>
 * <li>CreateTime : 2024/01/08 18:19</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Slf4j
@Component
public class ApproveRabbitMQReceiveListener {
    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private IManualService iManualService;

    @Resource
    private IVerificationService iVerificationService;

    @Resource
    private ICourtCostService iCourtCostService;

    @Resource
    private ICostChannelFeeService iCostChannelFeeService;

    @Resource
    private ILeaseIncomeService leaseIncomeService;

    @Resource
    private IChargeOffService iChargeOffService;

    @Resource
    private ITailDifferenceAdjustmentService iTailDifferenceAdjustmentService;

    @Resource
    private IOutTableAbsService iOutTableAbsService;

    @Resource
    private IAssetAbsRedeemService iAssetAbsRedeemService;

    @Resource
    private IAssetAbsTransferPaymentService iAssetAbsTransferPaymentService;

    @Resource
    private INonConfirmCollectionSecondDetailService nonConfirmCollectionSecondDetailService;

    @Resource
    private IParityTransferService iParityTransferService;
    @Resource
    private IPayVatService iPayVatService;
    @Resource
    private IImpairmentProvisionService iImpairmentProvisionService;
    @Resource
    private ITransferRegisterService iTransferRegisterService;
    @Resource
    private ISellRegisterService iSellRegisterService;
    @Resource
    private IRentRegisterService iRentRegisterService;
    @Resource
    private IRentIncomeConfirmService iRentIncomeConfirmService;

    @Resource
    private ILongReceivableRegisterService iLongReceivableRegisterService;

    @Resource
    private ILongIncomeConfirmService iLongIncomeConfirmService;
    @Resource
    private IInternalTransferService iInternalTransferService;
    @Resource
    private IOfflineContractService iOfflineContractService;
    @Resource
    private IContractHisService iContractHisService;
    @Resource
    private IRecyclingEquipmentInService iRecyclingEquipmentInService;
    @Resource
    private IRecyclingEquipmentOutDetailService iRecyclingEquipmentOutDetailService;
    @Resource
    private IPayableInsuranceService iPayableInsuranceService;

    @Resource
    private IMarginContractBalanceService iMarginContractBalanceService;

    @Resource
    private IContractStatusRecordService iContractStatusRecordService;
    @Resource
    private ITaOtherPayableService iTaOtherPayableService;
    @Resource
    private IServiceFeeNewService iServiceFeeService;
    @Resource
    private ITaReclassificationService iTaReclassificationService;


    @Resource
    private IConvertTransferService convertTransferService;

    @Resource
    private IDiscountedTransferInternalService discountedTransferInternalService;

    @Resource
    private IConvertTransferThirdPartService convertTransferThirdPartService;


    @Resource
    private IConvertTransferThirdPartPaymentService convertTransferThirdPartPaymentService;

    @Resource
    private IConvertTransferContractFeeService convertTransferContractFeeService;
    /**
     * 核销
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_HZHX})
    public void verificationMessage(Message message, Channel channel){
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("核销复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iVerificationService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("核销复核消费失败，失败原因：",e);
            throw new ServiceException("核销复核消费失败，失败原因："+e.getMessage());
        }
        log.info("核销审批通过");
    }

    /**
     * 收益计提
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_SYJT})
    public void syjtMessage(Message message, Channel channel){
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("收益计提复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            if (ProcessStatusEnum.REVIEWED.getCode().equals(approveDTO.getDocumentStatus())) {
                leaseIncomeService.pass(approveDTO);
            } else if (ProcessStatusEnum.REJECTED.getCode().equals(approveDTO.getDocumentStatus())) {
                leaseIncomeService.fail(approveDTO);
            }
        } catch (Exception e) {
            log.error("收益计提复核消费失败，失败原因：",e);
            throw new ServiceException("收益计提复核消费失败，失败原因："+e.getMessage());
        }
        log.info("收益计提审批通过");
    }


    /**
     * 未确认收款
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_SGPZRL, ApproveRabbitmqConfig.FINHUB_QUEUE_SGPZCX,
            ApproveRabbitmqConfig.FINHUB_QUEUE_SGPZTZ, ApproveRabbitmqConfig.FINHUB_QUEUE_SGPZMGRZRQ})
    public void wqrskQrMessage(Message message, Channel channel){
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("未确认收款 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
//            List<Long> ids = new ArrayList<>();
//            ids.add(approveDTO.getDocumentId());
            if (ProcessStatusEnum.REVIEWED.getCode().equals(approveDTO.getDocumentStatus())) {
                nonConfirmCollectionSecondDetailService.auditPass(approveDTO);
            } else if (ProcessStatusEnum.REJECTED.getCode().equals(approveDTO.getDocumentStatus())) {
                nonConfirmCollectionSecondDetailService.auditFailed(approveDTO);
            }
        } catch (Exception e) {
            log.error("wqrskQrMessage fail!",e);
        }
        log.info("未确认收款审批处理完成!");
    }


    /**
     * 未确认收款-手工调整余额
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_SGTZYE})
    public void sgtzyeMessage(Message message, Channel channel){
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("未确认收款-手工调整余额 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            if (ProcessStatusEnum.REVIEWED.getCode().equals(approveDTO.getDocumentStatus())) {
                nonConfirmCollectionSecondDetailService.auditPassForSGTZYE(approveDTO);
            } else if (ProcessStatusEnum.REJECTED.getCode().equals(approveDTO.getDocumentStatus())) {
                nonConfirmCollectionSecondDetailService.auditFailedForSGTZYE(approveDTO);
            }
        } catch (Exception e) {
            log.error("sgtzyeMessage fail!",e);
        }
        log.info("未确认收款-手工调整余额审批处理完成");
    }

    /**
     * 手工凭证
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_SGPZ})
    public void sgpzMessage(Message message, Channel channel){
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("手工凭证复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iManualService.updateStatus(approveDTO);
        } catch (Exception e) {
            log.error("手工凭证复核消费失败，失败原因：",e);
            throw new ServiceException("手工凭证复核消费失败，失败原因："+e.getMessage());
        }
    }

    /**
     * 诉讼费
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_SSF})
    public void ssfMessage(Message message, Channel channel){
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("诉讼费信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iCourtCostService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("诉讼费消费失败，失败原因：",e);
            throw new ServiceException("诉讼费消费失败，失败原因："+e.getMessage());
        }
    }

    /**
     * GPS
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_GPS})
    public void gpsMessage(Message message, Channel channel){
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("GPS信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iCostChannelFeeService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("GPS消费失败，失败原因：",e);
            throw new ServiceException("GPS消费失败，失败原因："+e.getMessage());
        }
    }

    /**
     * 手环设备款
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_SHSBK})
    public void shsbkMessage(Message message, Channel channel){
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("手环设备款信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iCostChannelFeeService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("手环设备款消费失败，失败原因：",e);
            throw new ServiceException("手环设备款消费失败，失败原因："+e.getMessage());
        }
    }

    /**
     * 收车费
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_SCF})
    public void scfMessage(Message message, Channel channel){
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("收车费信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iCostChannelFeeService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("收车费消费失败，失败原因：",e);
            throw new ServiceException("收车费消费失败，失败原因："+e.getMessage());
        }
    }

    /**
     * 经销商服务费
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_QDF})
    public void qdfMessage(Message message, Channel channel){
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("经销商服务费信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iCostChannelFeeService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("经销商服务费消费失败，失败原因：",e);
            throw new ServiceException("经销商服务费消费失败，失败原因："+e.getMessage());
        }
    }

    /**
     * Charge Off
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_CHARGEOFF})
    public void chargeOffMessage(Message message, Channel channel){
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("ChargeOff信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iChargeOffService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("ChargeOff消费失败，失败原因：",e);
            throw new ServiceException("ChargeOff消费失败，失败原因："+e.getMessage());
        }
    }

    /**
     * 尾差调整
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_WCTZ})
    public void wctzMessage(Message message, Channel channel){
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("尾差调整信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iTailDifferenceAdjustmentService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("尾差调整消费失败，失败原因：",e);
            throw new ServiceException("尾差调整消费失败，失败原因："+e.getMessage());
        }
    }


    /**
     * 出表ABC
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_CBABS})
    public void cbabsMessage(Message message, Channel channel){
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("出表ABC复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iOutTableAbsService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("出表ABC复核信息消费失败，失败原因：",e);
            throw new ServiceException("出表ABC复核信息消费失败，失败原因："+e.getMessage());
        }
    }

    /**
     * 资产赎回
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_ABSSH})
    public void zcshMessage(Message message, Channel channel){
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("资产赎回复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iAssetAbsRedeemService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("资产赎回复核信息消费失败，失败原因：",e);
            throw new ServiceException("资产赎回复核信息消费失败，失败原因："+e.getMessage());
        }
    }

    /**
     * 资产转付
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_ABSZF})
    public void zczfMessage(Message message, Channel channel){
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("资产转付复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iAssetAbsTransferPaymentService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("资产转付复核信息消费失败，失败原因：",e);
            throw new ServiceException("资产转付复核信息消费失败，失败原因："+e.getMessage());
        }
    }

    /**
     * 平价转让
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_PJZR})
    public void nbzrMessage(Message message, Channel channel){
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("内部转让复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iParityTransferService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("内部转让复核信息消费失败，失败原因：",e);
            throw new ServiceException("内部转让复核信息消费失败，失败原因："+e.getMessage());
        }
    }

    /**
     * 应交增值税
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_YJZZS})
    public void yjzzsMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("应交增值税复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iPayVatService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("应交增值税复核信息消费失败，失败原因：",e);
            throw new ServiceException("应交增值税复核信息消费失败，失败原因：" + e.getMessage());
        }
    }

    /**
     * 减值计提
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_JZJT})
    public void jzjtMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("减值计提复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iImpairmentProvisionService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("减值计提复核信息消费失败，失败原因：",e);
            throw new ServiceException("减值计提复核信息消费失败，失败原因：" + e.getMessage());
        }
    }

    /**
     * 抵债资产-转入登记
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_DZZCZRDJ})
    public void dzzczrdjMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("抵债资产-转入登记复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iTransferRegisterService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("抵债资产-转入登记复核信息消费失败，失败原因：",e);
            throw new ServiceException("抵债资产-转入登记复核信息消费失败，失败原因：" + e.getMessage());
        }
    }

    /**
     * 抵债资产-出售登记
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_DZZCCSDJ})
    public void dzzccsdjMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("抵债资产-出售登记复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iSellRegisterService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("抵债资产-出售登记复核信息消费失败，失败原因：",e);
            throw new ServiceException("抵债资产-出售登记复核信息消费失败，失败原因：" + e.getMessage());
        }
    }

    /**
     * 抵债资产-出租登记
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_DZZCCZDJ})
    public void dzzcczdjMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("抵债资产-出租登记复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iRentRegisterService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("抵债资产-出租登记复核信息消费失败，失败原因：",e);
            throw new ServiceException("抵债资产-出租登记复核信息消费失败，失败原因：" + e.getMessage());
        }
    }

    /**
     * 抵债资产-出租登记-收入确认
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_DZZCCZDJSRQR})
    public void dzzcczdjsrqrMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("抵债资产-出租登记-收入确认复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iRentIncomeConfirmService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("抵债资产-出租登记-收入确认复核信息消费失败，失败原因：",e);
            throw new ServiceException("抵债资产-出租登记-收入确认复核信息消费失败，失败原因：" + e.getMessage());
        }
    }

    /**
     * 长期应收款
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_CQYSK})
    public void cqyskMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("长期应收款复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iLongReceivableRegisterService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("长期应收款复核信息消费失败，失败原因：",e);
            throw new ServiceException("长期应收款复核信息消费失败，失败原因：" + e.getMessage());
        }
    }

    /**
     * 长期应收款-收入确认
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_CQYSKSRQR})
    public void cqysksrqrMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("长期应收款-收入确认复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iLongIncomeConfirmService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("长期应收款-收入确认复核信息消费失败，失败原因：",e);
            throw new ServiceException("长期应收款-收入确认复核信息消费失败，失败原因：" + e.getMessage());
        }
    }

    /**
     * 平价转让-内部调拨
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_PJZRDB})
    public void nbdbMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("平价转让-内部调拨复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iInternalTransferService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("平价转让-内部调拨复核信息消费失败，失败原因：",e);
            throw new ServiceException("平价转让-内部调拨复核信息消费失败，失败原因：" + e.getMessage());
        }
    }

    /**
     * 线下合同
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_XXHT})
    public void xxhtMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("线下合同复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iOfflineContractService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("线下合同复核信息消费失败，失败原因：",e);
            throw new ServiceException("线下合同复核信息消费失败，失败原因：" + e.getMessage());
        }
    }
    /**
     * 合同信息修改
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_HTXXXG})
    public void htxxxgMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("合同信息修改复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iContractHisService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("合同信息修改复核信息消费失败，失败原因：",e);
            throw new ServiceException("合同信息修改复核信息消费失败，失败原因：" + e.getMessage());
        }
    }
    /**
     * 设备回收-财务入库
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_CWRK})
    public void cwrkMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("线下合同复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iRecyclingEquipmentInService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("线下合同复核信息消费失败，失败原因：",e);
            throw new ServiceException("线下合同复核信息消费失败，失败原因：" + e.getMessage());
        }
    }

    /**
     * 设备回收-财务出库
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_CWCK})
    public void cwckMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("线下合同复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iRecyclingEquipmentOutDetailService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("线下合同复核信息消费失败，失败原因：",e);
            throw new ServiceException("线下合同复核信息消费失败，失败原因：" + e.getMessage());
        }
    }

    /**
     * 应付保险费
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_YFBXF})
    public void yfbxfMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("应付保险费复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iPayableInsuranceService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("应付保险费复核信息消费失败，失败原因：",e);
            throw new ServiceException("应付保险费复核信息消费失败，失败原因：" + e.getMessage());
        }
    }

    /**
     * 保证金
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_BZJ})
    public void bzjMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("保证金复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iMarginContractBalanceService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("保证金复核信息消费失败，失败原因：",e);
            throw new ServiceException("保证金复核信息消费失败，失败原因：" + e.getMessage());
        }
    }

    /**
     * 特殊合同状态
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_TSHT})
    public void tshtMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("特殊合同复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iContractStatusRecordService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("特殊合同复核信息消费失败，失败原因：",e);
            throw new ServiceException("特殊合同复核信息消费失败，失败原因：" + e.getMessage());
        }
    }

    /**
     * ta其他应付款
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_TAQTYFK})
    public void taqtyfkMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("ta其他应付款复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iTaOtherPayableService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("ta其他应付款复核信息消费失败，失败原因：",e);
            throw new ServiceException("ta其他应付款复核信息消费失败，失败原因：" + e.getMessage());
        }
    }

    /**
     * 咨询服务费分摊
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_FWFJT})
    public void fwfjtMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("咨询服务费分摊复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iServiceFeeService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("咨询服务费分摊复核信息消费失败，失败原因：",e);
            throw new ServiceException("咨询服务费分摊复核信息消费失败，失败原因：" + e.getMessage());
        }
    }

    /**
     * ta重分类明细表
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_TACFL})
    public void tacflMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("ta重分类明细表复核信息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            iTaReclassificationService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.error("ta重分类明细表复核信息消费失败，失败原因：",e);
            throw new ServiceException("ta重分类明细表复核信息消费失败，失败原因：" + e.getMessage());
        }
    }

    @RabbitListener(queues = ApproveRabbitmqConfig.FINHUB_QUEUE_ZJZR)
    public void zjzrHubMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("资产转让 - 折价转让 复核消息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            convertTransferService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.info("资产转让 - 折价转让 复核失败，失败原因是" + e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }

    @RabbitListener(queues = ApproveRabbitmqConfig.FINHUB_QUEUE_ZJZRDB)
    public void zjzrdbHubMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("资产转让- 折价转让调拨 复核消息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            discountedTransferInternalService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.info("资产转让- 折价转让调拨 复核失败，失败原因是" + e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }

    @RabbitListener(queues = ApproveRabbitmqConfig.FINHUB_QUEUE_DSFZR)
    public void dsfzrMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("资产转让 - 第三方转让 复核消息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            convertTransferThirdPartService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.info("资产转让 - 第三方转让 复核失败，失败原因是" + e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }

    @RabbitListener(queues = ApproveRabbitmqConfig.FINHUB_QUEUE_DSFZF)
    public void dsfzfMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("资产转让 - 第三方转付 复核消息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            convertTransferThirdPartPaymentService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.info("资产转让 - 第三方转付 复核失败，失败原因是" + e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }
    @RabbitListener(queues = ApproveRabbitmqConfig.FINHUB_QUEUE_ZRFY)
    public void zrfyMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("资产转让 - 转让合同费用 复核消息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            convertTransferContractFeeService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.info("资产转让 - 转让合同费用 复核失败，失败原因是" + e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }
    @RabbitListener(queues = ApproveRabbitmqConfig.FINHUB_QUEUE_ZRQT)
    public void zrqtMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("资产转让 - 转让其他 复核消息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            convertTransferContractFeeService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.info("资产转让 - 转让其他 复核失败，失败原因是" + e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }
    @RabbitListener(queues = ApproveRabbitmqConfig.FINHUB_QUEUE_ZFQT)
    public void zfqtMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("资产转让 - 转付其他 复核消息 msg：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            convertTransferContractFeeService.updateProcessStatus(approveDTO);
        } catch (Exception e) {
            log.info("资产转让 - 转付其他 复核失败，失败原因是" + e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 未确认收款-线下网银上传
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {ApproveRabbitmqConfig.FINHUB_QUEUE_XXWYSC})
    public void xxwyscMessage(Message message, Channel channel){
        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.info("未确认收款-线下网银上传MQ消息：{}", msg);
            CommonApproveDTO approveDTO = objectMapper.readValue(msg, CommonApproveDTO.class);
            if (ProcessStatusEnum.REVIEWED.getCode().equals(approveDTO.getDocumentStatus())) {
                nonConfirmCollectionSecondDetailService.auditPassForSGTZYE(approveDTO);
            } else if (ProcessStatusEnum.REJECTED.getCode().equals(approveDTO.getDocumentStatus())) {
                nonConfirmCollectionSecondDetailService.auditFailedForSGTZYE(approveDTO);
            }
        } catch (Exception e) {
            log.error("xxwyscMessage fail:{}",e.getMessage());
        }
        log.info("未确认收款-线下网银上传审批处理完成");
    }
}
