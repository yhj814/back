create table tbl_funding (
    id bigint unsigned auto_increment primary key,
    genre_type varchar(255) not null,
    investor_number int not null, -- 투자자수
    target_price int not null, -- 목표 금액
    converge_price int not null, -- 모인 금액
    funding_status varchar(255) default 0, -- 펀딩 상태
    file_content varchar(255),
    constraint fk_funding_post foreign key (id)
    references tbl_post(id)
);

select * from tbl_member;
insert into tbl_member
    (member_email, member_status, profile_img_url)
values ('test2@gmail.com', '활동 중', 'url.test2');

select * from tbl_member_profile;
insert into tbl_member_profile
(profile_name, profile_nickname, profile_gender, profile_age, profile_email, profile_phone, member_id)
values ('정지수', '별쌤', '여'
       , '21', 'profileEmailTest1@gmail.com', '01052530430', 1);

select * from tbl_post;
insert into tbl_post
    (post_title, post_content, post_type, member_profile_id)
values ('제목 test3', '내용 test3', '영상', 1);

select * from tbl_funding;
insert into tbl_funding
    (id, genre_type, investor_number, target_price, converge_price, funding_status)
values (2, '액션', 5, 50000000, 3000000,1);


select f.id, f.genre_type, p.post_title, p.post_content, p.post_type, p.member_profile_id, p.created_date, p.updated_date, mp.profile_nickname, mp.member_id, m.profile_img_url
from
    tbl_funding f
        join tbl_post p on f.id = p.id
        join tbl_member_profile mp on p.member_profile_id = mp.id
        join tbl_member m on mp.member_id = m.id and m.id = 1
order by f.id desc
limit 0, 2;

select count(*) from tbl_funding f
    join tbl_post p on f.id = p.id
    join tbl_member_profile mp on p.member_profile_id = mp.id
    join tbl_member m on mp.member_id = m.id and m.id = 1;
