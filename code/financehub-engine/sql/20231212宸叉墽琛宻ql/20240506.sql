-- 合同表新增索引
CREATE INDEX eg_contract_contract_code_is_null_idx ON financialdb.eg_contract USING btree (contract_code_m) WHERE contract_code_m IS NULL;
CREATE INDEX eg_contract_contract_code_m_idx ON financialdb.eg_contract USING btree (contract_code_m) WHERE contract_code = contract_code_m;

--凭证表新增外部接口id
ALTER TABLE financialdb.eg_voucher ADD interface_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_voucher.interface_id IS '外部接口数据id';

COMMENT ON COLUMN financialdb.eg_voucher.valid_flag IS '有效标识1:有效 2:凭证行为空 3:借贷金额未平，4:无效';

CREATE INDEX eg_contract_contract_code_is_null2_idx ON financialdb.eg_contract USING btree (contract_code_m) WHERE (contract_code_m IS null or contract_code_m = '');


CREATE TABLE financialdb.dws_bz_hetjyjgfyx_d (
	pt varchar(8) NULL,
	vc_yewxtid varchar(32) NULL,
	vc_hetbh varchar(32) NULL,
	vc_feiylx varchar(32) NULL,
	vc_feiymc varchar(32) NULL,
	dec_jiashj numeric(18, 2) NULL,
	dec_jine numeric(18, 2) NULL,
	dec_shuie numeric(18, 2) NULL,
	dec_shuil numeric(18, 2) NULL,
	vc_biz varchar(32) NULL,
	dec_feiybl numeric(18, 2) NULL,
	vc_jiesfs varchar(32) NULL,
	vc_shouzfx varchar(32) NULL,
	dt_chuangjsj date NULL
);
COMMENT ON TABLE financialdb.dws_bz_hetjyjgfyx_d IS '合同交易结构费用项';

-- Column comments

COMMENT ON COLUMN financialdb.dws_bz_hetjyjgfyx_d.pt IS '数据抽取日期';
COMMENT ON COLUMN financialdb.dws_bz_hetjyjgfyx_d.vc_yewxtid IS '业务系统编号';
COMMENT ON COLUMN financialdb.dws_bz_hetjyjgfyx_d.vc_hetbh IS '合同编号';
COMMENT ON COLUMN financialdb.dws_bz_hetjyjgfyx_d.vc_feiylx IS '费用类型';
COMMENT ON COLUMN financialdb.dws_bz_hetjyjgfyx_d.vc_feiymc IS '费用名称';
COMMENT ON COLUMN financialdb.dws_bz_hetjyjgfyx_d.dec_jiashj IS '价税合计';
COMMENT ON COLUMN financialdb.dws_bz_hetjyjgfyx_d.dec_jine IS '金额';
COMMENT ON COLUMN financialdb.dws_bz_hetjyjgfyx_d.dec_shuie IS '税额';
COMMENT ON COLUMN financialdb.dws_bz_hetjyjgfyx_d.dec_shuil IS '税率';
COMMENT ON COLUMN financialdb.dws_bz_hetjyjgfyx_d.vc_biz IS '币种';
COMMENT ON COLUMN financialdb.dws_bz_hetjyjgfyx_d.dec_feiybl IS '费用比率';
COMMENT ON COLUMN financialdb.dws_bz_hetjyjgfyx_d.vc_jiesfs IS '结算方式';
COMMENT ON COLUMN financialdb.dws_bz_hetjyjgfyx_d.vc_shouzfx IS '收支方向(收款/付款)';
COMMENT ON COLUMN financialdb.dws_bz_hetjyjgfyx_d.dt_chuangjsj IS '创建时间';

--以上prod 已执行 uat未执行