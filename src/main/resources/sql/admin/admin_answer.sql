create table tbl_admin_answer (
    id bigint unsigned auto_increment primary key,
    admin_answer_content varchar(255) not null,
    inquiry_id bigint unsigned not null,
    created_date datetime default current_timestamp,
    constraint fk_admin_answer_inquiry foreign key (inquiry_id)
       references tbl_inquiry(id)
);

select *from tbl_admin_answer;