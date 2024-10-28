create table tbl_member_profile (
    id bigint unsigned auto_increment primary key,
    profile_name varchar(255) not null,
    profile_nickname varchar(255) not null,
    profile_gender varchar(255) default 0,
    profile_age varchar(255) default 0,
    profile_email varchar(255) not null,
    profile_phone varchar(255) not null,
    profile_etc varchar(255),
    member_id bigint unsigned not null,
    updated_date datetime default current_timestamp,
    constraint fk_member_profile_member foreign key (member_id)
    references tbl_member(id)
);

select *from tbl_member_profile;

alter table tbl_member_profile
    add profile_name varchar(255) not null;

alter table tbl_member_profile
    add profile_nickname varchar(255) not null;
