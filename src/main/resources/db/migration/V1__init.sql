CREATE TABLE voucher (
  ID SERIAL,
  public_id TEXT,
  code TEXT,
  phone TEXT,
  date_generate timestamp,
  CONSTRAINT voucher_pk PRIMARY KEY (ID)
);


CREATE TABLE voucher_failed (
  ID SERIAL,
  phone TEXT,
  date_generate timestamp,
  CONSTRAINT voucher_failed_pk PRIMARY KEY (ID)
);