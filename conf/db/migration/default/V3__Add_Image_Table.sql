CREATE TABLE IMAGE (
  IMAGE_ID          BIGINT(20)   NOT NULL AUTO_INCREMENT,
  MEMBER_ID         BIGINT(20)   NOT NULL,
  IMAGE_NAME        VARCHAR(200) NOT NULL,
  IMAGE_DATA        MEDIUMTEXT   NOT NULL,
  REGISTER_DATETIME DATETIME     NOT NULL,
  REGISTER_USER     VARCHAR(200) NOT NULL,
  UPDATE_DATETIME   DATETIME     NOT NULL,
  UPDATE_USER       VARCHAR(200) NOT NULL,
  VERSION_NO        BIGINT(20)   NOT NULL,
  PRIMARY KEY (IMAGE_ID),
  FOREIGN KEY (MEMBER_ID) REFERENCES MEMBER (MEMBER_ID)
)
  ENGINE = INNODB
  DEFAULT CHARSET = UTF8MB4;