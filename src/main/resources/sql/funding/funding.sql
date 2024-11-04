create table tbl_funding (
    id bigint unsigned auto_increment primary key,
    genre_type varchar(255) not null, -- 로맨스, 코미디, 공포, 액션, 드라마 / ROMANCE, COMEDY, HORROR, ACTION, DRAMA
    investor_number int not null, -- 투자자수
    target_price int not null, -- 목표 금액
    converge_price int not null, -- 모인 금액
    funding_status varchar(255) default 'YES', -- 펀딩 상태 (펀딩 중/종료)
    file_content varchar(255),
    constraint fk_funding_post foreign key (id)
    references tbl_post(id)
);

select * from tbl_member;
insert into tbl_member
    (member_email, profile_img_url)
values ('test2@gmail.com', 'url.test2');

select * from tbl_member_profile;
insert into tbl_member_profile
(profile_name, profile_nickname, profile_gender, profile_age, profile_email, profile_phone, member_id)
values ('정지수', '별쌤', '여'
       , '21', 'profileEmailTest1@gmail.com', '01052530430', 1);

select * from tbl_post;
insert into tbl_post
    (post_title, post_content, post_type, member_profile_id)
values ('제목 글.test1', '내용 글.test1', '글', 1);

select * from tbl_funding;
insert into tbl_funding
    (id, genre_type, investor_number, target_price, converge_price)
values (10, 'DRAMA', 5, 50000000, 3000000);

select *
from tbl_post p join tbl_funding f on f.id = p.id;


select f.id, f.genre_type, p.post_title, p.post_content, p.post_type, p.member_profile_id, p.created_date, p.updated_date, mp.profile_nickname, mp.member_id, m.profile_img_url
from
    tbl_funding f
        join tbl_post p on f.id = p.id
        join tbl_member_profile mp on p.member_profile_id = mp.id
        join tbl_member m on mp.member_id = m.id and m.id = 1
order by f.id desc
limit 0, 2;

select f.id, f.genre_type, p.post_title, p.post_content, p.post_type, p.member_profile_id, p.created_date, p.updated_date, mp.profile_nickname, mp.member_id, m.profile_img_url
from
    tbl_funding f
        join tbl_post p on f.id = p.id
        join tbl_member_profile mp on p.member_profile_id = mp.id
        join tbl_member m on mp.member_id = m.id and m.id = 1
order by f.id desc;

select count(*) from tbl_funding f
    join tbl_post p on f.id = p.id
    join tbl_member_profile mp on p.member_profile_id = mp.id
    join tbl_member m on mp.member_id = m.id and m.id = 1;

select f.id, f.genre_type, p.post_title, p.post_content, p.post_type
     , p.member_profile_id, p.created_date, p.updated_date, mp.profile_nickname
     , mp.member_id, m.profile_img_url
    , fl.file_name, fl.file_size, fl.file_type, fl.file_path
from
    tbl_funding f
        join tbl_post p on f.id = p.id
        join tbl_member_profile mp on p.member_profile_id = mp.id
        join tbl_member m on mp.member_id = m.id and m.id = 1
        join tbl_post_file pfl on p.id = pfl.post_id
        join tbl_file fl on pfl.id = fl.id
order by f.id desc;

select id, post_id from tbl_post_file;
insert into tbl_post_file(post_id)
values (18);

select id, file_name, file_size, file_type, file_path, created_date, updated_date from tbl_file;
insert into tbl_file(file_name, file_size, file_type, file_path)
values ('thumbnail-09', '100*100', 'thumbnail', '파일 경로 test9');