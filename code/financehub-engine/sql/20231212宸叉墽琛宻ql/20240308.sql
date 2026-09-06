ALTER TABLE financialdb.eg_tail_difference_adjustment ADD approve_error_info text NULL;
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment.approve_error_info IS '审批报错信息';


ALTER TABLE financialdb.eg_tail_difference_adjustment ADD is_generate_voucher bpchar(1) NULL DEFAULT '0'::bpchar;
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment.is_generate_voucher IS '是否已生成凭证（0：未生成1：已生成）默认0';


ALTER TABLE financialdb.eg_tail_difference_adjustment_detail ADD business_code varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.business_code IS '业务编码';