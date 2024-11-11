create table tbl_apply_audition_notification (
    id bigint unsigned auto_increment primary key,
    audition_application_id bigint unsigned not null,
    constraint fk_apply_audition_notification_audition_application foreign key (audition_application_id)
        references tbl_audition_application (id)
);
alter table tbl_apply_audition_notification
    add constraint apply_audition_notification_member
        foreign key (member_id) references tbl_member (id);
