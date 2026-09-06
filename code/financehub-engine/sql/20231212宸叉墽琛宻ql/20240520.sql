ALTER TABLE financialdb.eg_rent_register_detail ADD rent_register_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_rent_register_detail.rent_register_id IS '出租登记id';
ALTER TABLE financialdb.eg_rent_register ADD version_num int4 DEFAULT 1 NULL;
COMMENT ON COLUMN financialdb.eg_rent_register.version_num IS '版本号';

ALTER TABLE financialdb.eg_long_receivable_register ADD version_num int4 DEFAULT 1 NULL;
COMMENT ON COLUMN financialdb.eg_long_receivable_register.version_num IS '版本号';
ALTER TABLE financialdb.eg_long_repayment_plan ADD long_register_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_long_repayment_plan.long_register_id IS '长期应收款id';
ALTER TABLE financialdb.eg_long_apportion ADD long_register_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_long_apportion.long_register_id IS '长期应收款id';

ALTER TABLE financialdb.eg_repayment_plan_exceldata ADD ebank_serial_number varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_repayment_plan_exceldata.ebank_serial_number IS '网银编号';
ALTER TABLE financialdb.eg_repayment_plan_exceldata ADD settlement_way varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_repayment_plan_exceldata.settlement_way IS '结算方式';
