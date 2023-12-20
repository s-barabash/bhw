create type advisor_role as enum ('associate', 'partner', 'senior');
create type phone_type as enum ('home', 'work', 'mobile');
create type application_status as enum ('new', 'assigned', 'on_hold', 'approved', 'canceled', 'declined');
create type user_type as enum ('advisor', 'applicant');


create table users
(
    id         bigserial,
    username   varchar(255) not null,
    email      varchar(255) not null check (email like '%@%'),
    type       user_type    not null,
    created_at timestamp    not null default now(),
    updated_at timestamp    not null default now(),

    constraint pk_users primary key (id),
    constraint uq_users_username unique (username),
    constraint uq_users_email unique (email)
);

create table advisors
(
    id         bigint,
    role       advisor_role not null,
    created_at timestamp    not null default now(),
    updated_at timestamp    not null default now(),

    constraint pk_advisors primary key (id),
    constraint fk_advisors_users
        foreign key (id)
            references users
            on delete cascade
);

create table applicants
(
    id         bigint,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    ssn        varchar(255) not null,
    created_at timestamp    not null default now(),
    updated_at timestamp    not null default now(),

    constraint pk_applicants primary key (id),
    constraint fk_applicants_users
        foreign key (id)
            references users
            on delete cascade
);

create table addresses
(
    id           bigserial,
    applicant_id bigint       not null,
    city         varchar(255) not null,
    street       varchar(255) not null,
    number       int          not null check (number > 0 and number < 300),
    zip          int          not null check (zip > 0 and zip < 100000),
    apt          int          not null check (apt > 0 and apt < 1000),
    created_at   timestamp    not null default now(),
    updated_at   timestamp    not null default now(),

    constraint pk_addresses primary key (id),
    constraint fk_addresses_applicants
        foreign key (applicant_id)
            references applicants (id)
            on delete cascade
);

create table phones
(
    id           bigserial,
    applicant_id bigint       not null,
    type         phone_type   not null,
    number       varchar(255) not null,
    created_at   timestamp    not null default now(),
    updated_at   timestamp    not null default now(),

    constraint pk_phones primary key (id),
    constraint fk_phones_applicants
        foreign key (applicant_id)
            references applicants (id)
            on delete cascade
);

create table applications
(
    id           bigserial,
    applicant_id bigint             not null,
    advisor_id   bigint,
    amount decimal(14, 2) not null,
    status       application_status not null default 'new',
    created_at   timestamp          not null default now(),
    updated_at   timestamp          not null default now(),
    assigned_at  timestamp,

    constraint pk_applications primary key (id),
    constraint fk_applications_applicants
        foreign key (applicant_id)
            references applicants (id)
            on delete cascade,
    constraint fk_applications_advisors
        foreign key (advisor_id)
            references advisors (id),

    constraint chk_applications_advisor_id_assigned_at check (
        (advisor_id is not null and assigned_at is not null) or
        (advisor_id is null and assigned_at is null)
        )
);


create index idx_addresses_applicant_id on addresses (applicant_id);
create index idx_phones_applicant_id on phones (applicant_id);
create index idx_applications_applicant_id on applications (applicant_id);
create index idx_applications_advisor_id on applications (advisor_id);
