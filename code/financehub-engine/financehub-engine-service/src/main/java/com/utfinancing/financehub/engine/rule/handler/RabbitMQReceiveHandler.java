package com.utfinancing.financehub.engine.rule.handler;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rabbitmq.client.Channel;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderSaveDTO;
import com.utfinancing.financehub.engine.claim.service.IClaimOrderService;
import com.utfinancing.financehub.engine.config.RabbitmqConfig;
import com.utfinancing.financehub.engine.enums.MqErrorMessageStatusEnum;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.finance.entity.FundBusinessSystemEbankAmountMappingEntity;
import com.utfinancing.financehub.engine.finance.model.dto.CloseAccountDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundBusinessSystemEbankAmountMappingDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundBusinessSystemEbankMappingDTO;
import com.utfinancing.financehub.engine.finance.model.dto.InvoiceClaimDTO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.rule.model.dto.MqErrorMessageDTO;
import com.utfinancing.financehub.engine.rule.service.IMqErrorMessageService;
import com.utfinancing.financehub.engine.rule.service.IRawTransactionDataService;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.rule.entity.RawTransactionDataEntity;
import com.utfinancing.financehub.engine.xxlJob.DataExecutionJobHandler;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 20/09/2023
 */
@Component
@Slf4j
public class RabbitMQReceiveHandler {


    @Autowired
    private IRawTransactionDataService rawTransactionDataService;
    @Autowired
    private DataExecutionJobHandler dataExecutionJobHandler;
    @Autowired
    private IFundPaymentDataService fundPaymentDataService;
    @Autowired
    private IFundEbankTransactionDataService ebankTransactionDataService;
    @Autowired
    private ILprDataService iLprDataService;
    @Autowired
    private IClaimOrderService claimOrderService;

    @Autowired
    private IRepaymentPlanService repaymentPlanService;

    @Autowired
    private IFundBusinessSystemEbankMappingService iFundBusinessSystemEbankMappingService;

    @Autowired
    private IInvoiceClaimService invoiceClaimService;

    @Autowired
    private IMqErrorMessageService mqErrorMessageService;

    @Autowired
    private ICloseAccountService iCloseAccountService;

    @Autowired
    private IHyFullOnlineBankBatchNoMappingService hyFullOnlineBankBatchNoMappingService;

    @Autowired
    private IFundBusinessSystemEbankAmountMappingService iFundBusinessSystemEbankAmountMappingService;

    @RabbitListener(queues = {RabbitmqConfig.QUEUE_TRANSACTION_DATA})
    public void receiveTransactionData(Message message, Channel channel){
        try {
            MessageProperties messageProperties = message.getMessageProperties();
            String messageId = null;
            if (messageProperties != null){
                messageId = messageProperties.getCorrelationId();
            }
            String messageString = StrUtil.str(message.getBody(), "UTF-8");
            log.info("receive QUEUE_TRANSACTION_DATA messageId:{}, message:{}", messageId, messageString);
            JSONObject jsonObject = JSONObject.parseObject(messageString);
            RawTransactionDataEntity entity = rawTransactionDataService.saveRawData(messageId, jsonObject);
            dataExecutionJobHandler.executeRawData(entity.getId());
        }catch (Exception e){
            log.error("receive message error.", e);
            throw e;
        }
    }

    @RabbitListener(queues = {RabbitmqConfig.QUEUE_REPAYMENT_PLAN_DATA})
    public void receiveRepaymentData(Message message, Channel channel){
        try {
            MessageProperties messageProperties = message.getMessageProperties();
            String messageId = null;
            if (messageProperties != null){
                messageId = messageProperties.getCorrelationId();
            }
            String messageString = StrUtil.str(message.getBody(), "UTF-8");
            log.info("receive QUEUE_REPAYMENT_PLAN_DATA messageId:{}, message:{}", messageId, messageString);
            JSONObject jsonObject = JSONObject.parseObject(messageString);
            repaymentPlanService.saveRawData(messageId, jsonObject);
        }catch (Exception e){
            log.error("receive message error.", e);
            throw e;
        }
    }

    /**
     * 接收恒运网银编号和批扣号的映射关系
     */
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_HY_ONLINE_BANK_BATCH_REFUND_MAPPING})
    public void receiveFullOnlineBankBatchRefundMappingData(Message message, Channel channel){
        try {
            MessageProperties messageProperties = message.getMessageProperties();
            String messageId = null;
            if (messageProperties != null){
                messageId = messageProperties.getCorrelationId();
            }
            String messageString = StrUtil.str(message.getBody(), "UTF-8");
            log.info("receive QUEUE_HY_ONLINE_BANK_BATCH_REFUND_MAPPING messageId:{}, message:{}", messageId, messageString);
            JSONObject jsonObject = JSONObject.parseObject(messageString);
            hyFullOnlineBankBatchNoMappingService.receiveBusinessDataFromMQ(messageId, jsonObject);
        }catch (Exception e){
            log.error("receive message error.", e);
            throw e;
        }
    }

    @RabbitListener(queues = {RabbitmqConfig.QUEUE_FUND_PAYMENT_DATA})
    public void receiveFundPaymentData(Message message, Channel channel){
        try {
            String messageString = StrUtil.str(message.getBody(), "UTF-8");
            log.info("receive QUEUE_FUND_PAYMENT_DATA message:{}", messageString);
            JSONObject jsonObject = JSONObject.parseObject(messageString);
            fundPaymentDataService.saveRawData(jsonObject);
        } catch (Exception e) {
            log.error("receiveFundPaymentData message error.", e);
            throw e;
        }
    }

    @RabbitListener(queues = {RabbitmqConfig.QUEUE_FUND_EBANK_TRANSACTION_DATA})
    public void receiveFundEbankTransactionData(Message message, Channel channel){
        try {
            String messageString = StrUtil.str(message.getBody(), "UTF-8");
            log.info("receive QUEUE_FUND_EBANK_TRANSACTION_DATA message:{}", messageString);
            JSONArray jsonArray = JSONArray.parseArray(messageString);
            ebankTransactionDataService.saveRawData(jsonArray);
        } catch (Exception e) {
            log.error("receiveFundEbankTransactionData message error.", e);
            throw e;
        }
    }

    @RabbitListener(queues = {RabbitmqConfig.QUEUE_FUND_LPR_DATA})
    public void receiveFundLprData(Message message, Channel channel){
        try {
            String messageString = StrUtil.str(message.getBody(), "UTF-8");
            log.info("receive QUEUE_FUND_LPR_DATA message:{}", messageString);
            JSONArray jsonArray = JSONArray.parse(messageString);
            iLprDataService.saveRawData(jsonArray);
        } catch (Exception e) {
            log.error("receiveFundLprData message error.", e);
            throw e;
        }
    }


    @RabbitListener(queues = {RabbitmqConfig.QUEUE_CLAIM_ORDER_TRANSACTION_DATA})
    public void receiveClaimOrderData(Message message, Channel channel){
        try {
            String messageString = StrUtil.str(message.getBody(), "UTF-8");
            log.info("receive QUEUE_CLAIM_ORDER_TRANSACTION_DATA message:{}", messageString);
            if (!JSONUtil.isTypeJSON(messageString)){
                //非JSON格式，直接跳过
                log.info("message is not json format. message:{}, queue: QUEUE_CLAIM_ORDER_TRANSACTION_DATA", messageString);
            }
            ClaimOrderSaveDTO claimOrderSaveDTO = JSONObject.parseObject(messageString, ClaimOrderSaveDTO.class);
            claimOrderService.saveOrderTransactionData(claimOrderSaveDTO);
        } catch (Exception e) {
            log.error("claimOrderService.saveOrderTransactionData error.", e);
            throw e;
        }
    }

    @RabbitListener(queues = {RabbitmqConfig.QUEUE_FUND_EBANK_MATCH})
    public void fundBusinessSystemEbankMatch(Message message, Channel channel){
        try {
            String messageString = StrUtil.str(message.getBody(), "UTF-8");
            log.info("receive QUEUE_FUND_EBANK_MATCH message:{}", messageString);
            if (!JSONUtil.isTypeJSON(messageString)){
                log.info("message is not json format. message:{}, queue: QUEUE_FUND_EBANK_MATCH", messageString);
                return;
            }
            List<FundBusinessSystemEbankMappingDTO> ebankMappingDTOList = JSONArray.parseArray(messageString,FundBusinessSystemEbankMappingDTO.class);
            log.info("queue: QUEUE_FUND_EBANK_MATCH,转换后的数据：{}", JSONObject.toJSONString(ebankMappingDTOList));
            iFundBusinessSystemEbankMappingService.saveEbankMapping(ebankMappingDTOList);
        } catch (Exception e) {
            log.error("receiveFundPaymentData message error.", e);
            throw e;
        }
    }

    @RabbitListener(queues = {RabbitmqConfig.QUEUE_FUND_INVOICE_CLAIM_DATA})
    public void fundInvoiceClaimData(Message message, Channel channel){
        try {
            String messageString = StrUtil.str(message.getBody(), "UTF-8");
            log.info("receive QUEUE_FUND_INVOICE_CLAIM_DATA message:{}", messageString);
            if (!JSONUtil.isTypeJSON(messageString)){
                log.info("message is not json format. message:{}, queue: QUEUE_FUND_INVOICE_CLAIM_DATA", messageString);
                return;
            }
            InvoiceClaimDTO invoiceClaimDTO =  JSONObject.parseObject(messageString,InvoiceClaimDTO.class);

            String mqData = JSONObject.toJSONString(invoiceClaimDTO);
            invoiceClaimDTO.setMqData(JSONObject.toJSONString(invoiceClaimDTO));

            transMqBusinessSource(invoiceClaimDTO);

            transMqOrgId(invoiceClaimDTO);

            log.info("queue: QUEUE_FUND_EBANK_MATCH,转换后的数据：{}", mqData);
            invoiceClaimService.saveMqInvoiceClaim(invoiceClaimDTO);
        } catch (Exception e) {
            log.error("receiveFundPaymentData message error.", e);
            throw e;
        }
    }

    //sourcefrom为2的数据，org_id按invoice_type判断：包含“恒运”主体是02-C0001；包含“恒信上海”主体是30001；其他主体默认是01-C0001
    private static void transMqOrgId(InvoiceClaimDTO invoiceClaimDTO) {
        String invoiceType = invoiceClaimDTO.getInvoiceType();
        String orgId = "";
        if(StringUtils.isEmpty(invoiceType)){
            orgId = "01-C0001";
        }else if(invoiceType.contains("恒运")){
            orgId = "02-C0001";
        }else if(invoiceType.contains("恒信上海")){
            orgId = "30001";
        }else if(invoiceType.contains("小微")){
            orgId = "80001";
        }else{
            orgId = "01-C0001";
        }
        invoiceClaimDTO.setOrgId(orgId);
    }

    private static void transMqBusinessSource(InvoiceClaimDTO invoiceClaimDTO) {
        /**
         * mq的business_source转换
         * 1、'TYPT'转为'1';
         * 2、'XWXT'转为'2';
         * 3、'SYCXT'转为'3';
         * 4、'CYCXT'转为'4';
         */
        String businessSource = invoiceClaimDTO.getBusinessSource();
        String newBusinessSource = null;
        if(StringUtils.isNotEmpty(businessSource)){
            if(StringUtils.equals(businessSource, SystemEnum.TYPT.getCode())){
                newBusinessSource = "1";
            }else if(StringUtils.equals(businessSource, SystemEnum.XWXT.getCode())){
                newBusinessSource = "2";
            }else if(StringUtils.equals(businessSource, SystemEnum.SYCXT.getCode())){
                newBusinessSource = "3";
            }else if(StringUtils.equals(businessSource, SystemEnum.CYCXT.getCode())){
                newBusinessSource = "4";
            }
        }
        invoiceClaimDTO.setBusinessSource(newBusinessSource);
    }

    @RabbitListener(queues = RabbitmqConfig.QUEUE_FINHUB_ERORR_MESSAGE)
    public void receiveConsumerErrorMessage(Message message, Channel channel){
        try {
            MqErrorMessageDTO errorMessageDTO = new MqErrorMessageDTO();
            String messageString = StrUtil.str(message.getBody(), "UTF-8");
            log.info("receive QUEUE_FINHUB_ERORR_MESSAGE message:{}", messageString);
            MessageProperties messageProperties = message.getMessageProperties();
            String exceptionMessage = messageProperties.getHeader("x-exception-message").toString();
            String originalRoutingKey = messageProperties.getHeader("x-original-routingKey");
            String originalExchange = messageProperties.getHeader("x-original-exchange");
            Object exceptionStacktrace = messageProperties.getHeader("x-exception-stacktrace");
            errorMessageDTO.setStatus(MqErrorMessageStatusEnum.NOT_PROCESS.getCode());
            errorMessageDTO.setMessageBody(messageString);
            errorMessageDTO.setExceptionMessage(StrUtil.subWithLength(exceptionMessage, 0, 400));
            errorMessageDTO.setOriginalRoutingKey(StrUtil.subWithLength(originalRoutingKey, 0, 128));
            errorMessageDTO.setOriginalExchange(StrUtil.subWithLength(originalExchange, 0, 255));
            errorMessageDTO.setExceptionStacktrace(StrUtil.toString(exceptionStacktrace));
            mqErrorMessageService.saveMqErrorMessage(errorMessageDTO);
        }catch (Exception e){
            log.error("save QUEUE_FINHUB_ERORR_MESSAGE error.", e);
        }
    }

    @RabbitListener(queues = {RabbitmqConfig.QUEUE_FINHUB_CLOSE_ACCOUNT_DATA})
    public void finhubCloseAccountData(Message message, Channel channel){
        try {
            String messageString = StrUtil.str(message.getBody(), "UTF-8");
            log.info("receive QUEUE_FINHUB_CLOSE_ACCOUNT_DATA message:{}", messageString);
            if (!JSONUtil.isTypeJSON(messageString)){
                log.info("message is not json format. message:{}, queue: QUEUE_FINHUB_CLOSE_ACCOUNT_DATA", messageString);
                return;
            }
            CloseAccountDTO closeAccountDTO =  JSONObject.parseObject(messageString,CloseAccountDTO.class);
            log.info("queue: QUEUE_FINHUB_CLOSE_ACCOUNT_DATA,转换后的数据：{}", JSONObject.toJSONString(closeAccountDTO));
            iCloseAccountService.saveCloseAccount(closeAccountDTO);
        } catch (Exception e) {
            log.error("QUEUE_FINHUB_CLOSE_ACCOUNT_DATA message error.", e);
            throw e;
        }
    }

    @RabbitListener(queues = {RabbitmqConfig.QUEUE_FUND_EBANK_AMOUNT_MATCH})
    public void finhubFundEbankAmountMatch(Message message, Channel channel){
        try {
            String messageString = StrUtil.str(message.getBody(), "UTF-8");
            log.info("receive QUEUE_FUND_EBANK_AMOUNT_MATCH message:{}", messageString);
            if (!JSONUtil.isTypeJSON(messageString)){
                log.info("message is not json format. message:{}, queue: QUEUE_FINHUB_CLOSE_ACCOUNT_DATA", messageString);
                return;
            }
            List<FundBusinessSystemEbankAmountMappingDTO> ebankMappingDTOList = JSONArray.parseArray(messageString, FundBusinessSystemEbankAmountMappingDTO.class);
            log.info("queue: QUEUE_FUND_EBANK_AMOUNT_MATCH,转换后的数据：{}", JSONObject.toJSONString(ebankMappingDTOList));
            iFundBusinessSystemEbankAmountMappingService.saveFundBusinessSystemEbankAmountMappingList(ebankMappingDTOList);
        } catch (Exception e) {
            log.error("QUEUE_FUND_EBANK_AMOUNT_MATCH message error.", e);
            throw e;
        }
    }

}
