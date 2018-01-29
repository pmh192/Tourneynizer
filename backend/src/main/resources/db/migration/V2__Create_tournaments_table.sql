CREATE TABLE tournaments (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(256) NOT NULL,
    address     VARCHAR(256) NOT NULL,
    startTime   TIMESTAMP WITH TIME ZONE,
    teamSize    INT NOT NULL,
    maxTeams    INT NOT NULL,
    timeCreated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    type        INT NOT NULL,
    numCourts   INT NOT NULL,
    creator_id  BIGINT NOT NULL REFERENCES users(id)
);

