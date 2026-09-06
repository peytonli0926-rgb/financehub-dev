--MQ异常消息记录表
CREATE TABLE financialdb.eg_mq_error_message (
	id int8 NOT NULL, -- ID
	message_body text NULL, -- 消息内容
	status varchar(10) NULL, -- 消息处理状态 0:未处理 1:已重推  2:已忽略
	original_routing_key varchar(128) NULL, -- 原始路由key
	original_exchange varchar(255) NULL, -- 原始交换机
	exception_message varchar(1200) NULL, -- 异常消息
	exception_stacktrace text NULL, -- 异常堆栈
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	del_flag bpchar(1) NULL DEFAULT '0'::bpchar, -- 删除标识(0:未删除,1:已删除)
	CONSTRAINT eg_mq_error_message_pkey PRIMARY KEY (id)
);
COMMENT ON TABLE financialdb.eg_mq_error_message IS 'MQ异常消息记录表';

-- Column comments

COMMENT ON COLUMN financialdb.eg_mq_error_message.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_mq_error_message.message_body IS '消息内容';
COMMENT ON COLUMN financialdb.eg_mq_error_message.status IS '消息处理状态 0:未处理 1:已重推  2:已忽略';
COMMENT ON COLUMN financialdb.eg_mq_error_message.original_routing_key IS '原始路由key';
COMMENT ON COLUMN financialdb.eg_mq_error_message.original_exchange IS '原始交换机';
COMMENT ON COLUMN financialdb.eg_mq_error_message.exception_message IS '异常消息';
COMMENT ON COLUMN financialdb.eg_mq_error_message.exception_stacktrace IS '异常堆栈';
COMMENT ON COLUMN financialdb.eg_mq_error_message.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_mq_error_message.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_mq_error_message.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_mq_error_message.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_mq_error_message.del_flag IS '删除标识(0:未删除,1:已删除)';
--成本表新增财务日期字段
ALTER TABLE financialdb.eg_cost_channel_fee ADD financial_date timestamp NULL;
COMMENT ON COLUMN financialdb.eg_cost_channel_fee.financial_date IS '财务日期';