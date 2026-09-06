ALTER TABLE financialdb.eg_cost_channel_fee ADD org_id varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_cost_channel_fee.org_id IS '组织机编码';

ALTER TABLE financialdb.eg_contract_status_record ADD source_from_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_contract_status_record.source_from_id IS '模块id';
ALTER TABLE financialdb.eg_contract_status_record ADD source_from_type varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_contract_status_record.source_from_type IS '模块类型';