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

select i.id, i.inquiry_status, p.post_title, p.created_date, p.post_content,
       m.profile_img_url ,mp.profile_nickname, aa.admin_answer_content
from
    tbl_inquiry i
        join tbl_post p on i.id = p.id
        join tbl_member_profile mp on p.member_profile_id = mp.id
        join tbl_member m on mp.member_id = m.id and m.id = 1
        join tbl_admin_answer aa on i.id = aa.inquiry_id
order by i.id desc;

select id, admin_answer_content, inquiry_id, created_date from tbl_admin_answer;
insert into tbl_admin_answer(admin_answer_content, inquiry_id)
values ('관리자 답변1', 25);
