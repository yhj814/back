create table tbl_post_file (
                               id bigint unsigned auto_increment primary key,
                               post_id bigint unsigned not null,
                               constraint fk_post_file_file foreign key (id)
                                   references tbl_file(id),
                               constraint fk_post_file_post foreign key (post_id)
                                   references tbl_post(id)
);