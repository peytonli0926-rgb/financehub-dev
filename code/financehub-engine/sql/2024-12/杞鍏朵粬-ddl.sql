CREATE TABLE eg_convert_transfer_other
(
    id                        BIGINT PRIMARY KEY,
    contract_code             VARCHAR(255),
    contract_name             VARCHAR(255),
    client_code               VARCHAR(255),
    client_name               VARCHAR(255),
    financial_contract_status VARCHAR(255),
    transfer_party            VARCHAR(255),
    transferee_party          VARCHAR(255),
    account_date              TIMESTAMP,
    reference_date            TIMESTAMP,
    invoice_flag              char       default '0'::bpchar,
    other_income              NUMERIC(20, 2),
    other_outcome             NUMERIC(20, 2),
    process_instance_id       BIGINT,
    process_status            VARCHAR(255),
    is_generate_voucher       char       default '0'::bpchar,
    voucher_id                VARCHAR(2000),
    error_info                varchar(2000),
    del_flag                  char       default '0'::bpchar,
    create_by                 varchar(255),
    create_time               timestamp,
    update_by                 varchar(255),
    update_time               timestamp
);

-- 添加列注释
COMMENT ON COLUMN eg_convert_transfer_other.id IS 'ID';
COMMENT ON COLUMN eg_convert_transfer_other.contract_code IS '合同编号';
COMMENT ON COLUMN eg_convert_transfer_other.contract_name IS '合同名称';
COMMENT ON COLUMN eg_convert_transfer_other.client_code IS '客户编码';
COMMENT ON COLUMN eg_convert_transfer_other.client_name IS '客户名称';
COMMENT ON COLUMN eg_convert_transfer_other.financial_contract_status IS '财务合同状态';
COMMENT ON COLUMN eg_convert_transfer_other.transfer_party IS '转让方';
COMMENT ON COLUMN eg_convert_transfer_other.transferee_party IS '受让方';
COMMENT ON COLUMN eg_convert_transfer_other.account_date IS '记账日期';
COMMENT ON COLUMN eg_convert_transfer_other.reference_date IS '基准日';
COMMENT ON COLUMN eg_convert_transfer_other.invoice_flag IS '转让后是否开发票（0：否，1：是）';
COMMENT ON COLUMN eg_convert_transfer_other.other_income IS '其他收入';
COMMENT ON COLUMN eg_convert_transfer_other.other_outcome IS '其他成本';
COMMENT ON COLUMN eg_convert_transfer_other.process_instance_id IS '流程id';
COMMENT ON COLUMN eg_convert_transfer_other.process_status IS '1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝';
COMMENT ON COLUMN eg_convert_transfer_other.is_generate_voucher IS '是否已生成凭证（0：未生成1：已生成）默认0';
COMMENT ON COLUMN eg_convert_transfer_other.voucher_id IS '凭证id,多个按照逗号分隔';
COMMENT ON COLUMN eg_convert_transfer_other.error_info IS '生成凭证报错信息';
COMMENT ON COLUMN eg_convert_transfer_other.del_flag IS '是否删除（0：未删除1：删除）默认0';
COMMENT ON COLUMN eg_convert_transfer_other.create_by IS '创建人';
COMMENT ON COLUMN eg_convert_transfer_other.create_time IS '创建时间';
COMMENT ON COLUMN eg_convert_transfer_other.update_by IS '更新人';
COMMENT ON COLUMN eg_convert_transfer_other.update_time IS '更新时间';

CREATE TABLE eg_convert_transfer_other_payment
(
    id                   BIGINT PRIMARY KEY,
    contract_code        VARCHAR(255),
    contract_name        VARCHAR(255),
    client_code          VARCHAR(255),
    client_name          VARCHAR(255),
    account_date         DATE,
    actual_date          date,
    plan_date            DATE,
    period               INTEGER,
    rent_amount          NUMERIC(20, 2),
    principal_amount     NUMERIC(20, 2),
    interest_amount      NUMERIC(20, 2),
    rent_actual          NUMERIC(20, 2),
    principal_actual     NUMERIC(20, 2),
    interest_actual      NUMERIC(20, 2),
    calculate_deductions NUMERIC(20, 2),
    invoice_flag         char       default '0'::bpchar,
    bank_account_code    VARCHAR(255),
    process_instance_id  BIGINT,
    process_status       VARCHAR(255),
    is_generate_voucher  char       default '0'::bpchar,
    voucher_id           VARCHAR(2000),
    error_info           VARCHAR(2000),
    del_flag             char       default '0'::bpchar,
    create_by            varchar(255),
    create_time          timestamp,
    update_by            varchar(255),
    update_time          timestamp
);

-- 添加列注释
COMMENT ON COLUMN eg_convert_transfer_other_payment.id IS 'ID';
COMMENT ON COLUMN eg_convert_transfer_other_payment.contract_code IS '合同编号';
COMMENT ON COLUMN eg_convert_transfer_other_payment.contract_name IS '合同名称';
COMMENT ON COLUMN eg_convert_transfer_other_payment.client_code IS '客户编码';
COMMENT ON COLUMN eg_convert_transfer_other_payment.client_name IS '客户名称';
COMMENT ON COLUMN eg_convert_transfer_other_payment.account_date IS '记账日期';
COMMENT ON COLUMN eg_convert_transfer_other_payment.actual_date IS '实付日期';
COMMENT ON COLUMN eg_convert_transfer_other_payment.plan_date IS '计划日期';
COMMENT ON COLUMN eg_convert_transfer_other_payment.period IS '计划期数';
COMMENT ON COLUMN eg_convert_transfer_other_payment.rent_amount IS '应付租金';
COMMENT ON COLUMN eg_convert_transfer_other_payment.principal_amount IS '应付本金';
COMMENT ON COLUMN eg_convert_transfer_other_payment.interest_amount IS '应付利息';
COMMENT ON COLUMN eg_convert_transfer_other_payment.rent_actual IS '实收租金';
COMMENT ON COLUMN eg_convert_transfer_other_payment.principal_actual IS '实收本金';
COMMENT ON COLUMN eg_convert_transfer_other_payment.interest_actual IS '实收利息';
COMMENT ON COLUMN eg_convert_transfer_other_payment.calculate_deductions IS '计算扣额';
COMMENT ON COLUMN eg_convert_transfer_other_payment.invoice_flag IS '转让后是否开发票（0：否，1：是）';
COMMENT ON COLUMN eg_convert_transfer_other_payment.bank_account_code IS '银行账户编码';
COMMENT ON COLUMN eg_convert_transfer_other_payment.process_instance_id IS '流程id';
COMMENT ON COLUMN eg_convert_transfer_other_payment.process_status IS '1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝';
COMMENT ON COLUMN eg_convert_transfer_other_payment.is_generate_voucher IS '是否已生成凭证（0：未生成1：已生成）默认0';
COMMENT ON COLUMN eg_convert_transfer_other_payment.voucher_id IS '凭证id,多个按照逗号分隔';
COMMENT ON COLUMN eg_convert_transfer_other_payment.error_info IS '生成凭证报错信息';
COMMENT ON COLUMN eg_convert_transfer_other_payment.del_flag IS '是否删除（0：未删除1：删除）默认0';
COMMENT ON COLUMN eg_convert_transfer_other_payment.create_by IS '创建人';
COMMENT ON COLUMN eg_convert_transfer_other_payment.create_time IS '创建时间';
COMMENT ON COLUMN eg_convert_transfer_other_payment.update_by IS '更新人';
COMMENT ON COLUMN eg_convert_transfer_other_payment.update_time IS '更新时间';