CREATE TABLE financialdb.eg_service_fee_init (
	id int8 NOT NULL,
	contract_code varchar(100) NULL,
	periods int4 NULL,
	apportion_amount numeric(20, 2) NULL,
	contract_code_m varchar(100) NULL
);
COMMENT ON TABLE financialdb.eg_service_fee_init IS '咨询服务费期初数据';

-- Column comments

COMMENT ON COLUMN financialdb.eg_service_fee_init.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_service_fee_init.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb.eg_service_fee_init.periods IS '本次分摊期数';
COMMENT ON COLUMN financialdb.eg_service_fee_init.apportion_amount IS '分摊金额';
COMMENT ON COLUMN financialdb.eg_service_fee_init.contract_code_m IS '主合同编号';
