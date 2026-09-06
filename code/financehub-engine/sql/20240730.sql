ALTER TABLE eg_interface_data ALTER COLUMN ebank_serial_number TYPE text USING ebank_serial_number::text;
ALTER TABLE eg_interface_data ALTER COLUMN ebank_number TYPE text USING ebank_number::text;
ALTER TABLE eg_interface_data ALTER COLUMN ebank_batch_no TYPE text USING ebank_batch_no::text;
