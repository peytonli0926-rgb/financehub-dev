-- financialdb3.eg_contract_month definition

-- Drop table

-- DROP TABLE financialdb3.eg_contract_month;

CREATE TABLE financialdb3.eg_contract_month (
	id int8 NOT NULL, -- ID
	contract_code varchar(100) NULL, -- 合同编号
	contract_name varchar(200) NULL, -- 合同名称
	client_code varchar(300) NULL, -- 客户编号
	client_name varchar(300) NULL, -- 客户名称
	org_id varchar(100) NULL, -- 组织机编码
	client_type varchar NULL, -- 客户类型
	lease_date_start timestamp NULL, -- 起租日
	lease_date_end timestamp NULL, -- 到期日
	business_code varchar(100) NULL, -- 业务类型编码
	business_name varchar(200) NULL, -- 业务类型名称
	invoice_type varchar NULL, -- 发票类型
	industry varchar NULL, -- 行业
	lease_type varchar NULL, -- 租赁类型
	contract_status varchar(50) NULL, -- 合同状态
	interest_rate_type varchar(50) NULL, -- 利率浮动类型
	classification_five varchar(100) NULL, -- 五级分类
	provision_type varchar NULL, -- 拨备类型
	return_type varchar NULL, -- 还租方式
	domestic_entrance varchar(50) NULL, -- 国产进口
	project_differentiate varchar(100) NULL, -- 项目区分
	business_plate varchar(100) NULL, -- 业务板块
	province_city varchar(200) NULL, -- 省市
	currency_type varchar(50) NULL, -- 币种
	contract_amount numeric(20, 2) NULL DEFAULT 0, -- 合同金额
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	del_flag bpchar(1) NULL DEFAULT '0'::bpchar, -- 删除标识(0:未删除,1:已删除)
	lease_interest_rate_year numeric(24, 6) NULL DEFAULT 0, -- 利率
	financial_contract_status_update_time timestamp(6) NULL, -- 财务合同状态更新时间
	financial_contract_status varchar(50) NULL, -- 财务合同状态
	system_label varchar(50) NULL, -- 系统标签
	transfer_org_id varchar(100) NULL, -- 转入公司
	transfer_contract_code varchar(100) NULL, -- 转入合同号
	transfer_contract_status varchar(50) NULL, -- 转入合同系统合同状态
	special_flag varchar(50) NULL DEFAULT ''::character varying, -- 特殊标识
	business_type_label varchar(50) NULL, -- 业务类型标签
	tax_rate numeric(24, 6) NULL DEFAULT 0, -- 税率
	income_calculate varchar(10) NULL, -- 收益计算
	income_provision_method varchar(10) NULL, -- 收益计提方式
	invoicing_flag varchar(10) NULL DEFAULT '开票'::character varying, -- 开票标识
	payable_device_amount numeric(20, 2) NULL, -- 应付设备款
	receivable_first_amount numeric(20, 2) NULL, -- 应收首付款
	lessor_insurance_amount numeric(20, 2) NULL, -- 出租人保险费
	receivable_margin_amount numeric(20, 2) NULL, -- 应收承租人履约保证金
	channel_fees numeric(20, 2) NULL, -- 渠道费用 (=应付渠道费用+应付海通渠道费用)
	payable_channel_expense numeric(20, 2) NULL, -- 应付渠道费用
	payable_inner_expense numeric(20, 2) NULL DEFAULT 0, -- 应付海通渠道费用
	receivable_procedure_amount numeric(20, 2) NULL DEFAULT 0, -- 应收手续费收入
	lessor_other_costs numeric(20, 2) NULL DEFAULT 0, -- 出租人其它成本=(应付其他+GPS预估费用+应付介绍费+应付法律费)
	payable_other_amount numeric(20, 2) NULL DEFAULT 0, -- 应付其他
	estimate_g_p_s_expense numeric(20, 2) NULL DEFAULT 0, -- GPS预估费用
	payable_introduce numeric(20, 2) NULL DEFAULT 0, -- 应付介绍费
	payable_law_amount numeric(20, 2) NULL DEFAULT 0, -- 应付法律费
	receivable_firm_rebate numeric(20, 2) NULL DEFAULT 0, -- 应收厂商返利
	receivable_insurance_amount numeric(20, 2) NULL DEFAULT 0, -- 应收保险费
	retained_price numeric(20, 2) NULL DEFAULT 0, -- 名义留购价
	receivable_other numeric(20, 2) NULL DEFAULT 0, -- 应收其他
	receivable_service_amount numeric(20, 2) NULL DEFAULT 0, -- 应收服务费
	vendor_margin_amount numeric(20, 2) NULL DEFAULT 0, -- 供应商保证金
	system_code varchar(100) NULL, -- 来源系统
	contract_create_dept varchar(100) NULL, -- 合同出单部门
	outtax_balance numeric(20, 2) NULL DEFAULT 0, -- 应交销项税余额
	lease_revenue_balance numeric(20, 2) NULL DEFAULT 0, -- 租赁收益余额
	depreciation_loss_balance numeric(20, 2) NULL DEFAULT 0, -- 转回拨备（减值准备余额）
	pay_method varchar(100) NULL, -- 还款标识
	contract_code_m varchar(50) NULL, -- 主合同编号
	payable_procedure_cost numeric(20, 2) NULL DEFAULT 0, -- 应付手续费金额
	postal_savings_project_type varchar(50) NULL DEFAULT '2'::character varying, -- 邮储项目类型
	financial_contract_status_imputation varchar(30) NULL, -- 归集财务合同状态
	gps_unit_price numeric(20, 2) NULL DEFAULT 0, -- gps费用单价
	payable_service numeric(20, 2) NULL DEFAULT 0, -- 应付经销商服务费
	payable_recycle_car_amount numeric(20, 2) NULL DEFAULT 0, -- 收车费汇总总额
	payable_bracelet_cost numeric(20, 2) NULL DEFAULT 0, -- 手环成本
	business_date timestamp NULL, -- 业务日期
	manual_lease_flag varchar(2) NULL DEFAULT 0, -- 0:非手工起租 1:手工起租
	business_category varchar(100) NULL, -- 合同业务大类
	province varchar(200) NULL, -- 省
	city varchar(200) NULL, -- 市
	report_flag varchar(2) NULL, -- 租赁大表报表查询时是否过滤标志 0:过滤, 1:不过滤
	is_dzzc varchar(10) NULL DEFAULT 0, -- 是否抵债资产，0否，1是
	rent_contract_total numeric(20, 2) NULL, -- 租赁合同总计
	last_cost numeric(20, 2) NULL, -- 期末残值
	lease_before_recevied_amount numeric(20, 2) NULL, -- 起租前已收租金
	depreciation_reserves_amount_kp numeric(20, 2) NULL DEFAULT 0, -- 核销合同开票时转回的拨备发生额
	depreciation_reserves_amount_sk numeric(20, 2) NULL DEFAULT 0, -- 核销合同收款时转回的拨备发生额
	outtax_amount numeric(20, 2) NULL DEFAULT 0, -- 核销合同税金计提金额
	lease_revenue_amount numeric(20, 2) NULL DEFAULT 0, -- 核销合同收益计提金额
	account_date timestamp NULL, -- 记账日期
	actual_service_amount numeric(20, 2) NULL DEFAULT 0, -- 实收服务费
	is_change_repayment varchar(1) NULL DEFAULT 0, -- 是否做过偿还计划变更(0:否, 1:是)
	paid_handling_fees numeric(20, 2) NULL DEFAULT 0, -- 实收手续费(含税)
	other_income numeric(20, 2) NULL DEFAULT 0, -- 其它收入(含税)
	receivable_rent numeric(20, 2) NULL DEFAULT 0, -- 应收租金
	accrual_amount numeric(20, 2) NULL DEFAULT 0, -- 应开票/计提税额
	tax_amount numeric(20, 2) NULL, -- 税金计提金额
	CONSTRAINT eg_contract_month_pkey PRIMARY KEY (id)
);
CREATE INDEX eg_contract_month_client_code_idx ON financialdb3.eg_contract_month USING btree (client_code);
CREATE INDEX eg_contract_month_code_m_idx ON financialdb3.eg_contract_month USING btree (contract_code_m, del_flag);
CREATE INDEX eg_contract_month_contract_code_idx ON financialdb3.eg_contract_month USING btree (contract_code);
CREATE INDEX eg_contract_month_contract_code_is_null2_idx ON financialdb3.eg_contract_month USING btree (contract_code_m) WHERE ((contract_code_m IS NULL) OR ((contract_code_m)::text = ''::text));
CREATE INDEX eg_contract_month_contract_code_m_idx ON financialdb3.eg_contract_month USING btree (contract_code_m) WHERE ((contract_code)::text = (contract_code_m)::text);
CREATE INDEX eg_contract_month_contract_name_idx ON financialdb3.eg_contract_month USING btree (contract_name);
COMMENT ON TABLE financialdb3.eg_contract_month IS '合同月表';

-- Column comments

COMMENT ON COLUMN financialdb3.eg_contract_month.id IS 'ID';
COMMENT ON COLUMN financialdb3.eg_contract_month.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb3.eg_contract_month.contract_name IS '合同名称';
COMMENT ON COLUMN financialdb3.eg_contract_month.client_code IS '客户编号';
COMMENT ON COLUMN financialdb3.eg_contract_month.client_name IS '客户名称';
COMMENT ON COLUMN financialdb3.eg_contract_month.org_id IS '组织机编码';
COMMENT ON COLUMN financialdb3.eg_contract_month.client_type IS '客户类型';
COMMENT ON COLUMN financialdb3.eg_contract_month.lease_date_start IS '起租日';
COMMENT ON COLUMN financialdb3.eg_contract_month.lease_date_end IS '到期日';
COMMENT ON COLUMN financialdb3.eg_contract_month.business_code IS '业务类型编码';
COMMENT ON COLUMN financialdb3.eg_contract_month.business_name IS '业务类型名称';
COMMENT ON COLUMN financialdb3.eg_contract_month.invoice_type IS '发票类型';
COMMENT ON COLUMN financialdb3.eg_contract_month.industry IS '行业';
COMMENT ON COLUMN financialdb3.eg_contract_month.lease_type IS '租赁类型';
COMMENT ON COLUMN financialdb3.eg_contract_month.contract_status IS '合同状态';
COMMENT ON COLUMN financialdb3.eg_contract_month.interest_rate_type IS '利率浮动类型';
COMMENT ON COLUMN financialdb3.eg_contract_month.classification_five IS '五级分类';
COMMENT ON COLUMN financialdb3.eg_contract_month.provision_type IS '拨备类型';
COMMENT ON COLUMN financialdb3.eg_contract_month.return_type IS '还租方式';
COMMENT ON COLUMN financialdb3.eg_contract_month.domestic_entrance IS '国产进口';
COMMENT ON COLUMN financialdb3.eg_contract_month.project_differentiate IS '项目区分';
COMMENT ON COLUMN financialdb3.eg_contract_month.business_plate IS '业务板块';
COMMENT ON COLUMN financialdb3.eg_contract_month.province_city IS '省市';
COMMENT ON COLUMN financialdb3.eg_contract_month.currency_type IS '币种';
COMMENT ON COLUMN financialdb3.eg_contract_month.contract_amount IS '合同金额';
COMMENT ON COLUMN financialdb3.eg_contract_month.create_by IS '创建人';
COMMENT ON COLUMN financialdb3.eg_contract_month.create_time IS '创建时间';
COMMENT ON COLUMN financialdb3.eg_contract_month.update_by IS '更新人';
COMMENT ON COLUMN financialdb3.eg_contract_month.update_time IS '更新时间';
COMMENT ON COLUMN financialdb3.eg_contract_month.del_flag IS '删除标识(0:未删除,1:已删除)';
COMMENT ON COLUMN financialdb3.eg_contract_month.lease_interest_rate_year IS '利率';
COMMENT ON COLUMN financialdb3.eg_contract_month.financial_contract_status_update_time IS '财务合同状态更新时间';
COMMENT ON COLUMN financialdb3.eg_contract_month.financial_contract_status IS '财务合同状态';
COMMENT ON COLUMN financialdb3.eg_contract_month.system_label IS '系统标签';
COMMENT ON COLUMN financialdb3.eg_contract_month.transfer_org_id IS '转入公司';
COMMENT ON COLUMN financialdb3.eg_contract_month.transfer_contract_code IS '转入合同号';
COMMENT ON COLUMN financialdb3.eg_contract_month.transfer_contract_status IS '转入合同系统合同状态';
COMMENT ON COLUMN financialdb3.eg_contract_month.special_flag IS '特殊标识';
COMMENT ON COLUMN financialdb3.eg_contract_month.business_type_label IS '业务类型标签';
COMMENT ON COLUMN financialdb3.eg_contract_month.tax_rate IS '税率';
COMMENT ON COLUMN financialdb3.eg_contract_month.income_calculate IS '收益计算';
COMMENT ON COLUMN financialdb3.eg_contract_month.income_provision_method IS '收益计提方式';
COMMENT ON COLUMN financialdb3.eg_contract_month.invoicing_flag IS '开票标识';
COMMENT ON COLUMN financialdb3.eg_contract_month.payable_device_amount IS '应付设备款';
COMMENT ON COLUMN financialdb3.eg_contract_month.receivable_first_amount IS '应收首付款';
COMMENT ON COLUMN financialdb3.eg_contract_month.lessor_insurance_amount IS '出租人保险费';
COMMENT ON COLUMN financialdb3.eg_contract_month.receivable_margin_amount IS '应收承租人履约保证金';
COMMENT ON COLUMN financialdb3.eg_contract_month.channel_fees IS '渠道费用 (=应付渠道费用+应付海通渠道费用)';
COMMENT ON COLUMN financialdb3.eg_contract_month.payable_channel_expense IS '应付渠道费用';
COMMENT ON COLUMN financialdb3.eg_contract_month.payable_inner_expense IS '应付海通渠道费用';
COMMENT ON COLUMN financialdb3.eg_contract_month.receivable_procedure_amount IS '应收手续费收入';
COMMENT ON COLUMN financialdb3.eg_contract_month.lessor_other_costs IS '出租人其它成本=(应付其他+GPS预估费用+应付介绍费+应付法律费)';
COMMENT ON COLUMN financialdb3.eg_contract_month.payable_other_amount IS '应付其他';
COMMENT ON COLUMN financialdb3.eg_contract_month.estimate_g_p_s_expense IS 'GPS预估费用';
COMMENT ON COLUMN financialdb3.eg_contract_month.payable_introduce IS '应付介绍费';
COMMENT ON COLUMN financialdb3.eg_contract_month.payable_law_amount IS '应付法律费';
COMMENT ON COLUMN financialdb3.eg_contract_month.receivable_firm_rebate IS '应收厂商返利';
COMMENT ON COLUMN financialdb3.eg_contract_month.receivable_insurance_amount IS '应收保险费';
COMMENT ON COLUMN financialdb3.eg_contract_month.retained_price IS '名义留购价';
COMMENT ON COLUMN financialdb3.eg_contract_month.receivable_other IS '应收其他';
COMMENT ON COLUMN financialdb3.eg_contract_month.receivable_service_amount IS '应收服务费';
COMMENT ON COLUMN financialdb3.eg_contract_month.vendor_margin_amount IS '供应商保证金';
COMMENT ON COLUMN financialdb3.eg_contract_month.system_code IS '来源系统';
COMMENT ON COLUMN financialdb3.eg_contract_month.contract_create_dept IS '合同出单部门';
COMMENT ON COLUMN financialdb3.eg_contract_month.outtax_balance IS '应交销项税余额';
COMMENT ON COLUMN financialdb3.eg_contract_month.lease_revenue_balance IS '租赁收益余额';
COMMENT ON COLUMN financialdb3.eg_contract_month.depreciation_loss_balance IS '转回拨备（减值准备余额）';
COMMENT ON COLUMN financialdb3.eg_contract_month.pay_method IS '还款标识';
COMMENT ON COLUMN financialdb3.eg_contract_month.contract_code_m IS '主合同编号';
COMMENT ON COLUMN financialdb3.eg_contract_month.payable_procedure_cost IS '应付手续费金额';
COMMENT ON COLUMN financialdb3.eg_contract_month.postal_savings_project_type IS '邮储项目类型';
COMMENT ON COLUMN financialdb3.eg_contract_month.financial_contract_status_imputation IS '归集财务合同状态';
COMMENT ON COLUMN financialdb3.eg_contract_month.gps_unit_price IS 'gps费用单价';
COMMENT ON COLUMN financialdb3.eg_contract_month.payable_service IS '应付经销商服务费';
COMMENT ON COLUMN financialdb3.eg_contract_month.payable_recycle_car_amount IS '收车费汇总总额';
COMMENT ON COLUMN financialdb3.eg_contract_month.payable_bracelet_cost IS '手环成本';
COMMENT ON COLUMN financialdb3.eg_contract_month.business_date IS '业务日期';
COMMENT ON COLUMN financialdb3.eg_contract_month.manual_lease_flag IS '0:非手工起租 1:手工起租';
COMMENT ON COLUMN financialdb3.eg_contract_month.business_category IS '合同业务大类';
COMMENT ON COLUMN financialdb3.eg_contract_month.province IS '省';
COMMENT ON COLUMN financialdb3.eg_contract_month.city IS '市';
COMMENT ON COLUMN financialdb3.eg_contract_month.report_flag IS '租赁大表报表查询时是否过滤标志 0:过滤, 1:不过滤';
COMMENT ON COLUMN financialdb3.eg_contract_month.is_dzzc IS '是否抵债资产，0否，1是';
COMMENT ON COLUMN financialdb3.eg_contract_month.rent_contract_total IS '租赁合同总计';
COMMENT ON COLUMN financialdb3.eg_contract_month.last_cost IS '期末残值';
COMMENT ON COLUMN financialdb3.eg_contract_month.lease_before_recevied_amount IS '起租前已收租金';
COMMENT ON COLUMN financialdb3.eg_contract_month.depreciation_reserves_amount_kp IS '核销合同开票时转回的拨备发生额';
COMMENT ON COLUMN financialdb3.eg_contract_month.depreciation_reserves_amount_sk IS '核销合同收款时转回的拨备发生额';
COMMENT ON COLUMN financialdb3.eg_contract_month.outtax_amount IS '核销合同税金计提金额';
COMMENT ON COLUMN financialdb3.eg_contract_month.lease_revenue_amount IS '核销合同收益计提金额';
COMMENT ON COLUMN financialdb3.eg_contract_month.account_date IS '记账日期';
COMMENT ON COLUMN financialdb3.eg_contract_month.actual_service_amount IS '实收服务费';
COMMENT ON COLUMN financialdb3.eg_contract_month.is_change_repayment IS '是否做过偿还计划变更(0:否, 1:是)';
COMMENT ON COLUMN financialdb3.eg_contract_month.paid_handling_fees IS '实收手续费(含税)';
COMMENT ON COLUMN financialdb3.eg_contract_month.other_income IS '其它收入(含税)';
COMMENT ON COLUMN financialdb3.eg_contract_month.receivable_rent IS '应收租金';
COMMENT ON COLUMN financialdb3.eg_contract_month.accrual_amount IS '应开票/计提税额';
COMMENT ON COLUMN financialdb3.eg_contract_month.tax_amount IS '税金计提金额';