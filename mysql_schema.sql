create table event
(
    id      int           not null
        primary key,
    date    datetime      not null,
    message varchar(1000) not null,
    url     varchar(1000) not null
);

create table holiday
(
    date date not null
        primary key
);

create table repeat_event
(
    id               int           not null
        primary key,
    until            date          not null,
    hour             int           not null,
    minute           int           not null,
    message          varchar(1000) not null,
    url              varchar(1000) not null,
    disableOnHoliday tinyint(1) default 1 null,
    cron             tinyint null,
    constraint repeat_event_hour_check
        check ((`hour` >= 0) and (`hour` < 24)),
    constraint repeat_event_minute_check
        check ((`minute` >= 0) and (`minute` < 60))
);
