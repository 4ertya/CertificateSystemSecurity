create sequence hibernate_sequence;

create table certificates
(
    id               bigserial not null,
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
    id   bigserial not null,
    name varchar(255),
    constraint tags_pkey
        primary key (id),
    constraint uk_name
        unique (name)
);

create table users
(
    id      bigserial not null,
    name    varchar(255),
    surname varchar(255),
    constraint users_pkey
        primary key (id)
);


create table orders
(
    id         bigserial not null,
    cost       numeric(19, 2),
    order_date timestamp,
    user_id    bigint,
    constraint orders_pkey
        primary key (id)
);

create table orders_certificates
(
    orders_id       bigint not null,
    certificates_id bigint not null,
    constraint fk_orders_id
        foreign key (orders_id) references orders
            on delete cascade,
    constraint fk_certificates_id
        foreign key (certificates_id) references certificates
            on delete cascade
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



create table revinfo
(
    rev      integer not null,
    revtstmp bigint,
    constraint revinfo_pkey
        primary key (rev)
);

create table certificates_audit_log
(
    id               bigint  not null,
    rev              integer not null,
    revtype          smallint,
    create_date      timestamp with time zone,
    description      varchar(255),
    duration         integer,
    last_update_date timestamp with time zone,
    name             varchar(255),
    price            numeric(19, 2),
    constraint certificates_audit_log_pkey
        primary key (id, rev),
    constraint fk_revinfo
        foreign key (rev) references revinfo
);

create table certificates_tags_audit_log
(
    rev             integer not null,
    certificates_id bigint  not null,
    tags_id         bigint  not null,
    revtype         smallint,
    constraint certificates_tags_audit_log_pkey
        primary key (rev, certificates_id, tags_id),
    constraint fk_revinfo
        foreign key (rev) references revinfo
);

create table orders_audit_log
(
    id         bigint  not null,
    rev        integer not null,
    revtype    smallint,
    cost       numeric(19, 2),
    order_date timestamp with time zone,
    user_id    bigint,
    constraint orders_audit_log_pkey
        primary key (id, rev),
    constraint fk_revinfo
        foreign key (rev) references revinfo
);

create table orders_certificates_audit_log
(
    rev             integer not null,
    orders_id       bigint  not null,
    certificates_id bigint  not null,
    revtype         smallint,
    constraint orders_certificates_audit_log_pkey
        primary key (rev, orders_id, certificates_id),
    constraint fk_revinfo
        foreign key (rev) references revinfo
);

create table tags_audit_log
(
    id      bigint  not null,
    rev     integer not null,
    revtype smallint,
    name    varchar(255),
    constraint tags_audit_log_pkey
        primary key (id, rev),
    constraint fk_revinfo
        foreign key (rev) references revinfo
);

