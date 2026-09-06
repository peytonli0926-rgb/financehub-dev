package com.utfinancing.financehub.engine.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.config.ApproveRabbitmqConfig</li>
 * <li>CreateTime : 2024/01/08 18:02</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Configuration
public class ApproveRabbitmqConfig {

    /**
     * 交换机
     */
    public static final String FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA = "finhub_exchange_direct_approval_data";

    /**
     * 公共的路由前缀
     */
    public static final String COMMON_FINHUB_ROUTION_KEY = "finhub_routing_key_%s";

    /**
     * 核销队列 队列命名规则按照”finhub_queue_"+单据类型
     * @return
     */
    public static final String FINHUB_QUEUE_HZHX = "finhub_queue_HZHX";

    /**
     * 核销routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_HZHX = "finhub_routing_key_HZHX";

    /**
     * 收益计提队列 队列命名规则按照”finhub_queue_"+单据类型
     * @return
     */
    public static final String FINHUB_QUEUE_SYJT = "finhub_queue_SYJT";

    /**
     * 收益计提routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_SYJT = "finhub_routing_key_SYJT";


    /**
     * 未确认收款认领队列 队列命名规则按照”finhub_queue_"+单据类型
     * @return
     */
    public static final String FINHUB_QUEUE_SGPZRL = "finhub_queue_SGPZRL";

    /**
     * 未确认收款认领routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_SGPZRL = "finhub_routing_key_SGPZRL";


    /**
     * 未确认收款认领队列 队列命名规则按照”finhub_queue_"+单据类型
     * @return
     */
    public static final String FINHUB_QUEUE_SGPZCX = "finhub_queue_SGPZCX";

    /**
     * 未确认收款冲销routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_SGPZCX = "finhub_routing_key_SGPZCX";


    /**
     * 未确认收款修改网银编号队列 队列命名规则按照”finhub_queue_"+单据类型
     * @return
     */
    public static final String FINHUB_QUEUE_SGPZTZ = "finhub_queue_SGPZTZ";

    /**
     * 未确认收款-修改网银编号routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_SGPZTZ = "finhub_routing_key_SGPZTZ";

    /**
     * 未确认收款修改入账日期队列 队列命名规则按照”finhub_queue_"+单据类型
     * @return
     */
    public static final String FINHUB_QUEUE_SGPZMGRZRQ = "finhub_queue_SGPZMGRZRQ";


    /**
     * 未确认收款-修改入账日期routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_SGPZMGRZRQ = "finhub_routing_key_SGPZMGRZRQ";

    /**
     * 未确认收款手动调整余额队列 队列命名规则按照”finhub_queue_"+单据类型
     * @return
     */
    public static final String FINHUB_QUEUE_SGTZYE = "finhub_queue_SGTZYE";


    /**
     * 未确认收款-手工调整余额routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_SGTZYE = "finhub_routing_key_SGTZYE";

    /**
     * 手工凭证队列 队列命名规则按照”finhub_queue_"+单据类型
     * @return
     */
    public static final String FINHUB_QUEUE_SGPZ = "finhub_queue_SGPZ";

    /**
     * 手工凭证 routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_SGPZ = "finhub_routing_key_SGPZ";

    /**
     * 诉讼费队列 队列命名规则按照”finhub_queue_"+单据类型
     * @return
     */
    public static final String FINHUB_QUEUE_SSF = "finhub_queue_SSF";

    /**
     * 诉讼费 routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_SSF = "finhub_routing_key_SSF";

    /**
     * 成本类-GPS队列 队列命名规则按照”finhub_queue_"+单据类型
     * @return
     */
    public static final String FINHUB_QUEUE_GPS = "finhub_queue_GPS";

    /**
     * 成本类-GPS routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_GPS = "finhub_routing_key_GPS";

    /**
     * 成本类-SHSBK队列 队列命名规则按照”finhub_queue_"+单据类型
     * @return
     */
    public static final String FINHUB_QUEUE_SHSBK = "finhub_queue_SHSBK";

    /**
     * 成本类-SHSBK routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_SHSBK = "finhub_routing_key_SHSBK";

    /**
     * 成本类-SCF队列 队列命名规则按照”finhub_queue_"+单据类型
     * @return
     */
    public static final String FINHUB_QUEUE_SCF = "finhub_queue_SCF";

    /**
     * 成本类-SCF routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_SCF = "finhub_routing_key_SCF";

    /**
     * 成本类-QDF队列 队列命名规则按照”finhub_queue_"+单据类型
     * @return
     */
    public static final String FINHUB_QUEUE_QDF = "finhub_queue_QDF";

    /**
     * 成本类-QDF routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_QDF = "finhub_routing_key_QDF";


    /**
     * ChargeOff-队列 队列命名规则按照”finhub_queue_"+单据类型
     * @return
     */
    public static final String FINHUB_QUEUE_CHARGEOFF = "finhub_queue_CHARGEOFF";

    /**
     * ChargeOff-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_CHARGEOFF = "finhub_routing_key_CHARGEOFF";

    /**
     * 尾差调整-队列 队列命名规则按照”finhub_queue_"+单据类型
     * @return
     */
    public static final String FINHUB_QUEUE_WCTZ = "finhub_queue_WCTZ";

    /**
     * 尾差调整-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_WCTZ = "finhub_routing_key_WCTZ";

    /**
     * 出表ABS-队列 队列命名规则按照”finhub_queue_"+单据类型
     * @return
     */
    public static final String FINHUB_QUEUE_CBABS = "finhub_queue_CBABS";

    /**
     * 出表ABS-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_CBABS = "finhub_routing_key_CBABS";


    /**
     * 资产转让赎回-队列 队列命名规则按照”finhub_queue_"+单据类型
     * @return
     */
    public static final String FINHUB_QUEUE_ABSSH = "finhub_queue_ABSSH";

    /**
     * 资产转让赎回-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_ABSSH = "finhub_routing_key_ABSSH";

    /**
     * 资产转付-队列 队列命名规则按照”finhub_queue_"+单据类型
     * @return
     */
    public static final String FINHUB_QUEUE_ABSZF = "finhub_queue_ABSZF";

    /**
     * 资产转付-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_ABSZF = "finhub_routing_key_ABSZF";

    /**
     * 平价转让-队列 队列命名规则按照”finhub_queue_"+单据类型
     * @return
     */
    public static final String FINHUB_QUEUE_PJZR = "finhub_queue_PJZR";

    /**
     * 平价转让-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_PJZR = "finhub_routing_key_PJZR";


    /**
     * 保证金-队列 队列命名规则按照”finhub_queue_"+单据类型
     * @return
     */
    public static final String FINHUB_QUEUE_BZJ = "finhub_queue_BZJ";

    /**
     * 保证金-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_BZJ = "finhub_routing_key_BZJ";

    /**
     * 特殊合同状态-队列 队列命名规则按照”finhub_queue_"+单据类型
     * @return
     */
    public static final String FINHUB_QUEUE_TSHT = "finhub_queue_TSHT";

    /**
     * 特殊合同状态-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_TSHT = "finhub_routing_key_TSHT";

    //声明交换机
    @Bean(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA)
    public Exchange FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA(){
        //durable(true) 持久化，mq重启之后交换机还在
        return ExchangeBuilder.directExchange(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA).durable(true).build();
    }

    //核销队列
    @Bean(FINHUB_QUEUE_CHARGEOFF)
    public Queue FINHUB_QUEUE_CHARGEOFF(){
        return new Queue(FINHUB_QUEUE_CHARGEOFF);
    }

    //核销队列
    @Bean(FINHUB_QUEUE_HZHX)
    public Queue FINHUB_QUEUE_HXHZ(){
        return new Queue(FINHUB_QUEUE_HZHX);
    }

    //收益计提队列
    @Bean(FINHUB_QUEUE_SYJT)
    public Queue FINHUB_QUEUE_SYJT(){
        return new Queue(FINHUB_QUEUE_SYJT);
    }

    //未确认收款-认领队列
    @Bean(FINHUB_QUEUE_SGPZRL)
    public Queue FINHUB_QUEUE_SGPZRL(){
        return new Queue(FINHUB_QUEUE_SGPZRL);
    }

    //未确认收款-认领冲销
    @Bean(FINHUB_QUEUE_SGPZCX)
    public Queue FINHUB_QUEUE_SGPZCX(){
        return new Queue(FINHUB_QUEUE_SGPZCX);
    }

    //未确认收款-修改入账日期
    @Bean(FINHUB_QUEUE_SGPZTZ)
    public Queue FINHUB_QUEUE_SGPZTZ(){
        return new Queue(FINHUB_QUEUE_SGPZTZ);
    }

    //未确认收款-修改网银编号
    @Bean(FINHUB_QUEUE_SGPZMGRZRQ)
    public Queue FINHUB_QUEUE_SGPZMGRZRQ(){
        return new Queue(FINHUB_QUEUE_SGPZMGRZRQ);
    }

    //未确认收款-手动调整余额
    @Bean(FINHUB_QUEUE_SGTZYE)
    public Queue FINHUB_QUEUE_SGTZYE(){
        return new Queue(FINHUB_QUEUE_SGTZYE);
    }

    //手工凭证队列
    @Bean(FINHUB_QUEUE_SGPZ)
    public Queue FINHUB_QUEUE_SGPZ(){
        return new Queue(FINHUB_QUEUE_SGPZ);
    }

    //手工凭证队列
    @Bean(FINHUB_QUEUE_SSF)
    public Queue FINHUB_QUEUE_SSF(){
        return new Queue(FINHUB_QUEUE_SSF);
    }

    //成本类-GPS
    @Bean(FINHUB_QUEUE_GPS)
    public Queue FINHUB_QUEUE_GPS(){
        return new Queue(FINHUB_QUEUE_GPS);
    }

    //成本类-GPS
    @Bean(FINHUB_QUEUE_SCF)
    public Queue FINHUB_QUEUE_SCF(){
        return new Queue(FINHUB_QUEUE_SCF);
    }

    //成本类-SHSBK
    @Bean(FINHUB_QUEUE_SHSBK)
    public Queue FINHUB_QUEUE_SHSBK(){
        return new Queue(FINHUB_QUEUE_SHSBK);
    }

    //成本类-QDF
    @Bean(FINHUB_QUEUE_QDF)
    public Queue FINHUB_QUEUE_QDF(){
        return new Queue(FINHUB_QUEUE_QDF);
    }

    //成本类-QDF
    @Bean(FINHUB_QUEUE_WCTZ)
    public Queue FINHUB_QUEUE_WCTZ(){
        return new Queue(FINHUB_QUEUE_WCTZ);
    }

    //成本类-QDF
    @Bean(FINHUB_QUEUE_CBABS)
    public Queue FINHUB_QUEUE_CBABS(){
        return new Queue(FINHUB_QUEUE_CBABS);
    }

    //成本类-QDF
    @Bean(FINHUB_QUEUE_ABSSH)
    public Queue FINHUB_QUEUE_ZCSH(){
        return new Queue(FINHUB_QUEUE_ABSSH);
    }

    //资产转付
    @Bean(FINHUB_QUEUE_ABSZF)
    public Queue FINHUB_QUEUE_ZCZF(){
        return new Queue(FINHUB_QUEUE_ABSZF);
    }

    //平价转让
    @Bean(FINHUB_QUEUE_PJZR)
    public Queue FINHUB_QUEUE_NBZR(){
        return new Queue(FINHUB_QUEUE_PJZR);
    }

    //保证金
    @Bean(FINHUB_QUEUE_BZJ)
    public Queue FINHUB_QUEUE_BZJ(){
        return new Queue(FINHUB_QUEUE_BZJ);
    }

    //特殊合同状态
    @Bean(FINHUB_QUEUE_TSHT)
    public Queue FINHUB_QUEUE_TSHT(){
        return new Queue(FINHUB_QUEUE_TSHT);
    }

    @Bean
    public Binding BINDING_QUEUE_HXHZ(@Qualifier(FINHUB_QUEUE_HZHX) Queue queue,
                                      @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_HZHX).noargs();
    }

    @Bean
    public Binding BINDING_QUEUE_SYJT(@Qualifier(FINHUB_QUEUE_SYJT) Queue queue,
                                      @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_SYJT).noargs();
    }

    @Bean
    public Binding BINDING_QUEUE_SGPZRL(@Qualifier(FINHUB_QUEUE_SGPZRL) Queue queue,
                                      @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_SGPZRL).noargs();
    }

    @Bean
    public Binding BINDING_QUEUE_SGPZCX(@Qualifier(FINHUB_QUEUE_SGPZCX) Queue queue,
                                          @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_SGPZCX).noargs();
    }

    @Bean
    public Binding BINDING_QUEUE_SGPZTZ(@Qualifier(FINHUB_QUEUE_SGPZTZ) Queue queue,
                                        @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_SGPZTZ).noargs();
    }

    @Bean
    public Binding BINDING_QUEUE_SGPZMGRZRQ(@Qualifier(FINHUB_QUEUE_SGPZMGRZRQ) Queue queue,
                                        @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_SGPZMGRZRQ).noargs();
    }

    @Bean
    public Binding BINDING_QUEUE_SGTZYE(@Qualifier(FINHUB_QUEUE_SGTZYE) Queue queue,
                                            @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_SGTZYE).noargs();
    }

    //手工凭证交换机绑定
    @Bean
    public Binding BINDING_QUEUE_SGPZ(@Qualifier(FINHUB_QUEUE_SGPZ) Queue queue,
                                                    @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_SGPZ).noargs();
    }

    //诉讼费
    @Bean
    public Binding BINDING_QUEUE_SSF(@Qualifier(FINHUB_QUEUE_SSF) Queue queue,
                                      @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_SSF).noargs();
    }

    //成本类GPS
    @Bean
    public Binding BINDING_QUEUE_GPS(@Qualifier(FINHUB_QUEUE_GPS) Queue queue,
                                     @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_GPS).noargs();
    }

    //成本类SHSBK
    @Bean
    public Binding BINDING_QUEUE_SHSBK(@Qualifier(FINHUB_QUEUE_SHSBK) Queue queue,
                                     @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_SHSBK).noargs();
    }

    //成本类SCF
    @Bean
    public Binding BINDING_QUEUE_SCF(@Qualifier(FINHUB_QUEUE_SCF) Queue queue,
                                     @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_SCF).noargs();
    }

    //成本类QDF
    @Bean
    public Binding BINDING_QUEUE_QDF(@Qualifier(FINHUB_QUEUE_QDF) Queue queue,
                                     @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_QDF).noargs();
    }

    //Charge-Off
    @Bean
    public Binding BINDING_QUEUE_CHARGEOFF(@Qualifier(FINHUB_QUEUE_CHARGEOFF) Queue queue,
                                           @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_CHARGEOFF).noargs();
    }

    //尾差调整
    @Bean
    public Binding BINDING_QUEUE_WCTZ(@Qualifier(FINHUB_QUEUE_WCTZ) Queue queue,
                                           @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_WCTZ).noargs();
    }

    //出表ABS
    @Bean
    public Binding BINDING_QUEUE_CBABS(@Qualifier(FINHUB_QUEUE_CBABS) Queue queue,
                                      @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_CBABS).noargs();
    }
    //资产赎回
    @Bean
    public Binding BINDING_QUEUE_ABSSH(@Qualifier(FINHUB_QUEUE_ABSSH) Queue queue,
                                       @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_ABSSH).noargs();
    }

    //资产转付
    @Bean
    public Binding BINDING_QUEUE_ABSZF(@Qualifier(FINHUB_QUEUE_ABSZF) Queue queue,
                                      @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_ABSZF).noargs();
    }

    //平价转让
    @Bean
    public Binding BINDING_QUEUE_NBZR(@Qualifier(FINHUB_QUEUE_PJZR) Queue queue,
                                       @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_PJZR).noargs();
    }

    /**
     * 应交销项税-队列 队列命名规则按照”finhub_queue_"+单据类型
     */
    public static final String FINHUB_QUEUE_YJZZS = "finhub_queue_YJZZS";
    /**
     * 应交销项税-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_YJZZS = "finhub_routing_key_YJZZS";
    //应交增值税
    @Bean(FINHUB_QUEUE_YJZZS)
    public Queue FINHUB_QUEUE_YJZZS(){
        return new Queue(FINHUB_QUEUE_YJZZS);
    }
    //应交增值税
    @Bean
    public Binding BINDING_QUEUE_YJZZS(@Qualifier(FINHUB_QUEUE_YJZZS) Queue queue,
                                      @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_YJZZS).noargs();
    }

    /**
     * 减值计提-队列 队列命名规则按照”finhub_queue_"+单据类型
     */
    public static final String FINHUB_QUEUE_JZJT = "finhub_queue_JZJT";

    /**
     * 减值计提-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_JZJT = "finhub_routing_key_JZJT";
    //减值计提
    @Bean(FINHUB_QUEUE_JZJT)
    public Queue FINHUB_QUEUE_JZJT(){
        return new Queue(FINHUB_QUEUE_JZJT);
    }
    //减值计提
    @Bean
    public Binding BINDING_QUEUE_JZJT(@Qualifier(FINHUB_QUEUE_JZJT) Queue queue,
                                      @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_JZJT).noargs();
    }

    /**
     * 抵债资产-转入登记-队列 队列命名规则按照”finhub_queue_"+单据类型
     */
    public static final String FINHUB_QUEUE_DZZCZRDJ = "finhub_queue_DZZCZRDJ";

    /**
     * 抵债资产-转入登记-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_DZZCZRDJ = "finhub_routing_key_DZZCZRDJ";
    //抵债资产-转入登记
    @Bean(FINHUB_QUEUE_DZZCZRDJ)
    public Queue FINHUB_QUEUE_DZZCZRDJ(){
        return new Queue(FINHUB_QUEUE_DZZCZRDJ);
    }
    //抵债资产-转入登记
    @Bean
    public Binding BINDING_QUEUE_DZZCZRDJ(@Qualifier(FINHUB_QUEUE_DZZCZRDJ) Queue queue,
                                      @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_DZZCZRDJ).noargs();
    }

    /**
     * 抵债资产-出售登记-队列 队列命名规则按照”finhub_queue_"+单据类型
     */
    public static final String FINHUB_QUEUE_DZZCCSDJ = "finhub_queue_DZZCCSDJ";

    /**
     * 抵债资产-出售登记-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_DZZCCSDJ = "finhub_routing_key_DZZCCSDJ";
    //抵债资产-出售登记
    @Bean(FINHUB_QUEUE_DZZCCSDJ)
    public Queue FINHUB_QUEUE_DZZCCSDJ(){
        return new Queue(FINHUB_QUEUE_DZZCCSDJ);
    }
    //抵债资产-出售登记
    @Bean
    public Binding BINDING_QUEUE_DZZCCSDJ(@Qualifier(FINHUB_QUEUE_DZZCCSDJ) Queue queue,
                                      @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_DZZCCSDJ).noargs();
    }

    /**
     * 抵债资产-出租登记-队列 队列命名规则按照”finhub_queue_"+单据类型
     */
    public static final String FINHUB_QUEUE_DZZCCZDJ = "finhub_queue_DZZCCZDJ";

    /**
     * 抵债资产-出租登记-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_DZZCCZDJ = "finhub_routing_key_DZZCCZDJ";
    //抵债资产-出租登记
    @Bean(FINHUB_QUEUE_DZZCCZDJ)
    public Queue FINHUB_QUEUE_DZZCCZDJ(){
        return new Queue(FINHUB_QUEUE_DZZCCZDJ);
    }
    //抵债资产-出租登记
    @Bean
    public Binding BINDING_QUEUE_DZZCCZDJ(@Qualifier(FINHUB_QUEUE_DZZCCZDJ) Queue queue,
                                      @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_DZZCCZDJ).noargs();
    }

    /**
     * 抵债资产-出租登记-收入确认-队列 队列命名规则按照”finhub_queue_"+单据类型
     */
    public static final String FINHUB_QUEUE_DZZCCZDJSRQR = "finhub_queue_DZZCCZDJSRQR";

    /**
     * 抵债资产-出租登记-收入确认-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_DZZCCZDJSRQR = "finhub_routing_key_DZZCCZDJSRQR";
    //抵债资产-出租登记-收入确认
    @Bean(FINHUB_QUEUE_DZZCCZDJSRQR)
    public Queue FINHUB_QUEUE_DZZCCZDJSRQR(){
        return new Queue(FINHUB_QUEUE_DZZCCZDJSRQR);
    }
    //抵债资产-出租登记-收入确认
    @Bean
    public Binding BINDING_QUEUE_DZZCCZDJSRQR(@Qualifier(FINHUB_QUEUE_DZZCCZDJSRQR) Queue queue,
                                      @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_DZZCCZDJSRQR).noargs();
    }

    /**
     * 长期应收款-队列 队列命名规则按照”finhub_queue_"+单据类型
     */
    public static final String FINHUB_QUEUE_CQYSK = "finhub_queue_CQYSK";

    /**
     * 长期应收款-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_CQYSK = "finhub_routing_key_CQYSK";
    //长期应收款
    @Bean(FINHUB_QUEUE_CQYSK)
    public Queue FINHUB_QUEUE_CQYSK(){
        return new Queue(FINHUB_QUEUE_CQYSK);
    }
    //长期应收款
    @Bean
    public Binding BINDING_QUEUE_CQYSK(@Qualifier(FINHUB_QUEUE_CQYSK) Queue queue,
                                      @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_CQYSK).noargs();
    }

    /**
     * 长期应收款-收入确认-队列 队列命名规则按照”finhub_queue_"+单据类型
     */
    public static final String FINHUB_QUEUE_CQYSKSRQR = "finhub_queue_CQYSKSRQR";

    /**
     * 长期应收款-收入确认-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_CQYSKSRQR = "finhub_routing_key_CQYSKSRQR";
    //长期应收款-收入确认
    @Bean(FINHUB_QUEUE_CQYSKSRQR)
    public Queue FINHUB_QUEUE_CQYSKSRQR(){
        return new Queue(FINHUB_QUEUE_CQYSKSRQR);
    }
    //长期应收款-收入确认
    @Bean
    public Binding BINDING_QUEUE_CQYSKSRQR(@Qualifier(FINHUB_QUEUE_CQYSKSRQR) Queue queue,
                                      @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_CQYSKSRQR).noargs();
    }

    /**
     * 平价转让-内部调拨-队列 队列命名规则按照”finhub_queue_"+单据类型
     */
    public static final String FINHUB_QUEUE_PJZRDB = "finhub_queue_PJZRDB";

    /**
     * 平价转让-内部调拨-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_PJZRDB = "finhub_routing_key_PJZRDB";
    //平价转让-内部调拨
    @Bean(FINHUB_QUEUE_PJZRDB)
    public Queue FINHUB_QUEUE_NBDB(){
        return new Queue(FINHUB_QUEUE_PJZRDB);
    }
    //平价转让-内部调拨
    @Bean
    public Binding BINDING_QUEUE_NBDB(@Qualifier(FINHUB_QUEUE_PJZRDB) Queue queue,
                                      @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_PJZRDB).noargs();
    }

    /**
     * 线下合同-队列 队列命名规则按照”finhub_queue_"+单据类型
     */
    public static final String FINHUB_QUEUE_XXHT = "finhub_queue_XXHT";

    /**
     * 线下合同-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_XXHT = "finhub_routing_key_XXHT";
    //线下合同
    @Bean(FINHUB_QUEUE_XXHT)
    public Queue FINHUB_QUEUE_XXHT(){
        return new Queue(FINHUB_QUEUE_XXHT);
    }
    //线下合同
    @Bean
    public Binding BINDING_QUEUE_XXHT(@Qualifier(FINHUB_QUEUE_XXHT) Queue queue,
                                      @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_XXHT).noargs();
    }

    /**
     * 合同信息修改-队列 队列命名规则按照”finhub_queue_"+单据类型
     */
    public static final String FINHUB_QUEUE_HTXXXG = "finhub_queue_HTXXXG";

    /**
     * 合同信息修改-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_HTXXXG = "finhub_routing_key_HTXXXG";
    //合同信息修改
    @Bean(FINHUB_QUEUE_HTXXXG)
    public Queue FINHUB_QUEUE_HTXXXG(){
        return new Queue(FINHUB_QUEUE_HTXXXG);
    }
    //合同信息修改
    @Bean
    public Binding BINDING_QUEUE_HTXXXG(@Qualifier(FINHUB_QUEUE_HTXXXG) Queue queue,
                                      @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_HTXXXG).noargs();
    }


    /**
     * 设备回收-财务入库-队列 队列命名规则按照”finhub_queue_"+单据类型
     */
    public static final String FINHUB_QUEUE_CWRK = "finhub_queue_HSSBCWRK";

    /**
     * 设备回收-财务出库-队列 队列命名规则按照”finhub_queue_"+单据类型
     */
    public static final String FINHUB_QUEUE_CWCK = "finhub_queue_HSSBCWCK";

    /**
     * 设备回收-财务入库-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_CWRK = "finhub_routing_key_HSSBCWRK";

    /**
     * 设备回收-财务入出库-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_CWCK = "finhub_routing_key_HSSBCWCK";

    //设备回收-财务入库
    @Bean(FINHUB_QUEUE_CWRK)
    public Queue FINHUB_QUEUE_CWRK(){
        return new Queue(FINHUB_QUEUE_CWRK);
    }
    //设备回收-财务入库
    @Bean
    public Binding BINDING_QUEUE_CWRK(@Qualifier(FINHUB_QUEUE_CWRK) Queue queue,
                                      @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_CWRK).noargs();
    }

    //设备回收-财务出库
    @Bean(FINHUB_QUEUE_CWCK)
    public Queue FINHUB_QUEUE_CWCK(){
        return new Queue(FINHUB_QUEUE_CWCK);
    }
    //设备回收-财务出库
    @Bean
    public Binding BINDING_QUEUE_CWCK(@Qualifier(FINHUB_QUEUE_CWCK) Queue queue,
                                      @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_CWCK).noargs();
    }

    /**
     * 应付保险费-队列 队列命名规则按照”finhub_queue_"+单据类型
     */
    public static final String FINHUB_QUEUE_YFBXF = "finhub_queue_YFBXF";

    /**
     * 应付保险费-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_YFBXF = "finhub_routing_key_YFBXF";
    //应付保险费
    @Bean(FINHUB_QUEUE_YFBXF)
    public Queue FINHUB_QUEUE_YFBXF(){
        return new Queue(FINHUB_QUEUE_YFBXF);
    }
    //应付保险费
    @Bean
    public Binding BINDING_QUEUE_YFBXF(@Qualifier(FINHUB_QUEUE_YFBXF) Queue queue,
                                      @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_YFBXF).noargs();
    }


    //保证金
    @Bean
    public Binding BINDING_QUEUE_BZJ(@Qualifier(FINHUB_QUEUE_BZJ) Queue queue,
                                      @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_BZJ).noargs();
    }

    //特殊合同状态
    @Bean
    public Binding BINDING_QUEUE_TSHT(@Qualifier(FINHUB_QUEUE_TSHT) Queue queue,
                                     @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_TSHT).noargs();
    }

    /**
     * ta其他应付款-队列 队列命名规则按照”finhub_queue_"+单据类型
     */
    public static final String FINHUB_QUEUE_TAQTYFK = "finhub_queue_TAQTYFK";

    /**
     * ta其他应付款-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_TAQTYFK = "finhub_routing_key_TAQTYFK";
    //ta其他应付款
    @Bean(FINHUB_QUEUE_TAQTYFK)
    public Queue FINHUB_QUEUE_TAQTYFK(){
        return new Queue(FINHUB_QUEUE_TAQTYFK);
    }
    //ta其他应付款
    @Bean
    public Binding BINDING_QUEUE_TAQTYFK(@Qualifier(FINHUB_QUEUE_TAQTYFK) Queue queue,
                                      @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_TAQTYFK).noargs();
    }

    /**
     * 咨询服务费分摊-队列 队列命名规则按照”finhub_queue_"+单据类型
     */
    public static final String FINHUB_QUEUE_FWFJT = "finhub_queue_FWFJT";

    /**
     * 咨询服务费分摊-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_FWFJT = "finhub_routing_key_FWFJT";
    //咨询服务费分摊
    @Bean(FINHUB_QUEUE_FWFJT)
    public Queue FINHUB_QUEUE_FWFJT(){
        return new Queue(FINHUB_QUEUE_FWFJT);
    }
    //咨询服务费分摊
    @Bean
    public Binding BINDING_QUEUE_FWFJT(@Qualifier(FINHUB_QUEUE_FWFJT) Queue queue,
                                      @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_FWFJT).noargs();
    }

    /**
     * ta重分类明细表-队列 队列命名规则按照”finhub_queue_"+单据类型
     */
    public static final String FINHUB_QUEUE_TACFL = "finhub_queue_TACFL";

    /**
     * ta重分类明细表-routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_TACFL = "finhub_routing_key_TACFL";
    //ta重分类明细表
    @Bean(FINHUB_QUEUE_TACFL)
    public Queue FINHUB_QUEUE_TACFL(){
        return new Queue(FINHUB_QUEUE_TACFL);
    }
    //ta重分类明细表
    @Bean
    public Binding FINHUB_QUEUE_TACFL(@Qualifier(FINHUB_QUEUE_TACFL) Queue queue,
                                         @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_TACFL).noargs();
    }

    /**
     * 队列命名规则按照”finhub_queue_"+单据类型
     */

    public static final String FINHUB_QUEUE_ZJZR = "finhub_queue_ZJZR";

    /**
     * outing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_ZJZR = "finhub_routing_key_ZJZR";

    /**
     * 折价转让队列
     *
     * @return
     */
    @Bean(FINHUB_QUEUE_ZJZR)
    public Queue FINHUB_QUEUE_ZJZR() {
        return new Queue(FINHUB_QUEUE_ZJZR);
    }

    /**
     * 折价转让 交换机绑定队列按指定key进行路由
     *
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding FINHUB_QUEUE_ZJZR(@Qualifier(FINHUB_QUEUE_ZJZR) Queue queue,
                                     @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_ZJZR).noargs();
    }


    /**
     * 队列命名规则按照”finhub_queue_"+单据类型
     */

    public static final String FINHUB_QUEUE_ZJZRDB = "finhub_queue_ZJZRDB";

    /**
     * outing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_ZJZRDB = "finhub_routing_key_ZJZRDB";

    /**
     * 折价转让队列
     *
     * @return
     */
    @Bean(FINHUB_QUEUE_ZJZRDB)
    public Queue FINHUB_QUEUE_ZJZRDB() {
        return new Queue(FINHUB_QUEUE_ZJZRDB);
    }

    /**
     * 折价转让 交换机绑定队列按指定key进行路由
     *
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding FINHUB_QUEUE_ZJZRDB(@Qualifier(FINHUB_QUEUE_ZJZRDB) Queue queue,
                                     @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_ZJZRDB).noargs();
    }

    /**
     * 队列命名规则按照”finhub_queue_"+单据类型
     */

    public static final String FINHUB_QUEUE_DSFZR = "finhub_queue_DSFZR";

    /**
     * outing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_DSFZR = "finhub_routing_key_DSFZR";

    /**
     * 第三方转让队列
     *
     * @return
     */
    @Bean(FINHUB_QUEUE_DSFZR)
    public Queue FINHUB_QUEUE_DSFZR() {
        return new Queue(FINHUB_QUEUE_DSFZR);
    }

    /**
     * 折价转让 交换机绑定队列按指定key进行路由
     *
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding FINHUB_QUEUE_ZRDSF(@Qualifier(FINHUB_QUEUE_DSFZR) Queue queue,
                                     @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_DSFZR).noargs();
    }
    /**
     * 第三方转付
     * 队列命名规则按照”finhub_queue_"+单据类型
     */

    public static final String FINHUB_QUEUE_DSFZF = "finhub_queue_DSFZF";

    /**
     * outing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_DSFZF = "finhub_routing_key_DSFZF";

    /**
     * 第三方转让队列
     *
     * @return
     */
    @Bean(FINHUB_QUEUE_DSFZF)
    public Queue FINHUB_QUEUE_DSFZF() {
        return new Queue(FINHUB_QUEUE_DSFZF);
    }

    /**
     * 折价转让 交换机绑定队列按指定key进行路由
     *
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding FINHUB_QUEUE_DSFZF(@Qualifier(FINHUB_QUEUE_DSFZF) Queue queue,
                                     @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_DSFZF).noargs();
    }

    /**
     * 资产转让 - 转让合同费
     * 队列命名规则按照”finhub_queue_"+单据类型
     */

    public static final String FINHUB_QUEUE_ZRFY = "finhub_queue_ZRFY";

    /**
     * outing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_ZRFY = "finhub_routing_key_ZRFY";

    /**
     * 第三方转让队列
     *
     * @return
     */
    @Bean(FINHUB_QUEUE_ZRFY)
    public Queue FINHUB_QUEUE_ZRFY() {
        return new Queue(FINHUB_QUEUE_ZRFY);
    }

    /**
     * 折价转让 交换机绑定队列按指定key进行路由
     *
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding FINHUB_QUEUE_ZRFY(@Qualifier(FINHUB_QUEUE_ZRFY) Queue queue,
                                     @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_ZRFY).noargs();
    }

    /**
     * 资产转让 - 转让合同费
     * 队列命名规则按照”finhub_queue_"+单据类型
     */

    public static final String FINHUB_QUEUE_ZRQT = "finhub_queue_ZRQT";

    /**
     * outing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_ZRQT = "finhub_routing_key_ZRQT";

    /**
     * 第三方转让队列
     *
     * @return
     */
    @Bean(FINHUB_QUEUE_ZRQT)
    public Queue FINHUB_QUEUE_ZRQT() {
        return new Queue(FINHUB_QUEUE_ZRQT);
    }

    /**
     * 折价转让 交换机绑定队列按指定key进行路由
     *
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding FINHUB_QUEUE_ZRQT(@Qualifier(FINHUB_QUEUE_ZRQT) Queue queue,
                                     @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_ZRQT).noargs();
    }

    /**
     * 资产转让 - 转让合同费
     * 队列命名规则按照”finhub_queue_"+单据类型
     */

    public static final String FINHUB_QUEUE_ZFQT = "finhub_queue_ZFQT";

    /**
     * outing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_ZFQT = "finhub_routing_key_ZFQT";

    /**
     * 第三方转让队列
     *
     * @return
     */
    @Bean(FINHUB_QUEUE_ZFQT)
    public Queue FINHUB_QUEUE_ZFQT() {
        return new Queue(FINHUB_QUEUE_ZFQT);
    }

    /**
     * 折价转让 交换机绑定队列按指定key进行路由
     *
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding FINHUB_QUEUE_ZFQT(@Qualifier(FINHUB_QUEUE_ZFQT) Queue queue,
                                     @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_ZRFY).noargs();
    }

    /**
     * 未确认收款-线下网银上传队列 队列命名规则按照”finhub_queue_"+单据类型
     * @return
     */
    public static final String FINHUB_QUEUE_XXWYSC = "finhub_queue_XXWYSC";

    /**
     * 未确认收款-线下网银上传routing-key 路由命名规则按照“finhub_routing_key_"+单据类型
     */
    public static final String FINHUB_ROUTING_KEY_XXWYSC = "finhub_routing_key_XXWYSC";
    //未确认收款-手动调整余额

    /**
     * @description: 未确认收款-线下网银上传队列-定义接收队列
     **/
    @Bean(FINHUB_QUEUE_XXWYSC)
    public Queue FINHUB_QUEUE_XXWYSC(){
        return new Queue(FINHUB_QUEUE_XXWYSC);
    }

    /**
     * @description: 未确认收款-线下网银上传队列-绑定路由和队列
     **/
    @Bean
    public Binding BINDING_QUEUE_XXWYSC(@Qualifier(FINHUB_QUEUE_XXWYSC) Queue queue,
                                        @Qualifier(FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(FINHUB_ROUTING_KEY_XXWYSC).noargs();
    }


}
