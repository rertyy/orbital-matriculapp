create table users
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

alter table users
    owner to postgres;

create table events
(
    event_id         serial
        primary key,
    event_name       varchar not null,
    event_body       varchar,
    event_start_date timestamp with time zone,
    event_end_date   timestamp with time zone
);

alter table events
    owner to postgres;

create table user_keep_track
(
    id       serial
        primary key,
    user_id  bigint
        references users,
    event_id bigint
        references events
);

alter table user_keep_track
    owner to postgres;

create table hyperlinks
(
    link_id   serial
        primary key,
    link_name varchar not null,
    link_body varchar
);

alter table hyperlinks
    owner to postgres;

create table categories
(
    cat_id     serial
        primary key,
    cat_name   varchar(50) not null
        constraint uq_cat_name
            unique,
    created_by integer     not null
        references users,
    created_at timestamp with time zone default now()
);

comment on table categories is 'forum categories';

alter table categories
    owner to postgres;

create table posts
(
    post_id      serial
        primary key,
    title        varchar                                not null,
    body         varchar                                not null,
    category_id  integer                                not null
        references categories,
    created_at   timestamp with time zone default now() not null,
    created_by   integer                                not null
        references users,
    last_updated timestamp with time zone default now() not null
);

comment on table posts is 'forum posts';

alter table posts
    owner to postgres;

