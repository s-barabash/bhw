CREATE TYPE advisor_role AS ENUM ('associate', 'partner', 'senior');
CREATE TYPE phone_type AS ENUM ('home', 'work', 'mobile');
CREATE TYPE application_status AS ENUM ('new', 'assigned', 'on_hold', 'approved', 'canceled', 'declined');
CREATE TYPE user_type AS ENUM ('advisor', 'applicant');


CREATE TABLE users
(
    id         bigserial,
    username   varchar(255) not null,
    email      varchar(255) not null check (email like '%@%'),
    type       user_type    not null,
    created_at timestamp    not null default now(),
    updated_at timestamp    not null default now(),

    constraint pk_users primary key (id),
    constraint uq_username unique (username),
    constraint uq_email unique (email)
);

CREATE TABLE advisors
(
    id         bigint,
    role       advisor_role not null,
    created_at timestamp    not null default now(),
    updated_at timestamp    not null default now(),

    constraint pk_advisors primary key (id),
    constraint fk_advisors_users
        foreign key (id)
            references users (id)
            on delete cascade
);

CREATE TABLE applicants
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
            references users (id)
            on delete cascade
);

CREATE TABLE addresses
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

CREATE TABLE phones
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

CREATE TABLE applications
(
    id           bigserial,
    applicant_id bigint             not null,
    advisor_id   bigint,
    amount       DECIMAL(14, 2)     not null,
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
        (advisor_id IS NOT NULL AND assigned_at IS NOT NULL) OR
        (advisor_id IS NULL AND assigned_at IS NULL)
        )
);


CREATE INDEX idx_addresses_applicant_id ON addresses (applicant_id);
CREATE INDEX idx_phones_applicant_id ON phones (applicant_id);
CREATE INDEX idx_applications_applicant_id ON applications (applicant_id);
CREATE INDEX idx_applications_advisor_id ON applications (advisor_id);

