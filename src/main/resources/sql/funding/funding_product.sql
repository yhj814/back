create table tbl_funding_product (
    id bigint unsigned auto_increment primary key,
    product_name varchar(255) not null,
    product_price int not null,
    product_amount int not null,
    funding_id bigint unsigned not null,
    constraint fk_funding_product_funding foreign key (funding_id)
    references tbl_funding(id)
);