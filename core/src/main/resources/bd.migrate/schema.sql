create sequence hibernate_sequence;
create sequence users_id_seq;
create sequence certificates_id_seq;
create sequence tags_id_seq;
create sequence orders_id_seq;
create sequence ordered_certificates_id_seq;

create table certificates
(
    id               bigint not null DEFAULT nextval('certificates_id_seq'),
    create_date      timestamp with time zone,
    description      varchar(255),
    duration         integer,
    last_update_date timestamp with time zone,
    name             varchar(255),
    price            numeric(19, 2),
    constraint certificates_pkey
        primary key (id)
);

create table tags
(
    id   bigint not null DEFAULT nextval('tags_id_seq'),
    name varchar(255),
    constraint tags_pkey
        primary key (id),
    constraint uk_name
        unique (name)
);

create table users
(
    id      bigint not null DEFAULT nextval('users_id_seq'),
    email    varchar(255) not null,
    name     varchar(255) not null,
    password varchar(255) not null,
    role     varchar(20) not null,
    surname  varchar(255) not null,
    constraint users_pkey
        primary key (id)
);


create table orders
(
    id         bigint not null DEFAULT nextval('orders_id_seq'),
    cost       numeric(19, 2),
    order_date timestamp,
    user_id    bigint,
    constraint fk_user_id
        foreign key (user_id) references users,
    constraint orders_pkey
        primary key (id)
);

create table ordered_certificates
(
    id             bigint not null DEFAULT nextval('ordered_certificates_id_seq'),
    certificate_id bigint,
    description    varchar(255),
    duration       integer,
    name           varchar(255),
    price          numeric(19, 2),
    order_id       bigint,
    constraint ordered_certificates_pkey
        primary key (id),
    constraint fk_order_id
        foreign key (order_id) references orders
);


create table certificates_tags
(
    certificates_id bigint not null,
    tags_id         bigint not null,
    constraint certificates_tags_pkey
        primary key (certificates_id, tags_id),
    constraint fk_certificates_id
        foreign key (certificates_id) references certificates,
    constraint fk_tags_id
        foreign key (tags_id) references tags
            on delete cascade
);



