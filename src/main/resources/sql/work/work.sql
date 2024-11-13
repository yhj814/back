create table tbl_work (
    id bigint unsigned primary key,
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
values(44,30000,'comedy','파일설명9','64');

select *from tbl_work;

select w.id, w.genre_type, w.thumbnail_file_id as thumbnailFileId,
       p.id, p.post_title, p.post_content, p.post_type, p.member_profile_id,
       p.created_date, mp.profile_nickname,
       mp.member_id, m.profile_img_url,
       fl.file_name as thumbnailFileName ,fl.file_path as thumbnailFilePath
from tbl_work w
         join tbl_post p on w.id = p.id and p.post_type = 'WORKVIDEO'
         join tbl_member_profile mp on p.member_profile_id = mp.id
         join tbl_member m on mp.member_id = m.id and m.id = 1
         join tbl_file fl on w.thumbnail_file_id = fl.id
order by w.id desc;