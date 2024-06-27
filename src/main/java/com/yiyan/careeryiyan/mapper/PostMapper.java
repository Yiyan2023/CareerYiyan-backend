package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.Comment;
import com.yiyan.careeryiyan.model.domain.LikeComment;
import com.yiyan.careeryiyan.model.domain.LikePost;
import com.yiyan.careeryiyan.model.domain.Post;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface PostMapper {
    @Insert("INSERT INTO post(post_content, post_create_at, user_id, post_photo_urls, post_title, post_parent_id)" +
            "VALUES (#{postContent}, #{postCreateAt}, #{userId}, #{postPhotoUrls}, #{postTitle}, #{postParentId})")
    @Options(useGeneratedKeys = true, keyProperty = "postId", keyColumn = "post_id")
    int insertPost(Post post);

//    @Delete("DELETE FROM post WHERE post_id = #{postId}")
    @Update("UPDATE post SET is_delete=1 WHERE post_id = #{postId}")
    void deletePost(String postId);

    @Select("SELECT * FROM post WHERE post_id = #{postId} AND is_delete = 0")
    Post getPostById(String postId);

    @Select("SELECT * FROM post WHERE user_id = #{userId} AND is_delete = 0")
    List<Post> getPostByUser(String userId);

    @Insert("INSERT INTO like_post(like_post_create_at, user_id, post_id)" +
            "VALUES (#{likePostCreateAt}, #{userId}, #{postId})")
    @Options(useGeneratedKeys = true, keyProperty = "likePostId", keyColumn = "like_post_id")
    int insertLikePost(LikePost likePost);

    @Select("SELECT * FROM like_post WHERE user_id = #{userId} AND post_id = #{postId} AND is_delete = 0")
    LikePost getLikePost(String userId, String postId);

//    @Delete("DELETE FROM like_post WHERE like_post_id = #{likePostId}")
    @Update("UPDATE like_post SET is_delete=1 WHERE like_post_id = #{likePostId}")
    void deleteLikePost(String likePostId);

    @Insert("INSERT INTO comment(post_id, user_id, comment_content, comment_create_at, comment_parent_id)" +
            "VALUES (#{postId}, #{userId}, #{commentContent}, #{commentCreateAt}, #{commentParentId})")
    @Options(useGeneratedKeys = true, keyProperty = "commentId", keyColumn = "comment_id")
    int insertComment(Comment comment);

//    @Delete("DELETE FROM comment WHERE comment_id = #{commentId}")
    @Update("UPDATE comment SET is_delete=1 WHERE comment_id = #{commentId}")
    void deleteComment(String commentId);

    @Select("SELECT * FROM comment WHERE comment_id = #{commentId} AND is_delete = 0")
    Comment getCommentById(String commentId);

    @Select("SELECT * FROM comment WHERE post_id = #{postId} " +
            "AND (#{commentParentId} IS NULL OR comment_parent_id = #{commentParentId}) AND is_delete = 0")
    List<Comment> getAllComments(String postId, String commentParentId);

    @Insert("INSERT INTO like_comment(like_comment_create_at, user_id, comment_id)" +
            "VALUES (#{likeCommentCreateAt}, #{userId}, #{commentId})")
    @Options(useGeneratedKeys = true, keyProperty = "likeCommentId", keyColumn = "like_comment_id")
    int insertLikeComment(LikeComment likeComment);

    @Select("SELECT * FROM like_comment WHERE user_id = #{userId} AND comment_id = #{commentId} AND is_delete = 0")
    LikeComment getLikeComment(String userId, String commentId);

//    @Delete("DELETE FROM like_comment WHERE like_comment_id = #{likeCommentId}")
    @Update("UPDATE like_comment SET is_delete=1 WHERE like_comment_id = #{likeCommentId}")
    void deleteLikeComment(String likeCommentId);
}