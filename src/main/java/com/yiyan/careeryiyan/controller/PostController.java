package com.yiyan.careeryiyan.controller;

import com.yiyan.careeryiyan.exception.BaseException;
import com.yiyan.careeryiyan.model.domain.Comment;
import com.yiyan.careeryiyan.model.domain.Post;
import com.yiyan.careeryiyan.model.domain.User;
import com.yiyan.careeryiyan.model.request.AddCommentRequest;
import com.yiyan.careeryiyan.model.request.AddPostRequest;
import com.yiyan.careeryiyan.model.response.StringResponse;
import com.yiyan.careeryiyan.service.PostService;
import com.yiyan.careeryiyan.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts")
public class PostController {
    /*
     * 新建动态、删除动态，获取用户所有动态，点赞动态
     * 创建评论，删除评论，回复评论，
     *
     */
    @Resource
    PostService postService;
    @Resource
    UserService userService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addPost(@RequestBody AddPostRequest req,
                                                       HttpServletRequest httpServletRequest) throws IOException {
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null)
            throw new BaseException("用户不存在");
        Post post = postService.addPost(req.getPostContent(), req.getPostPhotoUrls(), user);
        Map<String, Object> res = post.toDict();
        res.put("author", user.toDict());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<StringResponse> delPost(@PathVariable int id, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null)

            throw new BaseException("用户不存在");
        boolean res = postService.delPost(String.valueOf(id), user);
        String response = res ? "删除成功" : "删除失败";
        return ResponseEntity.ok(new StringResponse(response));
    }
    @PostMapping("/like/{id}")
    public ResponseEntity<StringResponse> likePost(@PathVariable int id,@RequestParam boolean status, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null)
            throw new BaseException("用户不存在");
        boolean res = postService.likePost(String.valueOf(id),user,status);
        String response = res ? "点赞成功" : "点赞失败";
        return ResponseEntity.ok(new StringResponse(response));
    }

    @PostMapping("/comments/like/{id}")
    public ResponseEntity<StringResponse> likeComment(@PathVariable int id,@RequestParam boolean status, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null)
            throw new BaseException("用户不存在");
        boolean res = postService.likeComment(String.valueOf(id),user,status);
        String response = res ? "评论点赞成功" : "评论点赞失败";
        return ResponseEntity.ok(new StringResponse(response));
    }
    /*
    转发动态
     */
    @PostMapping("repost/{id}")
    public ResponseEntity<Map<String, Object>> repost(@PathVariable int id,
                                                      @RequestParam String title,
                                                      HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null)
            throw new BaseException("用户不存在");
        Map<String, Object> res=postService.repost(String.valueOf(id),user,title);
        if(res==null){
            res=new HashMap<>();
            res.put("res","父帖已删除");
        }

        return ResponseEntity.ok(res);
    }


    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getUserPost(HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        if(user==null)
            throw new BaseException("用户不存在");
        return ResponseEntity.ok(postService.getPostsByUser(user));
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable int id,HttpServletRequest httpServletRequest) {
        User user=userService.getUserInfo(String.valueOf(id));
        return ResponseEntity.ok(postService.getPostsByUser(user));
    }


    @GetMapping("/{id}")
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
    @PostMapping("/comments/add")
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
    @PostMapping("/comments/delete/{id}")
    public ResponseEntity<StringResponse> delComment(@PathVariable int id, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null)
            throw new BaseException("用户不存在");
        boolean res = postService.delComment(String.valueOf(id), user);
        String response = res ? "删除成功" : "删除失败";
        return ResponseEntity.ok(new StringResponse(response));
    }
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Map<String, Object>>> getAllComment(@PathVariable int id,HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(postService.getAllComments(String.valueOf(id)));
    }
}
