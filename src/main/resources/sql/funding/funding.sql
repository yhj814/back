create table tbl_funding (
    id bigint unsigned auto_increment primary key,
    genre_type varchar(255) not null, -- 로맨스, 코미디, 공포, 액션, 드라마 / romance,comedy,horror,action,drama
    investor_number int not null, -- 투자자수
    target_price int not null, -- 목표 금액
    converge_price int not null, -- 모인 금액
    funding_status varchar(255) default 'YES', -- 펀딩 상태 (펀딩 중/종료)
    file_content varchar(255),
    thumbnail_file_id bigint unsigned,
    funding_content varchar(255),
    constraint fk_funding_post foreign key (id)
    references tbl_post(id),
    constraint fk_funding_thumbnail_file foreign key (thumbnail_file_id)
    references tbl_file(id)
);

select * from tbl_funding;

insert into tbl_funding
values (76,'horror',170,12200000,1900000,default,'파일1',
        1,'펀딩내용');