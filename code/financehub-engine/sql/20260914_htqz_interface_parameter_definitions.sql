-- Definitions required by the unified HTQZ interface and voucher rules.
-- Keep both the engine-local dictionary and the admin dictionary in sync:
-- RemoteDictService reads financialdb4 while local migration/audit tools read
-- financehub_lease.

DROP TEMPORARY TABLE IF EXISTS tmp_htqz_dict_type;
CREATE TEMPORARY TABLE tmp_htqz_dict_type (
 dict_name VARCHAR(100) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 dict_type VARCHAR(100) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 remark VARCHAR(500) COLLATE utf8mb4_0900_ai_ci,
 PRIMARY KEY(dict_type)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
INSERT INTO tmp_htqz_dict_type VALUES
 ('数据库字段类型','sys_database_column_type','接口配置字段类型'),
 ('租赁类型','LEASE_TYPE','合同起租事件租赁类型'),
 ('租赁方式','LEASE_METHOD','合同起租事件租赁方式'),
 ('系统来源','sys_form_source','合同起租事件来源系统'),
 ('起租事件变体','HTQZ_START_EVENT_VARIANT','合同起租事件标准化事件编码'),
 ('起租核算配置变体','HTQZ_ACCOUNTING_VARIANT','统一起租凭证的科目配置选择参数'),
 ('起租本金结转方式','HTQZ_PRINCIPAL_OFFSET_TYPE','统一起租凭证本金贷方选择参数'),
 ('细分场景','sys_sub_scene_type','凭证模板细分场景'),
 ('凭证类型','sys_voucher_type','凭证模板凭证类型');

INSERT INTO financehub_lease.sys_dict_type
 (dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
SELECT t.dict_name,t.dict_type,'0','htqz-interface-v1',NOW(),'htqz-interface-v1',NOW(),t.remark
FROM tmp_htqz_dict_type t
WHERE NOT EXISTS (SELECT 1 FROM financehub_lease.sys_dict_type x WHERE x.dict_type=t.dict_type);

INSERT INTO financialdb4.sys_dict_type
 (dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
SELECT t.dict_name,t.dict_type,'0','htqz-interface-v1',NOW(),'htqz-interface-v1',NOW(),t.remark
FROM tmp_htqz_dict_type t
WHERE NOT EXISTS (SELECT 1 FROM financialdb4.sys_dict_type x WHERE x.dict_type=t.dict_type);

DROP TEMPORARY TABLE IF EXISTS tmp_htqz_dict_data;
CREATE TEMPORARY TABLE tmp_htqz_dict_data (
 dict_sort INT NOT NULL,
 dict_label VARCHAR(1000) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 dict_value VARCHAR(100) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 dict_type VARCHAR(100) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 remark VARCHAR(500) COLLATE utf8mb4_0900_ai_ci,
 PRIMARY KEY(dict_type,dict_value)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
INSERT INTO tmp_htqz_dict_data VALUES
 (1,'文本','String','sys_database_column_type','接口文本字段'),
 (2,'数值','Number','sys_database_column_type','接口数值字段'),
 (3,'明细列表','List','sys_database_column_type','接口明细列表字段'),
 (1,'融资性租赁','FINANCE_LEASE','LEASE_TYPE','融资租赁起租'),
 (2,'经营性租赁','OPERATING_LEASE','LEASE_TYPE','经营租赁起租'),
 (1,'直租','DIRECT_LEASE','LEASE_METHOD','融资租赁直租'),
 (2,'回租','SALE_AND_LEASEBACK','LEASE_METHOD','融资租赁回租'),
 (1,'零售融资租赁业务系统','RETAIL_FINANCE_LEASE','sys_form_source','零售融资租赁业务系统'),
 (2,'融资租赁业务系统','FINANCE_LEASE','sys_form_source','融资租赁业务系统'),
 (3,'户用光伏业务系统','HOUSEHOLD_PV','sys_form_source','户用光伏业务系统'),
 (4,'经营租赁业务系统','OPERATING_LEASE','sys_form_source','经营租赁业务系统'),
 (1,'直租项目起租','ZZ005','HTQZ_START_EVENT_VARIANT','Z005/Z0Z05/ZZ005标准化值'),
 (2,'直租投放后起租','ZZ008','HTQZ_START_EVENT_VARIANT','Z008/Z009/Z0Z08/Z0Z09/ZZ008标准化值'),
 (3,'回租项目起租','HZ002','HTQZ_START_EVENT_VARIANT','H002/HZ002标准化值'),
 (4,'回租投放后起租','HZ006','HTQZ_START_EVENT_VARIANT','H005/H006/HZ006标准化值'),
 (5,'户用光伏项目投放后起租','HY008','HTQZ_START_EVENT_VARIANT','H008/HY008标准化值'),
 (6,'户用光伏项目公司投放后起租','HY072','HTQZ_START_EVENT_VARIANT','H072/HY072标准化值'),
 (7,'户用光伏大商渠道投放后起租','HY063','HTQZ_START_EVENT_VARIANT','H063/HY063标准化值'),
 (8,'经营租赁资产出租','JY003','HTQZ_START_EVENT_VARIANT','J003/JY003标准化值'),
 (1,'融资直租-不动产','ZLYW_DIRECT_REAL_ESTATE','HTQZ_ACCOUNTING_VARIANT','融资租赁直租不动产科目组'),
 (2,'融资直租-动产','ZLYW_DIRECT_MOVABLE','HTQZ_ACCOUNTING_VARIANT','融资租赁直租动产科目组'),
 (3,'融资回租-不动产','ZLYW_LEASEBACK_REAL_ESTATE','HTQZ_ACCOUNTING_VARIANT','融资租赁回租不动产科目组'),
 (4,'融资回租-动产','ZLYW_LEASEBACK_MOVABLE','HTQZ_ACCOUNTING_VARIANT','融资租赁回租动产科目组'),
 (5,'户用光伏融资租赁','ZLYW_HOUSEHOLD_PV','HTQZ_ACCOUNTING_VARIANT','户用光伏融资租赁科目组'),
 (6,'户用光伏经营租赁','JYZL_HOUSEHOLD_PV','HTQZ_ACCOUNTING_VARIANT','户用光伏经营租赁科目组'),
 (1,'融资租赁资产','LEASE_ASSET','HTQZ_PRINCIPAL_OFFSET_TYPE','贷记融资租赁资产'),
 (2,'预付融资租赁设备款','PREPAID_ASSET','HTQZ_PRINCIPAL_OFFSET_TYPE','贷记预付设备款'),
 (3,'项目公司预付款','PROJECT_COMPANY_PREPAID','HTQZ_PRINCIPAL_OFFSET_TYPE','贷记项目公司预付款'),
 (4,'大商预付款','DEALER_PREPAID','HTQZ_PRINCIPAL_OFFSET_TYPE','贷记大商预付款'),
 (1,'统一合同起租','LEASE_START','sys_sub_scene_type','HTQZ统一起租凭证'),
 (1,'转账凭证','05','sys_voucher_type','HTQZ起租凭证');

INSERT INTO financehub_lease.sys_dict_data
 (dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,
  create_by,create_time,update_by,update_time,remark)
SELECT t.dict_sort,t.dict_label,t.dict_value,t.dict_type,NULL,NULL,'N','0',
       'htqz-interface-v1',NOW(),'htqz-interface-v1',NOW(),t.remark
FROM tmp_htqz_dict_data t
WHERE NOT EXISTS (
 SELECT 1 FROM financehub_lease.sys_dict_data x
 WHERE x.dict_type=t.dict_type AND x.dict_value=t.dict_value
);

INSERT INTO financialdb4.sys_dict_data
 (dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,
  create_by,create_time,update_by,update_time,remark)
SELECT t.dict_sort,t.dict_label,t.dict_value,t.dict_type,NULL,NULL,'N','0',
       'htqz-interface-v1',NOW(),'htqz-interface-v1',NOW(),t.remark
FROM tmp_htqz_dict_data t
WHERE NOT EXISTS (
 SELECT 1 FROM financialdb4.sys_dict_data x
 WHERE x.dict_type=t.dict_type AND x.dict_value=t.dict_value
);

-- The unified voucher references exactly these interface fields. Upsert keeps
-- the interface-config page complete even if an earlier environment missed the
-- multi-business migration.
SET @htqz_scene_id := (SELECT id FROM financehub_lease.eg_scene WHERE scene_code='HTQZ' AND del_flag='0' LIMIT 1);
DROP TEMPORARY TABLE IF EXISTS tmp_htqz_required_field;
CREATE TEMPORARY TABLE tmp_htqz_required_field (
 sort_no INT NOT NULL, field_name VARCHAR(100) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 field_code VARCHAR(100) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 data_type VARCHAR(50) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 PRIMARY KEY(field_code)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
INSERT INTO tmp_htqz_required_field VALUES
 (0,'事件编码','event_code','String'),(0,'系统来源','source_system','String'),
 (0,'业务发生日期','business_date','String'),(0,'核算主体代码','accounting_org_code','String'),
 (0,'业务板块','business_line','String'),(0,'租赁类型','lease_category','String'),
 (0,'租赁方式','lease_method','String'),(0,'融资租赁合同号','contract_no','String'),
 (0,'币种','currency','String'),(0,'实际投放金额','actual_disbursement','Number'),
 (0,'应收利息总额','interest_tax_inclusive','Number'),(0,'留购价','residual_value','Number'),
 (100,'资产类别','asset_category','String'),(101,'起租事件变体','start_event_variant','String'),
 (102,'本金结转方式','principal_offset_type','String'),(103,'核算配置变体','accounting_variant','String'),
 (110,'起租不含税本金','lease_principal_net','Number'),(111,'起租不含税利息','lease_interest_net','Number'),
 (112,'起租不含税留购价','residual_value_net','Number'),(113,'起租利息税额','lease_interest_vat','Number'),
 (114,'起租留购价税额','residual_value_vat','Number'),
 (120,'已收手续费未摊销不含税金额','received_fee_unamortized_net','Number'),
 (121,'未收取手续费不含税金额','unreceived_fee_net','Number'),
 (122,'未收手续费未摊销不含税金额','unreceived_fee_unamortized_net','Number'),
 (123,'未收取手续费税额','unreceived_fee_vat','Number'),
 (124,'未收取手续费未摊销税额','unreceived_fee_unamortized_vat','Number'),
 (125,'未收取手续费含税金额','unreceived_fee_gross','Number'),
 (126,'未摊销手续费不含税合计','fee_unamortized_net_total','Number'),
 (127,'应收手续费不含税金额','service_fee_net','Number'),
 (128,'应收手续费税额','service_fee_vat','Number'),
 (130,'经营租赁资产成本不含税金额','operating_asset_cost_net','Number'),
 (131,'客户融资不含税总额','customer_finance_net','Number');

SET @field_id := 2110000000000031000;
INSERT INTO financehub_lease.eg_scene_fields
 (id,scene_id,scene_code,scene_name,field_name,field_code,data_type,create_by,create_time,
  update_by,update_time,del_flag,parent_id,required_flag,sort_no)
SELECT (@field_id:=@field_id+1),@htqz_scene_id,'HTQZ','起租',f.field_name,f.field_code,f.data_type,
       'htqz-interface-v1',NOW(),'htqz-interface-v1',NOW(),'0',NULL,'0',f.sort_no
FROM tmp_htqz_required_field f
WHERE NOT EXISTS (
 SELECT 1 FROM financehub_lease.eg_scene_fields x
 WHERE x.scene_code='HTQZ' AND x.field_code=f.field_code AND x.del_flag='0'
);

-- Acceptance counts. Expected: missing_interface_fields=0 and each named
-- dictionary group has the count declared below.
SELECT COUNT(*) AS missing_interface_fields
FROM tmp_htqz_required_field f
LEFT JOIN financehub_lease.eg_scene_fields x
 ON x.scene_code='HTQZ' AND x.field_code=f.field_code AND x.del_flag='0'
WHERE x.id IS NULL;
SELECT dict_type,COUNT(*) AS definition_count
FROM financialdb4.sys_dict_data
WHERE dict_type IN ('sys_database_column_type','LEASE_TYPE','LEASE_METHOD','sys_form_source',
 'HTQZ_START_EVENT_VARIANT','HTQZ_ACCOUNTING_VARIANT','HTQZ_PRINCIPAL_OFFSET_TYPE',
 'sys_sub_scene_type','sys_voucher_type') AND status='0'
GROUP BY dict_type ORDER BY dict_type;
