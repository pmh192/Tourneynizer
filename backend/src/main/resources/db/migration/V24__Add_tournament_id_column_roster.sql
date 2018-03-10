
ALTER TABLE roster ADD COLUMN tournament_id BIGINT;
ALTER TABLE roster ADD CONSTRAINT foreign_tournament_key FOREIGN KEY (tournament_id) REFERENCES tournaments(id);

UPDATE roster SET tournament_id=(SELECT tournament_id FROM teams WHERE teams.id=roster.team_id);

ALTER TABLE roster ALTER tournament_id SET NOT NULL;

