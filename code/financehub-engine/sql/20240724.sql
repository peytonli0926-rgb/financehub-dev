ALTER TABLE eg_margin_contract_balance_temp ADD bill_contract_code varchar(100) NOT NULL;
COMMENT ON COLUMN eg_margin_contract_balance_temp.bill_contract_code IS '借款合同编码';
ALTER TABLE eg_margin_contract_balance_temp DROP CONSTRAINT eg_margin_contract_balance_temp_pk;
ALTER TABLE eg_margin_contract_balance_temp ADD CONSTRAINT eg_margin_contract_balance_temp_pk PRIMARY KEY (contract_code,org_id,client_code,bill_contract_code);
--uat,dev,prod 已执行