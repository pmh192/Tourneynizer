CREATE TABLE user_participation (
    user_id             BIGINT NOT NULL REFERENCES users(id),
    tournament_id       BIGINT NOT NULL REFERENCES tournaments(id)
);

ALTER TABLE user_participation ADD UNIQUE (tournament_id, user_id);


