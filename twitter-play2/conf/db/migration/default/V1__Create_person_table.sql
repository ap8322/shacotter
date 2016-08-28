CREATE TABLE Member (
  member_id         BIGINT       NOT NULL AUTO_INCREMENT,
  email             VARCHAR(200) NOT NULL UNIQUE,
  password          VARCHAR(200) NOT NULL
  COMMENT 'ハッシュ化したものを保存',
  name              VARCHAR(200) NOT NULL,
  REGISTER_DATETIME DATETIME     NOT NULL,
  REGISTER_USER     VARCHAR(200) NOT NULL,
  UPDATE_DATETIME   DATETIME     NOT NULL,
  UPDATE_USER       VARCHAR(200) NOT NULL,
  VERSION_NO        BIGINT       NOT NULL,
  PRIMARY KEY (member_id),
  UNIQUE (email, name)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE Tweet (
  tweet_id          BIGINT       NOT NULL AUTO_INCREMENT,
  member_id         BIGINT       NOT NULL,
  tweet             VARCHAR(140) NOT NULL,
  tweet_at          TIMESTAMP    NOT NULL,
  REGISTER_DATETIME DATETIME     NOT NULL,
  REGISTER_USER     VARCHAR(200) NOT NULL,
  UPDATE_DATETIME   DATETIME     NOT NULL,
  UPDATE_USER       VARCHAR(200) NOT NULL,
  VERSION_NO        BIGINT       NOT NULL,
  PRIMARY KEY (Tweet_id),
  FOREIGN KEY (member_id) REFERENCES Member (member_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE Follow (
  follower_id       BIGINT       NOT NULL,
  followed_id       BIGINT       NOT NULL,
  REGISTER_DATETIME DATETIME     NOT NULL,
  REGISTER_USER     VARCHAR(200) NOT NULL,
  UPDATE_DATETIME   DATETIME     NOT NULL,
  UPDATE_USER       VARCHAR(200) NOT NULL,
  VERSION_NO        BIGINT       NOT NULL,
  UNIQUE (follower_id, followed_id),
  FOREIGN KEY (followed_id) REFERENCES Member (member_id),
  FOREIGN KEY (follower_id) REFERENCES Member (member_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE TweetEvaluete (
  tweet_id          INT(10)      NOT NULL
  COMMENT '評価されるコメントのID',
  evaluete_status   INT(1)       NOT NULL
  COMMENT '1ならいいねの状態､0ならどうでもいいねの状態',
  member_id         INT(10)      NOT NULL
  COMMENT '評価する人のID',
  REGISTER_DATETIME DATETIME     NOT NULL,
  REGISTER_USER     VARCHAR(200) NOT NULL,
  UPDATE_DATETIME   DATETIME     NOT NULL,
  UPDATE_USER       VARCHAR(200) NOT NULL,
  VERSION_NO        BIGINT       NOT NULL,
  UNIQUE (tweet_id, member_id),
  FOREIGN KEY (tweet_id) REFERENCES Tweet (tweet_id),
  FOREIGN KEY (member_id) REFERENCES Member (member_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;