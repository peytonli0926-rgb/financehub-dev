--新增系统关账表
-- financialdb.eg_close_account definition

-- Drop table

DROP TABLE financialdb.eg_close_account;

CREATE TABLE financialdb.eg_close_account (
                                              id int8 NOT NULL, -- ID
                                              system_code varchar(100) NULL, -- 系统来源（统一平台：TYPT,商用车系统：SYCXT,小微系统：XWXT,乘用车系统:CYCXT）¶
                                              "year" int NULL, -- 年份
                                              "month" int NULL, -- 月份
                                              close_date varchar(100) NULL, -- 关账日期
                                              operate_date varchar(100) NULL, -- 关账操作日期
                                              operate_user varchar(64) NULL, -- 关账人
                                              status varchar(100) NULL, -- 状态
                                              create_by varchar(64) NULL, -- 创建人
                                              create_time timestamp NULL, -- 创建时间
                                              update_by varchar(64) NULL, -- 更新人
                                              update_time timestamp NULL, -- 更新时间
                                              del_flag bpchar(1) NULL DEFAULT 0, -- 是否删除0：否，1：是
                                              CONSTRAINT eg_close_account_pk PRIMARY KEY (id)
);
CREATE INDEX eg_close_account_system_code_idx ON financialdb.eg_close_account USING btree (system_code, year, month, close_date);
COMMENT ON TABLE financialdb.eg_close_account IS '系统关账期间表';

-- Column comments

COMMENT ON COLUMN financialdb.eg_close_account.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_close_account.system_code IS '系统来源（统一平台：TYPT,商用车系统：SYCXT,小微系统：XWXT,乘用车系统:CYCXT）
';
COMMENT ON COLUMN financialdb.eg_close_account."year" IS '年份';
COMMENT ON COLUMN financialdb.eg_close_account."month" IS '月份';
COMMENT ON COLUMN financialdb.eg_close_account.close_date IS '关账日期';
COMMENT ON COLUMN financialdb.eg_close_account.operate_date IS '关账操作日期';
COMMENT ON COLUMN financialdb.eg_close_account.operate_user IS '关账人';
COMMENT ON COLUMN financialdb.eg_close_account.status IS '状态(有效，无效)';
COMMENT ON COLUMN financialdb.eg_close_account.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_close_account.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_close_account.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_close_account.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_close_account.del_flag IS '是否删除0：否，1：是';

--初始化数据
INSERT INTO financialdb.eg_close_account (id,system_code,"year","month",close_date,operate_date,operate_user,status,create_by,create_time,update_by,update_time,del_flag) VALUES
	 (1747424380055146498,'TYPT',2023,11,'2024-01-01','2024-01-01 15:27:02','100029','有效',NULL,'2024-01-17 09:03:32.438',NULL,'2024-01-17 09:03:32.438','0'),
	 (1749669439546806273,'SYCXT',2023,11,'2024-01-01','2024-01-01 15:27:02','201291','有效','system','2024-01-23 13:44:36.337','system','2024-01-23 13:44:36.337','0'),
	 (1747163035212582914,'XWXT',2023,11,'2024-07-03','2024-01-16 15:41:03','100029','有效',NULL,'2024-01-16 15:45:02.974',NULL,'2024-01-16 15:45:02.979','0'),
	 (1749669835167297538,'CYCXT',2023,11,'2024-01-01','2024-01-01 14:30:56','100029','有效',NULL,'2024-01-23 13:46:10.67',NULL,'2024-01-23 13:46:10.685','0'),
	 (1750400053602295810,'TYPT',2024,1,'2024-01-25','2024-01-25 14:08:26','admin','有效','system','2024-01-25 14:07:48.296','system','2024-01-25 14:07:48.296','0'),
	 (1750400274345533441,'TYPT',2024,2,'2024-02-26','2024-01-25 14:08:48','admin','有效','system','2024-01-25 14:08:41.016','system','2024-01-25 14:08:41.016','0'),
	 (1750400187547394049,'TYPT',2023,11,'2023-11-30','2024-01-25 14:08:58','admin','无效','system','2024-01-25 14:08:20.231','system','2024-01-25 14:08:20.231','0'),
	 (1750400340720394241,'TYPT',2023,11,'2023-11-30','2024-01-25 14:09:04','admin','有效','system','2024-01-25 14:08:56.762','system','2024-01-25 14:08:56.762','0'),
	 (1750400340720394242,'FINHUB',2023,11,'2023-11-30','2024-01-25 14:09:04','admin','有效','system','2024-01-25 14:08:56.762','system','2024-01-25 14:08:56.762','0');




