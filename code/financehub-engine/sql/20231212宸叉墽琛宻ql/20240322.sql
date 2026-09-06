-- financialdb.eg_non_confirm_collection_sum definition

-- Drop table

-- DROP TABLE financialdb.eg_non_confirm_collection_sum;

CREATE TABLE financialdb.eg_non_confirm_collection_sum (
	id int8 NOT NULL, -- ID
	ebank_number text NULL, -- 网银编号-资金系统
	org_id text NULL, -- 认领主体
	collection_accounts_bank text NULL, -- 到账主体
	collection_accounts_bank_no text NULL, -- 到账银行账号
	ebank_serial_number text NULL, -- 业务系统网银编号/批次号(业务系统网银编号或批扣批次（扣款渠道批次号，对应恒运VC_PINGZZY 凭证摘要显示）)
	create_by varchar(64) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(64) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	del_flag bpchar(1) NULL DEFAULT 0, -- 是否删除（0:否，1：是）
	org_name text NULL, -- 认领主体中文名称
	collection_accounts_bank_name text NULL, -- 到账主体中文名称
	business_ebank_number text NULL, -- 业务系统的网银编号
	ebank_mapping_id int8 NULL, -- 资金系统、业务系统网银编号映射表ID
	bank_amount numeric(20, 2) NULL -- 网银到账金额
);
COMMENT ON TABLE financialdb.eg_non_confirm_collection_sum IS '未确认收款汇总表';

-- Column comments

COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.ebank_number IS '网银编号-资金系统';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.org_id IS '认领主体';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.collection_accounts_bank IS '到账主体';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.collection_accounts_bank_no IS '到账银行账号';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.ebank_serial_number IS '业务系统网银编号/批次号(业务系统网银编号或批扣批次（扣款渠道批次号，对应恒运VC_PINGZZY 凭证摘要显示）)';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.del_flag IS '是否删除（0:否，1：是）';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.org_name IS '认领主体中文名称';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.collection_accounts_bank_name IS '到账主体中文名称';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.business_ebank_number IS '业务系统的网银编号';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.ebank_mapping_id IS '资金系统、业务系统网银编号映射表ID';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.bank_amount IS '网银到账金额';

ALTER TABLE financialdb.eg_non_confirm_collection_sum ADD accounts_receivable numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.accounts_receivable IS '应批扣金额';


-- financialdb.eg_non_confirm_collection_second_detail definition

-- Drop table

-- DROP TABLE financialdb.eg_non_confirm_collection_second_detail;

CREATE TABLE financialdb.eg_non_confirm_collection_second_detail (
	id int8 NOT NULL, -- ID
	sum_id int8 NOT NULL, -- 未确认收款汇总表id
	system_code varchar(100) NULL, -- 业务系统
	system_name varchar(100) NULL, -- 业务系统名称
	business_date timestamp NULL, -- 网银到账日期
	business_happen_date timestamp NULL, -- 网银确认日期
	currency_type varchar(100) NULL, -- 币种
	bank_amount numeric(20, 2) NULL, -- 网银到账金额
	remain_non_confirm_amount numeric(20, 2) NULL, -- 剩余未确认金额
	confirm_amount numeric(20, 2) NULL -- 已确认金额
);
COMMENT ON TABLE financialdb.eg_non_confirm_collection_second_detail IS '未确认收款明细表(第二层明细)';

-- Column comments

COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.sum_id IS '未确认收款汇总表id';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.system_code IS '业务系统';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.system_name IS '业务系统名称';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.business_date IS '网银到账日期';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.business_happen_date IS '网银确认日期';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.currency_type IS '币种';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.bank_amount IS '网银到账金额';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.remain_non_confirm_amount IS '剩余未确认金额';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.confirm_amount IS '已确认金额';



ALTER TABLE financialdb.eg_receive_tax RENAME COLUMN org_id TO seller_name;
ALTER TABLE financialdb.eg_receive_tax RENAME COLUMN invoice_accrual_name TO product_name;
