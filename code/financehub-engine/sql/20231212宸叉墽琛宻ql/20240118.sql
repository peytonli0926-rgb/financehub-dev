--手工表新增字段
ALTER TABLE financialdb.eg_manual ADD voucher_type varchar(100) NULL;
ALTER TABLE financialdb.eg_manual ADD voucher_num int8 NULL;
COMMENT ON COLUMN financialdb.eg_manual.voucher_num IS '凭证号';
ALTER TABLE financialdb.eg_manual ADD voucher_summary varchar(500) NULL;
COMMENT ON COLUMN financialdb.eg_manual.voucher_summary IS '摘要内容';
ALTER TABLE financialdb.eg_manual ADD scene_code varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual.scene_code IS '场景编码';
ALTER TABLE financialdb.eg_manual ADD scene_name varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual.scene_name IS '场景名称';
ALTER TABLE financialdb.eg_manual ADD sub_scene_type varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual.sub_scene_type IS '细分场景';
ALTER TABLE financialdb.eg_manual ADD attachment_num int NULL;
COMMENT ON COLUMN financialdb.eg_manual.attachment_num IS '附件数量';
ALTER TABLE financialdb.eg_manual ADD rate numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_manual.rate IS '汇率';
ALTER TABLE financialdb.eg_manual ADD business_code varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual.business_code IS '业务编码';
ALTER TABLE financialdb.eg_manual ADD business_name varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual.business_name IS '业务名称';
COMMENT ON COLUMN financialdb.eg_manual.voucher_type IS '凭证类型';


ALTER TABLE financialdb.eg_manual ADD error_info varchar(1000) NULL;
COMMENT ON COLUMN financialdb.eg_manual.error_info IS '审批报错信息';


--科目表新增字段
ALTER TABLE financialdb.eg_account ADD assist_flags varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_account.assist_flags IS '凭证行维度';
ALTER TABLE financialdb.eg_account ADD settlement_type varchar(30) NULL;
COMMENT ON COLUMN financialdb.eg_account.settlement_type IS '核算类型';
