CREATE TABLE IF NOT EXISTS product_complaint (

    id UUID NOT NULL PRIMARY KEY,
    product_id varchar(100),
    complainer_name varchar(100),
    product_complaint varchar(500),
    country varchar(20),
    complaint_date timestamp,
    counter int default 0,
    CONSTRAINT unique_name_and_product_id UNIQUE (complainer_name, product_id)

    );