CREATE TABLE user (
                      user_id INT AUTO_INCREMENT PRIMARY KEY,
                      user_name VARCHAR(255) NOT NULL,
                      user_pwd VARCHAR(255) NOT NULL,
                      user_email VARCHAR(255) NOT NULL,
                      user_gender VARCHAR(10),
                      user_salt VARCHAR(255),
                      user_reg_at DATETIME,
                      user_avatar_url VARCHAR(255),
                      user_nickname VARCHAR(255),
                      user_edu VARCHAR(255),
                      user_interest VARCHAR(255),
                      user_cv_url VARCHAR(255),
                      user_addr VARCHAR(255),
                      user_github_url VARCHAR(255),
                      user_blog_url VARCHAR(255),
                      user_influence INT DEFAULT 0 NOT NULL,
                      is_delete BOOL DEFAULT 0 NOT NULL
);

CREATE TABLE enterprise (
                            ep_id INT AUTO_INCREMENT PRIMARY KEY,
                            ep_name VARCHAR(255) NOT NULL,
                            ep_addr VARCHAR(255),
                            ep_desc TEXT,
                            ep_type VARCHAR(255),
                            ep_license VARCHAR(255),
                            ep_create_at DATETIME,
                            ep_avatar_url VARCHAR(255),
                            is_delete BOOL DEFAULT 0 NOT NULL
);
-- 招聘信息表
CREATE TABLE recruitment (
                             rc_id INT AUTO_INCREMENT PRIMARY KEY,
                             rc_name VARCHAR(255) NOT NULL,
                             rc_addr VARCHAR(255),
                             rc_tag VARCHAR(255),
                             rc_min_salary INT,
                             rc_max_salary INT,
                             rc_salary_count INT,
                             rc_edu VARCHAR(255),
                             rc_desc TEXT,
                             rc_total_count INT NOT NULL,
                             rc_offer_count INT DEFAULT 0 NOT NULL,
                             rc_accept_count INT DEFAULT 0 NOT NULL,
                             rc_create_at DATETIME,
                             ep_id INT NOT NULL,
                             is_delete BOOL DEFAULT 0 NOT NULL,
                             FOREIGN KEY (ep_id) REFERENCES enterprise(ep_id)
);

-- 创建全文索引
CREATE FULLTEXT INDEX full_index_name ON enterprise (ep_name);
-- 申请表
CREATE TABLE apply (
                       apply_id INT AUTO_INCREMENT PRIMARY KEY,
                       user_id INT,
                       rc_id INT,
                       apply_status INT,
                       apply_cv_url VARCHAR(255),
                       apply_create_at DATETIME NOT NULL,
                       apply_update_at DATETIME,
                       is_delete BOOL DEFAULT 0 NOT NULL,
                       FOREIGN KEY (user_id) REFERENCES user(user_id),
                       FOREIGN KEY (rc_id) REFERENCES recruitment(rc_id)
);

-- 创建全文索引
CREATE FULLTEXT INDEX full_index_name ON recruitment(rc_name);
-- 企业-用户表
CREATE TABLE enterprise_user (
                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                 ep_id INT NOT NULL,
                                 user_id INT NOT NULL,
                                 ep_user_auth INT,
                                 ep_user_title INT,
                                 ep_user_create_at DATETIME,
                                 is_delete BOOL DEFAULT 0 NOT NULL,
                                 FOREIGN KEY (ep_id) REFERENCES enterprise(ep_id),
                                 FOREIGN KEY (user_id) REFERENCES user(user_id)
);



-- 用户偏好表
CREATE TABLE user_recruitment_preferences (
                                              user_rc_pref_id INT NOT NULL PRIMARY KEY,
                                              user_id INT,
                                              rc_tag VARCHAR(255),
                                              is_delete BOOL DEFAULT 0 NOT NULL,
                                              FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- 帖子表
CREATE TABLE post (
                      post_id INT AUTO_INCREMENT PRIMARY KEY,
                      post_title VARCHAR(40),
                      post_content TEXT,
                      post_create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      user_id INT,
                      post_photo_urls JSON,
                      post_parent_id INT DEFAULT 0,
                      is_delete BOOL DEFAULT 0 NOT NULL,
                      FOREIGN KEY (user_id) REFERENCES user(user_id)
);
-- 评论表
CREATE TABLE comment (
                         comment_id INT AUTO_INCREMENT PRIMARY KEY,
                         post_id INT,
                         user_id INT,
                         comment_parent_id INT,
                         comment_content TEXT,
                         comment_create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         is_delete BOOL DEFAULT 0 NOT NULL,
                         FOREIGN KEY (post_id) REFERENCES post(post_id),
                         FOREIGN KEY (user_id) REFERENCES user(user_id),
                         FOREIGN KEY (comment_parent_id) REFERENCES comment(comment_id)
);

-- 点赞帖子表
CREATE TABLE like_post (
                           like_post_id INT AUTO_INCREMENT PRIMARY KEY,
                           like_post_create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           user_id INT,
                           post_id INT,
                           is_delete BOOL DEFAULT 0 NOT NULL,
                           FOREIGN KEY (user_id) REFERENCES user(user_id),
                           FOREIGN KEY (post_id) REFERENCES post(post_id)
);

-- 点赞评论表
CREATE TABLE like_comment (
                              like_comment_id INT AUTO_INCREMENT PRIMARY KEY,
                              like_comment_create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              user_id INT,
                              comment_id INT,
                              is_delete BOOL DEFAULT 0 NOT NULL,
                              FOREIGN KEY (user_id) REFERENCES user(user_id),
                              FOREIGN KEY (comment_id) REFERENCES comment(comment_id)
);


-- 关注企业表
CREATE TABLE follow_enterprise (
                                   follow_ep_id INT AUTO_INCREMENT PRIMARY KEY,
                                   ep_id INT,
                                   follow_ep_create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   is_delete BOOL DEFAULT 0 NOT NULL,
                                   FOREIGN KEY (ep_id) REFERENCES enterprise(ep_id)
);

-- 关注用户表
CREATE TABLE follow_user (
                             follow_user_id INT AUTO_INCREMENT PRIMARY KEY,
                             user_id INT,
                             follow_user_create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             is_delete BOOL DEFAULT 0 NOT NULL,
                             FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- 聊天表
CREATE TABLE chat (
                      chat_id INT AUTO_INCREMENT PRIMARY KEY,
                      chat_user_id_1 INT,
                      chat_user_id_2 INT,
                      is_delete BOOL DEFAULT 0 NOT NULL,
                      FOREIGN KEY (chat_user_id_1) REFERENCES user(user_id),
                      FOREIGN KEY (chat_user_id_2) REFERENCES user(user_id)
);

-- 消息表
CREATE TABLE message (
                         msg_id INT AUTO_INCREMENT PRIMARY KEY,
                         msg_content TEXT,
                         msg_create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         msg_is_read BOOLEAN DEFAULT 0,
                         msg_send_user_id INT,
                         msg_reply_msg_id INT,
                         is_delete BOOL DEFAULT 0 NOT NULL,
                         FOREIGN KEY (msg_send_user_id) REFERENCES user(user_id),
                         FOREIGN KEY (msg_reply_msg_id) REFERENCES message(msg_id)
);
