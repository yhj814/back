create table tbl_genre (
                           id bigint unsigned auto_increment primary key,
                           genre_type varchar(255) not null  -- 로맨스, 코미디, 공포, 액션, 드라마 순대로 0,1,2,3,4
);
