--新增表chargeoff
-- financialdb.eg_charge_off definition

-- Drop table

-- DROP TABLE financialdb.eg_charge_off;

CREATE TABLE financialdb.eg_charge_off (
                                           id int8 NOT NULL, -- ID
                                           contract_code varchar(100) NULL, -- 合同编号
                                           org_id varchar(100) NULL, -- 签约主体
                                           verification_status varchar(100) NULL, -- 核销状态
                                           client_code varchar(100) NULL, -- 客户编码
                                           client_name varchar(100) NULL, -- 客户名称
                                           verification_date timestamp NULL, -- 核销时间
                                           financial_expense_amount numeric(20, 2) NULL, -- 财务核销敞口
                                           provision_reversal_amount numeric(20, 2) NULL, -- 拨备转回金额
                                           provision_reversal_year varchar(50) NULL, -- 拨备转回年份
                                           tax_verification_date timestamp NULL, -- 税务核销日期
                                           tax_verification_amount numeric(20, 2) NULL, -- 税务核销金额
                                           process_instance_id int8 NULL, -- 流程实例id
                                           process_status varchar(100) NULL, -- 1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝
                                           del_flag bpchar(1) NULL DEFAULT '0'::bpchar, -- 是否删除（0：未删除1：删除）默认0
                                           create_by varchar(32) NULL, -- 创建人
                                           create_time timestamp NULL, -- 创建时间
                                           update_by varchar(32) NULL, -- 更新人
                                           update_time timestamp NULL, -- 更新时间
                                           CONSTRAINT eg_charge_off_pk PRIMARY KEY (id)
);
CREATE INDEX eg_charge_off_id_idx ON financialdb.eg_charge_off (id,contract_code,org_id,verification_status,client_code,verification_date);
COMMENT ON TABLE financialdb.eg_charge_off IS 'Charge Off手工上传表';

-- Column comments

COMMENT ON COLUMN financialdb.eg_charge_off.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_charge_off.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb.eg_charge_off.org_id IS '签约主体';
COMMENT ON COLUMN financialdb.eg_charge_off.verification_status IS '核销状态';
COMMENT ON COLUMN financialdb.eg_charge_off.client_code IS '客户编码';
COMMENT ON COLUMN financialdb.eg_charge_off.client_name IS '客户名称';
COMMENT ON COLUMN financialdb.eg_charge_off.verification_date IS '核销时间';
COMMENT ON COLUMN financialdb.eg_charge_off.financial_expense_amount IS '财务核销敞口';
COMMENT ON COLUMN financialdb.eg_charge_off.provision_reversal_amount IS '拨备转回金额';
COMMENT ON COLUMN financialdb.eg_charge_off.provision_reversal_year IS '拨备转回年份';
COMMENT ON COLUMN financialdb.eg_charge_off.tax_verification_date IS '税务核销日期';
COMMENT ON COLUMN financialdb.eg_charge_off.tax_verification_amount IS '税务核销金额';
COMMENT ON COLUMN financialdb.eg_charge_off.process_instance_id IS '流程实例id';
COMMENT ON COLUMN financialdb.eg_charge_off.process_status IS '1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝';
COMMENT ON COLUMN financialdb.eg_charge_off.del_flag IS '是否删除（0：未删除1：删除）默认0';
COMMENT ON COLUMN financialdb.eg_charge_off.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_charge_off.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_charge_off.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_charge_off.update_time IS '更新时间';