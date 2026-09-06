ALTER TABLE financialdb.eg_lease_income_details ADD ta_amount numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_lease_income_details.ta_amount IS '计提时ta金额';

ALTER TABLE financialdb.eg_lease_income_details ADD last_repayment_date timestamp(6) NULL;
COMMENT ON COLUMN financialdb.eg_lease_income_details.last_repayment_date IS '上次还款日';
ALTER TABLE financialdb.eg_lease_income_details ADD next_repayment_date timestamp(6) NULL;
COMMENT ON COLUMN financialdb.eg_lease_income_details.next_repayment_date IS '下次还款日';
--以上SQL 在uat,prod 已执行