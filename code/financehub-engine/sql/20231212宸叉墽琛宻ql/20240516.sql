ALTER TABLE financialdb.eg_repayment_plan_temp ADD source_irr_rate numeric(20, 6) NULL;
COMMENT ON COLUMN financialdb.eg_repayment_plan_temp.source_irr_rate IS '源IRR RATE';


ALTER TABLE financialdb.eg_impairment_provision_detail ADD target_amount numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.target_amount IS '目标币种本月计提金额';
ALTER TABLE financialdb.eg_impairment_provision_detail ADD exchange_rate numeric(20, 10) NULL;
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.exchange_rate IS '汇率';

--以上sql已执行prod
