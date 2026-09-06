--已执行，20240226-
-核销详情表新增字段记录生成凭证报错信息
ALTER TABLE financialdb.eg_verification_details ADD error_info varchar(2000) NULL;
COMMENT ON COLUMN financialdb.eg_verification_details.error_info IS '生成凭证报错信息';

--合同表金额字段默认值为0
ALTER TABLE financialdb.eg_contract ALTER COLUMN payable_inner_expense SET DEFAULT 0;
ALTER TABLE financialdb.eg_contract ALTER COLUMN receivable_procedure_amount SET DEFAULT 0;
ALTER TABLE financialdb.eg_contract ALTER COLUMN lessor_other_costs SET DEFAULT 0;
ALTER TABLE financialdb.eg_contract ALTER COLUMN payable_other_amount SET DEFAULT 0;
ALTER TABLE financialdb.eg_contract ALTER COLUMN estimate_g_p_s_expense SET DEFAULT 0;
ALTER TABLE financialdb.eg_contract ALTER COLUMN payable_introduce SET DEFAULT 0;
ALTER TABLE financialdb.eg_contract ALTER COLUMN payable_law_amount SET DEFAULT 0;
ALTER TABLE financialdb.eg_contract ALTER COLUMN receivable_firm_rebate SET DEFAULT 0;
ALTER TABLE financialdb.eg_contract ALTER COLUMN receivable_insurance_amount SET DEFAULT 0;
ALTER TABLE financialdb.eg_contract ALTER COLUMN retained_price SET DEFAULT 0;
ALTER TABLE financialdb.eg_contract ALTER COLUMN receivable_other SET DEFAULT 0;
ALTER TABLE financialdb.eg_contract ALTER COLUMN receivable_service_amount SET DEFAULT 0;
ALTER TABLE financialdb.eg_contract ALTER COLUMN vendor_margin_amount SET DEFAULT 0;
ALTER TABLE financialdb.eg_contract ALTER COLUMN outtax_balance SET DEFAULT 0;
ALTER TABLE financialdb.eg_contract ALTER COLUMN lease_revenue_balance SET DEFAULT 0;
ALTER TABLE financialdb.eg_contract ALTER COLUMN depreciation_loss_balance SET DEFAULT 0;
ALTER TABLE financialdb.eg_contract ALTER COLUMN payable_procedure_cost SET DEFAULT 0;
ALTER TABLE financialdb.eg_contract ALTER COLUMN gps_unit_price SET DEFAULT 0;
ALTER TABLE financialdb.eg_contract ALTER COLUMN payable_service SET DEFAULT 0;
ALTER TABLE financialdb.eg_contract ALTER COLUMN payable_bracelet_cost SET DEFAULT 0;
ALTER TABLE financialdb.eg_contract ALTER COLUMN contract_amount SET DEFAULT 0;
ALTER TABLE financialdb.eg_contract ALTER COLUMN lease_interest_rate_year SET DEFAULT 0;
ALTER TABLE financialdb.eg_contract ALTER COLUMN tax_rate SET DEFAULT 0;