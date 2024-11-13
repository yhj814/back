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
values (59,'2','.',',','10일시작','2024.08.14',
        '.','서울','.','.','.','모집중');

select a.id, a.audition_field, a.audition_career, a.expected_amount,
       a.audition_deadline, a.audition_personnel, a.audition_location, a.audition_status,
       p.post_title, p.post_content, p.created_date, mp.profile_name, mp.profile_email, m.id
from tbl_audition a
         join tbl_post p on a.id = p.id and p.post_type = 'AUDITIONVIDEO'
         join tbl_member_profile mp on p.member_profile_id = mp.id
         join tbl_member m on mp.member_id = m.id and m.id = 1
order by a.id desc;

select a.id, a.audition_field, a.audition_career, a.expected_amount,
       a.audition_deadline, a.audition_personnel, a.audition_location, a.audition_status,
       p.post_title, p.post_content, p.created_date,
       aa_mp.profile_name, aa_mp.profile_email
from
    tbl_audition_application aa
        join tbl_audition a on aa.audition_id = a.id
        join tbl_post p on a.id = p.id and a.id = 19
        join tbl_member_profile mp on p.member_profile_id = mp.id
        join tbl_member m on mp.member_id = m.id
        join tbl_member_profile aa_mp on aa.member_profile_id = aa_mp.id
order by a.id desc;
