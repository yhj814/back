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
values (3,'현실성을 떨어뜨리는 연출과 전개 방식은 관객의 이해를 저해할 수 있습니다. 이는 서사의 논리적 일관성과 관객의 몰입을 방해할 가능성이 높습니다.',3,now(),default,2);

select *from tbl_member_profile;

select *from tbl_reply;

drop table tbl_work_report;

