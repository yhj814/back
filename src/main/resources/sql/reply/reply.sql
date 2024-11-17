create table tbl_reply (
    id bigint unsigned auto_increment primary key,
    reply_content varchar(255) not null,
    member_profile_id bigint unsigned not null,
    work_id bigint unsigned not null,
    created_date datetime default current_timestamp,
    star int not null,
    constraint fk_reply_member_profile foreign key (member_profile_id)
    references tbl_member_profile(id),
    constraint fk_reply_work foreign key (work_id)
    references tbl_work(id)
);

alter table tbl_reply
add constraint fk_reply_member_profile foreign key (member_profile_id)
    references tbl_member_profile(id);

select * from tbl_reply;

insert into tbl_reply
values (25,'댓글adsadsadsadsadasadasdsdsadsadsa',9,12,now(),2);

select *from tbl_work;
select *from tbl_post;

select count(*) from tbl_reply r
                         join tbl_member_profile mp on mp.id = r.member_profile_id
                         join tbl_work w on w.id = r.work_id
                         join tbl_post p on p.id = w.id and p.post_type = 'WORKVIDEO'
                         join tbl_member m on m.id = mp.member_id and m.id = 1;