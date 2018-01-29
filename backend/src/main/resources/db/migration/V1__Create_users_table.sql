CREATE TABLE users (
    id          BIGSERIAL PRIMARY KEY,
    email       VARCHAR(256) NOT NULL,
    name        VARCHAR(256) NOT NULL,
    password    VARCHAR(256) NOT NULL,
    timeCreated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
