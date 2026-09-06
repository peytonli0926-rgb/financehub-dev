-- financialdb.eg_business_claim_repayment_record definition

-- Drop table

-- DROP TABLE financialdb.eg_business_claim_repayment_record;

CREATE TABLE financialdb.eg_business_claim_repayment_record (
	id int8 NOT NULL, -- ID
	system_code varchar(100) NULL,
	claim_amount numeric(20, 2) NULL, -- 认领金额
	create_by varchar(64) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(64) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	del_flag bpchar(1) NULL DEFAULT 0, -- 是否删除（0:否，1：是）
	client_code varchar(100) NULL, -- 客户编号
	contract_code varchar(100) NULL, -- 合同号
	currency_type varchar(100) NULL, -- 币种
	order_id varchar(100) NULL, -- 业务系统交易流水号
	business_date timestamp NULL, -- 业务日期(yyyy-MM-dd HH:mm:ss)
	ebank_serial_number varchar(60) NULL -- 网银流水号
);
COMMENT ON TABLE financialdb.eg_business_claim_repayment_record IS '业务系统对还款认领记录';

-- Column comments

COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.claim_amount IS '认领金额';
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.del_flag IS '是否删除（0:否，1：是）';
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.client_code IS '客户编号';
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.contract_code IS '合同号';
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.currency_type IS '币种';
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.order_id IS '业务系统交易流水号';
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.business_date IS '业务日期(yyyy-MM-dd HH:mm:ss)';
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.ebank_serial_number IS '网银流水号';


ALTER TABLE financialdb.eg_pay_vat ADD voucher_id varchar(2000) NULL;
COMMENT ON COLUMN financialdb.eg_pay_vat.voucher_id IS '凭证id,多个按照逗号分隔';
ALTER TABLE financialdb.eg_pay_vat ADD error_info varchar(2000) NULL;
COMMENT ON COLUMN financialdb.eg_pay_vat.error_info IS '生成凭证报错信息';
ALTER TABLE financialdb.eg_pay_vat ADD account_date timestamp NULL;
COMMENT ON COLUMN financialdb.eg_pay_vat.account_date IS '记账日期';