create table tbl_apply_audition_notification (
    id bigint unsigned auto_increment primary key,
    member_profile_id bigint unsigned null,
    audition_application_id bigint unsigned not null,
    message varchar(255) null,
    is_read tinyint(1) default 0 null,
    create_date datetime default CURRENT_TIMESTAMP null,
    sub_type varchar(255) null,
    constraint fk_apply_audition_notification_member_profile
    foreign key (member_profile_id) references tbl_member_profile (id),
    constraint fk_apply_audition_notification_audition_application
    foreign key (audition_application_id) references tbl_audition_application (id)
);

create table tbl_funding_product_notification (
    id bigint unsigned auto_increment primary key,
    member_profile_id bigint unsigned not null,
    buy_funding_product_id bigint unsigned not null,
    message varchar(255) null,
    is_read tinyint default 0 null,
    create_date datetime default CURRENT_TIMESTAMP null,
    sub_type varchar(255) null,
    constraint fk_funding_product_notification_member_profile
    foreign key (member_profile_id) references tbl_member_profile (id),
    constraint fk_funding_product_notification_buy_funding_product
    foreign key (buy_funding_product_id) references tbl_buy_funding_product (id)
);

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


create table tbl_work_notification (
    id bigint unsigned auto_increment primary key,
    member_profile_id bigint unsigned null,
    buy_work_id bigint unsigned not null,
    message varchar(255) null,
    is_read tinyint default 0 null,
    create_date datetime default CURRENT_TIMESTAMP null,
    sub_type varchar(255) null,
    constraint fk_work_notification_member_profile
    foreign key (member_profile_id) references tbl_member_profile (id),
    constraint fk_work_notification_buy_work
    foreign key (buy_work_id) references tbl_buy_work (id)
);


