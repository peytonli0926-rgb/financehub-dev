--资金系统交易数据添加字段
ALTER TABLE financialdb.eg_fund_ebank_transaction_data ADD bank_summary varchar(1000) NULL;
COMMENT ON COLUMN financialdb.eg_fund_ebank_transaction_data.bank_summary IS '银行交易摘要';
--网银编号映射表修改字段类型和长度 已执行
-- ALTER TABLE financialdb.eg_fund_business_system_ebank_mapping ALTER COLUMN ebank_serial_number TYPE text USING ebank_serial_number::text;
-- ALTER TABLE financialdb.eg_fund_business_system_ebank_mapping ALTER COLUMN match_number TYPE varchar(5000) USING match_number::varchar;
-- ALTER TABLE financialdb.eg_fund_business_system_ebank_mapping ALTER COLUMN ebank_number TYPE varchar(5000) USING ebank_number::varchar;
--合同表新增字段
ALTER TABLE financialdb.eg_contract ADD payable_recycle_car_amount numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_contract.payable_recycle_car_amount IS '收车费汇总总额';

-- 合同表新增手环成本字段
alter table eg_contract
	add payable_bracelet_cost numeric(20,2);

comment on column eg_contract.payable_bracelet_cost is '手环成本';

-- 偿还计划新增版本号
alter table eg_repayment_plan_his
	add version int4;
comment on column eg_repayment_plan_his.version is '版本号';

