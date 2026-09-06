CREATE TABLE financialdb.eg_kingdee_person (
	id int8 NOT NULL, -- ID
	eas_fid varchar(100) NULL, -- 金蝶主键FID
	code varchar(60) NULL,
	name varchar(200) null,
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp(6) NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp(6) NULL, -- 更新时间
	del_flag bpchar(1) NULL DEFAULT '0'::bpchar, -- 删除标识(0:未删除,1:已删除)
	CONSTRAINT eg_kingdee_person_pkey PRIMARY KEY (id)
);
COMMENT ON TABLE financialdb.eg_kingdee_person IS '金蝶-职员表';

-- Column comments

COMMENT ON COLUMN financialdb.eg_kingdee_person.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_kingdee_person.eas_fid IS '金蝶主键FID';
COMMENT ON COLUMN financialdb.eg_kingdee_person.code IS '编号';
COMMENT ON COLUMN financialdb.eg_kingdee_person.name IS '名称';
COMMENT ON COLUMN financialdb.eg_kingdee_person.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_kingdee_person.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_kingdee_person.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_kingdee_person.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_kingdee_person.del_flag IS '删除标识(0:未删除,1:已删除)';




CREATE TABLE financialdb.eg_kingdee_costcenter (
	id int8 NOT NULL, -- ID
	eas_fid varchar(100) NULL, -- 金蝶主键FID
	code varchar(60) NULL,
	name varchar(200) null,
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp(6) NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp(6) NULL, -- 更新时间
	del_flag bpchar(1) NULL DEFAULT '0'::bpchar, -- 删除标识(0:未删除,1:已删除)
	CONSTRAINT eg_kingdee_costcenter_pkey PRIMARY KEY (id)
);
COMMENT ON TABLE financialdb.eg_kingdee_costcenter IS '金蝶-成本中心';

-- Column comments

COMMENT ON COLUMN financialdb.eg_kingdee_costcenter.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_kingdee_costcenter.eas_fid IS '金蝶主键FID';
COMMENT ON COLUMN financialdb.eg_kingdee_costcenter.code IS '编号';
COMMENT ON COLUMN financialdb.eg_kingdee_costcenter.name IS '名称';
COMMENT ON COLUMN financialdb.eg_kingdee_costcenter.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_kingdee_costcenter.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_kingdee_costcenter.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_kingdee_costcenter.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_kingdee_costcenter.del_flag IS '删除标识(0:未删除,1:已删除)';



CREATE TABLE financialdb.eg_kingdee_bank (
	id int8 NOT NULL, -- ID
	eas_fid varchar(100) NULL, -- 金蝶主键FID
	code varchar(60) NULL,
	name varchar(200) null,
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp(6) NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp(6) NULL, -- 更新时间
	del_flag bpchar(1) NULL DEFAULT '0'::bpchar, -- 删除标识(0:未删除,1:已删除)
	CONSTRAINT eg_kingdee_bank_pkey PRIMARY KEY (id)
);
COMMENT ON TABLE financialdb.eg_kingdee_bank IS '金蝶-金融机构';

-- Column comments

COMMENT ON COLUMN financialdb.eg_kingdee_bank.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_kingdee_bank.eas_fid IS '金蝶主键FID';
COMMENT ON COLUMN financialdb.eg_kingdee_bank.code IS '编号';
COMMENT ON COLUMN financialdb.eg_kingdee_bank.name IS '名称';
COMMENT ON COLUMN financialdb.eg_kingdee_bank.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_kingdee_bank.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_kingdee_bank.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_kingdee_bank.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_kingdee_bank.del_flag IS '删除标识(0:未删除,1:已删除)';




CREATE TABLE financialdb.eg_kingdee_general_asst (
	id int8 NOT NULL, -- ID
	eas_fid varchar(100) NULL, -- 金蝶主键FID
	code varchar(60) NULL,
	name varchar(200) null,
	asst_type varchar(100) NULL,
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp(6) NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp(6) NULL, -- 更新时间
	del_flag bpchar(1) NULL DEFAULT '0'::bpchar, -- 删除标识(0:未删除,1:已删除)
	CONSTRAINT eg_kingdee_general_asst_pkey PRIMARY KEY (id)
);
COMMENT ON TABLE financialdb.eg_kingdee_general_asst IS '金蝶-自定义核算项目';

-- Column comments

COMMENT ON COLUMN financialdb.eg_kingdee_general_asst.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_kingdee_general_asst.eas_fid IS '金蝶主键FID';
COMMENT ON COLUMN financialdb.eg_kingdee_general_asst.code IS '编号';
COMMENT ON COLUMN financialdb.eg_kingdee_general_asst.name IS '名称';
COMMENT ON COLUMN financialdb.eg_kingdee_general_asst.asst_type IS '核算项目类型';
COMMENT ON COLUMN financialdb.eg_kingdee_general_asst.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_kingdee_general_asst.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_kingdee_general_asst.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_kingdee_general_asst.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_kingdee_general_asst.del_flag IS '删除标识(0:未删除,1:已删除)';