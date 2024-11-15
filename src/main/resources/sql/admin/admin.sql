use gb;
show tables;

create table tbl_admin (
    id bigint unsigned auto_increment primary key,
    admin_verify_code varchar(255) not null
);

select *from tbl_admin;

drop table tbl_admin;

insert into tbl_admin
values (1,'111111');
