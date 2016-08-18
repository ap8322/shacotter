CREATE TABLE Member (
  member_id INT          NOT NULL AUTO_INCREMENT,
  email     VARCHAR(255) NOT NULL UNIQUE,
  password  TEXT         NOT NULL
  COMMENT 'ハッシュ化したものを保存',
  name      VARCHAR(255) NOT NULL,
  PRIMARY KEY (member_id),
  UNIQUE (email, name)
);

CREATE TABLE Tweet (
  tweet_id  INT       NOT NULL AUTO_INCREMENT,
  member_id INT,
  tweet     VARCHAR(140),
  tweet_at  TIMESTAMP NOT NULL DEFAULT current_timestamp,
  PRIMARY KEY (Tweet_id),
  FOREIGN KEY (member_id) REFERENCES Member (member_id)
);

CREATE TABLE Follow (
  follower_id INT NOT NULL,
  followed_id INT NOT NULL,
  UNIQUE (follower_id, followed_id),
  FOREIGN KEY (followed_id) REFERENCES Member (member_id),
  FOREIGN KEY (follower_id) REFERENCES Member (member_id)
);

CREATE TABLE Eval (
  tweet_id    INT NOT NULL
  COMMENT '評価されるコメントのID',
  eval_status INT NOT NULL
  COMMENT '1ならいいねの状態､0ならどうでもいいねの状態',
  member_id   INT NOT NULL
  COMMENT '評価する人のID',
  UNIQUE (tweet_id, eval_status, member_id),
  FOREIGN KEY (tweet_id) REFERENCES Tweet (tweet_id),
  FOREIGN KEY (member_id) REFERENCES Member (member_id)
);


