--审批表
CREATE TABLE financialdb.eg_approve (
                                        id int8 NOT NULL, -- ID
                                        document_id int8 NOT NULL, -- 单据id
                                        document_type varchar(100) NOT NULL, -- 单据类型
                                        document_status varchar(100) NOT NULL, -- 单据状态
                                        submitter_num varchar(100) NULL, -- 提交人工号
                                        submitter_name varchar(100) NULL, -- 提交人姓名
                                        approver_num varchar(100) NULL, -- 审批人工号
                                        approver_name varchar(100) NULL, -- 审批人姓名
                                        submit_date timestamp NULL, -- 提交时间
                                        approver_date timestamp NULL, -- 审批时间
                                        url varchar(500) NULL, -- 跳转地址
                                        remark varchar(500) NULL, -- 审批备注
                                        del_flag bpchar(1) NULL DEFAULT '0'::bpchar, -- 是否删除（0：否，1：是）
                                        create_by varchar(64) NULL, -- 创建人
                                        create_time timestamp NULL, -- 创建时间
                                        update_by varchar(64) NULL, -- 更新人
                                        update_time timestamp NULL, -- 更新时间
                                        CONSTRAINT eg_approve_pk PRIMARY KEY (id)
);
CREATE INDEX eg_approve_id_idx ON financialdb.eg_approve USING btree (id, document_type, document_status, submitter_num, approver_num);
COMMENT ON TABLE financialdb.eg_approve IS '审批表';

-- Column comments

COMMENT ON COLUMN financialdb.eg_approve.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_approve.document_id IS '单据id';
COMMENT ON COLUMN financialdb.eg_approve.document_type IS '单据类型';
COMMENT ON COLUMN financialdb.eg_approve.document_status IS '单据状态';
COMMENT ON COLUMN financialdb.eg_approve.submitter_num IS '提交人工号';
COMMENT ON COLUMN financialdb.eg_approve.submitter_name IS '提交人姓名';
COMMENT ON COLUMN financialdb.eg_approve.approver_num IS '审批人工号';
COMMENT ON COLUMN financialdb.eg_approve.approver_name IS '审批人姓名';
COMMENT ON COLUMN financialdb.eg_approve.submit_date IS '提交时间';
COMMENT ON COLUMN financialdb.eg_approve.approver_date IS '审批时间';
COMMENT ON COLUMN financialdb.eg_approve.url IS '跳转地址';
COMMENT ON COLUMN financialdb.eg_approve.remark IS '审批备注';
COMMENT ON COLUMN financialdb.eg_approve.del_flag IS '是否删除（0：否，1：是）';
COMMENT ON COLUMN financialdb.eg_approve.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_approve.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_approve.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_approve.update_time IS '更新时间';