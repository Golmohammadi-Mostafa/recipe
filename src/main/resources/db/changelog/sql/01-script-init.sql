drop sequence if exists hibernate_sequence;
create sequence hibernate_sequence start 1 increment 1;

create table role
(
    id   bigserial
        primary key,
    name varchar(255) not null
);

create table users
(
    id       bigserial
        primary key,
    password varchar(255) not null,
    username varchar(255) not null
        constraint uk_username
            unique
);

create table recipe
(
    id           bigint       not null
        primary key,
    instructions varchar(255) not null,
    name         varchar(255) not null,
    serves       integer      not null,
    vegetarian   boolean      not null,
    user_id      bigint
        constraint fk_user
            references users
);
create table ingredient
(
    id        bigint       not null
        primary key,
    name      varchar(255) not null,
    recipe_id bigint       not null
        constraint fk_recipe
            references recipe
);

create table user_role
(
    user_id bigint not null
        constraint fk_users
            references users,
    role_id bigint not null
        constraint fk_role
            references role,
    primary key (user_id, role_id)
);
insert into role (id, name)
values (1, 'ROLE_ADMIN');
insert into role (id, name)
values (2, 'ROLE_USER');

