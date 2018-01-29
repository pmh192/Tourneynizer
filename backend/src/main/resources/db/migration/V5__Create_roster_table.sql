CREATE TABLE roster (
    id          BIGSERIAL PRIMARY KEY,
    team_id     BIGINT NOT NULL REFERENCES teams(id),
    user_id     BIGINT NOT NULL REFERENCES users(id),
    timeAdded   TIMESTAMP WITH TIME ZONE,
    isLeader    BOOLEAN NOT NULL DEFAULT FALSE
);

