CREATE TABLE financialdb.eg_non_confirm_collection_voucher_record (
	id int8 NOT NULL,
	detail_id int8 NULL,
	org_id varchar(100) NULL,
	cr_or_dt varchar(5) NULL,
	account_number varchar(50) NULL,
	voucher_comments varchar(2000) NULL,
	amount numeric(20, 2) NULL,
	client_code varchar(50) NULL,
	contract_code varchar(50) NULL,
	create_by varchar(64) NULL,
	create_time timestamp NULL,
	update_by varchar(64) NULL,
	update_time timestamp NULL,
	del_flag bpchar(1) NULL DEFAULT 0
);
COMMENT ON TABLE financialdb.eg_non_confirm_collection_voucher_record IS '未确认收款凭证记录';

-- Column comments

COMMENT ON COLUMN financialdb.eg_non_confirm_collection_voucher_record.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_voucher_record.detail_id IS '未确认收款明细表Id';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_voucher_record.org_id IS '做账主体';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_voucher_record.cr_or_dt IS '借贷方向';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_voucher_record.account_number IS '科目编码';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_voucher_record.voucher_comments IS '凭证摘要';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_voucher_record.amount IS '金额';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_voucher_record.client_code IS '客户编码';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_voucher_record.contract_code IS '合同编码';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_voucher_record.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_voucher_record.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_voucher_record.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_voucher_record.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_voucher_record.del_flag IS '是否删除（0:否，1：是）';

ALTER TABLE financialdb.eg_non_confirm_collection_voucher_record ADD org_name varchar(200) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_voucher_record.org_name IS '做账主体名称';
ALTER TABLE financialdb.eg_non_confirm_collection_voucher_record ADD account_name varchar(200) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_voucher_record.account_name IS '科目名称';

ALTER TABLE financialdb.eg_lease_income ADD system_code varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_lease_income.system_code IS '来源系统';
