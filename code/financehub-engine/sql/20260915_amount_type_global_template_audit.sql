-- 全局配置审计后的闭环处理：补齐通用金额类型，停用不可执行的旧尾差模板。
-- 旧尾差模板使用的金额类型既不在华夏金租金额类型字典，也无有效科目映射；
-- 它们是旧项目配置，不修改或删除已经生成的历史凭证。

USE financehub_lease;
START TRANSACTION;

DROP TEMPORARY TABLE IF EXISTS tmp_missing_general_amount_type;
CREATE TEMPORARY TABLE tmp_missing_general_amount_type (
 dict_sort INT NOT NULL,dict_label VARCHAR(100) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 dict_value VARCHAR(100) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 PRIMARY KEY(dict_value)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
INSERT INTO tmp_missing_general_amount_type VALUES
 (931,'拆放同业应收利息','placement_interest_receivable'),
 (932,'境内银行业存款类金融机构利息收入','deposit_interest_income'),
 (933,'即征即退销项税额（贷款服务）','output_vat_payable_share');

INSERT INTO financehub_lease.sys_dict_data
 (dict_sort,dict_label,dict_value,dict_type,is_default,status,create_by,create_time,
  update_by,update_time,remark)
SELECT t.dict_sort,t.dict_label,t.dict_value,'sys_cash_type','N','0',
 'amount-closure-v1',NOW(),'amount-closure-v1',NOW(),'跨业务可复用的经济事项'
FROM tmp_missing_general_amount_type t
WHERE NOT EXISTS (SELECT 1 FROM financehub_lease.sys_dict_data d
 WHERE d.dict_type='sys_cash_type' AND d.dict_value=t.dict_value);

INSERT INTO financialdb4.sys_dict_data
 (dict_sort,dict_label,dict_value,dict_type,is_default,status,create_by,create_time,
  update_by,update_time,remark)
SELECT t.dict_sort,t.dict_label,t.dict_value,'sys_cash_type','N','0',
 'amount-closure-v1',NOW(),'amount-closure-v1',NOW(),'跨业务可复用的经济事项'
FROM tmp_missing_general_amount_type t
WHERE NOT EXISTS (SELECT 1 FROM financialdb4.sys_dict_data d
 WHERE d.dict_type='sys_cash_type' AND d.dict_value=t.dict_value);

-- 旧 01~13 尾差模板缺少金额类型和科目配置。现行可执行尾差模板保留。
UPDATE eg_scene_voucher
SET del_flag='1',update_by='amount-closure-v1',update_time=NOW()
WHERE id BETWEEN 1795412364263837698 AND 1795412364289003529
 AND scene_id=(SELECT id FROM eg_scene WHERE scene_code='WCTZ' AND del_flag='0' LIMIT 1)
 AND LEFT(scene_voucher_name,2) BETWEEN '01' AND '13' AND del_flag='0';

COMMIT;

SELECT COUNT(*) AS active_template_amount_types_missing_dictionary
FROM eg_scene_voucher_entry e
JOIN eg_scene_voucher v ON v.id=e.scene_voucher_id AND v.del_flag='0'
JOIN eg_scene s ON s.id=v.scene_id AND s.del_flag='0'
LEFT JOIN financehub_lease.sys_dict_data d ON d.dict_type='sys_cash_type'
 AND d.dict_value=e.fund_type AND d.status='0'
WHERE e.del_flag='0' AND d.dict_code IS NULL;
SELECT COUNT(*) AS active_account_amount_types_missing_dictionary
FROM eg_account a
LEFT JOIN financehub_lease.sys_dict_data d ON d.dict_type='sys_cash_type'
 AND d.dict_value=a.fund_type AND d.status='0'
WHERE a.del_flag='0' AND d.dict_code IS NULL;
