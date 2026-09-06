ALTER TABLE eg_voucher ADD is_write_off bpchar(1) NULL DEFAULT 0;
COMMENT ON COLUMN eg_voucher.is_write_off IS '是否冲销（0：否，1：是）';
ALTER TABLE eg_voucher ADD manual_id int8 NULL;
COMMENT ON COLUMN eg_voucher.manual_id IS '手工凭证id';

ALTER TABLE eg_manual ADD source_id int8 NULL;
COMMENT ON COLUMN eg_manual.source_id IS '外部系统id';

CREATE TABLE eg_fund_business_system_ebank_amount_mapping (
	id int8 NOT NULL, -- ID
	match_number varchar(5000) NULL, -- 勾稽编号
	ebank_number varchar(5000) NULL, -- 资金系统网银编号
	ebank_serial_number text NULL, -- 业务系统网银编号/批次号(业务系统网银编号或批扣批次（扣款渠道批次号，对应恒运VC_PINGZZY 凭证摘要显示）)
	match_amount numeric(20, 2) NULL, -- 勾稽金额
	business_match_amount numeric(20, 2) null,
	create_by varchar(64) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(64) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	del_flag bpchar(1) NULL DEFAULT 0, -- 是否删除（0:否，1：是）
	CONSTRAINT eg_fund_business_system_ebank_amount_mapping_pk PRIMARY KEY (id)
);
CREATE INDEX eg_fund_business_system_ebank_amount_mapping_match_number_idx ON eg_fund_business_system_ebank_amount_mapping USING btree (match_number, ebank_number, id, ebank_serial_number);
COMMENT ON TABLE eg_fund_business_system_ebank_amount_mapping IS '资金系统、业务系统网银编号金额映射表';

-- Column comments

COMMENT ON COLUMN eg_fund_business_system_ebank_amount_mapping.id IS 'ID';
COMMENT ON COLUMN eg_fund_business_system_ebank_amount_mapping.match_number IS '勾稽编号';
COMMENT ON COLUMN eg_fund_business_system_ebank_amount_mapping.ebank_number IS '资金系统网银编号';
COMMENT ON COLUMN eg_fund_business_system_ebank_amount_mapping.ebank_serial_number IS '业务系统网银编号/批次号(业务系统网银编号或批扣批次（扣款渠道批次号，对应恒运VC_PINGZZY 凭证摘要显示）)';
COMMENT ON COLUMN eg_fund_business_system_ebank_amount_mapping.match_amount IS '勾稽金额';
COMMENT ON COLUMN eg_fund_business_system_ebank_amount_mapping.business_match_amount IS '业务勾稽金额';
COMMENT ON COLUMN eg_fund_business_system_ebank_amount_mapping.create_by IS '创建人';
COMMENT ON COLUMN eg_fund_business_system_ebank_amount_mapping.create_time IS '创建时间';
COMMENT ON COLUMN eg_fund_business_system_ebank_amount_mapping.update_by IS '更新人';
COMMENT ON COLUMN eg_fund_business_system_ebank_amount_mapping.update_time IS '更新时间';
COMMENT ON COLUMN eg_fund_business_system_ebank_amount_mapping.del_flag IS '是否删除（0:否，1：是）';

--以上dev,uat,prod已执行

CREATE INDEX eg_tail_difference_adjustment_detail_account_balance_idx ON financialdb4.eg_tail_difference_adjustment_detail (account_balance);
