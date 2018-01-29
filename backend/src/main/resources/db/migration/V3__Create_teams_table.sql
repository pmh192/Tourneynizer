CREATE TABLE teams (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(256) NOT NULL,
    timeCreated     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tournament_id   BIGINT NOT NULL REFERENCES tournaments(id),
    checkedIn       BOOLEAN NOT NULL DEFAULT FALSE
);

