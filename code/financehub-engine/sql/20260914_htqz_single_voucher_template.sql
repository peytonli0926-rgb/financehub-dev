-- Consolidate all HTQZ lease-start voucher rules into one voucher.
-- Each fund_type is retained once. The former voucher-level filters are moved
-- down into mutually-exclusive condition rows of that single fund_type.
-- This script is intentionally ordered after and supersedes
-- 20260914_htqz_multi_business_start_templates.sql.

START TRANSACTION;

SET @htqz_scene_id := (SELECT id FROM eg_scene WHERE scene_code='HTQZ' AND del_flag='0' LIMIT 1);
SET @unified_voucher_id := 2110000000000000200;
SET @already_single := (
 SELECT IF(COUNT(*)=1 AND MAX(id)=@unified_voucher_id,1,0)
 FROM eg_scene_voucher WHERE scene_id=@htqz_scene_id AND del_flag='0'
);

DROP TEMPORARY TABLE IF EXISTS tmp_htqz_old_rule;
CREATE TEMPORARY TABLE tmp_htqz_old_rule (
 old_voucher_id BIGINT NOT NULL,
 old_entry_id BIGINT NOT NULL,
 voucher_condition VARCHAR(2000) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 fund_type VARCHAR(100) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 voucher_summary VARCHAR(1000) COLLATE utf8mb4_0900_ai_ci,
 old_serial INT NOT NULL,
 entry_condition VARCHAR(2000) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 condition_description VARCHAR(1000) COLLATE utf8mb4_0900_ai_ci,
 amount_script VARCHAR(2000) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 amount_description VARCHAR(1000) COLLATE utf8mb4_0900_ai_ci,
 debit_credit_type VARCHAR(10) COLLATE utf8mb4_0900_ai_ci NOT NULL
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO tmp_htqz_old_rule
SELECT v.id,e.id,COALESCE(NULLIF(v.script_condition,''),'1==1'),e.fund_type,e.voucher_summary,
       c.serial,COALESCE(NULLIF(c.script_condition,''),'1==1'),c.dondition_description,
       c.script_amount,c.amount_description,c.debit_credit_type
FROM eg_scene_voucher v
JOIN eg_scene_voucher_entry e ON e.scene_voucher_id=v.id AND e.del_flag='0'
JOIN eg_scene_voucher_condition c ON c.scene_voucher_entry_id=e.id AND c.del_flag='0'
WHERE v.scene_id=@htqz_scene_id AND v.del_flag='0' AND @already_single=0;

DELETE c FROM eg_scene_voucher_condition c
JOIN eg_scene_voucher_entry e ON e.id=c.scene_voucher_entry_id
JOIN eg_scene_voucher v ON v.id=e.scene_voucher_id
WHERE v.scene_id=@htqz_scene_id AND @already_single=0;

DELETE e FROM eg_scene_voucher_entry e
JOIN eg_scene_voucher v ON v.id=e.scene_voucher_id
WHERE v.scene_id=@htqz_scene_id AND @already_single=0;

DELETE FROM eg_scene_voucher
WHERE scene_id=@htqz_scene_id AND @already_single=0;

INSERT INTO eg_scene_voucher
 (id,scene_id,source,voucher_type,company,business_date,voucher_date,currency,dept_name,
  voucher_summary,create_by,create_time,update_by,update_time,del_flag,script_condition,
  scene_voucher_name,sub_scene_type)
SELECT @unified_voucher_id,@htqz_scene_id,'{起租接口表.系统来源}','05',
       '{起租接口表.核算主体代码}','{起租接口表.业务发生日期}',
       '{起租接口表.业务发生日期}','{起租接口表.币种}',NULL,
       "'起租业务-'+{起租接口表.融资租赁合同号}",
       'htqz-single-voucher-v1',NOW(),'htqz-single-voucher-v1',NOW(),'0','1==1',
       '统一起租凭证','LEASE_START'
WHERE @already_single=0;

DROP TEMPORARY TABLE IF EXISTS tmp_htqz_unique_entry;
CREATE TEMPORARY TABLE tmp_htqz_unique_entry AS
SELECT fund_type,MIN(voucher_summary) AS voucher_summary
FROM tmp_htqz_old_rule
GROUP BY fund_type;

SET @entry_id := 2110000000000011000;
INSERT INTO eg_scene_voucher_entry
 (id,scene_voucher_id,fund_type,relate_bank_flag,bank_account,cash_attribute,voucher_summary,
  create_by,create_time,update_by,update_time,del_flag,cash_attribute_flag,assist_flags)
SELECT (@entry_id := @entry_id+1),@unified_voucher_id,fund_type,'0',NULL,NULL,voucher_summary,
       'htqz-single-voucher-v1',NOW(),'htqz-single-voucher-v1',NOW(),'0','0','0,1'
FROM tmp_htqz_unique_entry
ORDER BY fund_type;

DROP TEMPORARY TABLE IF EXISTS tmp_htqz_unified_condition;
CREATE TEMPORARY TABLE tmp_htqz_unified_condition AS
SELECT fund_type,
       ROW_NUMBER() OVER (PARTITION BY fund_type ORDER BY old_voucher_id,old_entry_id,old_serial) AS new_serial,
       CASE WHEN voucher_condition='1==1' THEN entry_condition
            ELSE CONCAT('(',voucher_condition,')&&(',entry_condition,')') END AS script_condition,
       CASE WHEN voucher_condition='1==1' THEN COALESCE(condition_description,entry_condition)
            ELSE CONCAT('模板条件：',voucher_condition,'；金额条件：',COALESCE(condition_description,entry_condition)) END AS condition_description,
       amount_script,amount_description,debit_credit_type
FROM tmp_htqz_old_rule;

SET @condition_id := 2110000000000021000;
INSERT INTO eg_scene_voucher_condition
 (id,scene_voucher_entry_id,serial,script_condition,dondition_description,script_amount,
  amount_description,debit_credit_type,create_by,create_time,update_by,update_time,del_flag)
SELECT (@condition_id := @condition_id+1),e.id,c.new_serial,c.script_condition,
       c.condition_description,c.amount_script,c.amount_description,c.debit_credit_type,
       'htqz-single-voucher-v1',NOW(),'htqz-single-voucher-v1',NOW(),'0'
FROM tmp_htqz_unified_condition c
JOIN eg_scene_voucher_entry e ON e.scene_voucher_id=@unified_voucher_id
 AND e.fund_type=c.fund_type AND e.del_flag='0'
ORDER BY e.id,c.new_serial;

COMMIT;

-- Acceptance checks: one template; each amount type occurs only once; every
-- amount type has at least one condition branch.
SELECT COUNT(*) AS voucher_count
FROM eg_scene_voucher WHERE scene_id=@htqz_scene_id AND del_flag='0';
SELECT COUNT(*) AS entry_count
FROM eg_scene_voucher_entry WHERE scene_voucher_id=@unified_voucher_id AND del_flag='0';
SELECT COUNT(*) AS condition_count
FROM eg_scene_voucher_condition c
JOIN eg_scene_voucher_entry e ON e.id=c.scene_voucher_entry_id
WHERE e.scene_voucher_id=@unified_voucher_id AND c.del_flag='0';
SELECT COUNT(*) AS duplicate_fund_type_groups FROM (
 SELECT fund_type FROM eg_scene_voucher_entry
 WHERE scene_voucher_id=@unified_voucher_id AND del_flag='0'
 GROUP BY fund_type HAVING COUNT(*)>1
) d;
SELECT COUNT(*) AS entries_without_condition
FROM eg_scene_voucher_entry e
LEFT JOIN eg_scene_voucher_condition c ON c.scene_voucher_entry_id=e.id AND c.del_flag='0'
WHERE e.scene_voucher_id=@unified_voucher_id AND e.del_flag='0' AND c.id IS NULL;
