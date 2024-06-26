package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.Comment;
import com.yiyan.careeryiyan.model.domain.Like;
import com.yiyan.careeryiyan.model.domain.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper {

    @Insert("INSERT INTO post( content, created_at, user_id, photos)" +
            "VALUES ( #{content}, #{createdAt}, #{userId}, #{photos})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertPost(Post post);


    @Delete("DELETE FROM post WHERE id=#{id}")
    void deletePost(String  id);

    @Select("SELECT * FROM post WHERE id=#{id}")
//    @Results(id = "postResultMap", value = {
//            @Result(property = "id", column = "id"),
//            @Result(property = "content", column = "content"),
//            @Result(property = "createdAt", column = "created_at"),
//            @Result(property = "userId", column = "user_id"),
//            @Result(property = "photosJson", column = "photos")
//    })
    Post getPostById(String id);

    @Select("SELECT * FROM post WHERE user_id=#{userId}")
//    @ResultMap("postResultMap")
    List<Post> getPostByUser(String userId);

//    @Select("SELECT * FROM like WHERE user_id=#{userId} and foreign_id=#{foreignId} and type=#{type} ORDER BY id DESC LIMIT 1")
//    Like getLike(String userId,String foreignId,int type);

    @Insert("INSERT INTO `like`(type,foreign_id,user_id,created_At)" +
            "values (#{type},#{foreignId},#{userId},#{createdAt})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int insertLike(Like like);

    @Select("SELECT * FROM `like` WHERE user_id=#{userId} AND foreign_id=#{foreignId} AND type=#{type} ")
    Like getLike(String userId, String foreignId, int type);

    @Delete("DELETE FROM like WHERE id=#{id}")
    void deleteLike(String id);
    @Insert("INSERT INTO comment(post_id,user_id,content,created_at,parent_id)" +
            "values (#{postId},#{userId},#{content},#{createdAt},#{parentId})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int insertComment(Comment comment);

    @Delete("DELETE FROM comment WHERE id=#{id}")
    void delComment(String id);

    @Select("SELECT * FROM comment WHERE id=#{id}")
    Comment getCommentById(String id);

    @Select("SELECT * FROM comment WHERE post_id=#{postId} AND parent_id=#{parentId}")
    List<Comment> getAllComments(String postId,String parentId);

}
