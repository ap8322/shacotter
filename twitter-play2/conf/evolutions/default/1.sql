# Users schema
 
# --- !Ups

CREATE TABLE Member (
  member_id int NOT NULL AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL UNIQUE ,
  password TEXT NOT NULL COMMENT 'ハッシュ化したものを保存',
  name VARCHAR(255) NOT NULL ,
  PRIMARY KEY (member_id),
  UNIQUE (email)
);

CREATE TABLE Tweet (
  tweet_id INT NOT NULL AUTO_INCREMENT,
  member_id INT,
  tweet VARCHAR(140),
  tweet_at TIMESTAMP DEFAULT current_timestamp,
  PRIMARY KEY (Tweet_id),
  FOREIGN KEY (member_id) REFERENCES Member(member_id)
);

CREATE TABLE Follow(
  follower_id INT NOT NULL,
  followed_id INT NOT NULL,
  UNIQUE (follower_id,followed_id),
  FOREIGN KEY (followed_id) REFERENCES Member(member_id),
  FOREIGN KEY (follower_id) REFERENCES Member(member_id)
);

INSERT INTO Member(member_id,email,password,name) VALUES (1,'test@gmail.com','test','test');
INSERT INTO Member(member_id,email,password,name) VALUES (2,'test2@gmail.com','test2','test2');
INSERT INTO Member(member_id,email,password,name) VALUES (3,'test3@gmail.com','test3','test3');
INSERT INTO Member(member_id,email,password,name) VALUES (4,'test4@gmail.com','test4','test4');
INSERT INTO Member(member_id,email,password,name) VALUES (5,'test5@gmail.com','test5','test5');
INSERT INTO Member(member_id,email,password,name) VALUES (6,'test6@gmail.com','test6','test6');
INSERT INTO Member(member_id,email,password,name) VALUES (7,'test7@gmail.com','test7','test7');

INSERT INTO Tweet(tweet_id,member_id,tweet) VALUES (1,1,'test');
INSERT INTO Tweet(tweet_id,member_id,tweet) VALUES (2,2,'test');
INSERT INTO Tweet(tweet_id,member_id,tweet) VALUES (3,3,'test');
INSERT INTO Tweet(tweet_id,member_id,tweet) VALUES (4,4,'test');
INSERT INTO Tweet(tweet_id,member_id,tweet) VALUES (5,5,'test');
INSERT INTO Tweet(tweet_id,member_id,tweet) VALUES (6,6,'test');
INSERT INTO Tweet(tweet_id,member_id,tweet) VALUES (7,7,'test');

INSERT INTO Follow(follower_id, followed_id) VALUES (1,2);
INSERT INTO Follow(follower_id, followed_id) VALUES (1,3);
INSERT INTO Follow(follower_id, followed_id) VALUES (2,1);


# --- !Downs
DROP TABLE Tweet;
DROP TABLE Follow;
DROP TABLE Member;