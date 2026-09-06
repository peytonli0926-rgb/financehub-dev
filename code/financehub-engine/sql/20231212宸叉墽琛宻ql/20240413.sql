CREATE TABLE financialdb.eg_non_confirm_collection_third_detail (
	id int8 NOT NULL,
	create_by varchar(64) NULL,
	create_time timestamp NULL,
	update_by varchar(64) NULL,
	update_time timestamp NULL,
	del_flag bpchar(1) NULL DEFAULT 0,
	collection_accounts_bank text NULL,
	org_id varchar(100) NULL,
	org_name varchar(200) NULL,
	scene_code varchar(50) NULL,
	scene_name varchar(200) NULL,
	business_ebank_number text NULL,
	ebank_serial_number varchar(60) NULL,
	system_code varchar(100) NULL,
	last_change_date timestamp NULL,
	business_date timestamp NULL,
	currency_type varchar(100) NULL,
	bank_amount numeric(20, 2) NULL DEFAULT 0,
	remain_non_confirm_amount numeric(20, 2) NULL,
	confirmed_amount numeric(20, 2) NULL,
	collection_accounts_bank_no text NULL,
	collection_accounts_bank_name text NULL,
	client_code varchar(100) NULL,
	client_name varchar(200) NULL,
	bank_summary varchar(1000) NULL,
	"comment" varchar(500) NULL,
	client_accounts_bank_no varchar(100) NULL,
	remark text NULL
);

-- Column comments

COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.del_flag IS '是否删除（0:否，1：是）';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.collection_accounts_bank IS '到账主体';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.org_id IS '组织机编码';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.org_name IS '签约主体名称';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.scene_code IS '场景编码';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.scene_name IS '场景名称';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.business_ebank_number IS '业务系统的网银编号';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.ebank_serial_number IS '网银流水号';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.last_change_date IS '最后修改日期';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.business_date IS '业务日期(yyyy-MM-dd HH:mm:ss)';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.currency_type IS '币种';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.bank_amount IS '网银到账金额';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.remain_non_confirm_amount IS '剩余未确认收款';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.confirmed_amount IS '已确认收款';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.collection_accounts_bank_no IS '到账银行账号';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.collection_accounts_bank_name IS '到账主体中文名称';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.client_code IS '客户编号';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.client_name IS '客户名称';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.bank_summary IS '银行交易摘要';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail."comment" IS '备注';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.client_accounts_bank_no IS '对方客户银行账号';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_third_detail.remark IS '备注';

CREATE INDEX eg_non_confirm_collection_third_detail_business_ebank_number_idx ON financialdb.eg_non_confirm_collection_third_detail (business_ebank_number,last_change_date);

ALTER TABLE financialdb.eg_hy_full_online_bank_batch_no_mapping ADD sum_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.sum_id IS '未确认收款汇总表id';

