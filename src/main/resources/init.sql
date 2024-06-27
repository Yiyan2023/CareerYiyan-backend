##################################################
#                    User                     #
##################################################
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

##################################################
#                    Post                     #
##################################################
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post`
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    title varchar(40) null ,
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id int,
    is_private bool default false,
    photos json,
    parent_id int default 0
);



##################################################
#                    Like                     #
##################################################
DROP TABLE IF EXISTS `like`;
CREATE TABLE `like`
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id int,
    `type` int,/*点赞类型，1：点赞帖子，2：点赞评论*/
    foreign_id int
);

##################################################
#                    Comment                     #
##################################################
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT,
    user_id INT,
    parent_id INT,
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


