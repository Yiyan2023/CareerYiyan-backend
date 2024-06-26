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
    PostService postService;
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

    @GetMapping("/getSalt")
    public ResponseEntity<StringResponse> getSalt(@RequestBody StringRequest request) {
        String salt = userService.getSaltByEmail(request.getValue());
        return ResponseEntity.ok(new StringResponse(salt));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest,
            HttpServletRequest httpServletRequest) {
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

    /*
     * 新建动态、删除动态，获取用户所有动态，点赞动态
     * 创建评论，删除评论，回复评论，
     */
    @PostMapping("/posts/add")
    public ResponseEntity<Map<String, Object>> addPost( @RequestParam("content") String content,
                                                        @RequestParam("title") String title,
                                                        @RequestParam(value = "photos", required = false) List<MultipartFile> photos,
                                                        HttpServletRequest httpServletRequest) throws IOException {
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null)
            throw new BaseException("用户不存在");
        Post post = postService.addPost(title,content,photos, user);
        Map<String, Object> res = post.toDict();
        res.put("author", user.toDict());
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/posts/delete/{id}")
    public ResponseEntity<StringResponse> delPost(@PathVariable int id, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null)

            throw new BaseException("用户不存在");
        boolean res = postService.delPost(String.valueOf(id), user);
        String response = res ? "删除成功" : "删除失败";
        return ResponseEntity.ok(new StringResponse(response));
    }

    @PostMapping("/posts/like/{id}")
    public ResponseEntity<StringResponse> likePost(@PathVariable int id,@RequestParam boolean status, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null)
            throw new BaseException("用户不存在");
        boolean res = postService.like(String.valueOf(id),user,status,1);
        String response = res ? "点赞成功" : "删除失败";
        return ResponseEntity.ok(new StringResponse(response));
    }


    @GetMapping("/posts/all")
    public ResponseEntity<List<Map<String, Object>>> getUserPost(HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        List<Post> posts = postService.getPostsByUser(user);
        List<Map<String, Object>> res = posts.stream()
                .map(Post::toDict)
                .collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }
    @GetMapping("/posts/{id}")
    public ResponseEntity<Map<String,Object>> getPost(@PathVariable int id,HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null) {
            throw new BaseException("用户不存在");
        }
        Map<String,Object> res=postService.getPost(String.valueOf(id),user);
        res.put("comments",postService.getAllComments(String.valueOf(id)));
        return ResponseEntity.ok(res);
    }

    /*
     * * 创建评论，删除评论，获取所有评论
     */
    @PostMapping("/posts/comments/add")
    public ResponseEntity<Map<String, Object>> addComment(@RequestBody AddCommentRequest req, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null) {
            throw new BaseException("用户不存在");
        }

        Comment comment = postService.addComment(req, user);
        if (comment == null) {
            throw new BaseException("新建失败");
        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.putAll(comment.toDict());
        responseData.put("author", user.toDict());
        return ResponseEntity.ok(responseData);
    }
    @DeleteMapping("/posts/comments/delete/{id}")
    public ResponseEntity<StringResponse> delComment(@PathVariable int id, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null)
            throw new BaseException("用户不存在");
        boolean res = postService.delComment(String.valueOf(id), user);
        String response = res ? "删除成功" : "删除失败";
        return ResponseEntity.ok(new StringResponse(response));
    }
    @GetMapping("/posts/{id}/comments")
    public ResponseEntity<List<Map<String, Object>>> getAllComment(@PathVariable int id,HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(postService.getAllComments(String.valueOf(id)));
    }
    @PostMapping("/uploadCV")
    public ResponseEntity<StringResponse> uploadCV(@RequestParam("file") MultipartFile file,
            HttpServletRequest httpServletRequest) throws IOException {
        User user = (User) httpServletRequest.getAttribute("user");
        String id = user.getId();
        String name = user.getNickname() + "_CV.pdf";

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
    public ResponseEntity<UserInfoResponse> showInfo(@RequestBody StringRequest stringRequest, HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getAttribute("user");
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
        String enterpriseId = enterpriseUser.getEnterpriseId();
        Enterprise enterprise = enterpriseService.getEnterpriseById(enterpriseId);
        userInfoResponse.setEnterpriseId(enterpriseId);
        userInfoResponse.setEnterpriseName(enterprise.getEnterpriseName());

        return ResponseEntity.ok(userInfoResponse);
    }

    @PostMapping("/verifyInfo")
    public ResponseEntity<UserInfoResponse> modifyInfo(@RequestBody ModifyInfoRequest modifyInfoRequest, HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getAttribute("user");
        String id = user.getId();

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
        String enterpriseId = enterpriseUser.getEnterpriseId();
        Enterprise enterprise = enterpriseService.getEnterpriseById(enterpriseId);
        userInfoResponse.setEnterpriseId(enterpriseId);
        userInfoResponse.setEnterpriseName(enterprise.getEnterpriseName());

        return ResponseEntity.ok(userInfoResponse);
    }

    @PostMapping("/uploadAvatar")
    public ResponseEntity<StringResponse> uploadAvatar(@RequestParam("file") MultipartFile file,
                                                       HttpServletRequest httpServletRequest) throws IOException {
        User user = (User) httpServletRequest.getAttribute("user");
        String id = user.getId();
        String name = user.getNickname() + "_avatar.jpg";

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
