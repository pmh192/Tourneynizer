ALTER TABLE roster ADD CONSTRAINT unique_team_user UNIQUE (team_id, user_id);
