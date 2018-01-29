CREATE TABLE matches (
    id          BIGSERIAL PRIMARY KEY,
    tournament  BIGINT NOT NULL REFERENCES tournaments(id),
    team1_id    BIGINT NOT NULL REFERENCES teams(id),
    team2_id    BIGINT NOT NULL REFERENCES teams(id),
    child1_id   BIGINT NOT NULL REFERENCES matches(id),
    child2_id   BIGINT NOT NULL REFERENCES matches(id),
    score1      BIGINT,
    score2      BIGINT,
    scoreType   INT,
    timeStart   TIMESTAMP WITH TIME ZONE,
    timeEnd     TIMESTAMP WITH TIME ZONE,
    refTeam_id  BIGINT REFERENCES teams(id),
    matchOrder  INT NOT NULL,
    courtNumber INT,
    finished    BOOLEAN NOT NULL DEFAULT FALSE
);
