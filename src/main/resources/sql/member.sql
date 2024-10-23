create table tbl_member(
                           id bigint unsigned auto_increment primary key,
                           member_name varchar(255) not null,
                           member_email varchar(255) not null,
                           profile_img_url varchar(255),
                           created_date datetime default current_timestamp,
                           updated_date datetime default current_timestamp
);
