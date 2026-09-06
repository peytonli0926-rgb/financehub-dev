CREATE INDEX eg_payable_insurance_details_business_date_idx ON financialdb3.eg_payable_insurance_details (business_date);
CREATE TABLE financialdb4.eg_assets_expense (
	id int8 NOT NULL,
	create_by varchar(32) NULL,
	create_time timestamp NULL,
	update_by varchar(32) NULL,
	update_time timestamp NULL,
	del_flag bpchar(1) NULL DEFAULT '0'::bpchar,
	expense_type varchar(50) NULL,
	supplier varchar NULL,
	contract_code varchar(100) NULL,
	contract_org_id varchar(100) NULL,
	expense_period varchar(20) NULL,
	assets_transfer_flag varchar(10) NULL,
	brief_and_execution_fee numeric(20, 2) NULL,
	basic_counsel_fee numeric(20, 2) NULL,
	risk_counsel_service_fee numeric(20, 2) NULL,
	received_amount numeric(20, 2) NULL,
	entrust_subject_matter_amount numeric(20, 2) NULL,
	debt_to_batch_no int4 NULL,
	service_fee_rate numeric(20, 5) NULL,
	service_fee_amount numeric(20, 2) NULL,
	overdue_days int4 NULL,
	account_age int4 NULL,
	fee_rate_version varchar(10) NULL,
	haul_distance numeric(20, 5) NULL,
	haul_amount numeric(20, 2) NULL,
	maintenance_amount numeric(20, 2) NULL,
	expense_total_amount numeric(20, 2) NULL,
	storage_days int4 NULL,
	storage_serivce_fee numeric(20, 2) NULL,
	assess_amount numeric(20, 2) NULL
);

-- Column comments

COMMENT ON COLUMN financialdb4.eg_assets_expense.id IS 'ID';
COMMENT ON COLUMN financialdb4.eg_assets_expense.create_by IS '创建人';
COMMENT ON COLUMN financialdb4.eg_assets_expense.create_time IS '创建时间';
COMMENT ON COLUMN financialdb4.eg_assets_expense.update_by IS '更新人';
COMMENT ON COLUMN financialdb4.eg_assets_expense.update_time IS '更新时间';
COMMENT ON COLUMN financialdb4.eg_assets_expense.del_flag IS '删除标识(0:未删除,1:已删除)';
COMMENT ON COLUMN financialdb4.eg_assets_expense.expense_type IS '费用类型';
COMMENT ON COLUMN financialdb4.eg_assets_expense.supplier IS '供应商';
COMMENT ON COLUMN financialdb4.eg_assets_expense.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb4.eg_assets_expense.contract_org_id IS '合同主体';
COMMENT ON COLUMN financialdb4.eg_assets_expense.expense_period IS '费用所属期';
COMMENT ON COLUMN financialdb4.eg_assets_expense.assets_transfer_flag IS '资产转让标识';
COMMENT ON COLUMN financialdb4.eg_assets_expense.brief_and_execution_fee IS '诉讼费/执行费金额';
COMMENT ON COLUMN financialdb4.eg_assets_expense.basic_counsel_fee IS '基础律师费';
COMMENT ON COLUMN financialdb4.eg_assets_expense.risk_counsel_service_fee IS '风险律师服务费';
COMMENT ON COLUMN financialdb4.eg_assets_expense.received_amount IS '回款金额';
COMMENT ON COLUMN financialdb4.eg_assets_expense.entrust_subject_matter_amount IS '委案标的金额';
COMMENT ON COLUMN financialdb4.eg_assets_expense.debt_to_batch_no IS '债转批次';
COMMENT ON COLUMN financialdb4.eg_assets_expense.service_fee_rate IS '服务费费率';
COMMENT ON COLUMN financialdb4.eg_assets_expense.overdue_days IS '逾期天数';
COMMENT ON COLUMN financialdb4.eg_assets_expense.account_age IS '帐龄';
COMMENT ON COLUMN financialdb4.eg_assets_expense.fee_rate_version IS '费率版本';
COMMENT ON COLUMN financialdb4.eg_assets_expense.haul_distance IS '运输距离';
COMMENT ON COLUMN financialdb4.eg_assets_expense.haul_amount IS '运输费用';
COMMENT ON COLUMN financialdb4.eg_assets_expense.maintenance_amount IS '整备费用';
COMMENT ON COLUMN financialdb4.eg_assets_expense.expense_total_amount IS '费用合计';
COMMENT ON COLUMN financialdb4.eg_assets_expense.storage_days IS '保管天数';
COMMENT ON COLUMN financialdb4.eg_assets_expense.storage_serivce_fee IS '仓储保管服务费';
COMMENT ON COLUMN financialdb4.eg_assets_expense.assess_amount IS '评估费金额';

ALTER TABLE financialdb4.eg_non_confirm_collection_account_checking ALTER COLUMN financial_primary_classic SET DEFAULT NULL;
ALTER TABLE financialdb4.eg_non_confirm_collection_account_checking ALTER COLUMN confirm_account_property SET DEFAULT NULL;

--以上dev,uat，prod 已执行