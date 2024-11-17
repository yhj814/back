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
values (17,'작품이 현실과 꿈의 경계를 다루면서 관객에게 혼란을 줄 수 있는 모호한 표현이나 부적절한 상징성을 사용한 점이 우려됩니다.',65,now(),default,5);

select *from tbl_member_profile;

select *from tbl_post;

