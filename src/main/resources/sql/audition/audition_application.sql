create table tbl_audition_application (
    id bigint unsigned auto_increment primary key,
    member_profile_id bigint unsigned not null,
    apply_etc varchar(255) not null,
    audition_id bigint unsigned not null,
    created_date datetime default current_timestamp,
    confirm_status varchar(255) default 'NO',
    constraint fk_audition_application_member_profile foreign key (member_profile_id)
    references tbl_member_profile(id),
    constraint fk_audition_application_audition foreign key (audition_id)
    references tbl_audition(id)
);


select *from tbl_member_profile;
select *from tbl_audition_application;

insert into tbl_audition_application
values (34,1,'.',57,now(),default);


select a.id, a.audition_field, a.audition_career, a.expected_amount,
       a.audition_deadline, a.audition_personnel, a.audition_location, a.audition_status,
       p.post_title, p.post_content, p.created_date,
       mp.profile_name, mp.profile_email
from tbl_audition_application aa
         join tbl_audition a on aa.audition_id = a.id
         join tbl_post p on a.id = p.id
         join tbl_member_profile mp on p.member_profile_id = mp.id
         join tbl_member m on mp.member_id = m.id
         join tbl_member_profile aa_mp on aa.member_profile_id = aa_mp.id
         join tbl_member aa_m on aa_mp.member_id = aa_m.id and aa_m.id = 1
order by a.id desc
limit 0, 2;