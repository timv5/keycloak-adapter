create table customers
(
    customer_id bigint,
    email varchar(50),
    username varchar(256),
    last_name varchar(256),
    first_name varchar(256)
);

CREATE SEQUENCE customers_seq
    START WITH 1
    INCREMENT BY 1;