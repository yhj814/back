create table tbl_post (
                          id bigint unsigned auto_increment primary key,
                          post_title varchar(255) not null,
                          post_content varchar(255) not null,
                          post_type varchar(255) not null, -- 영상, 글, 문의사항인지 Enum 으로 처리
                          member_profile_id bigint unsigned not null,
                          created_date datetime default current_timestamp,
                          updated_date datetime default current_timestamp,
                          constraint fk_post_member_profile foreign key (member_profile_id)
                              references tbl_member_profile(id)
);

select *from tbl_post;

SELECT * FROM tbl_post WHERE post_type = 'INQUIRY';

SELECT * FROM tbl_post WHERE post_type = 'VIDEO';

insert into tbl_post
values(28,'글제목9','글내용9','TEXT',1,now(),now());