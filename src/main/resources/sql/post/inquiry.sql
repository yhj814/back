create table tbl_inquiry (
    id bigint unsigned auto_increment primary key,
    inquiry_status varchar(255) default 0,
    constraint fk_inquiry_post foreign key (id)
    references tbl_post(id)
);

select *from tbl_inquiry;

SELECT * FROM tbl_inquiry LIMIT 0, 10; -- 쿼리를 직접 실행해 보기
