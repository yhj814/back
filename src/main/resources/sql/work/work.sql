create table tbl_work (
                          id bigint unsigned auto_increment primary key,
                          work_price varchar(255) not null,
                          genre_id bigint unsigned not null,
                          read_count int default 0,
                          constraint fk_work_post foreign key (id)
                              references tbl_post(id),
                          constraint fk_work_genre foreign key (genre_id)
                              references tbl_genre(id)
);


