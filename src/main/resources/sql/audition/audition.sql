create table tbl_audition (
    id bigint unsigned primary key,
    audition_field int not null,
    audition_career varchar(255),                   -- 경력
    expected_amount varchar(255),                   --
    service_start_date varchar(255) not null,
    audition_deadline varchar(255) not null,
    audition_personnel varchar(255) not null,
    audition_location varchar(255) not null,
    audition_background varchar(255),
    audition_category varchar(255),
    file_content varchar(255),
    audition_status varchar(255) default 'YES',
    constraint fk_audition_post foreign key (id)
    references tbl_post(id)
);

select * from tbl_audition;

insert into tbl_audition
values (30,'감독','.',',','10일시작','2024.10.1',)