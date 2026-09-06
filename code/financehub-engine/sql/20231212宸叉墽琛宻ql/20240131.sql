
--凭证表新增字段
ALTER TABLE financialdb.eg_voucher ADD valid_flag varchar(10) NULL;
COMMENT ON COLUMN financialdb.eg_voucher.valid_flag IS '有效标识: 1:有效 2:凭证行为空 3:借贷金额未平';
