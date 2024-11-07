create table tbl_member_profile (
    id bigint unsigned auto_increment primary key,
    profile_name varchar(255) not null,
    profile_nickname varchar(255) not null,
    profile_gender varchar(255) not null,
    profile_age varchar(255) not null,
    profile_email varchar(255) not null,
    profile_phone varchar(255) not null,
    profile_etc varchar(255),
    member_id bigint unsigned not null,
    created_date datetime default current_timestamp,
    updated_date datetime default current_timestamp,
    constraint fk_member_profile_member foreign key (member_id)
    references tbl_member(id)
);

select *from tbl_member_profile;

alter table tbl_member_profile
    add profile_name varchar(255) not null;

alter table tbl_member_profile
    add profile_nickname varchar(255) not null;

alter table tbl_member_profile
    add created_date datetime default current_timestamp;

DELETE from tbl_member_profile where profile_age = 12;

insert into tbl_member_profile(profile_name,profile_nickname,profile_gender,profile_age,profile_email,profile_phone,member_id)
value ('글모집7','글모집7','남','28',
       'qwqwd@gmail.com','01019674112','27');

delete from tbl_member_profile;