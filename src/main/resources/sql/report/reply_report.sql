create table tbl_reply_report (
    id bigint unsigned auto_increment primary key,
    report_contents varchar(255) not null,
    reply_id bigint unsigned not null,
    created_date datetime default current_timestamp,
    constraint fk_reply_report_reply foreign key (reply_id)
    references tbl_reply(id)
);