ALTER TABLE financialdb.eg_out_table_contract_detail ADD transfer_price numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.transfer_price IS '合同转让价格';

--凭证明细表新增字段判断是是否可以修改

ALTER TABLE eg_voucher_entry ADD edit_flag bpchar(1) NULL DEFAULT '0'::bpchar;
COMMENT ON COLUMN financialdb.eg_voucher_entry.edit_flag IS '是否可以编辑';


ALTER TABLE financialdb.eg_contract ADD business_category varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_contract.business_category IS '合同业务大类';