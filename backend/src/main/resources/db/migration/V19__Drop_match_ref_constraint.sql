ALTER TABLE matches ALTER COLUMN refteam_id DROP NOT NULL;
ALTER TABLE matches ALTER COLUMN team1_id DROP NOT NULL;
ALTER TABLE matches ALTER COLUMN team2_id DROP NOT NULL;
ALTER TABLE matches ADD COLUMN match_child1 BIGINT;
ALTER TABLE matches ADD COLUMN match_child2 BIGINT;

ALTER TABLE matches ADD CONSTRAINT match_team_child1 FOREIGN KEY (match_child1) REFERENCES matches(id);
ALTER TABLE matches ADD CONSTRAINT match_team_child2 FOREIGN KEY (match_child2) REFERENCES matches(id);
