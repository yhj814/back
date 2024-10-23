create table tbl_inquiry (
                             id bigint unsigned auto_increment primary key,
                             constraint fk_inquiry_post foreign key (id)
                                 references tbl_post(id)
);
