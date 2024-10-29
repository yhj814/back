create table tbl_post (
                          id bigint unsigned auto_increment primary key,
                          post_title varchar(255) not null,
                          post_content varchar(255) not null,
                          post_type varchar(255) not null, -- 영상, 글, 문의사항, 공지사항인지 Enum 으로 처리
                          member_profile_id bigint unsigned not null,
                          created_date datetime default current_timestamp,
                          updated_date datetime default current_timestamp,
                          constraint fk_post_member_profile foreign key (member_profile_id)
                              references tbl_member_profile(id)
);

select *from tbl_post;


alter table tbl_post
add constraint fk_post_member_profile foreign key (member_profile_id)
    references tbl_member_profile(id);

SELECT * FROM tbl_post WHERE post_type = 'INQUIRY';

SELECT
    p.id AS postId,
    p.post_title AS postTitle,
    p.post_content AS postContent,
    p.created_date AS postCreatedDate,
    mp.profile_name AS profileName,
    mp.profile_email AS profileEmail,
    i.inquiry_status AS inquiryStatus,
    aa.created_date AS adminAnswerCreatedDate
FROM
    tbl_post p
        JOIN
    tbl_member_profile mp ON p.member_profile_id = mp.id
        JOIN
    tbl_inquiry i ON p.id = i.id
        LEFT JOIN
    tbl_admin_answer aa ON i.id = aa.inquiry_id
WHERE
    p.post_type = 'INQUIRY'
ORDER BY
    p.created_date DESC
LIMIT 0, 5;
