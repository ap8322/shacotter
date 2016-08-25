CREATE TABLE Member (
  member_id INT(10)      NOT NULL AUTO_INCREMENT,
  email     VARCHAR(155) NOT NULL UNIQUE,
  password  VARCHAR(155) NOT NULL
  COMMENT 'ハッシュ化したものを保存',
  name      VARCHAR(155) NOT NULL,
  PRIMARY KEY (member_id),
  UNIQUE (email, name)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE Tweet (
  tweet_id  INT(10)      NOT NULL AUTO_INCREMENT,
  member_id INT(10)      NOT NULL,
  tweet     VARCHAR(140) NOT NULL,
  tweet_at  TIMESTAMP    NOT NULL,
  PRIMARY KEY (Tweet_id),
  FOREIGN KEY (member_id) REFERENCES Member (member_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE Follow (
  follower_id INT(10) NOT NULL,
  followed_id INT(10) NOT NULL,
  UNIQUE (follower_id, followed_id),
  FOREIGN KEY (followed_id) REFERENCES Member (member_id),
  FOREIGN KEY (follower_id) REFERENCES Member (member_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE Eval (
  tweet_id    INT(10) NOT NULL
  COMMENT '評価されるコメントのID',
  eval_status INT(1)  NOT NULL
  COMMENT '1ならいいねの状態､0ならどうでもいいねの状態',
  member_id   INT(10) NOT NULL
  COMMENT '評価する人のID',
  UNIQUE (tweet_id, member_id),
  FOREIGN KEY (tweet_id) REFERENCES Tweet (tweet_id),
  FOREIGN KEY (member_id) REFERENCES Member (member_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;