--尾差调整新增字段
ALTER TABLE financialdb.eg_tail_difference_adjustment_detail ADD receivable_rent numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.receivable_rent IS '应收租金余额';
ALTER TABLE financialdb.eg_tail_difference_adjustment_detail ADD receivable_residual_value numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.receivable_residual_value IS '应收期末残值余额';
ALTER TABLE financialdb.eg_tail_difference_adjustment_detail ADD payable_device numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.payable_device IS '应付设备款余额';
ALTER TABLE financialdb.eg_tail_difference_adjustment_detail ADD payable_other numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.payable_other IS '应付其他款项';
ALTER TABLE financialdb.eg_tail_difference_adjustment_detail ADD contract_status varchar(64) NULL;
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.contract_status IS '合同状态';
ALTER TABLE financialdb.eg_tail_difference_adjustment_detail ADD rental_income_after_total numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.rental_income_after_total IS '未实现融资收益-待摊收益';
ALTER TABLE financialdb.eg_tail_difference_adjustment_detail ADD rental_income_after_lease_total numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.rental_income_after_lease_total IS '未实现融资租赁收益-待摊收益';
ALTER TABLE financialdb.eg_voucher_to_eas_result ADD success_flag varchar(20) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_result.success_flag IS '是否成功标识';

ALTER TABLE financialdb.eg_tail_difference_adjustment_detail ADD overdue_days int4 NULL;
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.overdue_days IS '逾期天数';

ALTER TABLE financialdb.eg_tail_difference_adjustment_detail ADD client_code varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.client_code IS '客户编号';

ALTER TABLE financialdb.eg_tail_difference_adjustment_detail ADD error_info text NULL;
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.error_info IS '报错信息';
