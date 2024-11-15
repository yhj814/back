create table tbl_file (
    id bigint unsigned auto_increment primary key,
    file_name longtext not null,
    file_size varchar(255) not null,
    file_type varchar(255), -- 영상, 사진, 파일(그 외 파일: pdf, hwp 등 )
    file_path varchar(255) not null,
    created_date datetime default current_timestamp,
    updated_date datetime default current_timestamp
);

select * from tbl_file;