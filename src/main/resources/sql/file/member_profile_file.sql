create table tbl_member_profile_file (
    id bigint unsigned auto_increment primary key,
    member_profile_id bigint unsigned not null,
    constraint fk_member_profile_file_file foreign key (id)
    references tbl_file(id),
    constraint fk_member_profile_file_member_profile foreign key (member_profile_id)
    references tbl_member_profile (id)
);

