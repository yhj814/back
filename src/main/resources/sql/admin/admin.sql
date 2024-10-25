use gb;
show tables;

create table tbl_admin (
    id bigint unsigned auto_increment primary key,
    admin_verify_code varchar(255) not null
);

select *from tbl_admin;

drop table tbl_admin;
