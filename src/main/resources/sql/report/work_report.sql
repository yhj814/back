create table tbl_work_report (
    id bigint unsigned auto_increment primary key,
    report_contents varchar(255) not null,
    work_id bigint unsigned not null,
    created_date datetime default current_timestamp,
    constraint fk_work_report_funding foreign key (work_id)
    references tbl_work(id)
);
