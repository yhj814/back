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
    (member_name, member_email)
values ('정지수2', 'test2@gmail.com');

select * from tbl_post;
insert into tbl_post
    (post_title, post_content, post_type, member_id)
values ('test2 제목', 'test2 내용', '영상', 2);

select * from tbl_funding;
insert into tbl_funding
    (genre_type, investor_number, target_price, converge_price, funding_status)
values ('로맨스', '5', '3000000', '200000', );
