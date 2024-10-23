create table tbl_reply_notification (
                                        id bigint unsigned auto_increment primary key,
                                        reply_id bigint unsigned not null,
                                        constraint fk_reply_notification_reply foreign key (reply_id)
                                            references tbl_reply (id)
);