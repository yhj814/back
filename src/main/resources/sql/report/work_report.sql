create table tbl_work_report (
    id bigint unsigned auto_increment primary key,
    report_contents varchar(255) not null,
    work_id bigint unsigned not null,
    created_date datetime default current_timestamp,
    report_status varchar(255) default 'REPORT',
    constraint fk_work_report_work foreign key (work_id)
    references tbl_work(id)
);

select *from tbl_work_report;

insert into tbl_work_report
values (6,'신고내용6',15,now(),default);

drop table tbl_work_report;