
CREATE TABLE financialdb.eg_contract_status_record_temp (
	contract_code varchar(100) NULL, -- 合同编码
	org_id varchar(200) NULL, -- 签约主体
	financial_contract_status varchar(100) NULL, -- 状态
	financial_contract_status_update_time date NULL -- 更新时间
);
CREATE INDEX eg_contract_status_record_temp_contract_code_idx ON financialdb.eg_contract_status_record_temp USING btree (contract_code);
COMMENT ON TABLE financialdb.eg_contract_status_record_temp IS '合同状态记录临时表';

-- Column comments

COMMENT ON COLUMN financialdb.eg_contract_status_record_temp.contract_code IS '合同编码';
COMMENT ON COLUMN financialdb.eg_contract_status_record_temp.org_id IS '签约主体';
COMMENT ON COLUMN financialdb.eg_contract_status_record_temp.financial_contract_status IS '状态';
COMMENT ON COLUMN financialdb.eg_contract_status_record_temp.financial_contract_status_update_time IS '更新时间';

ALTER TABLE financialdb.eg_contract ADD is_change_repayment varchar(1) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_contract.is_change_repayment IS '是否做过偿还计划变更(0:否, 1:是)';
ALTER TABLE eg_voucher ADD eas_voucher_number varchar(50) NULL;
COMMENT ON COLUMN eg_voucher.eas_voucher_number IS '金蝶凭证号';
--以上prod 已执行