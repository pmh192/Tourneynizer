ALTER TABLE teamRequest DROP CONSTRAINT unique_team_user_request, ADD CONSTRAINT unique_team_user_request UNIQUE (team_id, user_id, requester_id);

