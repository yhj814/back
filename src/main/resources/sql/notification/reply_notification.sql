create table tbl_reply_notification (
    id bigint unsigned auto_increment primary key,
    reply_id bigint unsigned not null,
    member_profile_id bigint unsigned null,
    message varchar(255) null,
    is_read tinyint(1) default 0 null,
    create_date datetime default CURRENT_TIMESTAMP null,
    sub_type varchar(255) null,
    constraint fk_reply_notification_member_profile
        foreign key (member_profile_id) references tbl_member_profile (id),
    constraint fk_reply_notification_reply
        foreign key (reply_id) references tbl_reply (id)
);
