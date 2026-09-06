--生产已执行
--新增渠道费表
-- financialdb.eg_cost_channel_fee_tax_apportion definition

-- Drop table

-- DROP TABLE financialdb.eg_cost_channel_fee_tax_apportion;

CREATE TABLE financialdb.eg_cost_channel_fee_tax_apportion (
                                                               id int8 NOT NULL, -- id
                                                               channel_type varchar(100) NOT NULL, -- 渠道费类型
                                                               contract_code varchar(100) NOT NULL, -- 合同编码
                                                               channel_code varchar(100) NOT NULL, -- 渠道编码
                                                               channel_name varchar(100) NOT NULL, -- 渠道名称
                                                               no_tax_amount numeric(20, 2) NULL, -- 金额（不含税）
                                                               tax_amount numeric(20, 2) NULL, -- 税额
                                                               voucher_ids varchar(100) NULL, -- 凭证id,多个以逗号分隔
                                                               create_by varchar(100) NULL, -- 创建人
                                                               create_time timestamp NULL, -- 创建时间
                                                               update_by varchar(100) NULL, -- 更新人
                                                               update_time timestamp NULL, -- 更新时间
                                                               del_flag bpchar(1) NULL DEFAULT 0, -- 是否删除(0:否，1：是)
                                                               CONSTRAINT eg_cost_channel_fee_tax_apportion_pk PRIMARY KEY (id, channel_type, contract_code, channel_code, channel_name)
);
COMMENT ON TABLE financialdb.eg_cost_channel_fee_tax_apportion IS '成本类渠道费税额分摊';

-- Column comments

COMMENT ON COLUMN financialdb.eg_cost_channel_fee_tax_apportion.id IS 'id';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee_tax_apportion.channel_type IS '渠道费类型';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee_tax_apportion.contract_code IS '合同编码';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee_tax_apportion.channel_code IS '渠道编码';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee_tax_apportion.channel_name IS '渠道名称';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee_tax_apportion.no_tax_amount IS '金额（不含税）';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee_tax_apportion.tax_amount IS '税额';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee_tax_apportion.voucher_ids IS '凭证id,多个以逗号分隔';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee_tax_apportion.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee_tax_apportion.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee_tax_apportion.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee_tax_apportion.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee_tax_apportion.del_flag IS '是否删除(0:否，1：是)';

-- Permissions

ALTER TABLE financialdb.eg_cost_channel_fee_tax_apportion OWNER TO financialdb;
GRANT ALL ON TABLE financialdb.eg_cost_channel_fee_tax_apportion TO financialdb;


-- financialdb.eg_cost_channel_fee definition

-- Drop table

-- DROP TABLE financialdb.eg_cost_channel_fee;

CREATE TABLE financialdb.eg_cost_channel_fee (
                                                 id int8 NOT NULL, -- ID
                                                 channel_type varchar(32) NOT NULL, -- 渠道类型（1：经销商服务费，2：外部渠道费，3：海通渠道费）
                                                 contract_code varchar(64) NOT NULL, -- 合同编号
                                                 channel_code varchar(100) NOT NULL, -- 渠道方编码
                                                 channel_name varchar(100) NOT NULL, -- 渠道方名称
                                                 host_factory varchar(100) NULL, -- 主机厂
                                                 actual_amount numeric(20, 2) NULL, -- 实付金额（含税）
                                                 no_tax_transaction_amount numeric(20, 2) NULL, -- 对应交易结构金额（不含税）
                                                 structure_type varchar(100) NULL, -- 交易结构调整类型
                                                 voucher_ids varchar(100) NULL, -- 凭证id,多个以逗号分隔
                                                 create_by varchar(100) NULL, -- 创建人
                                                 create_time timestamp NULL, -- 创建时间
                                                 update_by varchar(100) NULL, -- 更新人
                                                 update_time timestamp NULL, -- 更新时间
                                                 del_flag bpchar(1) NULL DEFAULT 0, -- 是否删除（0：否，1：是）
                                                 CONSTRAINT eg_cost_channel_fee_pk PRIMARY KEY (channel_type, contract_code, channel_code, id, channel_name)
);
COMMENT ON TABLE financialdb.eg_cost_channel_fee IS '成本类支付-经销商服务费、外部渠道费，海通渠道费';

-- Column comments

COMMENT ON COLUMN financialdb.eg_cost_channel_fee.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee.channel_type IS '渠道类型（1：经销商服务费，2：外部渠道费，3：海通渠道费）';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee.channel_code IS '渠道方编码';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee.channel_name IS '渠道方名称';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee.host_factory IS '主机厂';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee.actual_amount IS '实付金额（含税）';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee.no_tax_transaction_amount IS '对应交易结构金额（不含税）';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee.structure_type IS '交易结构调整类型';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee.voucher_ids IS '凭证id,多个以逗号分隔';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_cost_channel_fee.del_flag IS '是否删除（0：否，1：是）';

-- Permissions

ALTER TABLE financialdb.eg_cost_channel_fee OWNER TO financialdb;
GRANT ALL ON TABLE financialdb.eg_cost_channel_fee TO financialdb;