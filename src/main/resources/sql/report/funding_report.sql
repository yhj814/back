create table tbl_funding_report (
    id bigint unsigned auto_increment primary key,
    report_contents varchar(255) not null,
    funding_id bigint unsigned not null,
    created_date datetime default current_timestamp,
    report_status varchar(255) default 'Report',
        constraint fk_funding_report_funding foreign key (funding_id)
    references tbl_funding(id)
);
