--已执行，20240226
--开票认领表新增字段
ALTER TABLE financialdb.eg_invoice_claim ADD process_status varchar(100) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_invoice_claim.process_status IS '处理状态（0：未认领，1：已认领）';
ALTER TABLE financialdb.eg_invoice_claim ADD manual_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_invoice_claim.manual_id IS '手工凭证ID';

--手工凭证表新加字段
ALTER TABLE financialdb.eg_manual ADD source_from varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual.source_from IS '数据来源（KJFP：开票认领）';