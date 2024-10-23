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