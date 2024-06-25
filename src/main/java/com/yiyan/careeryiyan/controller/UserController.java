package com.yiyan.careeryiyan.controller;

import com.yiyan.careeryiyan.config.OSSConfig;
import com.yiyan.careeryiyan.exception.BaseException;
import com.yiyan.careeryiyan.model.domain.User;
import com.yiyan.careeryiyan.model.response.StringResponse;
import com.yiyan.careeryiyan.model.request.LoginRequest;
import com.yiyan.careeryiyan.model.request.RegisterRequest;
import com.yiyan.careeryiyan.model.request.StringRequest;
import com.yiyan.careeryiyan.service.UserService;
import com.yiyan.careeryiyan.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;
    @Resource
    OSSConfig ossConfig;

    @PostMapping("/register")
    public ResponseEntity<StringResponse> register(@RequestBody RegisterRequest registerRequest, HttpServletRequest httpServletRequest) {
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        String email = registerRequest.getEmail();
        String salt = registerRequest.getSalt();
        userService.register(username, password, email, salt);
        return ResponseEntity.ok(new StringResponse("成功"));
    }

    @GetMapping("/getSalt")
    public ResponseEntity<StringResponse> getSalt(@RequestBody StringRequest request) {
        String salt = userService.getSaltByEmail(request.getValue());
        return ResponseEntity.ok(new StringResponse(salt));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        User user = userService.login(email, password);
        Map<String, Object> map = user.toDict();
        String token = JwtUtil.generateToken(user.getId());
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
        String name = user.getNickname() + "_CV.pdf";

        if(ObjectUtils.isEmpty(file) || file.getSize() <= 0){
            return ResponseEntity.badRequest().body(new StringResponse("File is empty"));
        }
        if (!file.getContentType().equals("application/pdf")) {
            return ResponseEntity.badRequest().body(new StringResponse("File must be a PDF"));
        }
        String res = ossConfig.upload(file, "CV", name);
        if (res != null){
            return ResponseEntity.ok(new StringResponse(res));
        } else {
            return ResponseEntity.badRequest().body(new StringResponse("简历上传失败"));
        }

    }


}
