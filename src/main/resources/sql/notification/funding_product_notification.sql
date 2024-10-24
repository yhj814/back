create table tbl_funding_product_notification (
                                                  id bigint unsigned auto_increment primary key,
                                                  member_id bigint unsigned not null, -- 구매자 id 입니다.
                                                  funding_product_id bigint unsigned not null,
                                                  constraint fk_buy_funding_product_notification_member foreign key (member_id)
                                                      references tbl_member (id),
                                                  constraint fk_funding_product_notification_funding_product foreign key (funding_product_id)
                                                      references tbl_funding_product (id)
);