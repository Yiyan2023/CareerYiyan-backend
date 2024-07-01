package com.yiyan.careeryiyan.service;

import com.yiyan.careeryiyan.mapper.EnterpriseMapper;
import com.yiyan.careeryiyan.mapper.FollowMapper;
import com.yiyan.careeryiyan.mapper.UserMapper;
import com.yiyan.careeryiyan.model.domain.Enterprise;
import com.yiyan.careeryiyan.model.domain.FollowEnterprise;
import com.yiyan.careeryiyan.model.domain.FollowUser;
import com.yiyan.careeryiyan.model.domain.User;
import com.yiyan.careeryiyan.utils.MapUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FollowService {

    @Resource
    FollowMapper followMapper;

    @Resource
    EnterpriseMapper enterpriseMapper;

    @Resource
    UserMapper userMapper;

    public int followUser(String userId, String targetUserId) {
        //关注返回0，取关返回1
        User targetUser =userMapper.getUserById(targetUserId);
        if(targetUser==null){
            return -1;
        }
        FollowUser followUser = followMapper.getFollowUser(userId, targetUserId);

        if (followUser == null) {
            System.out.println("关注");
            followMapper.insertFollowUser(userId, targetUserId);
            return 0;
        } else {
            followMapper.updateFollowUser(userId, targetUserId, !followUser.getIsDelete());
            return followUser.getIsDelete() ? 0 : 1;
        }
    }

    public int followEnterprise(String userId, String epId) {
        FollowEnterprise fp = followMapper.getFollowEp(userId, epId);
        //M:判断企业是否存在 记得判断
        Enterprise enterprise=enterpriseMapper.getEnterpriseByEpId(epId);
        if(enterprise == null){
            return -1;
        }
        System.out.println(userId + " " + epId);
        if (fp == null) {
            followMapper.insertFollowEnterprise(userId, epId);
            return 0;
        } else {
            // 取消关注企业
            followMapper.updateFollowEnterprise(userId, epId, !fp.getIsDelete());
            return fp.getIsDelete() ? 0 : 1;
        }
    }

    public Map<String, Object> getFollowingUsers(String userId) {
        List<User> followingUsers = followMapper.getFollowingUsers(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("followingUsers", followingUsers);
        result.put("count", followingUsers.size());
        return result;
    }

    public Map<String, Object> getFollowingEnterprises(String userId) {
        List<Map<String, Object>> followingEnterprises = followMapper.getFollowingEnterprises(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("followingEnterprises", MapUtil.convertKeysToCamelCase(followingEnterprises));
        result.put("count", followingEnterprises.size());
        return result;
    }

    public Map<String, Object> getUserFollowers(String userId) {
        List<Map<String, Object>> followers = followMapper.getUserFollowers(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("followers", followers);
        result.put("count", followers.size());
        return result;
    }

    public Map<String, Object> getEnterpriseFollowers(String epId) {
        List<Map<String, Object>> followers = followMapper.getEnterpriseFollowers(epId);
        Map<String, Object> result = new HashMap<>();
        result.put("followers", followers);
        result.put("count", followers.size());
        return result;
    }

    public boolean checkFollow(String userId, String targetuserId) {
        FollowUser followUser = followMapper.getFollowUser(userId, targetuserId);
        return followUser != null && !followUser.getIsDelete();
    }

    public boolean checkFollowEnterprise(String userId, String epId) {
        FollowEnterprise followEnterprise = followMapper.getFollowEp(userId, epId);
        return followEnterprise != null && !followEnterprise.getIsDelete();
    }
}