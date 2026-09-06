--手工凭证
-- financialdb.eg_manual_voucher definition

-- Drop table

-- DROP TABLE financialdb.eg_manual_voucher;

CREATE TABLE financialdb.eg_manual_voucher (
                                               id int8 NOT NULL, -- ID
                                               contract_code varchar(100) NULL, -- 合同编码
                                               contract_name varchar(100) NULL, -- 合同名称
                                               client_code varchar(100) NULL, -- 客户编码
                                               client_name varchar(100) NULL, -- 客户名称
                                               org_id varchar(100) NULL, -- 签约主体
                                               period_code int4 NULL, -- 会计期间（yyyyMM）
                                               business_date timestamp NULL, -- 业务日期
                                               voucher_date timestamp NULL, -- 记账日期(财务日期)
                                               voucher_type varchar(100) NULL, -- 凭证类型
                                               voucher_summary varchar(500) NULL, -- 摘要内容
                                               scene_code varchar(100) NULL, -- 业务场景编码
                                               account_code varchar(100) NULL, -- 科目编码
                                               account_name varchar(100) NULL, -- 科目名称
                                               currency_code varchar(64) NULL, -- 币种编码
                                               rate numeric(20, 2) NULL, -- 汇率
                                               debit_amount numeric(20, 2) NULL, -- 借方发生额
                                               credit_amount numeric(20, 2) NULL, -- 贷方发生额
                                               is_cash_flow bpchar(1) NULL DEFAULT 0, -- 是否有现金流量（0：否，1：是）
                                               cash_flow_marker varchar(100) NULL, -- 现金流量标记
                                               subsidiary_account varchar(100) NULL, -- 辅助帐摘要
                                               create_by varchar(64) NULL, -- 创建人
                                               create_time timestamp NULL, -- 创建时间
                                               update_by varchar(64) NULL, -- 更新人
                                               update_time timestamp NULL, -- 更新时间
                                               del_flag bpchar(1) NULL DEFAULT 0, -- 是否删除（0：否，1：是）
                                               manual_id int8 NULL, -- 手工Id
                                               CONSTRAINT eg_manual_voucher_pk PRIMARY KEY (id)
);
COMMENT ON TABLE financialdb.eg_manual_voucher IS '手工凭证表';

-- Column comments

COMMENT ON COLUMN financialdb.eg_manual_voucher.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_manual_voucher.contract_code IS '合同编码';
COMMENT ON COLUMN financialdb.eg_manual_voucher.contract_name IS '合同名称';
COMMENT ON COLUMN financialdb.eg_manual_voucher.client_code IS '客户编码';
COMMENT ON COLUMN financialdb.eg_manual_voucher.client_name IS '客户名称';
COMMENT ON COLUMN financialdb.eg_manual_voucher.org_id IS '签约主体';
COMMENT ON COLUMN financialdb.eg_manual_voucher.period_code IS '会计期间（yyyyMM）';
COMMENT ON COLUMN financialdb.eg_manual_voucher.business_date IS '业务日期';
COMMENT ON COLUMN financialdb.eg_manual_voucher.voucher_date IS '记账日期(财务日期)';
COMMENT ON COLUMN financialdb.eg_manual_voucher.voucher_type IS '凭证类型';
COMMENT ON COLUMN financialdb.eg_manual_voucher.voucher_summary IS '摘要内容';
COMMENT ON COLUMN financialdb.eg_manual_voucher.scene_code IS '业务场景编码';
COMMENT ON COLUMN financialdb.eg_manual_voucher.account_code IS '科目编码';
COMMENT ON COLUMN financialdb.eg_manual_voucher.account_name IS '科目名称';
COMMENT ON COLUMN financialdb.eg_manual_voucher.currency_code IS '币种编码';
COMMENT ON COLUMN financialdb.eg_manual_voucher.rate IS '汇率';
COMMENT ON COLUMN financialdb.eg_manual_voucher.debit_amount IS '借方发生额';
COMMENT ON COLUMN financialdb.eg_manual_voucher.credit_amount IS '贷方发生额';
COMMENT ON COLUMN financialdb.eg_manual_voucher.is_cash_flow IS '是否有现金流量（0：否，1：是）';
COMMENT ON COLUMN financialdb.eg_manual_voucher.cash_flow_marker IS '现金流量标记';
COMMENT ON COLUMN financialdb.eg_manual_voucher.subsidiary_account IS '辅助帐摘要';
COMMENT ON COLUMN financialdb.eg_manual_voucher.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_manual_voucher.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_manual_voucher.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_manual_voucher.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_manual_voucher.del_flag IS '是否删除（0：否，1：是）';
COMMENT ON COLUMN financialdb.eg_manual_voucher.manual_id IS '手工Id';
