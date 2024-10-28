create table tbl_buy_funding_product (
                                         id bigint unsigned auto_increment primary key,
                                         member_id bigint unsigned not null,
                                         funding_product_id bigint unsigned not null,
                                         funding_send_status varchar(255) default 0,
                                         constraint fk_buy_funding_product_member foreign key (member_id)
                                             references tbl_member (id),
                                         constraint fk_buy_funding_product_funding_product foreign key (funding_product_id)
                                             references tbl_funding_product (id)
);

select *
from tbl_funding;

select id, product_name, product_price, product_amount, funding_id
from tbl_funding_product;
insert into tbl_funding_product
(product_name, product_price, product_amount, funding_id)
values ('펀딩 상품3 이름 test4', '40000', '50', 3);

select id, member_id, funding_product_id, funding_send_status
from tbl_buy_funding_product;
insert into tbl_buy_funding_product
(member_id, funding_product_id, funding_send_status)
values ();