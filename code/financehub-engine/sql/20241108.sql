ALTER TABLE financialdb4.eg_contract_status_record_temp ADD transfer_org_id varchar(100) NULL;
COMMENT ON COLUMN financialdb4.eg_contract_status_record_temp.transfer_org_id IS '转入公司';
ALTER TABLE financialdb4.eg_contract_status_record_temp ADD transfer_contract_code varchar(100) NULL;
COMMENT ON COLUMN financialdb4.eg_contract_status_record_temp.transfer_contract_code IS '转入合同号';
ALTER TABLE financialdb4.eg_contract_status_record_temp ADD transfer_contract_status varchar(50) NULL;
COMMENT ON COLUMN financialdb4.eg_contract_status_record_temp.transfer_contract_status IS '转入合同系统合同状态';
