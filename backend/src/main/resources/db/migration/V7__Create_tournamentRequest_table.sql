CREATE TABLE tournamentRequest (
    id              BIGSERIAL PRIMARY KEY,
    tournament_id   BIGINT NOT NULL REFERENCES tournaments(id),
    team_id         BIGINT NOT NULL REFERENCES teams(id),
    timeRequested   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    accepted        BOOLEAN DEFAULT NULL
);
