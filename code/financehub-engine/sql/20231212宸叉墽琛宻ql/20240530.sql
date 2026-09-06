-- financialdb.eg_service_fee_plan definition

-- Drop table

-- DROP TABLE financialdb.eg_service_fee_plan;

CREATE TABLE financialdb.eg_service_fee_plan (
	id int8 NOT NULL, -- ID
	contract_code varchar(100) NULL, -- 合同编号
	org_id varchar(50) NULL, -- 签约主体
	service_fee_no varchar(100) NULL, -- 服务费协议编号
	service_org_id varchar(50) NULL, -- 服务费签约主体
	service_fee_amortization_rate numeric(14, 6) DEFAULT 0 NULL, -- 服务费摊销利率
	actual_accrued_amount numeric(20, 2) DEFAULT 0 NULL, -- 实际计提金额
	adjust_amount numeric(20, 2) DEFAULT 0 NULL, -- 调整金额
	del_flag bpchar(1) DEFAULT 0 NULL, -- 是否删除 0：未删除1：已删除
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp(6) NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp(6) NULL, -- 更新时间
	plan_date date NULL, -- 计划日期
	service_fee_total numeric(20, 2) DEFAULT 0 NULL, -- 服务费总额
	service_fee_agreed_amount numeric(20, 2) DEFAULT 0 NULL, -- 服务费协议金额
	actual_receive_service_fee numeric(20, 2) DEFAULT 0 NULL, -- 实收服务费金额
	contract_confirmed_amount numeric(20, 2) DEFAULT 0 NULL, -- 合同一次性确认金额
	agreed_confirmed_amount numeric(20, 2) DEFAULT 0 NULL, -- 协议一次性确认金额
	agreed_apportion_amount numeric(20, 2) DEFAULT 0 NULL, -- 协议分摊金额
	contract_device_amount numeric(20, 2) DEFAULT 0 NULL, -- 合同设备金额
	plan_apportion_amount numeric(20, 2) DEFAULT 0 NULL, -- 计划分摊金额
	periods int4 NULL -- 期数
);
COMMENT ON TABLE financialdb.eg_service_fee_plan IS '服务费计划表';

-- Column comments

COMMENT ON COLUMN financialdb.eg_service_fee_plan.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_service_fee_plan.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb.eg_service_fee_plan.org_id IS '签约主体';
COMMENT ON COLUMN financialdb.eg_service_fee_plan.service_fee_no IS '服务费协议编号';
COMMENT ON COLUMN financialdb.eg_service_fee_plan.service_org_id IS '服务费签约主体';
COMMENT ON COLUMN financialdb.eg_service_fee_plan.service_fee_amortization_rate IS '服务费摊销利率';
COMMENT ON COLUMN financialdb.eg_service_fee_plan.actual_accrued_amount IS '实际计提金额';
COMMENT ON COLUMN financialdb.eg_service_fee_plan.adjust_amount IS '调整金额';
COMMENT ON COLUMN financialdb.eg_service_fee_plan.del_flag IS '是否删除 0：未删除1：已删除';
COMMENT ON COLUMN financialdb.eg_service_fee_plan.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_service_fee_plan.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_service_fee_plan.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_service_fee_plan.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_service_fee_plan.plan_date IS '计划日期';
COMMENT ON COLUMN financialdb.eg_service_fee_plan.service_fee_total IS '服务费总额';
COMMENT ON COLUMN financialdb.eg_service_fee_plan.service_fee_agreed_amount IS '服务费协议金额';
COMMENT ON COLUMN financialdb.eg_service_fee_plan.actual_receive_service_fee IS '实收服务费金额';
COMMENT ON COLUMN financialdb.eg_service_fee_plan.contract_confirmed_amount IS '合同一次性确认金额';
COMMENT ON COLUMN financialdb.eg_service_fee_plan.agreed_confirmed_amount IS '协议一次性确认金额';
COMMENT ON COLUMN financialdb.eg_service_fee_plan.agreed_apportion_amount IS '协议分摊金额';
COMMENT ON COLUMN financialdb.eg_service_fee_plan.contract_device_amount IS '合同设备金额';
COMMENT ON COLUMN financialdb.eg_service_fee_plan.plan_apportion_amount IS '计划分摊金额';
COMMENT ON COLUMN financialdb.eg_service_fee_plan.periods IS '期数';