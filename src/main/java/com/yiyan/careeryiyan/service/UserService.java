package com.yiyan.careeryiyan.service;

import com.yiyan.careeryiyan.exception.BaseException;
import com.yiyan.careeryiyan.mapper.UserOnlineMapper;
import com.yiyan.careeryiyan.mapper.UserRecruitmentPreferencesMapper;
import com.yiyan.careeryiyan.mapper.UserMapper;
import com.yiyan.careeryiyan.model.domain.User;
import com.yiyan.careeryiyan.model.domain.UserOnline;
import com.yiyan.careeryiyan.model.domain.UserRecruitmentPreferences;
import jakarta.annotation.Resource;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService {


    @Resource
    UserMapper userMapper;
    @Resource
    UserRecruitmentPreferencesMapper userRecruitmentPreferencesMapper;
    @Autowired
    private UserOnlineMapper userOnlineMapper;

    public static String saltEncryption(String password, String salt) {
        password = password + salt;
        return DigestUtils.sha256Hex(password);
    }




    public void register(String userName, String userPwd, String userEmail, String userSalt) {

        boolean isEmailValid = userEmail.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        if (!isEmailValid) {
            throw new BaseException("邮箱格式不正确");
        }
        User user = userMapper.getUserByEmail(userEmail);
        Date created_at = new Date();
        if (user != null) {
            throw new BaseException("邮箱已注册");
        }
        user = new User();
        user.setUserName(userName);
        user.setUserEmail(userEmail);
        user.setUserSalt(userSalt);
        user.setUserPwd(saltEncryption(userPwd, userSalt));
        user.setUserRegAt(created_at);
        user.setUserGender("男");
        user.setUserEdu("未知");
        user.setUserAvatarUrl("https://career-yiyan.oss-cn-beijing.aliyuncs.com/test/chiikawa.png");
        user.setUserNickname(userName);
        user.setUserInterest("该用户没有设置自己的兴趣");
//        user.("未知"); 这是原本的position字段

        userMapper.insertUser(user);
    }

    public User login(String email, String password) {
        User user = userMapper.getUserByEmail(email);
        if (user == null) {
            throw new BaseException("邮箱未注册");
        }
        password = saltEncryption(password, user.getUserSalt());
        System.out.println(password);
        if (!user.getUserPwd().equals(password)) {
            throw new BaseException("密码不正确");
        }
        return user;
    }

    public User getUserInfo(String id){
        User user = userMapper.getUserById(id);
        if(user==null)
            throw new BaseException("用户不存在");
        return user;
    }

    public String getSaltByEmail(String email){
        User user = userMapper.getUserByEmail(email);
        if(user==null)
            throw new BaseException("用户不存在");
        return user.getUserSalt();
    }

    public int updateUserInfo(User user){
        int res = userMapper.modifyUser(user);
        if(res == 0){
            throw new BaseException("修改失败");
        }
        return res;
    }

    public int updateAvatar(String avatarUrl,String id){
        int res = userMapper.updateAvatarUrl(avatarUrl, id);
        return res;
    }

    public int updateCV(String CV,String id){
        int res = userMapper.updateCV(CV, id);
        return res;
    }


    public List<UserRecruitmentPreferences> getUserRecruitmentPreferences(String userId) {
        return userRecruitmentPreferencesMapper.getUserRecruitmentPreferences(userId);
    }

    public void deleteUserRecruitmentPreferences(String userId) {
        userRecruitmentPreferencesMapper.deleteUserRecruitmentPreferences(userId);
    }

    public int insertUserRecruitmentPreferences(String userId, String recruitmentTag) {
        return userRecruitmentPreferencesMapper.insertUserRecruitmentPreferences(userId, recruitmentTag);
    }

    public Map<String, Object> getUserInfoById(String userId){
        return userMapper.getUserInfoById(userId);
    }

    public UserOnline getUserOnlineByUserId(String userId) {
        return userOnlineMapper.getUserOnlineByUserId(userId);
    }

    public List<String> getUserRcTags(String userId) {
        return userMapper.getUserRcTags(userId);

    }

}