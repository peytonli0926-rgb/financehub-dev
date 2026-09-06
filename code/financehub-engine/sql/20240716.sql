ALTER TABLE financialdb.eg_business_claim_repayment_record ADD voucher_ids varchar(1000) NULL;
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.voucher_ids IS '凭证id';

-- eg_fund_business_system_ebank_pc_amount definition

-- Drop table

-- DROP TABLE eg_fund_business_system_ebank_pc_amount;

CREATE TABLE eg_fund_business_system_ebank_pc_amount (
	id int8 NOT NULL, -- ID
	ebank_amount_id int8 NOT NULL, -- ebank_amount_id
	match_number varchar(100) NULL, -- 勾稽编号
	ebank_serial_number varchar(100) NULL, -- 业务系统网银编号/批次号(业务系统网银编号或批扣批次（扣款渠道批次号，对应恒运VC_PINGZZY 凭证摘要显示）)
	business_match_amount numeric(20, 2) NULL, -- 批次金额
	create_by varchar(64) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(64) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	del_flag bpchar(1) NULL DEFAULT 0, -- 是否删除（0:否，1：是）
	CONSTRAINT eg_fund_business_system_ebank_pc_amount_pk PRIMARY KEY (id)
);
CREATE INDEX eg_fund_business_system_ebank_pc_amount_ebank_amount_id_idx ON eg_fund_business_system_ebank_pc_amount USING btree (ebank_amount_id, match_number);
COMMENT ON TABLE eg_fund_business_system_ebank_pc_amount IS '资金系统、业务系统批次、金额';

-- Column comments

COMMENT ON COLUMN eg_fund_business_system_ebank_pc_amount.id IS 'ID';
COMMENT ON COLUMN eg_fund_business_system_ebank_pc_amount.ebank_amount_id IS 'ebank_amount_id';
COMMENT ON COLUMN eg_fund_business_system_ebank_pc_amount.match_number IS '勾稽编号';
COMMENT ON COLUMN eg_fund_business_system_ebank_pc_amount.ebank_serial_number IS '业务系统网银编号/批次号(业务系统网银编号或批扣批次（扣款渠道批次号，对应恒运VC_PINGZZY 凭证摘要显示）)';
COMMENT ON COLUMN eg_fund_business_system_ebank_pc_amount.business_match_amount IS '批次金额';
COMMENT ON COLUMN eg_fund_business_system_ebank_pc_amount.create_by IS '创建人';
COMMENT ON COLUMN eg_fund_business_system_ebank_pc_amount.create_time IS '创建时间';
COMMENT ON COLUMN eg_fund_business_system_ebank_pc_amount.update_by IS '更新人';
COMMENT ON COLUMN eg_fund_business_system_ebank_pc_amount.update_time IS '更新时间';
COMMENT ON COLUMN eg_fund_business_system_ebank_pc_amount.del_flag IS '是否删除（0:否，1：是）';


-- eg_fund_business_system_ebank_wy_amount definition

-- Drop table

-- DROP TABLE eg_fund_business_system_ebank_wy_amount;

CREATE TABLE eg_fund_business_system_ebank_wy_amount (
	id int8 NOT NULL, -- ID
	ebank_amount_id int8 NOT NULL, -- ebank_amount_id
	match_number varchar(5000) NULL, -- 勾稽编号
	ebank_number varchar(5000) NULL, -- 资金系统网银编号
	wy_amount numeric(20, 2) NULL, -- 网银金额
	create_by varchar(64) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(64) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	del_flag bpchar(1) NULL DEFAULT 0, -- 是否删除（0:否，1：是）
	CONSTRAINT eg_fund_business_system_ebank_wy_amount_pk PRIMARY KEY (id)
);
CREATE INDEX eg_fund_business_system_ebank_wy_amount_ebank_amount_id_idx ON eg_fund_business_system_ebank_wy_amount USING btree (ebank_amount_id);
COMMENT ON TABLE eg_fund_business_system_ebank_wy_amount IS '资金系统、业务系统网银编号金额';

-- Column comments

COMMENT ON COLUMN eg_fund_business_system_ebank_wy_amount.id IS 'ID';
COMMENT ON COLUMN eg_fund_business_system_ebank_wy_amount.ebank_amount_id IS 'ebank_amount_id';
COMMENT ON COLUMN eg_fund_business_system_ebank_wy_amount.match_number IS '勾稽编号';
COMMENT ON COLUMN eg_fund_business_system_ebank_wy_amount.ebank_number IS '资金系统网银编号';
COMMENT ON COLUMN eg_fund_business_system_ebank_wy_amount.wy_amount IS '网银金额';
COMMENT ON COLUMN eg_fund_business_system_ebank_wy_amount.create_by IS '创建人';
COMMENT ON COLUMN eg_fund_business_system_ebank_wy_amount.create_time IS '创建时间';
COMMENT ON COLUMN eg_fund_business_system_ebank_wy_amount.update_by IS '更新人';
COMMENT ON COLUMN eg_fund_business_system_ebank_wy_amount.update_time IS '更新时间';
COMMENT ON COLUMN eg_fund_business_system_ebank_wy_amount.del_flag IS '是否删除（0:否，1：是）';

ALTER TABLE financialdb4.eg_fund_business_system_ebank_amount_mapping ADD mq_message text NULL;
COMMENT ON COLUMN financialdb4.eg_fund_business_system_ebank_amount_mapping.mq_message IS 'mq原始数据';


ALTER TABLE eg_contract_balance_temp ADD receivable_other_collection_balance numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN eg_contract_balance_temp.receivable_other_collection_balance IS '其它应收款项余额';

ALTER TABLE eg_contract_balance_temp ADD receivable_other_collection_amount numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN eg_contract_balance_temp.receivable_other_collection_amount IS '其它应收款项发生额';
--以上sql uat dev，prod 已执行
