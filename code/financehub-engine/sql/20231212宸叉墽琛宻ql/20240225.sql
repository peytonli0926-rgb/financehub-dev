--已执行，20240226
DROP TABLE IF EXISTS eg_data_execution_task;
CREATE TABLE eg_data_execution_task(
    id bigint,
    system_code VARCHAR(32),
    status VARCHAR(32),
    task_start_time timestamp,
    task_end_time timestamp,
    business_date_start timestamp,
    business_date_end timestamp,
    data_size int,
    data_success_size int,
    data_failed_size int,
    error_message VARCHAR(2000),
    create_by VARCHAR(32),
    create_time TIMESTAMP,
    update_by VARCHAR(32),
    update_time TIMESTAMP,
    del_flag char(1) DEFAULT  '0',
    PRIMARY KEY (id)
);

COMMENT ON TABLE eg_data_execution_task IS '业务系统数据执行任务表';
COMMENT ON COLUMN eg_data_execution_task.id IS 'ID';
COMMENT ON COLUMN eg_data_execution_task.system_code IS '系统编码';
COMMENT ON COLUMN eg_data_execution_task.status IS '任务状态';
COMMENT ON COLUMN eg_data_execution_task.task_start_time IS '任务开始时间';
COMMENT ON COLUMN eg_data_execution_task.task_end_time IS '任务结束时间';
COMMENT ON COLUMN eg_data_execution_task.business_date_start IS '业务日期开始时间';
COMMENT ON COLUMN eg_data_execution_task.business_date_end IS '业务日期结束时间';
COMMENT ON COLUMN eg_data_execution_task.data_size IS '任务处理数据总条数';
COMMENT ON COLUMN eg_data_execution_task.data_success_size IS '成功数据条数';
COMMENT ON COLUMN eg_data_execution_task.data_failed_size IS '失败数据条数';
COMMENT ON COLUMN eg_data_execution_task.error_message IS '任务异常消息';
COMMENT ON COLUMN eg_data_execution_task.create_by IS '创建人';
COMMENT ON COLUMN eg_data_execution_task.create_time IS '创建时间';
COMMENT ON COLUMN eg_data_execution_task.update_by IS '更新人';
COMMENT ON COLUMN eg_data_execution_task.update_time IS '更新时间';
COMMENT ON COLUMN eg_data_execution_task.del_flag IS '删除标识(0:未删除,1:已删除)';

--注意：以下脚本须停止服务后再执行
update eg_raw_transaction_data set business_date = to_date(message_content::jsonb->>'businessDate', 'yyyy-mm-dd HH24:mi:ss')
where message_content::jsonb->>'businessDate' is not null and message_content::jsonb->>'businessDate' != '';

update eg_raw_transaction_data set business_date = create_time where business_date is null;

update eg_raw_transaction_data t set message_status = (select message_status from eg_raw_transaction_data_bak b where b.id = t.id);

update eg_raw_transaction_data set message_status = 'NOT_EXECUTE' where message_status = '已入库';


INSERT INTO financialdb.sys_dict_type (dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark) VALUES
	 ('自动跑凭证的业务系统','sys_auto_execute_system','0','103073','2024-02-25 23:01:03.744','103073','2024-02-25 23:01:03.744','状态为有效的参数会自动执行');

INSERT INTO financialdb.sys_dict_data (dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark) VALUES
	 (1,'小微系统','XWXT','sys_auto_execute_system',NULL,NULL,'N','0','103073','2024-02-25 23:01:49.145','103073','2024-02-25 23:08:44.873',NULL),
	 (1,'统一平台','TYPT','sys_auto_execute_system',NULL,NULL,'N','0','103073','2024-02-25 23:01:57.433','103073','2024-02-25 23:08:45.432',NULL),
	 (1,'商用车系统','SYCXT','sys_auto_execute_system',NULL,NULL,'N','0','103073','2024-02-25 23:01:29.790','103073','2024-02-25 23:08:43.542',NULL),
	 (1,'乘用车系统','CYCXT','sys_auto_execute_system',NULL,NULL,'N','0','103073','2024-02-25 23:01:19.854','103073','2024-02-25 23:08:44.152',NULL);

