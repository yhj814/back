create table tbl_admin_answer (
    id bigint unsigned auto_increment primary key,
    admin_answer_content varchar(255) not null,
    inquiry_id bigint unsigned not null,
    created_date datetime default current_timestamp,
    constraint fk_admin_answer_inquiry foreign key (inquiry_id)
       references tbl_inquiry(id)
);

drop table tbl_admin_answer;

select *from tbl_admin_answer;

insert into tbl_admin_answer
values(1,'답변1',8,now());

INSERT INTO tbl_admin_answer (admin_answer_content, inquiry_id) VALUES ('답변2', 9);
INSERT INTO tbl_admin_answer (admin_answer_content, inquiry_id) VALUES ('답변3', 10);


SELECT * FROM tbl_admin_answer WHERE inquiry_id IN (SELECT id FROM tbl_inquiry);