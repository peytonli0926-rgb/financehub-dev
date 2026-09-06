--合同表新增字段
ALTER TABLE financialdb.eg_contract ADD business_date timestamp NULL;
COMMENT ON COLUMN financialdb.eg_contract.business_date IS '业务日期';
