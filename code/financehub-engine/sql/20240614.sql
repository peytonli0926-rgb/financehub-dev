COMMENT ON COLUMN financialdb.eg_manual_voucher.bank_no IS '银行编码';

ALTER TABLE financialdb.eg_business_claim_repayment_record ADD is_cross_org varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.is_cross_org IS '是否存在跨主体(0;否;1:是),存在的情况下的跨主体金额';
