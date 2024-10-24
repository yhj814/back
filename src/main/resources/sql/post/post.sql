create table tbl_post (
                          id bigint unsigned auto_increment primary key,
                          post_title varchar(255) not null,
                          post_content varchar(255) not null,
                          post_type varchar(255) not null, -- 영상, 글, 문의사항, 공지사항인지 순서대로 0,1,2,3
                          member_id bigint unsigned not null,
                          created_date datetime default current_timestamp,
                          updated_date datetime default current_timestamp,
                          constraint fk_post_member foreign key (member_id)
                              references tbl_member(id)
);

