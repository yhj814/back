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