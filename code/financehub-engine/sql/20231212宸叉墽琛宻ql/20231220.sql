ALTER TABLE financialdb.eg_interface_data ADD bank_org_id varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_interface_data.bank_org_id IS '银行账号签约主体';
ALTER TABLE financialdb.eg_interface_data ADD cross_contract_flag varchar(50) NULL;
COMMENT ON COLUMN financialdb.eg_interface_data.cross_contract_flag IS '跨合同抵扣保证金标识(0:应收,1:保证金)';