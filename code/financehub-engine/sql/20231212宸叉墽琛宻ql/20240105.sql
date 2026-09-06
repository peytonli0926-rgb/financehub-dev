--开票认领修改字段备注
COMMENT ON COLUMN financialdb.eg_invoice_claim.business_source IS '业务来源(1：租赁 2：小微 3:商用车 4：乘用车 5：现代物流 6：运通宝 7:长江联合 8:供应链保理)';
--成本表新增字段
ALTER TABLE financialdb.eg_cost_channel_fee ADD is_contract_status bpchar(1) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_cost_channel_fee.is_contract_status IS '是否区分合同状态(0：否，1：是)';
ALTER TABLE financialdb.eg_cost_channel_fee ADD process_status varchar(64) NULL DEFAULT 1;
COMMENT ON COLUMN financialdb.eg_cost_channel_fee.process_status IS '处理状态(1:已录入，2：已提交，3：已复核4：已传至金蝶)';
--开票认领新增字段
ALTER TABLE financialdb.eg_invoice_claim ADD error_message text NULL;
COMMENT ON COLUMN financialdb.eg_invoice_claim.error_message IS '生成凭证报错原因';
COMMENT ON COLUMN financialdb.eg_invoice_claim.source_from IS '数据来源（0：纸质发票，1：电子发票，2：MQ）';
ALTER TABLE financialdb.eg_invoice_claim ADD org_id varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_invoice_claim.org_id IS '签约主体';
