create table tbl_work_report (
    id bigint unsigned auto_increment primary key,
    report_contents varchar(255) not null,
    work_id bigint unsigned not null,
    created_date datetime default current_timestamp,
    report_status varchar(255) default 'REPORT',
    member_profile_id bigint unsigned not null,
    constraint fk_work_report_work foreign key (work_id)
    references tbl_work(id),
    constraint fk_work_report_member_profile foreign key (member_profile_id)
    references tbl_member_profile(id)
);

select *from tbl_work_report;

insert into tbl_work_report
values (19,'신고내용19',44,now(),default,10);

select *from tbl_member_profile;

select *from tbl_reply;

drop table tbl_work_report;

