package com.yiyan.careeryiyan.controller;

import com.yiyan.careeryiyan.config.OSSConfig;
import com.yiyan.careeryiyan.exception.BaseException;
import com.yiyan.careeryiyan.mapper.PostMapper;
import com.yiyan.careeryiyan.model.domain.Post;
import com.yiyan.careeryiyan.model.domain.User;
import com.yiyan.careeryiyan.model.request.AddPostRequest;
import com.yiyan.careeryiyan.model.response.StringResponse;
import com.yiyan.careeryiyan.model.request.LoginRequest;
import com.yiyan.careeryiyan.model.request.RegisterRequest;
import com.yiyan.careeryiyan.model.request.StringRequest;
import com.yiyan.careeryiyan.service.PostService;
import com.yiyan.careeryiyan.service.UserService;
import com.yiyan.careeryiyan.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Delete;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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
    @PostMapping("/addPost")
    public ResponseEntity<Map<String, Object>> addPost(@RequestBody AddPostRequest req,
            HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null)
            throw new BaseException("用户不存在");
        Post post = postService.addPost(req, user);
        Map<String, Object> res = post.toDict();
        res.put("author", user.toDict());
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/delPost/{id}")
    public ResponseEntity<StringResponse> delPost(@PathVariable int id, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null)

            throw new BaseException("用户不存在");
        boolean res = postService.delPost(String.valueOf(id), user);
        String response = res ? "删除成功" : "删除失败";
        return ResponseEntity.ok(new StringResponse(response));
    }

    @GetMapping("/getUserPost")
    public ResponseEntity<List<Map<String, Object>>> getUserPost(HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        List<Post> posts = postService.getPostsByUser(user);
        List<Map<String, Object>> res = posts.stream()
                .map(Post::toDict)
                .collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/uploadCV")
    public ResponseEntity<StringResponse> uploadCV(@RequestParam("file") MultipartFile file,
            HttpServletRequest httpServletRequest) throws IOException {
        User user = (User) httpServletRequest.getAttribute("user");
        String name = user.getNickname() + "_CV.pdf";

        if (ObjectUtils.isEmpty(file) || file.getSize() <= 0) {
            throw new BaseException("File is empty");
        }
        if (!file.getContentType().equals("application/pdf")) {
            throw new BaseException("File must be a PDF");
        }
        String res = ossConfig.upload(file, "CV", name);
        if (res != null) {
            return ResponseEntity.ok(new StringResponse(res));
        } else {
            throw new BaseException("简历上传失败");
        }

    }

}
