ALTER TABLE financialdb4.eg_contract ADD payable_insurance_amount numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb4.eg_contract.payable_insurance_amount IS '应付保险费';
ALTER TABLE financialdb4.eg_contract ADD deduction_device_amount numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb4.eg_contract.first_rent IS '抵扣设备款';

ALTER TABLE financialdb4.eg_contract_month ADD payable_insurance_amount numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb4.eg_contract_month.payable_insurance_amount IS '应付保险费';
ALTER TABLE financialdb4.eg_contract_month ADD deduction_device_amount numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb4.eg_contract_month.first_rent IS '抵扣设备款';


ALTER TABLE financialdb4.eg_contract ADD procedure801 varchar NULL;
COMMENT ON COLUMN financialdb4.eg_contract.procedure801 IS 'hetjyjgfyx费用类型801';
ALTER TABLE financialdb4.eg_contract_month ADD procedure801 varchar NULL;
COMMENT ON COLUMN financialdb4.eg_contract_month.procedure801 IS 'hetjyjgfyx费用类型801';


CREATE TABLE financialdb4.eg_impairment_third_stage_record (
	id int8 NOT NULL,
	del_flag bpchar(1) NULL DEFAULT 0,
	create_by varchar(32) NULL,
	create_time timestamp(6) NULL,
	update_by varchar(32) NULL,
	update_time timestamp(6) NULL,
	contract_code varchar(100) NULL,
	upload_periods varchar(10) NULL
);

-- Column comments

COMMENT ON COLUMN financialdb4.eg_impairment_third_stage_record.del_flag IS '是否删除 0：未删除1：已删除';
COMMENT ON COLUMN financialdb4.eg_impairment_third_stage_record.create_by IS '创建人';
COMMENT ON COLUMN financialdb4.eg_impairment_third_stage_record.create_time IS '创建时间';
COMMENT ON COLUMN financialdb4.eg_impairment_third_stage_record.update_by IS '更新人';
COMMENT ON COLUMN financialdb4.eg_impairment_third_stage_record.update_time IS '更新时间';
COMMENT ON COLUMN financialdb4.eg_impairment_third_stage_record.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb4.eg_impairment_third_stage_record.upload_periods IS '上传账期';
COMMENT ON TABLE financialdb4.eg_impairment_third_stage_record IS '减值三阶段上传记录';

ALTER TABLE financialdb4.eg_repayment_plan ADD impairment_third_stage varchar(20) NULL;
COMMENT ON COLUMN financialdb4.eg_repayment_plan.impairment_third_stage IS '减值三阶段标识';
ALTER TABLE financialdb4.eg_repayment_plan ALTER COLUMN impairment_third_stage SET DEFAULT 0;

ALTER TABLE financialdb4.eg_repayment_plan_temp ADD impairment_third_stage varchar(20) NULL;
COMMENT ON COLUMN financialdb4.eg_repayment_plan_temp.impairment_third_stage IS '减值三阶段标识';
ALTER TABLE financialdb4.eg_repayment_plan_temp ALTER COLUMN impairment_third_stage SET DEFAULT 0;

ALTER TABLE financialdb4.eg_repayment_plan_provision ADD impairment_third_stage varchar(20) NULL;
COMMENT ON COLUMN financialdb4.eg_repayment_plan_provision.impairment_third_stage IS '减值三阶段标识';
ALTER TABLE financialdb4.eg_repayment_plan_provision ALTER COLUMN impairment_third_stage SET DEFAULT 0;