-- 1.先删除原表
DROP TABLE financialdb.eg_pay_vat;

-- 2.创建新表
-- financialdb.eg_pay_vat definition

-- Drop table

-- DROP TABLE financialdb.eg_pay_vat;

CREATE TABLE financialdb.eg_pay_vat (
	id int8 NOT NULL, -- ID
	contract_id int8 NULL, -- 合同id
	contract_code varchar(100) NULL, -- 合同编号
	contract_type varchar(20) NULL, -- 合同类型
	lease_type varchar(30) NULL, -- 租赁类型
	tax_rate numeric(20, 2) NULL, -- 税率
	client_code varchar(100) NULL, -- 客户编号
	client_name varchar(200) NULL, -- 客户名称
	invoicing_flag varchar(10) NULL, -- 开票标识
	contract_status varchar(50) NULL, -- 合同状态
	financial_contract_status varchar(50) NULL, -- 财务合同状态
	org_id varchar(100) NULL, -- 开票主体
	tax_payable numeric(20, 2) DEFAULT 0 NULL, -- 应开税额
	tax_accrued numeric(20, 2) DEFAULT 0 NULL, -- 已计提税额
	tax_invoiced numeric(20, 2) DEFAULT 0 NULL, -- 已开票税额
	actual_tax_balance numeric(20, 2) DEFAULT 0 NULL, -- 实际剩余税额
	should_tax_balance numeric(20, 2) DEFAULT 0 NULL, -- 应剩余税额（按计划）
	account_balance numeric(20, 2) DEFAULT 0 NULL, -- 科目余额
	report_balance numeric(20, 2) DEFAULT 0 NULL, -- 报表余额
	exception_type text NULL, -- 异常类型
	contract_code_m varchar(50) NULL, -- 租赁合同编号
	service_fee_received numeric(20, 2) DEFAULT 0 NULL, -- 服务费实收金额
	service_fee_received_date timestamp NULL, -- 服务费实收日期
	retained_price_balance numeric(20, 2) DEFAULT 0 NULL, -- 留购价余额
	lease_date_start timestamp NULL, -- 合同起租日
	lease_date_end timestamp NULL, -- 合同到期日
	process_status varchar(64) DEFAULT 0 NULL, -- 处理状态
	is_generate_voucher bpchar(1) DEFAULT 0 NULL, -- 是否已生成凭证(0-否，1-是)
	process_instance_id int8 NULL, -- 流程实例id
	voucher_id text NULL, -- 凭证id,多个按照逗号分隔
	error_info varchar(2000) NULL, -- 生成凭证报错信息
	account_date timestamp NULL, -- 记账日期
	del_flag bpchar(1) DEFAULT 0 NULL, -- 是否删除（0-否，1-是）
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	CONSTRAINT eg_pay_vat_2_pk PRIMARY KEY (id)
);
CREATE INDEX eg_pay_vat_2_client_code_idx ON financialdb.eg_pay_vat USING btree (client_code);
CREATE INDEX eg_pay_vat_2_contract_code_idx ON financialdb.eg_pay_vat USING btree (contract_code);
CREATE INDEX eg_pay_vat_2_org_id_idx ON financialdb.eg_pay_vat USING btree (org_id);
CREATE INDEX eg_pay_vat_process_status_idx ON financialdb.eg_pay_vat USING btree (process_status);
COMMENT ON TABLE financialdb.eg_pay_vat IS '应交增值税';

-- Column comments

COMMENT ON COLUMN financialdb.eg_pay_vat.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_pay_vat.contract_id IS '合同id';
COMMENT ON COLUMN financialdb.eg_pay_vat.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb.eg_pay_vat.contract_type IS '合同类型';
COMMENT ON COLUMN financialdb.eg_pay_vat.lease_type IS '租赁类型';
COMMENT ON COLUMN financialdb.eg_pay_vat.tax_rate IS '税率';
COMMENT ON COLUMN financialdb.eg_pay_vat.client_code IS '客户编号';
COMMENT ON COLUMN financialdb.eg_pay_vat.client_name IS '客户名称';
COMMENT ON COLUMN financialdb.eg_pay_vat.invoicing_flag IS '开票标识';
COMMENT ON COLUMN financialdb.eg_pay_vat.contract_status IS '合同状态';
COMMENT ON COLUMN financialdb.eg_pay_vat.financial_contract_status IS '财务合同状态';
COMMENT ON COLUMN financialdb.eg_pay_vat.org_id IS '开票主体';
COMMENT ON COLUMN financialdb.eg_pay_vat.tax_payable IS '应开税额';
COMMENT ON COLUMN financialdb.eg_pay_vat.tax_accrued IS '已计提税额';
COMMENT ON COLUMN financialdb.eg_pay_vat.tax_invoiced IS '已开票税额';
COMMENT ON COLUMN financialdb.eg_pay_vat.actual_tax_balance IS '实际剩余税额';
COMMENT ON COLUMN financialdb.eg_pay_vat.should_tax_balance IS '应剩余税额（按计划）';
COMMENT ON COLUMN financialdb.eg_pay_vat.account_balance IS '科目余额';
COMMENT ON COLUMN financialdb.eg_pay_vat.report_balance IS '报表余额';
COMMENT ON COLUMN financialdb.eg_pay_vat.exception_type IS '异常类型';
COMMENT ON COLUMN financialdb.eg_pay_vat.contract_code_m IS '租赁合同编号';
COMMENT ON COLUMN financialdb.eg_pay_vat.service_fee_received IS '服务费实收金额';
COMMENT ON COLUMN financialdb.eg_pay_vat.service_fee_received_date IS '服务费实收日期';
COMMENT ON COLUMN financialdb.eg_pay_vat.retained_price_balance IS '留购价余额';
COMMENT ON COLUMN financialdb.eg_pay_vat.lease_date_start IS '合同起租日';
COMMENT ON COLUMN financialdb.eg_pay_vat.lease_date_end IS '合同到期日';
COMMENT ON COLUMN financialdb.eg_pay_vat.process_status IS '处理状态';
COMMENT ON COLUMN financialdb.eg_pay_vat.is_generate_voucher IS '是否已生成凭证(0-否，1-是)';
COMMENT ON COLUMN financialdb.eg_pay_vat.process_instance_id IS '流程实例id';
COMMENT ON COLUMN financialdb.eg_pay_vat.voucher_id IS '凭证id,多个按照逗号分隔';
COMMENT ON COLUMN financialdb.eg_pay_vat.error_info IS '生成凭证报错信息';
COMMENT ON COLUMN financialdb.eg_pay_vat.account_date IS '记账日期';
COMMENT ON COLUMN financialdb.eg_pay_vat.del_flag IS '是否删除（0-否，1-是）';
COMMENT ON COLUMN financialdb.eg_pay_vat.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_pay_vat.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_pay_vat.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_pay_vat.update_time IS '更新时间';


ALTER TABLE financialdb.eg_payable_insurance ADD process_instance_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_payable_insurance.process_instance_id IS '流程实例id';

ALTER TABLE financialdb.eg_non_confirm_collection_second_detail ADD is_relate_client_auxiliary_account varchar(1) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.is_relate_client_auxiliary_account IS '是否涉及其他客户及辅助帐';

