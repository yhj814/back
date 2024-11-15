create table tbl_reply_report (
    id bigint unsigned auto_increment primary key,
    report_contents varchar(255) not null,
    reply_id bigint unsigned not null,
    created_date datetime default current_timestamp,
    report_status varchar(255) default 'REPORT',
    member_profile_id bigint unsigned not null,
    constraint fk_reply_report_reply foreign key (reply_id)
    references tbl_reply(id),
    constraint fk_reply_report_member_profile foreign key (member_profile_id)
    references tbl_member_profile(id)
);

select * from tbl_reply_report;

insert into tbl_reply_report
values (2,'검토 부탁드립니다',6,now(),default,3);

select * from tbl_reply;
