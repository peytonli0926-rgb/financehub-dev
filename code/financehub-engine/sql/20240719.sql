---------------------------------已在prod执行
ALTER TABLE financialdb.eg_interface_data ADD is_extract_data varchar(1) NULL;
COMMENT ON COLUMN financialdb.eg_interface_data.is_extract_data IS '是否已经抽取到认领记录中';
ALTER TABLE financialdb.eg_interface_data ALTER COLUMN is_extract_data SET DEFAULT 0;
CREATE TABLE eg_voucher_entry_dimension_value (
	id int8 NOT NULL, -- ID
	org_id varchar(100) NULL, -- 组织机编码
	account_code varchar(100) NULL, -- 科目编码
	dim varchar(1000) NULL, -- 维度:用|分隔
	dim_value varchar(1000) NULL, -- 维度值:用|分隔
	source_system varchar(100) NULL -- 来源
);
COMMENT ON TABLE eg_voucher_entry_dimension_value IS '凭证维度和值对应表';

-- Column comments

COMMENT ON COLUMN eg_voucher_entry_dimension_value.id IS 'ID';
COMMENT ON COLUMN eg_voucher_entry_dimension_value.org_id IS '组织机编码';
COMMENT ON COLUMN eg_voucher_entry_dimension_value.account_code IS '科目编码';
COMMENT ON COLUMN eg_voucher_entry_dimension_value.dim IS '维度:用|分隔';
COMMENT ON COLUMN eg_voucher_entry_dimension_value.dim_value IS '维度值:用|分隔';
COMMENT ON COLUMN eg_voucher_entry_dimension_value.source_system IS '来源';
ALTER TABLE financialdb.eg_repayment_plan ALTER COLUMN service_fee_amortization_rate TYPE numeric(20, 6) USING service_fee_amortization_rate::numeric;
ALTER TABLE financialdb.eg_repayment_plan_temp ALTER COLUMN service_fee_amortization_rate TYPE numeric(20, 6) USING service_fee_amortization_rate::numeric;
ALTER TABLE financialdb.eg_repayment_plan_provision ALTER COLUMN service_fee_amortization_rate TYPE numeric(20, 6) USING service_fee_amortization_rate::numeric;

--------------------------------------已在PROD执行------------------------------------------------------------------