package com.yiyan.careeryiyan.controller;

import com.yiyan.careeryiyan.config.OSSConfig;
import com.yiyan.careeryiyan.exception.BaseException;
import com.yiyan.careeryiyan.mapper.PostMapper;
import com.yiyan.careeryiyan.model.domain.*;
import com.yiyan.careeryiyan.mapper.PostMapper;
import com.yiyan.careeryiyan.model.domain.Post;
import com.yiyan.careeryiyan.model.request.*;
import com.yiyan.careeryiyan.model.request.AddPostRequest;
import com.yiyan.careeryiyan.model.request.*;
import com.yiyan.careeryiyan.model.response.StringResponse;
import com.yiyan.careeryiyan.model.response.UserInfoResponse;
import com.yiyan.careeryiyan.model.response.UserSaltResponse;
import com.yiyan.careeryiyan.service.EnterpriseService;
import com.yiyan.careeryiyan.service.PostService;
import com.yiyan.careeryiyan.model.request.LoginRequest;
import com.yiyan.careeryiyan.model.request.RegisterRequest;
import com.yiyan.careeryiyan.model.request.StringRequest;

import com.yiyan.careeryiyan.service.PostService;
import com.yiyan.careeryiyan.service.UserService;
import com.yiyan.careeryiyan.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Delete;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Collectors;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;
    @Resource
    OSSConfig ossConfig;
    @Resource
    EnterpriseService enterpriseService;

    @PostMapping("/register")
    public ResponseEntity<StringResponse> register(@RequestBody RegisterRequest registerRequest,
            HttpServletRequest httpServletRequest) {
        String userName = registerRequest.getUserName();
        String userPwd = registerRequest.getUserPwd();
        String userEmail = registerRequest.getUserEmail();
        String userSalt = registerRequest.getUserSalt();
        userService.register(userName, userPwd, userEmail, userSalt);
        return ResponseEntity.ok(new StringResponse("成功"));
    }

    @GetMapping("/salt")
    public ResponseEntity<UserSaltResponse> getSalt(@RequestParam String email) {
        String salt = userService.getSaltByEmail(email);
        return ResponseEntity.ok(new UserSaltResponse(salt));
    }
    //db refactor at 2024年6月29日00点19分
    //simple test pass 2024年6月29日00点26分
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest,
            HttpServletRequest httpServletRequest) {
        String email = loginRequest.getUserEmail();
        String password = loginRequest.getUserPwd();
        User user = userService.login(email, password);
        Map<String, Object> map = user.toDict();
        String token = JwtUtil.generateToken(user.getUserId());
        map.put("token", token);
        return ResponseEntity.ok(map);
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getUserProfile(HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");

        if (user == null)
            throw new BaseException("用户不存在");
        return ResponseEntity.ok(user.toDict());
    }


    @PostMapping("/uploadCV")
    public ResponseEntity<Map<String, String>> uploadCV(@RequestParam("file") MultipartFile file,
            HttpServletRequest httpServletRequest) throws IOException {
        User user = (User) httpServletRequest.getAttribute("user");
        String id = user.getUserId();
        String name = user.getUserName() + "_CV.pdf";

        if (ObjectUtils.isEmpty(file) || file.getSize() <= 0) {
            throw new BaseException("File is empty");
        }
        if (!file.getContentType().equals("application/pdf")) {
            throw new BaseException("File must be a PDF");
        }
        Map<String, String> res = new HashMap<>();
        String userCvUrl = ossConfig.upload(file, "CV", name);
        if (userCvUrl != null) {
            int res2 = userService.updateCV(userCvUrl, id);
            if (res2 == 0)
                throw new BaseException("用户头像后台修改失败");
            res.put("userCvUrl", userCvUrl);
            return ResponseEntity.ok(res);
        } else {
            throw new BaseException("简历上传失败");
        }

    }

    @PostMapping("/getInfo")
    public ResponseEntity<Map<String, Object>> showInfo(@RequestBody UserIdRequest userIdRequest){
        String id = userIdRequest.getUserId();

        Map<String, Object> res = new HashMap<>();
        User userShow = userService.getUserInfo(id);
        if (userShow == null){
            throw new BaseException("用户不存在");
        }
        res.put("user", userShow);

        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(id);
        Map<String, Object> enterprise = new HashMap<>();

        if(enterpriseUser == null){
            enterprise.put("epUserId", null);
            enterprise.put("epId",null);
            enterprise.put("epUserAuth", null);
            enterprise.put("epUserTitle", null);
            enterprise.put("epUserCreateAt", null);
            enterprise.put("isDelete", null);
            enterprise.put("epName", null);
        } else {
            enterprise.put("epUserId", enterpriseUser.getEpUserId());
            enterprise.put("epId",enterpriseUser.getEpId());
            enterprise.put("epUserAuth", enterpriseUser.getEpUserAuth());
            enterprise.put("epUserTitle", enterpriseUser.getEpUserTitle());
            enterprise.put("epUserCreateAt", enterpriseUser.getEpUserCreateAt());
            enterprise.put("isDelete", enterpriseUser.getIsDelete());
            Enterprise enterprise1 = enterpriseService.getEnterpriseByEpId(enterpriseUser.getEpId());
            enterprise.put("epName", enterprise1.getEpName());
        }
        res.put("enterpriseUser", enterprise);

        List<UserRecruitmentPreferences> userRecruitmentPreferencesList = userService.getUserRecruitmentPreferences(id);
        res.put("userRecruitmentPreference", userRecruitmentPreferencesList);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/verifyInfo")
    public ResponseEntity<StringResponse> modifyInfo(@RequestBody ModifyInfoRequest modifyInfoRequest, HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getAttribute("user");
        String id = modifyInfoRequest.getUser().getUserId();

        // 修改user表
        int res = userService.updateUserInfo(modifyInfoRequest.getUser());
        if (res == 0)
            throw new BaseException("修改失败");

        // 修改rc表
        userService.deleteUserRecruitmentPreferences(id);
        String rcTag = "";
        List<String> rcTagList = modifyInfoRequest.getRcTag();
        for(int i = 0; i < rcTagList.size(); i++){
            res = userService.insertUserRecruitmentPreferences(id, rcTagList.get(i));
            if(res == 0)
                throw new BaseException("新增失败");
        }


        return ResponseEntity.ok(new StringResponse("更新成功"));
    }

    @PostMapping("/uploadAvatar")
    public ResponseEntity<Map<String, String>> uploadAvatar(@RequestParam("file") MultipartFile file,
                                                       HttpServletRequest httpServletRequest) throws IOException {
        User user = (User) httpServletRequest.getAttribute("user");
        String id = user.getUserId();
        String name = user.getUserNickname()+ "_avatar.jpg";

        if (ObjectUtils.isEmpty(file) || file.getSize() <= 0) {
            throw new BaseException("File is empty");
        }
        if (!file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png")) {
            throw new BaseException("File must be a jpg/png");
        }
        Map<String, String> res = new HashMap<>();
        String userAvatarUrl = ossConfig.upload(file, "avatar", name);
        if (userAvatarUrl != null) {
            int res2 = userService.updateAvatar(userAvatarUrl, id);
            if (res2 == 0)
                throw new BaseException("用户头像后台修改失败");
            res.put("userAvatarUrl", userAvatarUrl);
            return ResponseEntity.ok(res);
        } else {
            throw new BaseException("简历上传失败");
        }

    }


    private UserInfoResponse convertToUserInfo(User user) {
        if (user == null) {
            return null;
        }
        UserInfoResponse userInfo = new UserInfoResponse();
        for (Field userField : User.class.getDeclaredFields()) {
            try {
                userField.setAccessible(true);
                Field userInfoField = UserInfoResponse.class.getDeclaredField(userField.getName());
                userInfoField.setAccessible(true);
                userInfoField.set(userInfo, userField.get(user));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // 忽略字段不存在或无法访问的异常
            }
        }
        return userInfo;
    }




}
