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


select bw_mp.member_id, bw.work_id, p.post_title, w.genre_type, p.created_date, p.post_content,
       m.profile_img_url, mp.profile_nickname, w.work_price,
       fl.file_name, fl.file_path
    from tbl_buy_work bw
        join tbl_work w on bw.work_id = w.id
        join tbl_post p on w.id = p.id and p.post_type = 'VIDEO'
        join tbl_member_profile mp on p.member_profile_id = mp.id
        join tbl_member m on mp.member_id = m.id
        join tbl_member_profile bw_mp on bw.member_profile_id = bw_mp.id
        join tbl_member bw_m on bw_mp.member_id = bw_m.id and bw_m.id = 15
        join tbl_post_file pfl on p.id = pfl.post_id
        join tbl_file fl on pfl.id = fl.id
order by w.id desc
limit 0, 2;

delete
from tbl_buy_work
where id = 2;

insert into tbl_buy_work
    (member_profile_id, work_id)
values (15, )