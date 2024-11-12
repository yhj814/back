create table tbl_work_notification (
    id bigint unsigned auto_increment primary key,
    member_profile_id bigint unsigned null,
    buy_work_id bigint unsigned not null,
    message varchar(255) null,
    is_read tinyint default 0 null,
    create_date datetime default CURRENT_TIMESTAMP null,
    constraint fk_work_notification_member_profile
    foreign key (member_profile_id) references tbl_member_profile (id),
    constraint fk_work_notification_buy_work
    foreign key (buy_work_id) references tbl_buy_work (id)
);


select id, member_id, work_id from
tbl_work_notification;


