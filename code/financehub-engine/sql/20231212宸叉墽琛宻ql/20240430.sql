ALTER TABLE financialdb.eg_check_common_data ADD period_code int4 NULL;
COMMENT ON COLUMN financialdb.eg_check_common_data.period_code IS '会计期间';

ALTER TABLE financialdb.eg_check_common_data_result ADD period_code int4 NULL;
COMMENT ON COLUMN financialdb.eg_check_common_data_result.period_code IS '会计期间';

ALTER TABLE financialdb.eg_check_common_finance_data ADD period_code int4 NULL;
COMMENT ON COLUMN financialdb.eg_check_common_finance_data.period_code IS '会计期间';

ALTER TABLE financialdb.eg_check_common_finance_data_result ADD period_code int4 NULL;
COMMENT ON COLUMN financialdb.eg_check_common_finance_data_result.period_code IS '会计期间';