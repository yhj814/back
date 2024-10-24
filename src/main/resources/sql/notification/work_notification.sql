
create table tbl_work_notification (
                                       id bigint unsigned auto_increment primary key,
                                       member_id bigint unsigned not null,  -- 구매자 id 입니다
                                       work_id bigint unsigned not null,
                                       constraint fk_buy_work_notification_member foreign key (member_id)
                                           references tbl_member (id),
                                       constraint fk_work_notification_buy_work foreign key (work_id)
                                           references tbl_work (id)
);