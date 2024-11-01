create table tbl_buy_work (
    id bigint unsigned auto_increment primary key,
    member_profile_id bigint unsigned not null,
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