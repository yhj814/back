create table tbl_audition_application_file (
                                               id bigint unsigned auto_increment primary key,
                                               audition_application_id bigint unsigned not null,
                                               file_id bigint unsigned not null ,
                                               constraint fk_audition_application_file_file foreign key (file_id)
                                                   references tbl_file(id),
                                               constraint fk_audition_application_file_audition_application foreign key (audition_application_id)
                                                   references tbl_audition_application (id)
);
