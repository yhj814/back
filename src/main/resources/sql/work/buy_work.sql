create table tbl_buy_work (
                              id bigint unsigned auto_increment primary key,
                              member_id bigint unsigned not null,
                              work_id bigint unsigned not null,
                              work_send_status varchar(255) default 0, -- 안보낸거 0, 보낸거 1
                              constraint fk_buy_work_member foreign key (member_id)
                                  references tbl_member (id),
                              constraint fk_buy_work_work foreign key (work_id)
                                  references tbl_work (id)
);

select * from tbl_work;
select * from tbl_post;