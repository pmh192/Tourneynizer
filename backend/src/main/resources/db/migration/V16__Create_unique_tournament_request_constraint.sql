ALTER TABLE tournamentRequest ADD CONSTRAINT unique_team_tournament_request UNIQUE (team_id, tournament_id);
