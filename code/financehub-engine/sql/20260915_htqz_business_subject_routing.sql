-- HTQZ subject-routing closure:
-- raw interface condition -> canonical parameter -> derived accounting business
-- -> amount type -> exact account.  No default-business fallback is allowed by
-- the application for HTQZ.

START TRANSACTION;

-- Asset category is deliberately a small accounting-routing dictionary.  It
-- is not the detailed physical_asset_category dictionary used by asset cards.
INSERT INTO financehub_lease.sys_dict_type
 (dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
SELECT '起租资产类别','HTQZ_ASSET_CATEGORY','0','htqz-routing-v1',NOW(),
       'htqz-routing-v1',NOW(),'融资租赁起租科目路由：动产/不动产'
WHERE NOT EXISTS (
 SELECT 1 FROM financehub_lease.sys_dict_type WHERE dict_type='HTQZ_ASSET_CATEGORY'
);

INSERT INTO financialdb4.sys_dict_type
 (dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
SELECT '起租资产类别','HTQZ_ASSET_CATEGORY','0','htqz-routing-v1',NOW(),
       'htqz-routing-v1',NOW(),'融资租赁起租科目路由：动产/不动产'
WHERE NOT EXISTS (
 SELECT 1 FROM financialdb4.sys_dict_type WHERE dict_type='HTQZ_ASSET_CATEGORY'
);

DROP TEMPORARY TABLE IF EXISTS tmp_htqz_asset_category;
CREATE TEMPORARY TABLE tmp_htqz_asset_category (
 dict_sort INT NOT NULL,
 dict_label VARCHAR(100) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 dict_value VARCHAR(100) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 remark VARCHAR(500) COLLATE utf8mb4_0900_ai_ci,
 PRIMARY KEY(dict_value)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
INSERT INTO tmp_htqz_asset_category VALUES
 (1,'动产','MOVABLE','融资租赁起租科目路由'),
 (2,'不动产','REAL_ESTATE','融资租赁起租科目路由');

INSERT INTO financehub_lease.sys_dict_data
 (dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,
  create_by,create_time,update_by,update_time,remark)
SELECT t.dict_sort,t.dict_label,t.dict_value,'HTQZ_ASSET_CATEGORY',NULL,NULL,'N','0',
       'htqz-routing-v1',NOW(),'htqz-routing-v1',NOW(),t.remark
FROM tmp_htqz_asset_category t
WHERE NOT EXISTS (
 SELECT 1 FROM financehub_lease.sys_dict_data d
 WHERE d.dict_type='HTQZ_ASSET_CATEGORY' AND d.dict_value=t.dict_value
);

INSERT INTO financialdb4.sys_dict_data
 (dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,
  create_by,create_time,update_by,update_time,remark)
SELECT t.dict_sort,t.dict_label,t.dict_value,'HTQZ_ASSET_CATEGORY',NULL,NULL,'N','0',
       'htqz-routing-v1',NOW(),'htqz-routing-v1',NOW(),t.remark
FROM tmp_htqz_asset_category t
WHERE NOT EXISTS (
 SELECT 1 FROM financialdb4.sys_dict_data d
 WHERE d.dict_type='HTQZ_ASSET_CATEGORY' AND d.dict_value=t.dict_value
);

UPDATE eg_scene_fields
SET field_name='资产类别（融资租赁系统必填）',update_by='htqz-routing-v1',update_time=NOW()
WHERE scene_code='HTQZ' AND field_code='asset_category' AND del_flag='0';

-- Canonicalise the raw fields before rule execution.  The Java routing layer
-- also validates/normalises these values, so API and manual execution behave
-- consistently even when field-mapping cache is stale.
DROP TEMPORARY TABLE IF EXISTS tmp_htqz_route_mapping;
CREATE TEMPORARY TABLE tmp_htqz_route_mapping (
 id BIGINT NOT NULL,
 field_code VARCHAR(100) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 field_name VARCHAR(100) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 source_value VARCHAR(1000) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 target_value VARCHAR(200) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 target_field_code VARCHAR(100) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 PRIMARY KEY(id)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
INSERT INTO tmp_htqz_route_mapping VALUES
 (2110000000000041001,'assetCategory','资产类别-动产','动产,MOVABLE','MOVABLE','asset_category'),
 (2110000000000041002,'assetCategory','资产类别-不动产','不动产,REAL_ESTATE','REAL_ESTATE','asset_category'),
 (2110000000000041003,'leaseMethod','租赁方式-直租','直租,DIRECT,DIRECT_LEASE','DIRECT_LEASE','lease_method'),
 (2110000000000041004,'leaseMethod','租赁方式-回租','回租,LEASEBACK,SALE_AND_LEASEBACK','SALE_AND_LEASEBACK','lease_method');

INSERT INTO eg_field_mapping
 (id,system_code,field_code,field_name,source_value,target_value,default_value,
  create_by,create_time,update_by,update_time,del_flag,target_field_code)
SELECT t.id,'FINANCE_LEASE',t.field_code,t.field_name,t.source_value,t.target_value,NULL,
       'htqz-routing-v1',NOW(),'htqz-routing-v1',NOW(),'0',t.target_field_code
FROM tmp_htqz_route_mapping t
WHERE NOT EXISTS (
 SELECT 1 FROM eg_field_mapping m
 WHERE m.system_code='FINANCE_LEASE' AND m.field_code=t.field_code
   AND m.target_value=t.target_value AND m.target_field_code=t.target_field_code
   AND m.del_flag='0'
);

COMMIT;

-- Acceptance checks. Expected: 2, 4, 0, 0.
SELECT COUNT(*) AS asset_category_parameter_count
FROM financialdb4.sys_dict_data
WHERE dict_type='HTQZ_ASSET_CATEGORY' AND status='0';
SELECT COUNT(*) AS finance_route_mapping_count
FROM eg_field_mapping
WHERE system_code='FINANCE_LEASE' AND target_field_code IN ('asset_category','lease_method') AND del_flag='0';
SELECT COUNT(*) AS duplicate_active_subject_keys
FROM (
 SELECT business_code,fund_type
 FROM eg_account WHERE del_flag='0'
 GROUP BY business_code,fund_type HAVING COUNT(*)>1
) d;
SELECT COUNT(*) AS duplicate_htqz_amount_entries
FROM (
 SELECT e.fund_type
 FROM eg_scene_voucher v
 JOIN eg_scene_voucher_entry e ON e.scene_voucher_id=v.id AND e.del_flag='0'
 WHERE v.scene_id=(SELECT id FROM eg_scene WHERE scene_code='HTQZ' AND del_flag='0' LIMIT 1)
   AND v.del_flag='0'
 GROUP BY e.fund_type HAVING COUNT(*)>1
) d;
