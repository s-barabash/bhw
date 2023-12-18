--liquibase formatted sql

--changeset sbarabash:1
CREATE TABLE cameras
(
    id         SERIAL PRIMARY KEY NOT NULL,
    nasa_id    INT                NOT NULL UNIQUE,
    name       VARCHAR(36)        NOT NULL,
    created_at TIMESTAMP          NOT NULL DEFAULT NOW()
);

CREATE TABLE pictures
(
    id         SERIAL PRIMARY KEY NOT NULL,
    nasa_id    INT                NOT NULL UNIQUE,
    image_src  VARCHAR(255)       NOT NULL,
    camera_id  INT                NOT NULL,
    created_at TIMESTAMP          NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_pictures_cameras
        FOREIGN KEY (camera_id)
            REFERENCES cameras (id)
            ON DELETE CASCADE
);

--rollback drop table pictures;
--rollback drop table cameras;
