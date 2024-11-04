create table tbl_buy_funding_product (
    id bigint unsigned auto_increment primary key,
    member_profile_id bigint unsigned not null, -- 회원 프로필(구매자) FK
    funding_product_id bigint unsigned not null, -- 펀딩 상품 FK
    funding_send_status varchar(255) default 'NO', -- NO: 안보냄, YES: 보냄
    created_date datetime default current_timestamp,
    constraint fk_buy_funding_product_member_profile foreign key (member_profile_id)
    references tbl_member_profile (id),
    constraint fk_buy_funding_product_funding_product foreign key (funding_product_id)
    references tbl_funding_product (id)
);

select *
from tbl_funding;

select *
from tbl_member;
insert into tbl_member
(member_email)
values ('member12@gmail.com');

select *
from tbl_member_profile;
insert into tbl_member_profile
(profile_name, profile_nickname, profile_gender, profile_age, profile_email, profile_phone, member_id)
values ('이름12', '닉네임12', '여'
       , '30', 'test12@gmail.com', '01221231322', 12);

select id, post_title, post_content, post_type, member_profile_id, created_date, updated_date
from tbl_post;
insert into tbl_post
(post_title, post_content, post_type, member_profile_id)
values ('이름11 post 제목1', '이름11 post 내용1', 'VIDEO', 11);

select id, genre_type, investor_number, target_price, converge_price, funding_status, file_content
from tbl_funding;
insert into tbl_funding
(genre_type, investor_number, target_price, converge_price)
values ('코미디', 5, 100000000, 100000000);

select id, product_name, product_price, product_amount, funding_id
from tbl_funding_product;
insert into tbl_funding_product
(product_name, product_price, product_amount, funding_id)
values ('이름11 펀딩 상품', '50000', '50', 18);

select id, member_profile_id, funding_product_id, funding_send_status
from tbl_buy_funding_product;
insert into tbl_buy_funding_product
(member_profile_id, funding_product_id)
values (1, 27);

SELECT f.id, f.genre_type, p.post_title, p.post_content, p.post_type, p.member_profile_id, p.created_date, p.updated_date, mp.profile_nickname, mp.member_id, m.profile_img_url
FROM
    tbl_funding f
        JOIN tbl_post p ON f.id = p.id
        JOIN tbl_member_profile mp ON p.member_profile_id = mp.id
        JOIN tbl_member m ON mp.member_id = m.id
WHERE
    m.id = 1;

SELECT bfp.id AS 'buy_funding_product ID', bfp.funding_product_id AS 'funding_product ID', bfp.funding_send_status AS '발송 여부'
      ,fp.product_name, fp.product_price, fp.funding_id AS 'funding ID'
     ,bfp_mp.profile_name AS '구매자 이름', bfp_mp.profile_email AS '구매자 이메일'
FROM
    tbl_buy_funding_product bfp
        JOIN tbl_funding_product fp ON bfp.funding_product_id = fp.id
        JOIN tbl_funding f ON fp.funding_id = f.id
        JOIN tbl_post p ON f.id = p.id AND f.id = 9
        JOIN tbl_member_profile mp ON p.member_profile_id = mp.id
        JOIN tbl_member m ON mp.member_id = m.id
        JOIN tbl_member_profile bfp_mp ON bfp.member_profile_id = bfp_mp.id
order by f.id desc;
-- 그 회원의 펀딩 포스트 Id

select count(*) FROM
    tbl_buy_funding_product bfp
        JOIN tbl_funding_product fp ON bfp.funding_product_id = fp.id
        JOIN tbl_funding f ON fp.funding_id = f.id
        JOIN tbl_post p ON f.id = p.id AND f.id = 9
        JOIN tbl_member_profile mp ON p.member_profile_id = mp.id
        JOIN tbl_member m ON mp.member_id = m.id
        JOIN tbl_member_profile bfp_mp ON bfp.member_profile_id = bfp_mp.id;

update tbl_buy_funding_product
set funding_send_status = 'YES'
where id = 12;

-- ******************************************************************
-- **결제한 펀딩

select bfp_m.id as '나', m.id as '판매자', bfp.id as '구매한 펀딩 상품 ID', bfp.funding_product_id as '펀딩 상품 ID'
        ,fp.product_name as '펀딩 상품명', fp.product_price as '펀딩 상품 가격', fp.funding_id as '펀딩(포스트) ID',
       f.genre_type, p.post_title, p.post_content, bfp_mp.profile_nickname as '내 닉네임', p.created_date
from
    tbl_buy_funding_product bfp
        join tbl_funding_product fp on bfp.funding_product_id = fp.id
        join tbl_funding f on fp.funding_id = f.id
        join tbl_post p on f.id = p.id
        join tbl_member_profile mp on p.member_profile_id = mp.id
        join tbl_member m on mp.member_id = m.id
        join tbl_member_profile bfp_mp on bfp.member_profile_id = bfp_mp.id
        join tbl_member bfp_m on bfp_mp.member_id = bfp_m.id and bfp_m.id = 2;

select bfp_mp.member_id as '나', fp.funding_id as '펀딩(포스트) ID',
        p.post_title, f.genre_type, p.created_date, p.post_content,
       m.profile_img_url as '판매자 프로필 이미지' , mp.profile_nickname as '판매자 닉네임'
     ,fp.product_name as '펀딩 상품명', fp.product_price as '펀딩 상품 가격'
       , fl.file_name, fl.file_size, fl.file_type, fl.file_path
from
    tbl_buy_funding_product bfp
        join tbl_funding_product fp on bfp.funding_product_id = fp.id
        join tbl_funding f on fp.funding_id = f.id
        join tbl_post p on f.id = p.id and p.post_type = 'VIDEO'
        join tbl_member_profile mp on p.member_profile_id = mp.id
        join tbl_member m on mp.member_id = m.id
        join tbl_member_profile bfp_mp on bfp.member_profile_id = bfp_mp.id
        join tbl_member bfp_m on bfp_mp.member_id = bfp_m.id and bfp_m.id = 1
        join tbl_post_file pfl on p.id = pfl.post_id
        join tbl_file fl on pfl.id = fl.id;
