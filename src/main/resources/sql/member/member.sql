create table tbl_member(
    id bigint unsigned auto_increment primary key,
    member_email varchar(255) not null,
    member_status varchar(255) default 0,
    profile_img_url varchar(255),
    created_date datetime default current_timestamp,
    updated_date datetime default current_timestamp
);

select * from tbl_member;



insert into tbl_member (member_email, profile_img_url)
values ( 'john.doe@example.com', 'profile.jpg');

insert into tbl_member (member_email, profile_img_url)
values ( 'test2@gamail.com', 'profile.jpg');

use gb;