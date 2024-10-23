create table tbl_audition (
                              id bigint unsigned auto_increment primary key,
                              audition_field int not null,
                              audition_career varchar(255),
                              expected_amount varchar(255),
                              service_start_date varchar(255) not null,
                              audition_deadline varchar(255) not null,
                              audition_personnel varchar(255) not null,
                              audition_location varchar(255) not null,
                              audition_background varchar(255),
                              audition_category varchar(255),
                              audition_status varchar(255) default 0,
                              constraint fk_audition_post foreign key (id)
                                  references tbl_post(id)
);