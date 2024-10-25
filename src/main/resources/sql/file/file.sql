create table tbl_file (
                          id bigint unsigned auto_increment primary key,
                          file_name varchar(255) not null,
                          file_size varchar(255) not null,
                          file_type varchar(255) default 0, -- 영상, 사진, 썸네일 등
                          file_path varchar(255) not null,
                          created_date datetime default current_timestamp,
                          updated_date datetime default current_timestamp
);