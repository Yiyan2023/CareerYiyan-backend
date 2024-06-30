package com.yiyan.careeryiyan.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface RecommandMapper {

    @Select("select r.*,\n" +
            "       e.*,\n" +
            "       u.*,\n" +
            "       r.rc_id           as rc_id,\n" +
            "       r.ep_id           as ep_id,\n" +
            "       u.user_id         as hr_id,\n" +
            "       u.user_name       as hr_name,\n" +
            "       u.user_avatar_url as hr_avatar_url,\n" +
            "       count(apply_id)   as a_count\n" +
            "from recruitment r\n" +
            "         left join apply a on r.rc_id = a.rc_id\n" +
            "         join enterprise e on r.ep_id = e.ep_id\n" +
            "         join enterprise_user eu on e.ep_id = eu.ep_id\n" +
            "         join user u on eu.user_id = u.user_id\n" +
            "where apply_create_at >= DATE_SUB(now(), interval 6 day)\n" +
            "  and r.is_delete = 0\n" +
            "  and a.is_delete = 0\n" +
            "  and u.is_delete = 0\n" +
            "group by rc_id,user_id,e.ep_id\n" +
            "order by a_count desc\n" +
            "limit 20;")
    List<Map<String, Object>> getHotRecruitmentList();

    //完成
    @Select("SELECT e.*,e.ep_id as ep_id, COUNT(rc_id) AS rc_count\n" +
            "FROM enterprise e\n" +
            "LEFT JOIN recruitment r ON e.ep_id = r.ep_id\n" +
            "WHERE rc_create_at >= DATE_SUB(now(), INTERVAL 6 MONTH) and e.is_delete=0 and r.is_delete=0\n" +
            "GROUP BY e.ep_id\n" +
            "ORDER BY rc_count DESC\n" +
            "LIMIT 20;")
    List<Map<String, Object>> getHotEnterpriseList();

    //完成
    @Select("select u.*,e.*, u.user_id as user_id,e.ep_id as ep_id, count(follow_user_id) as p_count\n" +
            "from user u\n" +
            "         left join follow_user fu on u.user_id = fu.following_user_id\n" +
            "         join enterprise_user eu on u.user_id = eu.user_id\n" +
            "         join enterprise e on eu.ep_id = e.ep_id\n" +
            "where follow_user_create_at >= DATE_SUB(now(), INTERVAL 6 MONTH)\n" +
            "  and u.is_delete = 0\n" +
            "  and fu.is_delete = 0\n" +
            "group by user_id,ep_id\n" +
            "order by p_count desc\n" +
            "limit 20;")
    List<Map<String, Object>> getHotUserList();

    //完成
    @Select("select p.*, u.*, u.user_id as user_id, COUNT(like_post_id) AS lp_count\n" +
            "from post p\n" +
            "         left join like_post lp on p.post_id = lp.post_id\n" +
            "         join user u on p.user_id = u.user_id\n" +
            "where like_post_create_at >= DATE_SUB(now(), INTERVAL 6 MONTH)\n" +
            "  and p.is_delete = 0\n" +
            "  and u.is_delete = 0\n" +
            "  and lp.is_delete = 0\n" +
            "group by p.post_id\n" +
            "order by lp_count desc\n" +
            "limit 20;")
    List<Map<String, Object>> getHotPostList();
}
