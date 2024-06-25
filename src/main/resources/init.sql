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
    title varchar(40),
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id int,
    is_private bool default false
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
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
