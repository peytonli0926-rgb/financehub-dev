ALTER TABLE financialdb.eg_contract_ta_amount ADD org_id varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_contract_ta_amount.org_id IS '组织机编码';

--新增传eas2结果记录表
-- financialdb.eg_send_eas2_result definition

-- Drop table

-- DROP TABLE financialdb.eg_send_eas2_result;

CREATE TABLE financialdb.eg_send_eas2_result (
	id int8 NOT NULL, -- ID
	fid text NULL, -- fid
	is_success varchar(100) NULL, -- 是否成功（sucs：成功，errs：失败）
	system_code varchar(100) NULL -- 数据来源：eas1:EAS1,金蝶中间库：KINGDEE_MIDDLE,中台：FINHUB
);
COMMENT ON TABLE financialdb.eg_send_eas2_result IS '记录发送EAS2数据是否成功表';

-- Column comments

COMMENT ON COLUMN financialdb.eg_send_eas2_result.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_send_eas2_result.fid IS 'fid';
COMMENT ON COLUMN financialdb.eg_send_eas2_result.is_success IS '是否成功（sucs：成功，errs：失败）';
COMMENT ON COLUMN financialdb.eg_send_eas2_result.system_code IS '数据来源：eas1:EAS1,金蝶中间库：KINGDEE_MIDDLE,中台：FINHUB';

