package com.yiyan.careeryiyan.controller;

import com.yiyan.careeryiyan.config.OSSConfig;
import com.yiyan.careeryiyan.exception.BaseException;
import com.yiyan.careeryiyan.mapper.PostMapper;
import com.yiyan.careeryiyan.model.domain.Enterprise;
import com.yiyan.careeryiyan.model.domain.EnterpriseUser;
import com.yiyan.careeryiyan.model.domain.Post;
import com.yiyan.careeryiyan.mapper.PostMapper;
import com.yiyan.careeryiyan.model.domain.Comment;
import com.yiyan.careeryiyan.model.domain.Post;
import com.yiyan.careeryiyan.model.domain.User;
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
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        String email = registerRequest.getEmail();
        String salt = registerRequest.getSalt();
        userService.register(username, password, email, salt);
        return ResponseEntity.ok(new StringResponse("成功"));
    }

    @GetMapping("/salt")
    public ResponseEntity<UserSaltResponse> getSalt(@RequestParam String email) {
        String salt = userService.getSaltByEmail(email);
        return ResponseEntity.ok(new UserSaltResponse(salt));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest,
            HttpServletRequest httpServletRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
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
    public ResponseEntity<StringResponse> uploadCV(@RequestParam("file") MultipartFile file,
            HttpServletRequest httpServletRequest) throws IOException {
        User user = (User) httpServletRequest.getAttribute("user");
        String id = user.getUserId();
        String name = user.getUserNickname() + "_CV.pdf";

        if (ObjectUtils.isEmpty(file) || file.getSize() <= 0) {
            throw new BaseException("File is empty");
        }
        if (!file.getContentType().equals("application/pdf")) {
            throw new BaseException("File must be a PDF");
        }
        String res = ossConfig.upload(file, "CV", name);
        if (res != null) {
            int res2 = userService.updateCV(res, id);
            if (res2 == 0)
                throw new BaseException("用户头像后台修改失败");
            return ResponseEntity.ok(new StringResponse(res));
        } else {
            throw new BaseException("简历上传失败");
        }

    }

    @PostMapping("/getInfo")
    public ResponseEntity<UserInfoResponse> showInfo(@RequestBody StringRequest stringRequest){
        String id = stringRequest.getValue();

//        System.out.println(id);
//        System.out.println(stringRequest);

        User userShow = userService.getUserInfo(id);

//        System.out.println(userShow);
        if (userShow == null){
            throw new BaseException("用户不存在");
        }
        UserInfoResponse userInfoResponse = convertToUserInfo(userShow);
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(id);
        if(enterpriseUser != null){
            String enterpriseId = enterpriseUser.getEpId();
            Enterprise enterprise = enterpriseService.getEnterpriseById(enterpriseId);
            userInfoResponse.setEnterpriseId(enterpriseId);
            userInfoResponse.setEnterpriseName(enterprise.getEpName());
        }
        return ResponseEntity.ok(userInfoResponse);
    }

    @PostMapping("/verifyInfo")
    public ResponseEntity<UserInfoResponse> modifyInfo(@RequestBody ModifyInfoRequest modifyInfoRequest, HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getAttribute("user");
        String id = user.getUserId();

        modifyInfoRequest.setId(id);
        int res = userService.updateUserInfo(modifyInfoRequest);
        if (res == 0)
            throw new BaseException("修改失败");

        User userShow = userService.getUserInfo(id);
        if (userShow == null){
            throw new BaseException("用户不存在");
        }

        UserInfoResponse userInfoResponse = convertToUserInfo(userShow);
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(id);
        String enterpriseId = enterpriseUser.getEpId();
        Enterprise enterprise = enterpriseService.getEnterpriseById(enterpriseId);
        userInfoResponse.setEnterpriseId(enterpriseId);
        userInfoResponse.setEnterpriseName(enterprise.getEpName());

        return ResponseEntity.ok(userInfoResponse);
    }

    @PostMapping("/uploadAvatar")
    public ResponseEntity<StringResponse> uploadAvatar(@RequestParam("file") MultipartFile file,
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
        String res = ossConfig.upload(file, "avatar", name);
        if (res != null) {
            int res2 = userService.updateAvatar(res, id);
            if (res2 == 0)
                throw new BaseException("用户头像后台修改失败");
            return ResponseEntity.ok(new StringResponse(res));
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
