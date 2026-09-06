ALTER TABLE financialdb.eg_non_confirm_collection_account_checking ADD reclass_amount numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.reclass_amount IS '重分类金额';
ALTER TABLE financialdb.eg_non_confirm_collection_account_checking ADD reclass_account_number varchar(50) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.reclass_account_number IS '重分类科目';



ALTER TABLE eg_fund_ebank_transaction_data ADD collection_type varchar(100) NULL;
COMMENT ON COLUMN eg_fund_ebank_transaction_data.collection_type IS '收款类型';

ALTER TABLE eg_fund_payment_data ADD business_date varchar(100) NULL;
COMMENT ON COLUMN eg_fund_payment_data.business_date IS '业务日期操作付款日期，格式：yyyy-MM-dd HH:mm:ss';


-- financialdb.eg_margin_contract_balance_temp definition

-- Drop table

-- DROP TABLE financialdb.eg_margin_contract_balance_temp;

CREATE TABLE financialdb.eg_margin_contract_balance_temp (
	contract_code varchar(100) NOT NULL, -- 合同编码
	org_id varchar(100) NOT NULL, -- 签约主体
	client_code varchar(100) NOT NULL, -- 客户编码
	business_code varchar(100) NULL, -- 业务编码
	contract_name varchar(200) NULL, -- 合同名称
	lease_date_start timestamp NULL, -- 起租开始时间
	lease_date_end timestamp NULL, -- 起租结束时间
	business_name varchar(200) NULL, -- 业务名称
	currency_type varchar(50) NULL, -- 币种
	client_name varchar(200) NULL, -- 客户名称
	supplier_margin_balance numeric NULL, -- 供应商保证金余额
	payable_margin_balance numeric NULL, -- 应付款保证金余额
	other_margin_balance numeric NULL, -- 其他保证金余额
	lessee_margin_balance int4 NULL, -- 承租人保证金余额
	CONSTRAINT eg_margin_contract_balance_temp_pk PRIMARY KEY (contract_code, org_id, client_code)
);

-- Column comments

COMMENT ON COLUMN financialdb.eg_margin_contract_balance_temp.contract_code IS '合同编码';
COMMENT ON COLUMN financialdb.eg_margin_contract_balance_temp.org_id IS '签约主体';
COMMENT ON COLUMN financialdb.eg_margin_contract_balance_temp.client_code IS '客户编码';
COMMENT ON COLUMN financialdb.eg_margin_contract_balance_temp.business_code IS '业务编码';
COMMENT ON COLUMN financialdb.eg_margin_contract_balance_temp.contract_name IS '合同名称';
COMMENT ON COLUMN financialdb.eg_margin_contract_balance_temp.lease_date_start IS '起租开始时间';
COMMENT ON COLUMN financialdb.eg_margin_contract_balance_temp.lease_date_end IS '起租结束时间';
COMMENT ON COLUMN financialdb.eg_margin_contract_balance_temp.business_name IS '业务名称';
COMMENT ON COLUMN financialdb.eg_margin_contract_balance_temp.currency_type IS '币种';
COMMENT ON COLUMN financialdb.eg_margin_contract_balance_temp.client_name IS '客户名称';
COMMENT ON COLUMN financialdb.eg_margin_contract_balance_temp.supplier_margin_balance IS '供应商保证金余额';
COMMENT ON COLUMN financialdb.eg_margin_contract_balance_temp.payable_margin_balance IS '应付款保证金余额';
COMMENT ON COLUMN financialdb.eg_margin_contract_balance_temp.other_margin_balance IS '其他保证金余额';
COMMENT ON COLUMN financialdb.eg_margin_contract_balance_temp.lessee_margin_balance IS '承租人保证金余额';


