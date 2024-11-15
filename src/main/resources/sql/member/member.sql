create table tbl_member(
    id bigint unsigned auto_increment primary key,
    member_email varchar(255) not null,
    member_status varchar(255) default 'YES',
    profile_img_url varchar(255),
    created_date datetime default current_timestamp,
    updated_date datetime default current_timestamp
);

select * from tbl_member;

DELETE from tbl_member where id = 1;

insert into tbl_member (member_email, profile_img_url)
values ( 'john.doe@example.com', 'profile.jpg');

insert into tbl_member (member_email, profile_img_url)
values ( 'test37@gamail.com', 'profile37.jpg');

use gb;

create database gb;

select * from tbl_member;
select * from tbl_member_profile;

select mp.profile_name, mp.profile_nickname, mp.profile_gender, mp.profile_age, mp.profile_email,
       mp.profile_phone, mp.profile_etc, mp.member_id, mp.created_date, mp.updated_date,
       m.id, m.member_status
from tbl_member_profile mp
         join tbl_member m on mp.member_id = m.id
                              and m.member_status = 'YES' and m.id = 1;

# <update id="softDelete">
# UPDATE TBL_MEMBER
# SET STATUS = 0
# WHERE ID = #{id}
#           </update>

update tbl_member
set member_status = 'NO'
where id = 2;