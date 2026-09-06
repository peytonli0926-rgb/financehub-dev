--凭证行
ALTER TABLE financialdb.eg_voucher_entry ADD assist_flags varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_entry.assist_flags IS '凭证行维度';
ALTER TABLE financialdb.eg_voucher_entry DROP COLUMN voucher_amount;



--凭证行配置
ALTER TABLE financialdb.eg_scene_voucher_entry DROP COLUMN client_flag;
ALTER TABLE financialdb.eg_scene_voucher_entry DROP COLUMN contract_flag;

-- 偿还计算临时表
create sequence eg_repayment_plan_temp_row_no_seq;

create table if not exists eg_repayment_plan_temp
(
	id int8 not null
		constraint eg_repayment_plan_temp_pkey
			primary key,
	client_code varchar(100),
	client_name varchar(200),
	contract_code varchar(100),
	contract_name varchar(200),
	plan_date date,
	rent_amount numeric(20,2),
	principal_amount numeric(20,2),
	interest_amount numeric(20,2),
	principal_tax numeric(20,2),
	interest_tax numeric(20,2),
	outflow_amount numeric(20,2),
	planned_interest numeric(20,2),
	planned_principal numeric(20,2),
	cash_flow numeric(20,2),
	opening_amortized_cost numeric(20,2),
	ending_amortized_cost numeric(20,2),
	actual_daily_rate numeric(14,6),
	rental_income numeric(20,2),
	service_fee_amortization_rate numeric(14,6),
	service_fee_amortization_income numeric(20,2),
	xirr_rate numeric(14,6),
	actual_repayment_date date,
	actual_repayment_principal_balance numeric(20,2),
	actual_repayment_principal_amount numeric(20,2),
	actual_repayment_interes_balance numeric(20,2),
	actual_repayment_interes_amount numeric(20,2),
	recapture_status varchar(100),
	overdue_earnings numeric(20,2),
	create_by varchar(32),
	create_time timestamp(6),
	update_by varchar(32),
	update_time timestamp(6),
	del_flag bpchar(1) default '0',
	comment varchar(500),
	overdue_days int4,
	periods int4,
	pay_method varchar(100),
	message_id varchar(100),
	system_code varchar(100),
	rental_income_on_balance numeric(20,2) default 0,
	rental_income_off_balance numeric(20,2) default 0,
	allocation_method varchar(100),
	allocation_ratio numeric(24,6),
	service_fee_received numeric(20,2),
	service_fee_allocation_no_tax numeric(20,2),
	last_month_service_fee_allocation_no_tax numeric(20,2),
	reclassification_adjustment_no_tax_amount numeric(20,2),
	x_year_month_adjustment_amount numeric(20,2),
	not_accrued_amount numeric(20,2),
	accumulated_accrued_amount numeric(20,2),
	accumulated_actual_repayment_interes_amount numeric(20,2),
	paid_in_handling_fees_add_other_income_sub_costs numeric(20,2),
	ta_reclassification numeric(20,2),
	unrealized_revenue numeric(20,2),
	previous_paid_period timestamp(6),
	rental_income_before_total numeric(20,2),
	rental_income_after_total numeric(20,2),
	overdue_adjustment_amount numeric(20,2),
	total_recorded_amount numeric(20,2),
	confirmed_actual_receipt numeric(20,2),
	accrued bpchar(1) default '0',
	income_provision_method varchar(10),
	exception_type varchar(200),
	rental_income_on_balance_confirmed numeric(20,2),
	rental_income_off_balance_confirmed numeric(20,2),
	actual_repayment_rent_amount numeric(20,2),
	labor_overdue_days int4,
	before_x_year_month_amount numeric(20,2),
	allocation_after_x_year_month_balance numeric(20,2),
	org_id varchar(100),
	labor_overdue_mark varchar(100),
	process_method varchar(100),
	observed bpchar(1),
	observed_expiration_date timestamp(6),
	last_repayment_date timestamp(6),
	next_payment_date timestamp(6),
	confirmed_income numeric(20,2),
	paid_handling_fees numeric(20,2),
	other_income numeric(20,2),
	paid_other_costs numeric(20,2),
	invoicing_flag varchar(10),
	manual_change_mark varchar(10),
	plan_date_period int4,
	processing_status bpchar(1) default '0',
	row_no int8 default nextval('eg_repayment_plan_temp_row_no_seq'::regclass) not null
);



comment on table eg_repayment_plan_temp is '偿还计划测算临时表';

comment on column eg_repayment_plan_temp.id is 'ID';

comment on column eg_repayment_plan_temp.client_code is '客户编码';

comment on column eg_repayment_plan_temp.client_name is '客户名称';

comment on column eg_repayment_plan_temp.contract_code is '合同编号';

comment on column eg_repayment_plan_temp.contract_name is '合同名称';

comment on column eg_repayment_plan_temp.plan_date is '日期';

comment on column eg_repayment_plan_temp.rent_amount is '租金';

comment on column eg_repayment_plan_temp.principal_amount is '本金';

comment on column eg_repayment_plan_temp.interest_amount is '利息';

comment on column eg_repayment_plan_temp.principal_tax is '本金-税金';

comment on column eg_repayment_plan_temp.interest_tax is '利息-税金';

comment on column eg_repayment_plan_temp.outflow_amount is '资金流出';

comment on column eg_repayment_plan_temp.planned_interest is '计划利息(不含税)';

comment on column eg_repayment_plan_temp.planned_principal is '计划本金(不含税)';

comment on column eg_repayment_plan_temp.cash_flow is '现金流';

comment on column eg_repayment_plan_temp.opening_amortized_cost is '期初摊余成本';

comment on column eg_repayment_plan_temp.ending_amortized_cost is '期末摊余成本';

comment on column eg_repayment_plan_temp.actual_daily_rate is '实际日利率';

comment on column eg_repayment_plan_temp.rental_income is '租赁收入';

comment on column eg_repayment_plan_temp.service_fee_amortization_rate is '服务费摊销利率';

comment on column eg_repayment_plan_temp.service_fee_amortization_income is '服务费摊销收入';

comment on column eg_repayment_plan_temp.xirr_rate is 'XIRR';

comment on column eg_repayment_plan_temp.actual_repayment_date is '实际归还日期';

comment on column eg_repayment_plan_temp.actual_repayment_principal_balance is '实际归还本金余额';

comment on column eg_repayment_plan_temp.actual_repayment_principal_amount is '实际归还本金发生额';

comment on column eg_repayment_plan_temp.actual_repayment_interes_balance is '实际归还利息余额';

comment on column eg_repayment_plan_temp.actual_repayment_interes_amount is '实际归还利息发生额';

comment on column eg_repayment_plan_temp.recapture_status is '回笼状态';

comment on column eg_repayment_plan_temp.overdue_earnings is '逾期收益';

comment on column eg_repayment_plan_temp.create_by is '创建人';

comment on column eg_repayment_plan_temp.create_time is '创建时间';

comment on column eg_repayment_plan_temp.update_by is '更新人';

comment on column eg_repayment_plan_temp.update_time is '更新时间';

comment on column eg_repayment_plan_temp.del_flag is '删除标识(0:未删除,1:已删除)';

comment on column eg_repayment_plan_temp.comment is '备注';

comment on column eg_repayment_plan_temp.overdue_days is '逾期天数';

comment on column eg_repayment_plan_temp.periods is '期数';

comment on column eg_repayment_plan_temp.pay_method is '还款标识 期初(下还),期末(上还)';

comment on column eg_repayment_plan_temp.rental_income_on_balance is '表内租赁收入';

comment on column eg_repayment_plan_temp.rental_income_off_balance is '表外租赁收入';

comment on column eg_repayment_plan_temp.allocation_method is '分摊方式(租赁收入分摊、服务费收入分摊)';

comment on column eg_repayment_plan_temp.allocation_ratio is ' 分摊比例';

comment on column eg_repayment_plan_temp.service_fee_received is '服务费实收（税后）';

comment on column eg_repayment_plan_temp.service_fee_allocation_no_tax is '应分摊的服务费收入（税后）';

comment on column eg_repayment_plan_temp.last_month_service_fee_allocation_no_tax is '上月服务费应分摊金额（税后）';

comment on column eg_repayment_plan_temp.reclassification_adjustment_no_tax_amount is '本月重分类调整(税后)';

comment on column eg_repayment_plan_temp.x_year_month_adjustment_amount is 'X年X月调整';

comment on column eg_repayment_plan_temp.not_accrued_amount is '实际未计提金额';

comment on column eg_repayment_plan_temp.accumulated_accrued_amount is '累计已计提金额';

comment on column eg_repayment_plan_temp.accumulated_actual_repayment_interes_amount is ' 累计实收利息（不含税）';

comment on column eg_repayment_plan_temp.paid_in_handling_fees_add_other_income_sub_costs is ' 实收手续费+实收其他收入-实付成本';

comment on column eg_repayment_plan_temp.ta_reclassification is 'TA重分类';

comment on column eg_repayment_plan_temp.unrealized_revenue is '未实现收益总额';

comment on column eg_repayment_plan_temp.previous_paid_period is '上期实收期间';

comment on column eg_repayment_plan_temp.rental_income_before_total is '本月以前';

comment on column eg_repayment_plan_temp.rental_income_after_total is '本月之后';

comment on column eg_repayment_plan_temp.overdue_adjustment_amount is '当月逾期调整额';

comment on column eg_repayment_plan_temp.total_recorded_amount is '合计入账金额';

comment on column eg_repayment_plan_temp.confirmed_actual_receipt is '实收-已确认';

comment on column eg_repayment_plan_temp.accrued is '是否计提';

comment on column eg_repayment_plan_temp.income_provision_method is '计提方式';

comment on column eg_repayment_plan_temp.exception_type is '异常类型';

comment on column eg_repayment_plan_temp.rental_income_on_balance_confirmed is ' 已确认逾期收益(表外)';

comment on column eg_repayment_plan_temp.rental_income_off_balance_confirmed is ' 已确认租赁收益(表内)';

comment on column eg_repayment_plan_temp.actual_repayment_rent_amount is '实际归还本金';

comment on column eg_repayment_plan_temp.labor_overdue_days is ' 人工逾期天数';

comment on column eg_repayment_plan_temp.before_x_year_month_amount is 'X年X月以前';

comment on column eg_repayment_plan_temp.allocation_after_x_year_month_balance is 'X年X月摊销后余额';

comment on column eg_repayment_plan_temp.org_id is '签约主体';

comment on column eg_repayment_plan_temp.labor_overdue_mark is '手工逾期标识';

comment on column eg_repayment_plan_temp.process_method is '处理方式';

comment on column eg_repayment_plan_temp.observed is '是否观察期';

comment on column eg_repayment_plan_temp.observed_expiration_date is '观察期到期日';

comment on column eg_repayment_plan_temp.last_repayment_date is '上次还款日';

comment on column eg_repayment_plan_temp.next_payment_date is '下次回款日';

comment on column eg_repayment_plan_temp.confirmed_income is '已确认收益';

comment on column eg_repayment_plan_temp.paid_handling_fees is '实收手续费';

comment on column eg_repayment_plan_temp.other_income is '其他收入';

comment on column eg_repayment_plan_temp.paid_other_costs is '实付其他成本';

comment on column eg_repayment_plan_temp.invoicing_flag is '开票标识';

comment on column eg_repayment_plan_temp.manual_change_mark is '交易结构手工调整标志';

comment on column eg_repayment_plan_temp.plan_date_period is '计划还款日(yyyyMM)';

comment on column eg_repayment_plan_temp.processing_status is '处理状态';

comment on column eg_repayment_plan_temp.row_no is '序号';

create index if not exists idx_repayment_plan_temp_contract_code
	on eg_repayment_plan_temp (contract_code);

create index if not exists idx_repayment_plan_temp_plan_date_period
	on eg_repayment_plan_temp (plan_date_period);

--偿还计划新增字段
alter table eg_repayment_plan
    add amortized bpchar(1);
comment on column eg_repayment_plan.amortized is '是否已摊销(是否计提为是,且计提则为已摊销,0:未摊销,1:已摊销)';
alter table eg_repayment_plan
    add on_and_off_balance_sheet bpchar(1);
comment on column eg_repayment_plan.on_and_off_balance_sheet is '表内表外(0:表内,1:表外)';

alter table eg_repayment_plan_temp
    add amortized bpchar(1);
comment on column eg_repayment_plan_temp.amortized is '是否已摊销(是否计提为是,且计提则为已摊销,0:未摊销,1:已摊销)';
alter table eg_repayment_plan_temp
    add on_and_off_balance_sheet bpchar(1);
comment on column eg_repayment_plan_temp.on_and_off_balance_sheet is '表内表外(0:表内,1:表外)';




--资金系统和业务系统网银编号映射表
-- financialdb.eg_fund_business_system_ebank_mapping definition

-- Drop table

-- DROP TABLE financialdb.eg_fund_business_system_ebank_mapping;

CREATE TABLE financialdb.eg_fund_business_system_ebank_mapping (
                                                                   id int8 NOT NULL, -- ID
                                                                   match_number varchar(100) NULL, -- 勾稽编号
                                                                   ebank_number varchar(100) NULL, -- 资金系统网银编号
                                                                   ebank_serial_number varchar(100) NULL, -- 业务系统网银编号/批次号(业务系统网银编号或批扣批次（扣款渠道批次号，对应恒运VC_PINGZZY 凭证摘要显示）)
                                                                   match_amount numeric(20, 2) NULL, -- 勾稽金额
                                                                   create_by varchar(64) NULL, -- 创建人
                                                                   create_time timestamp NULL, -- 创建时间
                                                                   update_by varchar(64) NULL, -- 更新人
                                                                   update_time timestamp NULL, -- 更新时间
                                                                   del_flag bpchar(1) NULL DEFAULT 0, -- 是否删除（0:否，1：是）
                                                                   CONSTRAINT eg_fund_business_system_ebank_mapping_pk PRIMARY KEY (id)
);
CREATE INDEX eg_fund_business_system_ebank_mapping_match_number_idx ON financialdb.eg_fund_business_system_ebank_mapping USING btree (match_number, ebank_number, id, ebank_serial_number);
COMMENT ON TABLE financialdb.eg_fund_business_system_ebank_mapping IS '资金系统、业务系统网银编号映射表';

-- Column comments

COMMENT ON COLUMN financialdb.eg_fund_business_system_ebank_mapping.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_fund_business_system_ebank_mapping.match_number IS '勾稽编号';
COMMENT ON COLUMN financialdb.eg_fund_business_system_ebank_mapping.ebank_number IS '资金系统网银编号';
COMMENT ON COLUMN financialdb.eg_fund_business_system_ebank_mapping.ebank_serial_number IS '业务系统网银编号/批次号(业务系统网银编号或批扣批次（扣款渠道批次号，对应恒运VC_PINGZZY 凭证摘要显示）)';
COMMENT ON COLUMN financialdb.eg_fund_business_system_ebank_mapping.match_amount IS '勾稽金额';
COMMENT ON COLUMN financialdb.eg_fund_business_system_ebank_mapping.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_fund_business_system_ebank_mapping.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_fund_business_system_ebank_mapping.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_fund_business_system_ebank_mapping.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_fund_business_system_ebank_mapping.del_flag IS '是否删除（0:否，1：是）';

-- Permissions

ALTER TABLE financialdb.eg_fund_business_system_ebank_mapping OWNER TO financialdb;
GRANT ALL ON TABLE financialdb.eg_fund_business_system_ebank_mapping TO financialdb;