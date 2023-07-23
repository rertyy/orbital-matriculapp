create table if not exists users
(
    user_id      serial
        primary key,
    username     varchar(50)                            not null
        unique,
    password     varchar                                not null,
    email        varchar(255)                           not null
        unique,
    time_created timestamp with time zone default now() not null
);

create table if not exists events
(
    event_id         serial
        primary key          not null,
    event_name       varchar not null,
    event_body       varchar,
    event_start_date timestamp with time zone,
    event_end_date   timestamp with time zone
);

create table if not exists threads
(
    thread_id         serial                                 not null
        primary key,
    thread_name       varchar(50)                            not null,
    thread_body       varchar                                not null,
    thread_created_by integer                                not null
        references users (user_id)
            on delete set null,
    thread_created_at timestamp with time zone default now() not null
);


create table if not exists replies
(
    reply_id           serial
        primary key                                           not null,
    reply_title        varchar                                not null,
    reply_body         varchar                                not null,
    thread_id          integer                                not null
        references threads (thread_id)
            on delete set null,
    reply_created_by   integer                                not null
        references users (user_id)
            on delete set null,
    reply_created_at   timestamp with time zone default now() not null,
    reply_last_updated timestamp with time zone default now() not null
);

create table if not exists schema_migrations
(
    version bigint  not null
        primary key,
    dirty   boolean not null
);



