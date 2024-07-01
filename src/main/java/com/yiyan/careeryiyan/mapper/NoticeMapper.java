package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.Notice;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoticeMapper {

    @Insert({
            "<script>",
            "INSERT INTO notice",
            "<trim prefix='(' suffix=')' suffixOverrides=','>",
            "<if test='noticeType != null'>notice_type,</if>",
            "<if test='noticeContent != null'>notice_content,</if>",
            "<if test='postId != null'>post_id,</if>",
            "<if test='epId != null'>ep_id,</if>",
            "<if test='userId != null'>user_id,</if>",
            "notice_create_at",  // 添加字段
            "</trim>",
            "<trim prefix='VALUES (' suffix=')' suffixOverrides=','>",
            "<if test='noticeType != null'>#{noticeType},</if>",
            "<if test='noticeContent != null'>#{noticeContent},</if>",
            "<if test='postId != null'>#{postId},</if>",
            "<if test='epId != null'>#{epId},</if>",
            "<if test='userId != null'>#{userId},</if>",
            "now()",  // 添加值
            "</trim>",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "noticeId")
    void insertNotice(Notice notice);


    @Update({
            "<script>",
            "UPDATE notice",
            "SET is_read = 1",
            "WHERE user_id = #{userId} and notice_id IN",
            "<foreach item='id' collection='noticeIds' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    void markAsReadBatch(@Param("userId") String userId,@Param("noticeIds") List<String> noticeIds);

    @Select(
            "SELECT * FROM notice\n" +
                    "WHERE user_id=#{userId};"
    )
    List<Notice> getNotices(String userId);
}

