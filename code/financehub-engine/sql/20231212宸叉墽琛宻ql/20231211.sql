--开票认领新增字段
--生产已执行 start
ALTER TABLE financialdb.eg_invoice_claim ADD seller_name varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_invoice_claim.seller_name IS '销方名称';
ALTER TABLE financialdb.eg_invoice_claim ADD business_source varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_invoice_claim.business_source IS '业务来源';
ALTER TABLE financialdb.eg_invoice_claim ADD "comments" varchar(1000) NULL;
COMMENT ON COLUMN financialdb.eg_invoice_claim."comments" IS '备注';
----生产已执行 end
