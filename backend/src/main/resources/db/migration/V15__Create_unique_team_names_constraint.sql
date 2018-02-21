ALTER TABLE teams ADD CONSTRAINT unique_team_name_request UNIQUE (tournament_id, name);
