--核销详情凭证id类型有Long改为varchar
--生产已执行 start
ALTER TABLE financialdb.eg_verification_details ALTER COLUMN voucher_id TYPE varchar(2000);
COMMENT ON COLUMN financialdb.eg_verification_details.voucher_id IS '凭证id,多个按照逗号分隔';
--生产已执行 end