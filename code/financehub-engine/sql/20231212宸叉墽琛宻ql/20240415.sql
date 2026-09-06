--合同表新增省市字段
ALTER TABLE financialdb.eg_contract ADD province varchar(200) NULL;
COMMENT ON COLUMN financialdb.eg_contract.province IS '省';
ALTER TABLE financialdb.eg_contract ADD city varchar(200) NULL;
COMMENT ON COLUMN financialdb.eg_contract.city IS '市';

ALTER TABLE financialdb.eg_voucher_to_eas_record ADD line_no int8 NULL;
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.line_no IS '行号';

--偿还计划表新增字段

ALTER TABLE financialdb.eg_repayment_plan ADD ebank_serial_number varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_repayment_plan.ebank_serial_number IS '网银编号';
ALTER TABLE financialdb.eg_repayment_plan ADD settlement_way varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_repayment_plan.settlement_way IS '结算方式';

--偿还历史表
ALTER TABLE financialdb.eg_repayment_plan_his ADD ebank_serial_number varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_repayment_plan_his.ebank_serial_number IS '网银编号';
ALTER TABLE financialdb.eg_repayment_plan_his ADD settlement_way varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_repayment_plan_his.settlement_way IS '结算方式';
ALTER TABLE financialdb.eg_repayment_plan_his ADD unrealized_revenue numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_repayment_plan_his.unrealized_revenue IS '未实现收益总额';


-- financialdb.eg_long_receivable_register definition

-- Drop table

-- DROP TABLE financialdb.eg_long_receivable_register;

CREATE TABLE financialdb.eg_long_receivable_register (
	id int8 NOT NULL, -- ID
	long_receivable_number varchar(100) NULL, -- 长期应收款编号
	contract_code varchar(100) NULL, -- 合同编号
	project_name varchar(100) NULL, -- 项目名称
	client_name varchar(200) NULL, -- 客户名称
	org_id varchar(100) NULL, -- 签约主体
	receivable_total numeric(20, 2) NULL, -- 应收总额
	receivable_principal numeric(20, 2) NULL, -- 应收本金
	receivable_interest numeric(20, 2) NULL, -- 应收利息
	other_receivable numeric(20, 2) NULL, -- 其他应收款
	process_status varchar(64) DEFAULT 1 NULL, -- 处理状态
	process_instance_id int8 NULL, -- 流程实例id
	is_generate_voucher bpchar(1) DEFAULT 0 NULL, -- 是否已生成凭证(0-否，1-是)
	voucher_id varchar(2000) NULL, -- 凭证id,多个按照逗号分隔
	error_info varchar(2000) NULL, -- 生成凭证报错信息
	account_date timestamp NULL, -- 记账日期
	del_flag bpchar(1) DEFAULT 0 NULL, -- 是否删除（0-否，1-是）
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	client_code varchar(100) NULL, -- 客户编号
	CONSTRAINT eg_long_receivable_register_pk PRIMARY KEY (id)
);
COMMENT ON TABLE financialdb.eg_long_receivable_register IS '长期应收款登记';

-- Column comments

COMMENT ON COLUMN financialdb.eg_long_receivable_register.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_long_receivable_register.long_receivable_number IS '长期应收款编号';
COMMENT ON COLUMN financialdb.eg_long_receivable_register.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb.eg_long_receivable_register.project_name IS '项目名称';
COMMENT ON COLUMN financialdb.eg_long_receivable_register.client_name IS '客户名称';
COMMENT ON COLUMN financialdb.eg_long_receivable_register.org_id IS '签约主体';
COMMENT ON COLUMN financialdb.eg_long_receivable_register.receivable_total IS '应收总额';
COMMENT ON COLUMN financialdb.eg_long_receivable_register.receivable_principal IS '应收本金';
COMMENT ON COLUMN financialdb.eg_long_receivable_register.receivable_interest IS '应收利息';
COMMENT ON COLUMN financialdb.eg_long_receivable_register.other_receivable IS '其他应收款';
COMMENT ON COLUMN financialdb.eg_long_receivable_register.process_status IS '处理状态';
COMMENT ON COLUMN financialdb.eg_long_receivable_register.process_instance_id IS '流程实例id';
COMMENT ON COLUMN financialdb.eg_long_receivable_register.is_generate_voucher IS '是否已生成凭证(0-否，1-是)';
COMMENT ON COLUMN financialdb.eg_long_receivable_register.voucher_id IS '凭证id,多个按照逗号分隔';
COMMENT ON COLUMN financialdb.eg_long_receivable_register.error_info IS '生成凭证报错信息';
COMMENT ON COLUMN financialdb.eg_long_receivable_register.account_date IS '记账日期';
COMMENT ON COLUMN financialdb.eg_long_receivable_register.del_flag IS '是否删除（0-否，1-是）';
COMMENT ON COLUMN financialdb.eg_long_receivable_register.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_long_receivable_register.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_long_receivable_register.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_long_receivable_register.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_long_receivable_register.client_code IS '客户编号';

-- financialdb.eg_long_repayment_plan definition

-- Drop table

-- DROP TABLE financialdb.eg_long_repayment_plan;

CREATE TABLE financialdb.eg_long_repayment_plan (
	id int8 NOT NULL, -- ID
	long_receivable_number varchar(100) NULL, -- 长期应收款编号
	contract_code varchar(100) NULL, -- 合同编号
	receivable_date timestamp NULL, -- 应收日期
	receivable_total numeric(20, 2) NULL, -- 应收总额
	receivable_principal numeric(20, 2) NULL, -- 应收本金
	receivable_interest numeric(20, 2) NULL, -- 应收利息
	voucher_id varchar(2000) NULL, -- 凭证id(多个逗号分隔)
	account_date timestamp NULL, -- 财务日期
	error_info varchar(2000) NULL, -- 生成凭证报错信息
	del_flag bpchar(1) DEFAULT 0 NULL, -- 是否删除（0-否，1-是）
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	CONSTRAINT eg_long_repayment_plan_pk PRIMARY KEY (id)
);
COMMENT ON TABLE financialdb.eg_long_repayment_plan IS '长期应收款-偿还计划';

-- Column comments

COMMENT ON COLUMN financialdb.eg_long_repayment_plan.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_long_repayment_plan.long_receivable_number IS '长期应收款编号';
COMMENT ON COLUMN financialdb.eg_long_repayment_plan.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb.eg_long_repayment_plan.receivable_date IS '应收日期';
COMMENT ON COLUMN financialdb.eg_long_repayment_plan.receivable_total IS '应收总额';
COMMENT ON COLUMN financialdb.eg_long_repayment_plan.receivable_principal IS '应收本金';
COMMENT ON COLUMN financialdb.eg_long_repayment_plan.receivable_interest IS '应收利息';
COMMENT ON COLUMN financialdb.eg_long_repayment_plan.voucher_id IS '凭证id(多个逗号分隔)';
COMMENT ON COLUMN financialdb.eg_long_repayment_plan.account_date IS '财务日期';
COMMENT ON COLUMN financialdb.eg_long_repayment_plan.error_info IS '生成凭证报错信息';
COMMENT ON COLUMN financialdb.eg_long_repayment_plan.del_flag IS '是否删除（0-否，1-是）';
COMMENT ON COLUMN financialdb.eg_long_repayment_plan.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_long_repayment_plan.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_long_repayment_plan.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_long_repayment_plan.update_time IS '更新时间';

-- financialdb.eg_long_apportion definition

-- Drop table

-- DROP TABLE financialdb.eg_long_apportion;

CREATE TABLE financialdb.eg_long_apportion (
	id int8 NOT NULL, -- ID
	long_receivable_number varchar(100) NULL, -- 长期应收款编号
	contract_code varchar(100) NULL, -- 合同编号
	receivable_date timestamp NULL, -- 日期
	receivable_total numeric(20, 2) NULL, -- 应收总额
	receivable_principal numeric(20, 2) NULL, -- 应收本金
	receivable_interest numeric(20, 2) NULL, -- 应收利息
	residual_principal numeric(20, 2) NULL, -- 剩余本金
	amortized_cost numeric(20, 2) NULL, -- 摊余成本
	confirm_income numeric(20, 2) NULL, -- 确认收入
	voucher_id varchar(2000) NULL, -- 凭证id(多个逗号分隔)
	account_date timestamp NULL, -- 财务日期
	error_info varchar(2000) NULL, -- 生成凭证报错信息
	del_flag bpchar(1) DEFAULT 0 NULL, -- 是否删除（0-否，1-是）
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	CONSTRAINT eg_long_apportion_pk PRIMARY KEY (id)
);
COMMENT ON TABLE financialdb.eg_long_apportion IS '长期应收款-分摊表';

-- Column comments

COMMENT ON COLUMN financialdb.eg_long_apportion.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_long_apportion.long_receivable_number IS '长期应收款编号';
COMMENT ON COLUMN financialdb.eg_long_apportion.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb.eg_long_apportion.receivable_date IS '日期';
COMMENT ON COLUMN financialdb.eg_long_apportion.receivable_total IS '应收总额';
COMMENT ON COLUMN financialdb.eg_long_apportion.receivable_principal IS '应收本金';
COMMENT ON COLUMN financialdb.eg_long_apportion.receivable_interest IS '应收利息';
COMMENT ON COLUMN financialdb.eg_long_apportion.residual_principal IS '剩余本金';
COMMENT ON COLUMN financialdb.eg_long_apportion.amortized_cost IS '摊余成本';
COMMENT ON COLUMN financialdb.eg_long_apportion.confirm_income IS '确认收入';
COMMENT ON COLUMN financialdb.eg_long_apportion.voucher_id IS '凭证id(多个逗号分隔)';
COMMENT ON COLUMN financialdb.eg_long_apportion.account_date IS '财务日期';
COMMENT ON COLUMN financialdb.eg_long_apportion.error_info IS '生成凭证报错信息';
COMMENT ON COLUMN financialdb.eg_long_apportion.del_flag IS '是否删除（0-否，1-是）';
COMMENT ON COLUMN financialdb.eg_long_apportion.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_long_apportion.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_long_apportion.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_long_apportion.update_time IS '更新时间';

-- financialdb.eg_long_income_confirm definition

-- Drop table

-- DROP TABLE financialdb.eg_long_income_confirm;

CREATE TABLE financialdb.eg_long_income_confirm (
	id int8 NOT NULL, -- ID
	long_receivable_number varchar(100) NULL, -- 长期应收款编号
	contract_code varchar(100) NULL, -- 合同编号
	project_name varchar(100) NULL, -- 项目名称
	account_month date NULL, -- 记账月份
	confirm_income_amount numeric(20, 2) NULL, -- 确认收入金额
	process_status varchar(64) DEFAULT 1 NULL, -- 处理状态
	process_instance_id int8 NULL, -- 流程实例id
	voucher_id varchar(2000) NULL, -- 凭证id(多个逗号分隔)
	account_date timestamp NULL, -- 财务日期
	error_info varchar(2000) NULL, -- 生成凭证报错信息
	client_name varchar(200) NULL, -- 客户名称
	client_code varchar(100) NULL, -- 客户编号
	org_id varchar(100) NULL, -- 签约主体
	del_flag bpchar(1) DEFAULT 0 NULL, -- 是否删除（0-否，1-是）
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	is_generate_voucher bpchar(1) DEFAULT 0 NULL, -- 是否已生成凭证(0-否，1-是)
	CONSTRAINT eg_long_confirm_income_pk PRIMARY KEY (id)
);
COMMENT ON TABLE financialdb.eg_long_income_confirm IS '长期应收款-收入确认';

-- Column comments

COMMENT ON COLUMN financialdb.eg_long_income_confirm.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_long_income_confirm.long_receivable_number IS '长期应收款编号';
COMMENT ON COLUMN financialdb.eg_long_income_confirm.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb.eg_long_income_confirm.project_name IS '项目名称';
COMMENT ON COLUMN financialdb.eg_long_income_confirm.account_month IS '记账月份';
COMMENT ON COLUMN financialdb.eg_long_income_confirm.confirm_income_amount IS '确认收入金额';
COMMENT ON COLUMN financialdb.eg_long_income_confirm.process_status IS '处理状态';
COMMENT ON COLUMN financialdb.eg_long_income_confirm.process_instance_id IS '流程实例id';
COMMENT ON COLUMN financialdb.eg_long_income_confirm.voucher_id IS '凭证id(多个逗号分隔)';
COMMENT ON COLUMN financialdb.eg_long_income_confirm.account_date IS '财务日期';
COMMENT ON COLUMN financialdb.eg_long_income_confirm.error_info IS '生成凭证报错信息';
COMMENT ON COLUMN financialdb.eg_long_income_confirm.client_name IS '客户名称';
COMMENT ON COLUMN financialdb.eg_long_income_confirm.client_code IS '客户编号';
COMMENT ON COLUMN financialdb.eg_long_income_confirm.org_id IS '签约主体';
COMMENT ON COLUMN financialdb.eg_long_income_confirm.del_flag IS '是否删除（0-否，1-是）';
COMMENT ON COLUMN financialdb.eg_long_income_confirm.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_long_income_confirm.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_long_income_confirm.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_long_income_confirm.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_long_income_confirm.is_generate_voucher IS '是否已生成凭证(0-否，1-是)';