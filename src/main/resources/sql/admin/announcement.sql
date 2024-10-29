create table tbl_announcement(
    id bigint unsigned auto_increment primary key,
    announcement_title varchar(255) not null,
    announcement_content varchar(255) not null,
    created_date datetime default current_timestamp,
    updated_date datetime default current_timestamp
);

select * from tbl_announcement;

drop table tbl_announcement;