--手工表
-- financialdb.eg_manual definition

-- Drop table

-- DROP TABLE financialdb.eg_manual;

CREATE TABLE financialdb.eg_manual (
                                       id int8 NOT NULL, -- ID
                                       org_id varchar(100) NOT NULL, -- 签约主体
                                       period_code int4 NOT NULL, -- 会计期间
                                       business_date timestamp NOT NULL, -- 业务日期
                                       voucher_date timestamp NOT NULL, -- 记账日期（财务日期）
                                       currency_code varchar(100) NOT NULL, -- 币种编码
                                       process_status varchar(100) NULL, -- 处理状态(1:已录入，2：已提交，3：已复核4：已传至金蝶)
                                       process_instance_id int8 NULL, -- 流程实例id
                                       create_by varchar(64) NULL, -- 创建人
                                       create_time timestamp NULL, -- 创建时间
                                       update_by varchar(64) NULL, -- 更新人
                                       update_time timestamp NULL, -- 更新时间
                                       del_flag bpchar(1) NULL DEFAULT 0, -- 是否删除（0：否，1：是）
                                       CONSTRAINT eg_manual_pk PRIMARY KEY (id)
);
CREATE INDEX eg_manual_id_idx ON financialdb.eg_manual USING btree (id, org_id, period_code, business_date, voucher_date, currency_code, process_status);
COMMENT ON TABLE financialdb.eg_manual IS '手工表';

-- Column comments

COMMENT ON COLUMN financialdb.eg_manual.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_manual.org_id IS '签约主体';
COMMENT ON COLUMN financialdb.eg_manual.period_code IS '会计期间';
COMMENT ON COLUMN financialdb.eg_manual.business_date IS '业务日期';
COMMENT ON COLUMN financialdb.eg_manual.voucher_date IS '记账日期（财务日期）';
COMMENT ON COLUMN financialdb.eg_manual.currency_code IS '币种编码';
COMMENT ON COLUMN financialdb.eg_manual.process_status IS '处理状态(1:已录入，2：已提交，3：已复核4：已传至金蝶)';
COMMENT ON COLUMN financialdb.eg_manual.process_instance_id IS '流程实例id';
COMMENT ON COLUMN financialdb.eg_manual.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_manual.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_manual.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_manual.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_manual.del_flag IS '是否删除（0：否，1：是）';

-- Permissions

ALTER TABLE financialdb.eg_manual OWNER TO financialdb;
GRANT ALL ON TABLE financialdb.eg_manual TO financialdb;
