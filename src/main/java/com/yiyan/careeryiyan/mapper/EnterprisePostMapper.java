package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.EnterprisePost;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EnterprisePostMapper {

    @Select("SELECT post_id FROM enterprise_post WHERE ep_id = #{epId} " +
            "AND is_delete = 0 ORDER BY p.post_id DESC")
    List<String> getEnterprisePosts(String epId);

    @Insert("INSERT INTO enterprise_post(post_id,ep_id, is_delete) " +
            "VALUES (#{postId}, #{epId}, #{isDelete})")
    @Options(useGeneratedKeys = true, keyProperty = "epPostId", keyColumn = "ep_post_id")
    int insertEnterprisePost(EnterprisePost enterprisePost);

    @Select("SELECT * FROM enterprise_post WHERE ep_id=#{epId} AND post_id=#{postId}")
    EnterprisePost findEnterprisePost(String epId, String postId);

    @Update("UPDATE enterprise_post SET is_delete=#{isDelete} WHERE ep_post_id=#{epPostId}")
    int updateEnterprisePost(String epPostId, int isDelete);
}
