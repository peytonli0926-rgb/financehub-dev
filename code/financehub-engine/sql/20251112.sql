ALTER TABLE financialdb3.eg_contract ADD receivable_vendor_procedure_amount numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb3.eg_contract.receivable_vendor_procedure_amount IS '供应商手续费';

ALTER TABLE financialdb3.eg_contract_month ADD receivable_vendor_procedure_amount numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb3.eg_contract_month.receivable_vendor_procedure_amount IS '供应商手续费';


COMMENT ON COLUMN financialdb3.eg_lease_income_details.paid_interest IS '实收利息(不含税)';
COMMENT ON COLUMN financialdb3.eg_lease_income_details.paid_handling_fees IS '实收手续费(不含税)';
COMMENT ON COLUMN financialdb3.eg_lease_income_details.other_income IS '其它收入(不含税)';

ALTER TABLE financialdb3.eg_lease_income_details ADD cash_change numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb3.eg_lease_income_details.cash_change IS '现金流变化';


CREATE TABLE financialdb3.eg_lease_income_upload_record (
	id int8 NOT NULL,
	business_date date NULL,
	contract_code varchar(100) NULL,
	accrued varchar(10) NULL,
	labor_overdue_mark varchar(10) NULL,
	income_provision_method varchar(20) NULL,
	"comment" varchar(2000) NULL,
	previous_paid_period date NULL
);
COMMENT ON TABLE financialdb3.eg_lease_income_upload_record IS '收益计提上传记录';

-- Column comments

COMMENT ON COLUMN financialdb3.eg_lease_income_upload_record.business_date IS '计提月份';
COMMENT ON COLUMN financialdb3.eg_lease_income_upload_record.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb3.eg_lease_income_upload_record.accrued IS '是否计提';
COMMENT ON COLUMN financialdb3.eg_lease_income_upload_record.labor_overdue_mark IS '是否逾期';
COMMENT ON COLUMN financialdb3.eg_lease_income_upload_record.income_provision_method IS '计提方式(XIRR分摊收益/实收/IRR分摊收益)';
COMMENT ON COLUMN financialdb3.eg_lease_income_upload_record."comment" IS '备注';
COMMENT ON COLUMN financialdb3.eg_lease_income_upload_record.previous_paid_period IS '上期实收期间';

ALTER TABLE financialdb3.eg_lease_income_upload_record ADD del_flag bpchar(1) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb3.eg_lease_income_upload_record.del_flag IS '是否删除 0：未删除1：已删除';
ALTER TABLE financialdb3.eg_lease_income_upload_record ADD create_by varchar(32) NULL;
COMMENT ON COLUMN financialdb3.eg_lease_income_upload_record.create_by IS '创建人';
ALTER TABLE financialdb3.eg_lease_income_upload_record ADD create_time timestamp(6) NULL;
COMMENT ON COLUMN financialdb3.eg_lease_income_upload_record.create_time IS '创建时间';
ALTER TABLE financialdb3.eg_lease_income_upload_record ADD update_by varchar(32) NULL;
COMMENT ON COLUMN financialdb3.eg_lease_income_upload_record.update_by IS '更新人';
ALTER TABLE financialdb3.eg_lease_income_upload_record ADD update_time timestamp(6) NULL;
COMMENT ON COLUMN financialdb3.eg_lease_income_upload_record.update_time IS '更新时间';

-- financialdb3.eg_special_contract_overdue_record definition

-- Drop table

-- DROP TABLE financialdb3.eg_special_contract_overdue_record;

CREATE TABLE financialdb3.eg_special_contract_overdue_record (
	id int8 NOT NULL,
	business_date date NULL, -- 计提月份
	contract_code varchar(100) NULL, -- 合同编号
	upload_periods varchar(10) NULL, -- 上传账期
	labor_overdue_mark varchar(10) NULL, -- 是否逾期
	previous_paid_period date NULL, -- 上期实收期间
	del_flag bpchar(1) NULL DEFAULT 0, -- 是否删除 0：未删除1：已删除
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp(6) NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp(6) NULL -- 更新时间
);
COMMENT ON TABLE financialdb3.eg_special_contract_overdue_record IS '特殊合同逾期上传记录';

-- Column comments

COMMENT ON COLUMN financialdb3.eg_special_contract_overdue_record.business_date IS '计提月份';
COMMENT ON COLUMN financialdb3.eg_special_contract_overdue_record.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb3.eg_special_contract_overdue_record.upload_periods IS '上传账期';
COMMENT ON COLUMN financialdb3.eg_special_contract_overdue_record.labor_overdue_mark IS '是否逾期';
COMMENT ON COLUMN financialdb3.eg_special_contract_overdue_record.previous_paid_period IS '上期实收期间';
COMMENT ON COLUMN financialdb3.eg_special_contract_overdue_record.del_flag IS '是否删除 0：未删除1：已删除';
COMMENT ON COLUMN financialdb3.eg_special_contract_overdue_record.create_by IS '创建人';
COMMENT ON COLUMN financialdb3.eg_special_contract_overdue_record.create_time IS '创建时间';
COMMENT ON COLUMN financialdb3.eg_special_contract_overdue_record.update_by IS '更新人';
COMMENT ON COLUMN financialdb3.eg_special_contract_overdue_record.update_time IS '更新时间';