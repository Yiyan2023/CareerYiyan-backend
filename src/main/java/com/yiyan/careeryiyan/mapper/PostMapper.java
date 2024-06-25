package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper {

    @Insert("INSERT INTO post(id,title,content,created_at,user_id)" +
            "values (#{id},#{title},#{content},#{createdAt},#{userId})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int insertPost(Post post);

    @Delete("DELETE FROM post WHERE id=#{id}")
    void deletePost(String  id);

    @Select("SELECT * FROM post WHERE id=#{id}")
    Post getPostById(String id);

    @Select("SELECT * FROM post WHERE user_id=#{userId}")
    List<Post> getPostByUser(String userId);
//    @Select("SELECT * FROM post WHERE user_id in ")
//    List<Post> getPostByEn(String userId);
}
