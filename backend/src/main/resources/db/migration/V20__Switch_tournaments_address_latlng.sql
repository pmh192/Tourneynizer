ALTER TABLE tournaments DROP COLUMN address;
ALTER TABLE tournaments ADD COLUMN lat double precision NOT NULL;
ALTER TABLE tournaments ADD COLUMN lng double precision NOT NULL;
