create table tbl_funding_product (
    id bigint unsigned auto_increment primary key,
    product_name varchar(255) not null,
    product_price int not null,
    product_amount int not null,
    funding_id bigint unsigned not null,
    status varchar(255) not null default '상품 게시 중',
    constraint fk_funding_product_funding foreign key (funding_id)
    references tbl_funding(id)
);

insert into tbl_funding_product
values (17,'상품15',20000,'20',76);

select *from tbl_funding_product;