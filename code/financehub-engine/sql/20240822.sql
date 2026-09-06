ALTER TABLE financialdb4.eg_voucher ADD easbzcode varchar(200) NULL;
COMMENT ON COLUMN financialdb4.eg_voucher.easbzcode IS 'easbzcode';

ALTER TABLE financialdb4.eg_voucher ADD is_send_kingdee bpchar(1) NULL DEFAULT '0'::bpchar;
COMMENT ON COLUMN financialdb4.eg_voucher.is_send_kingdee IS '是否发送金蝶（0：未发送，1：已发送）该字段只针对不汇总分录数据';
