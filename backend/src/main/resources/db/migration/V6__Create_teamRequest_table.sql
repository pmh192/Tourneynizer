CREATE TABLE teamRequest (
    id              BIGSERIAL PRIMARY KEY,
    team_id         BIGINT NOT NULL REFERENCES teams(id),
    user_id         BIGINT NOT NULL REFERENCES users(id),
    timeRequested   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    requester_id    BIGINT NOT NULL REFERENCES users(id),
    accepted        BOOLEAN DEFAULT NULL
);

