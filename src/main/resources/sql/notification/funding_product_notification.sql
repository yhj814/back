create table tbl_funding_product_notification (
                                                  id bigint unsigned auto_increment primary key,
                                                  member_profile_id bigint unsigned not null,
                                                  buy_funding_product_id bigint unsigned not null,
                                                  message varchar(255) null,
                                                  is_read tinyint default 0 null,
                                                  create_date datetime default CURRENT_TIMESTAMP null,
                                                  constraint fk_funding_product_notification_member_profile
                                                      foreign key (member_profile_id) references tbl_member_profile (id),
                                                  constraint fk_funding_product_notification_buy_funding_product
                                                      foreign key (buy_funding_product_id) references tbl_buy_funding_product (id)
);