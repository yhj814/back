create table tbl_buy_funding_product (
    id bigint unsigned auto_increment primary key,
    member_profile_id bigint unsigned not null, -- 구매자
    funding_product_id bigint unsigned not null,
    funding_send_status varchar(255) default 0,
    constraint fk_buy_funding_product_member_profile foreign key (member_profile_id)
    references tbl_member_profile (id),
    constraint fk_buy_funding_product_funding_product foreign key (funding_product_id)
    references tbl_funding_product (id)
);

select *
from tbl_funding;

select *
from tbl_member;

select *
from tbl_member_profile;
insert into tbl_member_profile
(profile_name, profile_nickname, profile_gender, profile_age, profile_email, profile_phone, member_id)
values ('홍길동', '너구리쌤', '남', '30', 'test2@gmail.com', '01029390392', 2);

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