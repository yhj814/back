create table tbl_buy_work (
    id bigint unsigned auto_increment primary key,
    member_profile_id bigint unsigned not null, -- 구매자
    work_id bigint unsigned not null,
    work_send_status varchar(255) default 'NO', -- NO: 안보냄, YES: 보냄
    created_date datetime default current_timestamp,
    constraint fk_buy_work_member_profile foreign key (member_profile_id)
    references tbl_member_profile (id),
    constraint fk_buy_work_work foreign key (work_id)
    references tbl_work (id)
);

select * from tbl_work;
select * from tbl_post;
select * from tbl_buy_work;
select * from tbl_member_profile;

insert into tbl_buy_work (member_profile_id, work_id)
values (12, 40);

#     private Long id; // 작품 ID
#     private Long memberProfileId; // 회원 프로필 ID - 구매자
#     private Long workId; // 작품 상품 ID
#     private String workSendStatus; // 발송 여부
#     private String createdDate; // 구매 날짜
#     private String workPrice; // 작품 가격
#     private String profileName; // 구매자 이름
#     private String profileEmail; // 구매자 이메일

select bw.id, bw.work_send_status,bw.work_id
     , w.work_price, bw_mp.profile_name, bw_mp.profile_email
from
    tbl_buy_work bw
        join tbl_work w on bw.work_id = w.id
        join tbl_post p on w.id = p.id and w.id = 38
        join tbl_member_profile mp on p.member_profile_id = mp.id
        join tbl_member m on mp.member_id = m.id
        join tbl_member_profile bw_mp on bw.member_profile_id = bw_mp.id
order by w.id desc
limit 0, 2;

select count(*) from
    tbl_buy_work bw
        join tbl_work w on bw.work_id = w.id
        join tbl_post p on w.id = p.id and w.id = 38
        join tbl_member_profile mp on p.member_profile_id = mp.id
        join tbl_member m on mp.member_id = m.id
        join tbl_member_profile bw_mp on bw.member_profile_id = bw_mp.id;

select w.id, w.work_price, w.genre_type, w.file_content, w.read_count
from tbl_work w join tbl_post p
                     on w.id = p.id and w.id = 38 and p.post_type = 'VIDEO';