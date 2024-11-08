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

select mp.id, mp.profile_name, mp.profile_nickname, mp.profile_gender, mp.profile_age, mp.profile_email,
       mp.profile_phone, mp.profile_etc, mp.member_id, mp.created_date, mp.updated_date,
       m.id, m.member_status, fl.file_name, fl.file_path
from tbl_member_profile mp
join tbl_member m on mp.member_id = m.id and m.id = 2
join tbl_member_profile_file mpf on mpf.member_profile_id = mp.id
join tbl_file fl on fl.id = mpf.id;

update tbl_member_profile
set profile_nickname = '수정된 닉네임', profile_gender = '남성',
    profile_age = '23', profile_email = 'update@gmail.com', profile_phone = '010-3333-3333',
    profile_etc = '수정된 추가사항'
where member_id = 2;

insert into tbl_file(file_name, file_size, file_type, file_path)
values ('회원프로필 파일 이름3', '파일 사이즈', 'pdf', '2024/11/08');
insert into tbl_member_profile_file(id, member_profile_id)
values (14, 2);

select * from tbl_file;
select * from tbl_member_profile_file;
select * from tbl_member_profile;