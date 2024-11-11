create table tbl_work (
    id bigint unsigned primary key,
    work_price varchar(255) not null,
    genre_type varchar(255) not null,
    file_content varchar(255),
    read_count int default 0,
    thumbnail_file_id bigint unsigned,
    constraint fk_work_post foreign key (id)
    references tbl_post(id),
    constraint fk_work_thumbnail_file foreign key (thumbnail_file_id)
    references tbl_file(id)
);

insert into tbl_work
values(44,30000,'comedy','파일설명9','64');

select *from tbl_work;