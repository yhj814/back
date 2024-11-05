create table tbl_work (
    id bigint unsigned auto_increment primary key,
    work_price varchar(255) not null,
    genre_type varchar(255) not null,
    file_content varchar(255),
    read_count int default 0,
    thumbnail_file_id bigint unsigned,
    constraint fk_work_post foreign key (id)
    references tbl_post(id),
    constraint fk_work_thumbnail_file foreign key (thumbnail_file_id)
    references tbl_file(id)
);

insert into tbl_work
values(10,9000,'comedy','파일설명6','7');

select *from tbl_work;

select w.id, w.genre_type, w.thumbnail_file_id,
       p.id, p.post_title, p.post_content, p.post_type, p.member_profile_id,
       p.created_date, mp.profile_nickname,
       mp.member_id, m.profile_img_url,
       fl.file_name as thumbnail_file_name ,fl.file_path as thumbnail_file_path
from tbl_work w
         join tbl_post p on w.id = p.id and p.post_type = 'VIDEO'
         join tbl_member_profile mp on p.member_profile_id = mp.id
         join tbl_member m on mp.member_id = m.id and m.id = 15
         join tbl_post_file pfl on p.id = pfl.post_id
         join tbl_file fl on pfl.id = fl.id and w.thumbnail_file_id = fl.id
order by w.id desc;