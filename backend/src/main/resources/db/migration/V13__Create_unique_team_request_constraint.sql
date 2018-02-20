ALTER TABLE teamRequest ADD CONSTRAINT unique_team_user_request UNIQUE (team_id, user_id);
