create table tbl_file (
                          id bigint unsigned auto_increment primary key,
                          file_size varchar(255) not null,
                          file_type varchar(255) not null, -- 영상, 사진, 썸네일 등
                          file_path varchar(255) not null,
                          file_content varchar(255),
                          created_date datetime default current_timestamp,
                          updated_date datetime default current_timestamp
);