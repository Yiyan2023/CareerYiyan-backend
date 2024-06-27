package com.yiyan.careeryiyan.service;

import com.yiyan.careeryiyan.exception.BaseException;
import com.yiyan.careeryiyan.mapper.UserMapper;
import com.yiyan.careeryiyan.model.domain.User;
import com.yiyan.careeryiyan.model.domain.UserJobPreferences;
import com.yiyan.careeryiyan.model.request.ModifyInfoRequest;
import jakarta.annotation.Resource;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {


    @Resource
    UserMapper userMapper;

    public static String saltEncryption(String password, String salt) {
        password = password + salt;
        return DigestUtils.sha256Hex(password);
    }


    public void register(String username, String password, String email, String salt) {

        boolean isEmailValid = email.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        if (!isEmailValid) {
            throw new BaseException("邮箱格式不正确");
        }
        User user = userMapper.getUserByEmail(email);
        Date created_at = new Date();
        if (user != null) {
            throw new BaseException("邮箱已注册");
        }
        user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setSalt(salt);
        user.setPassword(saltEncryption(password, salt));
        user.setRegisterTime(created_at);
        user.setGender("男");
        user.setEducation("未知");
        user.setAvatarUrl("https://career-yiyan.oss-cn-beijing.aliyuncs.com/test/chiikawa.png");
        user.setNickname(username);
        user.setInterests("该用户没有设置自己的兴趣");
        user.setPosition("未知");
        userMapper.insertUser(user);
    }

    public User login(String email, String password) {
        User user = userMapper.getUserByEmail(email);
        if (user == null) {
            throw new BaseException("邮箱未注册");
        }
        password = saltEncryption(password, user.getSalt());
        System.out.println(password);
        if (!user.getPassword().equals(password)) {
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
        return user.getSalt();
    }

    public int updateUserInfo(ModifyInfoRequest request){
        int res = userMapper.modifyUser(request);
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


    public List<UserJobPreferences> getUserJobPreferences(String userId) {
        return userMapper.getUserJobPreferences(userId);
    }
}