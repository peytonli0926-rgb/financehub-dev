ALTER TABLE financialdb.eg_repayment_plan ADD source_irr_rate numeric(20, 6) NULL;
COMMENT ON COLUMN financialdb.eg_repayment_plan.source_irr_rate IS '源IRR RATE';

ALTER TABLE financialdb.eg_repayment_plan_xw ADD source_irr_rate numeric(20, 6) NULL;
COMMENT ON COLUMN financialdb.eg_repayment_plan_xw.source_irr_rate IS '源IRR RATE';

ALTER TABLE financialdb.eg_repayment_plan_hy ADD source_irr_rate numeric(20, 6) NULL;
COMMENT ON COLUMN financialdb.eg_repayment_plan_hy.source_irr_rate IS '源IRR RATE';

ALTER TABLE financialdb.eg_repayment_plan_pl ADD source_irr_rate numeric(20, 6) NULL;
COMMENT ON COLUMN financialdb.eg_repayment_plan_pl.source_irr_rate IS '源IRR RATE';


-- financialdb.eg_outstanding_amount_init definition

-- Drop table

-- DROP TABLE financialdb.eg_outstanding_amount_init;

CREATE TABLE financialdb.eg_outstanding_amount_init (
	id int8 NULL, -- 主键
	contract_code varchar(100) NULL, -- 合同编号
	account_number varchar(100) NULL, -- 科目编号
	account_name varchar(100) NULL, -- 科目名称
	client_code varchar(100) NULL, -- 客户编号
	org_id varchar(100) NULL, -- 签约主体
	end_balance_for numeric(20, 2) NULL -- 金额
);
CREATE INDEX eg_outstanding_amount_init_contract_code_idx ON financialdb.eg_outstanding_amount_init USING btree (contract_code);
CREATE INDEX eg_outstanding_amount_init_id_idx ON financialdb.eg_outstanding_amount_init USING btree (id);
COMMENT ON TABLE financialdb.eg_outstanding_amount_init IS '合同未实现收益的初始化数据';

-- Column comments

COMMENT ON COLUMN financialdb.eg_outstanding_amount_init.id IS '主键';
COMMENT ON COLUMN financialdb.eg_outstanding_amount_init.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb.eg_outstanding_amount_init.account_number IS '科目编号';
COMMENT ON COLUMN financialdb.eg_outstanding_amount_init.account_name IS '科目名称';
COMMENT ON COLUMN financialdb.eg_outstanding_amount_init.client_code IS '客户编号';
COMMENT ON COLUMN financialdb.eg_outstanding_amount_init.org_id IS '签约主体';
COMMENT ON COLUMN financialdb.eg_outstanding_amount_init.end_balance_for IS '金额';


-- financialdb.eg_voucher_amount_init definition

-- Drop table

-- DROP TABLE financialdb.eg_voucher_amount_init;

CREATE TABLE financialdb.eg_voucher_amount_init (
	id int8 NULL, -- 主键
	fid varchar(100) NULL, -- 金蝶主键
	org_id varchar(100) NULL, -- 签约主体
	"period" varchar(100) NULL, -- 期间
	contract_code varchar(100) NULL, -- 合同编号
	client_code varchar(100) NULL, -- 客户编号
	account_number varchar(100) NULL, -- 科目编号
	account_name varchar(100) NULL, -- 科目名称
	fab_stract varchar(100) NULL,
	dt_amount numeric(20, 2) NULL, -- 借方金额
	cr_amount numeric(20, 2) NULL -- 贷方金额
);
CREATE INDEX eg_voucher_amount_init_id_idx ON financialdb.eg_voucher_amount_init USING btree (id);
CREATE INDEX eg_voucher_amount_init_idx ON financialdb.eg_voucher_amount_init USING btree (contract_code);
COMMENT ON TABLE financialdb.eg_voucher_amount_init IS '金蝶202311期合同的借方金额';

-- Column comments

COMMENT ON COLUMN financialdb.eg_voucher_amount_init.id IS '主键';
COMMENT ON COLUMN financialdb.eg_voucher_amount_init.fid IS '金蝶主键';
COMMENT ON COLUMN financialdb.eg_voucher_amount_init.org_id IS '签约主体';
COMMENT ON COLUMN financialdb.eg_voucher_amount_init."period" IS '期间';
COMMENT ON COLUMN financialdb.eg_voucher_amount_init.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb.eg_voucher_amount_init.client_code IS '客户编号';
COMMENT ON COLUMN financialdb.eg_voucher_amount_init.account_number IS '科目编号';
COMMENT ON COLUMN financialdb.eg_voucher_amount_init.account_name IS '科目名称';
COMMENT ON COLUMN financialdb.eg_voucher_amount_init.dt_amount IS '借方金额';
COMMENT ON COLUMN financialdb.eg_voucher_amount_init.cr_amount IS '贷方金额';


-- financialdb.eg_repayment_plan_provision definition

-- Drop table

-- DROP TABLE financialdb.eg_repayment_plan_provision;

CREATE TABLE financialdb.eg_repayment_plan_provision (
	id int8 NOT NULL, -- ID
	client_code varchar(100) NULL, -- 客户编码
	client_name varchar(200) NULL, -- 客户名称
	contract_code varchar(100) NULL, -- 合同编号
	contract_name varchar(200) NULL, -- 合同名称
	plan_date date NULL, -- 日期
	rent_amount numeric(20, 2) NULL, -- 租金
	principal_amount numeric(20, 2) NULL, -- 本金
	interest_amount numeric(20, 2) NULL, -- 利息
	principal_tax numeric(20, 2) NULL, -- 本金-税金
	interest_tax numeric(20, 2) NULL, -- 利息-税金
	outflow_amount numeric(20, 2) NULL, -- 资金流出
	planned_interest numeric(20, 2) NULL, -- 计划利息(不含税)
	planned_principal numeric(20, 2) NULL, -- 计划本金(不含税)
	cash_flow numeric(20, 2) NULL, -- 现金流
	opening_amortized_cost numeric(20, 2) NULL, -- 期初摊余成本
	ending_amortized_cost numeric(20, 2) NULL, -- 期末摊余成本
	actual_daily_rate numeric(14, 6) NULL, -- 实际日利率
	rental_income numeric(20, 2) NULL, -- 租赁收入
	service_fee_amortization_rate numeric(14, 6) NULL, -- 服务费摊销利率
	service_fee_amortization_income numeric(20, 2) NULL, -- 服务费摊销收入
	xirr_rate numeric(20, 6) NULL, -- XIRR
	actual_repayment_date date NULL, -- 实际归还日期
	actual_repayment_principal_balance numeric(20, 2) NULL, -- 实际归还本金余额
	actual_repayment_principal_amount numeric(20, 2) NULL, -- 实际归还本金发生额
	actual_repayment_interes_balance numeric(20, 2) NULL, -- 实际归还利息余额
	actual_repayment_interes_amount numeric(20, 2) NULL, -- 实际归还利息发生额
	recapture_status varchar(100) NULL, -- 回笼状态
	overdue_earnings numeric(20, 2) NULL, -- 逾期收益
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	del_flag bpchar(1) NULL DEFAULT '0'::bpchar, -- 删除标识(0:未删除,1:已删除)
	"comment" varchar(500) NULL, -- 备注
	overdue_days int4 NULL, -- 逾期天数
	periods int4 NULL, -- 期数
	pay_method varchar(100) NULL, -- 还款标识 期初(下还),期末(上还)
	message_id varchar(100) NULL,
	system_code varchar(100) NULL,
	rental_income_on_balance numeric(20, 2) NULL DEFAULT 0, -- 表内租赁收入
	rental_income_off_balance numeric(20, 2) NULL DEFAULT 0, -- 表外租赁收入
	allocation_method varchar(100) NULL, -- 分摊方式(租赁收入分摊、服务费收入分摊)
	allocation_ratio numeric(24, 6) NULL, --  分摊比例
	service_fee_received numeric(20, 2) NULL, -- 服务费实收（税后）
	service_fee_allocation_no_tax numeric(20, 2) NULL, -- 应分摊的服务费收入（税后）
	last_month_service_fee_allocation_no_tax numeric(20, 2) NULL, -- 上月服务费应分摊金额（税后）
	reclassification_adjustment_no_tax_amount numeric(20, 2) NULL, -- 本月重分类调整(税后)
	x_year_month_adjustment_amount numeric(20, 2) NULL, -- X年X月调整
	not_accrued_amount numeric(20, 2) NULL, -- 实际未计提金额
	accumulated_accrued_amount numeric(20, 2) NULL, -- 累计已计提金额
	accumulated_actual_repayment_interes_amount numeric(20, 2) NULL, --  累计实收利息（不含税）
	paid_in_handling_fees_add_other_income_sub_costs numeric(20, 2) NULL, --  实收手续费+实收其他收入-实付成本
	ta_reclassification numeric(20, 2) NULL, -- TA重分类
	unrealized_revenue numeric(20, 2) NULL, -- 未实现收益总额
	previous_paid_period timestamp(6) NULL, -- 上期实收期间
	rental_income_before_total numeric(20, 2) NULL, -- 本月以前
	rental_income_after_total numeric(20, 2) NULL, -- 本月之后
	overdue_adjustment_amount numeric(20, 2) NULL, -- 当月逾期调整额
	total_recorded_amount numeric(20, 2) NULL, -- 合计入账金额
	confirmed_actual_receipt numeric(20, 2) NULL, -- 实收-已确认
	accrued bpchar(1) NULL DEFAULT '0'::bpchar, -- 是否计提
	income_provision_method varchar(10) NULL, -- 计提方式
	exception_type varchar(200) NULL, -- 异常类型
	rental_income_on_balance_confirmed numeric(20, 2) NULL, --  已确认逾期收益(表外)
	rental_income_off_balance_confirmed numeric(20, 2) NULL, --  已确认租赁收益(表内)
	actual_repayment_rent_amount numeric(20, 2) NULL, -- 实际归还租金
	labor_overdue_days int4 NULL, --  人工逾期天数
	before_x_year_month_amount numeric(20, 2) NULL, -- X年X月以前
	allocation_after_x_year_month_balance numeric(20, 2) NULL, -- X年X月摊销后余额
	org_id varchar(100) NULL, -- 签约主体
	labor_overdue_mark varchar(100) NULL, -- 手工逾期标识
	process_method varchar(100) NULL, -- 处理方式
	observed bpchar(1) NULL, -- 是否观察期
	observed_expiration_date timestamp(6) NULL, -- 观察期到期日
	last_repayment_date timestamp(6) NULL, -- 上次还款日
	next_payment_date timestamp(6) NULL, -- 下次回款日
	confirmed_income numeric(20, 2) NULL, -- 已确认收益
	paid_handling_fees numeric(20, 2) NULL, -- 实收手续费
	other_income numeric(20, 2) NULL, -- 其他收入
	paid_other_costs numeric(20, 2) NULL, -- 实付其他成本
	invoicing_flag varchar(10) NULL, -- 开票标识
	manual_change_mark varchar(10) NULL, -- 交易结构手工调整标志
	plan_date_period int4 NULL, -- 计划还款日(yyyyMM)
	amortized bpchar(1) NULL, -- 是否已摊销(是否计提为是,且计提则为已摊销,0:未摊销,1:已摊销)
	on_and_off_balance_sheet bpchar(1) NULL, -- 表内表外(0:表内,1:表外)
	source_irr_rate numeric(20, 6) NULL, -- 源IRR Rate
	CONSTRAINT eg_repayment_plan_provision_pkey PRIMARY KEY (id)
);
CREATE INDEX idx_repayment_plan_provision_contract_code ON financialdb.eg_repayment_plan_provision USING btree (contract_code);
CREATE INDEX idx_repayment_plan_provision_plan_date_period ON financialdb.eg_repayment_plan_provision USING btree (plan_date_period);
COMMENT ON TABLE financialdb.eg_repayment_plan_provision IS '偿还计划测算表-计提用';

-- Column comments

COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.client_code IS '客户编码';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.client_name IS '客户名称';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.contract_name IS '合同名称';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.plan_date IS '日期';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.rent_amount IS '租金';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.principal_amount IS '本金';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.interest_amount IS '利息';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.principal_tax IS '本金-税金';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.interest_tax IS '利息-税金';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.outflow_amount IS '资金流出';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.planned_interest IS '计划利息(不含税)';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.planned_principal IS '计划本金(不含税)';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.cash_flow IS '现金流';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.opening_amortized_cost IS '期初摊余成本';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.ending_amortized_cost IS '期末摊余成本';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.actual_daily_rate IS '实际日利率';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.rental_income IS '租赁收入';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.service_fee_amortization_rate IS '服务费摊销利率';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.service_fee_amortization_income IS '服务费摊销收入';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.xirr_rate IS 'XIRR';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.actual_repayment_date IS '实际归还日期';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.actual_repayment_principal_balance IS '实际归还本金余额';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.actual_repayment_principal_amount IS '实际归还本金发生额';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.actual_repayment_interes_balance IS '实际归还利息余额';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.actual_repayment_interes_amount IS '实际归还利息发生额';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.recapture_status IS '回笼状态';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.overdue_earnings IS '逾期收益';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.del_flag IS '删除标识(0:未删除,1:已删除)';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision."comment" IS '备注';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.overdue_days IS '逾期天数';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.periods IS '期数';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.pay_method IS '还款标识 期初(下还),期末(上还)';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.rental_income_on_balance IS '表内租赁收入';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.rental_income_off_balance IS '表外租赁收入';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.allocation_method IS '分摊方式(租赁收入分摊、服务费收入分摊)';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.allocation_ratio IS ' 分摊比例';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.service_fee_received IS '服务费实收（税后）';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.service_fee_allocation_no_tax IS '应分摊的服务费收入（税后）';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.last_month_service_fee_allocation_no_tax IS '上月服务费应分摊金额（税后）';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.reclassification_adjustment_no_tax_amount IS '本月重分类调整(税后)';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.x_year_month_adjustment_amount IS 'X年X月调整';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.not_accrued_amount IS '实际未计提金额';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.accumulated_accrued_amount IS '累计已计提金额';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.accumulated_actual_repayment_interes_amount IS ' 累计实收利息（不含税）';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.paid_in_handling_fees_add_other_income_sub_costs IS ' 实收手续费+实收其他收入-实付成本';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.ta_reclassification IS 'TA重分类';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.unrealized_revenue IS '未实现收益总额';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.previous_paid_period IS '上期实收期间';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.rental_income_before_total IS '本月以前';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.rental_income_after_total IS '本月之后';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.overdue_adjustment_amount IS '当月逾期调整额';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.total_recorded_amount IS '合计入账金额';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.confirmed_actual_receipt IS '实收-已确认';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.accrued IS '是否计提';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.income_provision_method IS '计提方式';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.exception_type IS '异常类型';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.rental_income_on_balance_confirmed IS ' 已确认逾期收益(表外)';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.rental_income_off_balance_confirmed IS ' 已确认租赁收益(表内)';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.actual_repayment_rent_amount IS '实际归还租金';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.labor_overdue_days IS ' 人工逾期天数';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.before_x_year_month_amount IS 'X年X月以前';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.allocation_after_x_year_month_balance IS 'X年X月摊销后余额';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.org_id IS '签约主体';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.labor_overdue_mark IS '手工逾期标识';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.process_method IS '处理方式';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.observed IS '是否观察期';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.observed_expiration_date IS '观察期到期日';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.last_repayment_date IS '上次还款日';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.next_payment_date IS '下次回款日';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.confirmed_income IS '已确认收益';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.paid_handling_fees IS '实收手续费';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.other_income IS '其他收入';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.paid_other_costs IS '实付其他成本';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.invoicing_flag IS '开票标识';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.manual_change_mark IS '交易结构手工调整标志';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.plan_date_period IS '计划还款日(yyyyMM)';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.amortized IS '是否已摊销(是否计提为是,且计提则为已摊销,0:未摊销,1:已摊销)';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.on_and_off_balance_sheet IS '表内表外(0:表内,1:表外)';
COMMENT ON COLUMN financialdb.eg_repayment_plan_provision.source_irr_rate IS '源IRR Rate';

-- financialdb.eg_contract_ta_amount definition

-- Drop table

-- DROP TABLE financialdb.eg_contract_ta_amount;

CREATE TABLE financialdb.eg_contract_ta_amount (
	id int8 NULL,
	contract_code varchar(100) NULL, -- 合同编码
	ta_amount numeric(20, 2) NULL, -- ta金额
	transaction_date date NULL, -- 传送日期
	create_by varchar(32) NULL,
	create_time timestamp NULL,
	update_by varchar(32) NULL,
	update_time timestamp NULL,
	del_flag bpchar(1) NULL DEFAULT '0'::bpchar
);
COMMENT ON TABLE financialdb.eg_contract_ta_amount IS '合同TA收款金额';

-- Column comments

COMMENT ON COLUMN financialdb.eg_contract_ta_amount.contract_code IS '合同编码';
COMMENT ON COLUMN financialdb.eg_contract_ta_amount.ta_amount IS 'ta金额';
COMMENT ON COLUMN financialdb.eg_contract_ta_amount.transaction_date IS '传送日期';