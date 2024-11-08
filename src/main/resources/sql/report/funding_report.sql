create table tbl_funding_report (
    id bigint unsigned auto_increment primary key,
    report_contents varchar(255) not null,
    funding_id bigint unsigned not null,
    created_date datetime default current_timestamp,
    report_status varchar(255) default 'REPORT',
    member_profile_id bigint unsigned not null,
    constraint fk_funding_report_funding foreign key (funding_id)
    references tbl_funding(id),
    constraint fk_funding_report_member_profile foreign key (member_profile_id)
    references tbl_member_profile(id)
);

select  *from tbl_funding_report;

insert into tbl_funding_report
values (30,'펀딩신고27',75,now(),default,1);

select*from tbl_member_profile;