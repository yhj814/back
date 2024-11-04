create table tbl_inquiry (
    id bigint unsigned auto_increment primary key,
    inquiry_status varchar(255),
    constraint fk_inquiry_post foreign key (id)
    references tbl_post(id)
);

select *from tbl_inquiry;

SELECT * FROM tbl_inquiry LIMIT 0, 10; -- 쿼리를 직접 실행해 보기

select id, post_title, post_content, post_type, member_profile_id, created_date, updated_date
from tbl_post where post_type = 'INQUIRY';
insert into tbl_post (post_title, post_content, post_type, member_profile_id)
values ('문의 제목7', '문의 내용7', 'INQUIRY', 1);

select id, inquiry_status from tbl_inquiry;
insert into tbl_inquiry(id, inquiry_status)
values (25,'NO');

select p.id as postId,
       p.post_title as postTitle,
       p.post_content as postContent,
       p.created_date as postCreatedDate,
       m.profile_img_url as profileImgUrl,
       mp.profile_nickname as profileNickname,
       i.inquiry_status as inquiryStatus,
       mp.member_id as memberId
from
    tbl_inquiry i
        join tbl_post p on i.id = p.id
        join tbl_member_profile mp on p.member_profile_id = mp.id
        join tbl_member m on mp.member_id = m.id and mp.member_id = 1
        and p.post_type = 'INQUIRY'
order by i.id desc
limit 0, 2;

select count(*) from
    tbl_inquiry i
        join tbl_post p on i.id = p.id
        join tbl_member_profile mp on p.member_profile_id = mp.id
        join tbl_member m on mp.member_id = m.id and m.id = 1
        and p.post_type = 'INQUIRY';

select aa.id, admin_answer_content, aa.inquiry_id, created_date, i.inquiry_status
from tbl_admin_answer aa join tbl_inquiry i
on aa.inquiry_id = i.id and aa.inquiry_id = 19;
