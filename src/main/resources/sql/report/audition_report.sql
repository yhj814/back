create table tbl_audition_report (
    id bigint unsigned auto_increment primary key,
    report_contents varchar(255) not null,
    audition_id bigint unsigned not null,
    created_date datetime default current_timestamp,
    report_status varchar(255) default 'REPORT',
    member_profile_id bigint unsigned not null,
    constraint fk_audition_report_audition foreign key (audition_id)
    references tbl_audition(id),
    constraint fk_audition_report_member_profile foreign key (member_profile_id)
    references tbl_member_profile(id)
);


select *from tbl_audition_report;

insert into tbl_audition_report
values (23,'영상모집ㄴㅁㅇㄴㅁㄴㅇㅇㅁㅁㅁㄴ',59,now(),default,12);

