ALTER TABLE financialdb.eg_contract_balance ADD bank_deposits_balance numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance.bank_deposits_balance IS '银行存款余额';
ALTER TABLE financialdb.eg_contract_balance ADD bank_deposits_amount numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance.bank_deposits_amount IS '银行存款发生额';
ALTER TABLE financialdb.eg_contract_balance_latest ADD bank_deposits_balance numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance_latest.bank_deposits_balance IS '银行存款余额';
ALTER TABLE financialdb.eg_contract_balance_latest ADD bank_deposits_amount numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance_latest.bank_deposits_amount IS '银行存款发生额';
ALTER TABLE financialdb.eg_contract_balance_temp ADD bank_deposits_balance numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.bank_deposits_balance IS '银行存款余额';
ALTER TABLE financialdb.eg_contract_balance_temp ADD bank_deposits_amount numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.bank_deposits_amount IS '银行存款发生额';


-- financialdb.eg_internal_transfer definition

-- Drop table

-- DROP TABLE financialdb.eg_internal_transfer;

CREATE TABLE financialdb.eg_internal_transfer (
	id int8 NOT NULL, -- ID
	batch varchar(100) NOT NULL, -- 批次
	transfer_party varchar(100) NULL, -- 转让方
	contract_code varchar(100) NULL, -- 合同编码
	amount numeric(20, 2) DEFAULT 0 NULL, -- 余额
	payment_date timestamp NULL, -- 支付日期
	bank_account varchar(100) NULL, -- 银行账号
	process_instance_id int8 NULL, -- 流程id
	process_status varchar(100) DEFAULT 1 NULL, -- 处理状态 1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝
	is_generate_voucher bpchar(1) DEFAULT '0'::bpchar NULL, -- 是否已生成凭证（0：未生成1：已生成）默认0
	voucher_id varchar(2000) NULL, -- 凭证id,多个按照逗号分隔
	error_info varchar(2000) NULL, -- 生成凭证报错信息
	account_date timestamp NULL, -- 记账日期
	del_flag bpchar(1) DEFAULT 0 NULL, -- 是否删除（0-否，1-是）
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	finance_date timestamp NULL, -- 财务日期
	client_code varchar(100) NULL, -- 客户编码
	CONSTRAINT eg_internal_transfer_pk PRIMARY KEY (id)
);
COMMENT ON TABLE financialdb.eg_internal_transfer IS '资产转让-内部调拨';

-- Column comments

COMMENT ON COLUMN financialdb.eg_internal_transfer.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_internal_transfer.batch IS '批次';
COMMENT ON COLUMN financialdb.eg_internal_transfer.transfer_party IS '转让方';
COMMENT ON COLUMN financialdb.eg_internal_transfer.contract_code IS '合同编码';
COMMENT ON COLUMN financialdb.eg_internal_transfer.amount IS '余额';
COMMENT ON COLUMN financialdb.eg_internal_transfer.payment_date IS '支付日期';
COMMENT ON COLUMN financialdb.eg_internal_transfer.bank_account IS '银行账号';
COMMENT ON COLUMN financialdb.eg_internal_transfer.process_instance_id IS '流程id';
COMMENT ON COLUMN financialdb.eg_internal_transfer.process_status IS '处理状态 1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝';
COMMENT ON COLUMN financialdb.eg_internal_transfer.is_generate_voucher IS '是否已生成凭证（0：未生成1：已生成）默认0';
COMMENT ON COLUMN financialdb.eg_internal_transfer.voucher_id IS '凭证id,多个按照逗号分隔';
COMMENT ON COLUMN financialdb.eg_internal_transfer.error_info IS '生成凭证报错信息';
COMMENT ON COLUMN financialdb.eg_internal_transfer.account_date IS '记账日期';
COMMENT ON COLUMN financialdb.eg_internal_transfer.del_flag IS '是否删除（0-否，1-是）';
COMMENT ON COLUMN financialdb.eg_internal_transfer.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_internal_transfer.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_internal_transfer.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_internal_transfer.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_internal_transfer.finance_date IS '财务日期';
COMMENT ON COLUMN financialdb.eg_internal_transfer.client_code IS '客户编码';


--手工表新增客户类型字段
ALTER TABLE financialdb.eg_manual_voucher ADD client_type varchar NULL;
COMMENT ON COLUMN financialdb.eg_manual_voucher.client_type IS '客户类型';

ALTER TABLE financialdb.eg_manual ADD recheck_user_no varchar(50) NULL;
COMMENT ON COLUMN financialdb.eg_manual.recheck_user_no IS '复核人工号';

ALTER TABLE financialdb.eg_manual ADD recheck_user_name varchar(50) NULL;
COMMENT ON COLUMN financialdb.eg_manual.recheck_user_name IS '复核人姓名';

ALTER TABLE financialdb.eg_non_confirm_collection_second_detail ADD auditor varchar(20) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.auditor IS '审核人';
ALTER TABLE financialdb.eg_non_confirm_collection_second_detail ADD auditor_name varchar(50) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.auditor_name IS '审核人姓名';
