create table tbl_audition_application (
    id bigint unsigned auto_increment primary key,
    member_profile_id bigint unsigned not null,
    apply_etc varchar(255) not null,
    audition_id bigint unsigned not null,
    created_date datetime default current_timestamp,
    confirm_status varchar(255) default 'NO',
    constraint fk_audition_application_member_profile foreign key (member_profile_id)
    references tbl_member_profile(id),
    constraint fk_audition_application_audition foreign key (audition_id)
    references tbl_audition(id)
);

select *from tbl_audition_application;