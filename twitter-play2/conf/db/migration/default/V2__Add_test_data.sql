INSERT INTO Member(member_id,email,password,name) VALUES (1,'test@gmail.com','$2a$10$0kKzAaqjCC/pCV74DdosNej5eEDrqLusbHnDChTskqBnzEvI/wpqS','test');
INSERT INTO Member(member_id,email,password,name) VALUES (2,'test2@gmail.com','$2a$10$0kKzAaqjCC/pCV74DdosNej5eEDrqLusbHnDChTskqBnzEvI/wpqS','test2');
INSERT INTO Member(member_id,email,password,name) VALUES (3,'test3@gmail.com','$2a$10$0kKzAaqjCC/pCV74DdosNej5eEDrqLusbHnDChTskqBnzEvI/wpqS','test3');
INSERT INTO Member(member_id,email,password,name) VALUES (4,'test4@gmail.com','$2a$10$0kKzAaqjCC/pCV74DdosNej5eEDrqLusbHnDChTskqBnzEvI/wpqS','test4');
INSERT INTO Member(member_id,email,password,name) VALUES (5,'test5@gmail.com','$2a$10$0kKzAaqjCC/pCV74DdosNej5eEDrqLusbHnDChTskqBnzEvI/wpqS','test5');
INSERT INTO Member(member_id,email,password,name) VALUES (6,'test6@gmail.com','$2a$10$0kKzAaqjCC/pCV74DdosNej5eEDrqLusbHnDChTskqBnzEvI/wpqS','test6');
INSERT INTO Member(member_id,email,password,name) VALUES (7,'test7@gmail.com','$2a$10$0kKzAaqjCC/pCV74DdosNej5eEDrqLusbHnDChTskqBnzEvI/wpqS','test7');

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
