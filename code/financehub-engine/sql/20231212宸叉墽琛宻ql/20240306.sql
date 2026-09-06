--尾差调整新增字段
ALTER TABLE financialdb.eg_tail_difference_adjustment_detail ADD voucher_ids text NULL;
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.voucher_ids IS '凭证id,多个按照逗号分隔';