package com.utfinancing.financehub.engine.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author : lixin
 * @Date : Create in 20/09/2023
 */
@Configuration
public class RabbitmqConfig {

    //交易数据队列
    public static final String QUEUE_TRANSACTION_DATA = "finhub_queue_transaction_data";
    public static final String ROUTINGKEY_TRANSACTION_DATA = "finhub_routingkey_transaction_data";

    //偿还计划队列
    public static final String QUEUE_REPAYMENT_PLAN_DATA = "finhub_queue_repayment_plan_data";
    public static final String ROUTINGKEY_REPAYMENT_PLAN_DATA= "finhub_routingkey_repayment_plan_data";


    //恒运网银编号和批扣号映射队列
    public static final String QUEUE_HY_ONLINE_BANK_BATCH_REFUND_MAPPING = "finhub_queue_hy_online_bank_batch_refund_mapping";
    public static final String ROUTINGKEY_HY_ONLINE_BANK_BATCH_REFUND_MAPPING = "finhub_routingkey_hy_online_bank_batch_refund_mapping";

    //资金系统
    //付款数据
    public static final String QUEUE_FUND_PAYMENT_DATA = "finhub_queue_fund_payment_data";
    public static final String ROUTINGKEY_FUND_PAYMENT_DATA = "finhub_routingkey_fund_payment_data";

    //网银收付款数据
    public static final String QUEUE_FUND_EBANK_TRANSACTION_DATA = "finhub_queue_fund_ebank_transaction_data";
    public static final String ROUTINGKEY_FUND_EBANK_TRANSACTION_DATA = "finhub_routingkey_fund_ebank_transaction_data";

    //LPR数据
    public static final String QUEUE_FUND_LPR_DATA = "finhub_queue_fund_lpr_data";
    public static final String ROUTINGKEY_FUND_LPR_DATA = "finhub_routingkey_fund_lpr_data";
    //end资金系统


    //报销系统
    //报销单据交易数据
    public static final String QUEUE_CLAIM_ORDER_TRANSACTION_DATA = "finhub_queue_claim_order_transaction_data";
    public static final String ROUTINGKEY_CLAIM_ORDER_TRANSACTION_DATA = "finhub_routingkey_claim_order_transaction_data";

    public static final String EXCHANGE_DIRECT_TRANSACTION_DATA = "finhub_exchang_direct_transaction_data";

    //资金系统和业务系统网银编号映射关系队列
    public static final String QUEUE_FUND_EBANK_MATCH = "finhub_queue_fund_ebank_match";
    //资金系统和业务系统网银编号映射关系队列 routingkey
    public static final String ROUTINGKEY_FUND_EBANK_MATCH = "finhub_routingkey_fund_ebank_match";

    //开票认领队列
    public static final String QUEUE_FUND_INVOICE_CLAIM_DATA = "finhub_queue_fund_invoice_claim_data";
    //开票认领队列 routingkey
    public static final String ROUTINGKEY_FUND_INVOICE_CLAIM_DATA = "finhub_routingkey_fund_invoice_claim_data";

    //财务中台消费错误队列
    public static final String QUEUE_FINHUB_ERORR_MESSAGE = "finhub_queue_error_message";
    public static final String ROUTINGKEY_FINHUB_ERROR_MESSAGE = "finhub_routingkey_error_message";

    //系统关账期间数据
    public static final String QUEUE_FINHUB_CLOSE_ACCOUNT_DATA = "finhub_queue_close_account_data";
    public static final String ROUTINGKEY_FINHUB_CLOSE_ACCOUNT_DATA = "finhub_routingkey_close_account_data";


    //资金系统和业务系统网银编号金额映射关系队列
    public static final String QUEUE_FUND_EBANK_AMOUNT_MATCH = "finhub_queue_fund_ebank_amount_match";
    //资金系统和业务系统网银编号金额映射关系 routingkey
    public static final String ROUTINGKEY_FUND_EBANK_AMOUNT_MATCH = "finhub_routingkey_fund_ebank_amount_match";

    //声明交换机
    @Bean(EXCHANGE_DIRECT_TRANSACTION_DATA)
    public Exchange EXCHANGE_DIRECT_TRANSACTION_DATA(){
        //durable(true) 持久化，mq重启之后交换机还在
        return ExchangeBuilder.directExchange(EXCHANGE_DIRECT_TRANSACTION_DATA).durable(true).build();
    }

    //交易数据队列
    @Bean(QUEUE_TRANSACTION_DATA)
    public Queue QUEUE_TRANSACTION_DATA(){
        return new Queue(QUEUE_TRANSACTION_DATA);
    }

    //偿还计划队列
    @Bean(QUEUE_REPAYMENT_PLAN_DATA)
    public Queue QUEUE_REPAYMENT_PLAN_DATA(){
        return new Queue(QUEUE_REPAYMENT_PLAN_DATA);
    }

    // 恒运网银编号和批扣号映射队列
    @Bean(QUEUE_HY_ONLINE_BANK_BATCH_REFUND_MAPPING)
    public Queue QUEUE_HY_ONLINE_BANK_BATCH_REFUND_MAPPING(){
        return new Queue(QUEUE_HY_ONLINE_BANK_BATCH_REFUND_MAPPING);
    }

    //付款数据队列
    @Bean(QUEUE_FUND_PAYMENT_DATA)
    public Queue QUEUE_FUND_PAYMENT_DATA(){
        return new Queue(QUEUE_FUND_PAYMENT_DATA);
    }

    //网银收付款数据
    @Bean(QUEUE_FUND_EBANK_TRANSACTION_DATA)
    public Queue QUEUE_FUND_EBANK_TRANSACTION_DATA(){
        return new Queue(QUEUE_FUND_EBANK_TRANSACTION_DATA);
    }

    //lpr数据
    @Bean(QUEUE_FUND_LPR_DATA)
    public Queue QUEUE_FUND_LPR_DATA(){
        return new Queue(QUEUE_FUND_LPR_DATA);
    }

    //报销系统-报销单据
    @Bean(QUEUE_CLAIM_ORDER_TRANSACTION_DATA)
    public Queue QUEUE_CLAIM_ORDER_TRANSACTION_DATA(){
        return new Queue(QUEUE_CLAIM_ORDER_TRANSACTION_DATA);
    }

    //资金系统，业务系统网银编号映射表数据
    @Bean(QUEUE_FUND_EBANK_MATCH)
    public Queue QUEUE_FUND_EBANK_MATCH(){
        return new Queue(QUEUE_FUND_EBANK_MATCH);
    }

    //开票认领
    @Bean(QUEUE_FUND_INVOICE_CLAIM_DATA)
    public Queue QUEUE_FUND_INVOICE_CLAIM_DATA(){
        return new Queue(QUEUE_FUND_INVOICE_CLAIM_DATA);
    }

    @Bean(QUEUE_FINHUB_ERORR_MESSAGE)
    public Queue QUEUE_FINHUB_ERORR_MESSAGE(){
        return new Queue(QUEUE_FINHUB_ERORR_MESSAGE);
    }

    //交易数据队列
    @Bean(QUEUE_FINHUB_CLOSE_ACCOUNT_DATA)
    public Queue QUEUE_FINHUB_CLOSE_ACCOUNT_DATA(){
        return new Queue(QUEUE_FINHUB_CLOSE_ACCOUNT_DATA);
    }

    @Bean(QUEUE_FUND_EBANK_AMOUNT_MATCH)
    public Queue QUEUE_FUND_EBANK_AMOUNT_MATCH(){
        return new Queue(QUEUE_FUND_EBANK_AMOUNT_MATCH);
    }

    //队列绑定交换机-交易数据队列
    @Bean
    public Binding BINDING_QUEUE_TRANSACTION_DATA(@Qualifier(QUEUE_TRANSACTION_DATA) Queue queue,
                                              @Qualifier(EXCHANGE_DIRECT_TRANSACTION_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_TRANSACTION_DATA).noargs();
    }

    //队列绑定交换机-偿还计划队列
    @Bean
    public Binding BINDING_QUEUE_REPAYMENT_PLAN_DATA(@Qualifier(QUEUE_REPAYMENT_PLAN_DATA) Queue queue,
                                                  @Qualifier(EXCHANGE_DIRECT_TRANSACTION_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_REPAYMENT_PLAN_DATA).noargs();
    }

    //队列绑定交换机-恒运网银编号和批扣号映射队列
    @Bean
    public Binding BINDING_QUEUE_HY_ONLINE_BANK_BATCH_REFUND_MAPPING(@Qualifier(QUEUE_HY_ONLINE_BANK_BATCH_REFUND_MAPPING) Queue queue,
                                                     @Qualifier(EXCHANGE_DIRECT_TRANSACTION_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_HY_ONLINE_BANK_BATCH_REFUND_MAPPING).noargs();
    }

    //队列绑定交换机-付款数据队列
    @Bean
    public Binding BINDING_QUEUE_FUND_PAYMENT_DATA(@Qualifier(QUEUE_FUND_PAYMENT_DATA) Queue queue,
                                                    @Qualifier(EXCHANGE_DIRECT_TRANSACTION_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_FUND_PAYMENT_DATA).noargs();
    }

    //队列绑定交换机-网银收付款数据队列
    @Bean
    public Binding BINDING_QUEUE_FUND_EBANK_TRANSACTION_DATA(@Qualifier(QUEUE_FUND_EBANK_TRANSACTION_DATA) Queue queue,
                                                   @Qualifier(EXCHANGE_DIRECT_TRANSACTION_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_FUND_EBANK_TRANSACTION_DATA).noargs();
    }

    //队列绑定交换机-LPR数据队列
    @Bean
    public Binding BINDING_QUEUE_FUND_LPR_DATA(@Qualifier(QUEUE_FUND_LPR_DATA) Queue queue,
                                                   @Qualifier(EXCHANGE_DIRECT_TRANSACTION_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_FUND_LPR_DATA).noargs();
    }

    //队列绑定交换机-报销系统-报销单据
    @Bean
    public Binding BINDING_QUEUE_CLAIM_ORDER_TRANSACTION_DATA(@Qualifier(QUEUE_CLAIM_ORDER_TRANSACTION_DATA) Queue queue,
                                               @Qualifier(EXCHANGE_DIRECT_TRANSACTION_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_CLAIM_ORDER_TRANSACTION_DATA).noargs();
    }

    //队列绑定交换机-资金系统，业务系统网银编号映射表
    @Bean
    public Binding BINDING_QUEUE_FUND_EBANK_MATCH(@Qualifier(QUEUE_FUND_EBANK_MATCH) Queue queue,
                                                              @Qualifier(EXCHANGE_DIRECT_TRANSACTION_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_FUND_EBANK_MATCH).noargs();
    }

    //队列绑定交换机-资金系统，业务系统网银编号映射表
    @Bean
    public Binding QUEUE_FUND_INVOICE_CLAIM_DATA(@Qualifier(QUEUE_FUND_INVOICE_CLAIM_DATA) Queue queue,
                                                  @Qualifier(EXCHANGE_DIRECT_TRANSACTION_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_FUND_INVOICE_CLAIM_DATA).noargs();
    }

    //队列绑定交换机-财务中台消费错误消息
    @Bean
    public Binding QUEUE_FUNHUB_ERORR_MESSAGE(@Qualifier(QUEUE_FINHUB_ERORR_MESSAGE) Queue queue,
                                                 @Qualifier(EXCHANGE_DIRECT_TRANSACTION_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_FINHUB_ERROR_MESSAGE).noargs();
    }

    //当消费者的消息失败重试次数用尽后，将失败的消息【丢弃给指定的error交换机的error队列】
    @Bean
    public MessageRecoverer republishMessageRecoverer(RabbitTemplate rabbitTemplate){
        return new RepublishMessageRecoverer(rabbitTemplate, EXCHANGE_DIRECT_TRANSACTION_DATA, ROUTINGKEY_FINHUB_ERROR_MESSAGE);
    }

    @Bean
    public Binding QUEUE_FINHUB_CLOSE_ACCOUNT_DATA(@Qualifier(QUEUE_FINHUB_CLOSE_ACCOUNT_DATA) Queue queue,
                                              @Qualifier(EXCHANGE_DIRECT_TRANSACTION_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_FINHUB_CLOSE_ACCOUNT_DATA).noargs();
    }

    @Bean
    public Binding QUEUE_FUND_EBANK_AMOUNT_MATCH(@Qualifier(QUEUE_FUND_EBANK_AMOUNT_MATCH) Queue queue,
                                                   @Qualifier(EXCHANGE_DIRECT_TRANSACTION_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_FUND_EBANK_AMOUNT_MATCH).noargs();
    }

}
