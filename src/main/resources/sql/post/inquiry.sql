create table tbl_inquiry (
    id bigint unsigned auto_increment primary key,
    inquiry_status varchar(255) default 0, -- 0 미답변, 1 답변
    constraint fk_inquiry_post foreign key (id)
    references tbl_post(id)
);