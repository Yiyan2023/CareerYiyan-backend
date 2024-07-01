package com.yiyan.careeryiyan.controller;

import com.yiyan.careeryiyan.exception.BaseException;
import com.yiyan.careeryiyan.mapper.PostMapper;
import com.yiyan.careeryiyan.model.domain.*;
import com.yiyan.careeryiyan.model.request.*;
import com.yiyan.careeryiyan.model.response.StringResponse;
import com.yiyan.careeryiyan.service.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
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
    @Resource
    PostMapper postMapper;
    @Resource
    EnterpriseService enterpriseService;
    @Resource
    AddNoticeService addNoticeService;

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

    @PostMapping("/delete")
    public ResponseEntity<StringResponse> delPost(@RequestBody Map<String,String> map, HttpServletRequest httpServletRequest) {
//        String id=rb.getParameter("id");
        String id=map.get("id");
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null)

            throw new BaseException("用户不存在");
        boolean res = postService.delPost(String.valueOf(id), user);
        String response = res ? "删除成功" : "删除失败";
        return ResponseEntity.ok(new StringResponse(response));
    }
    @PostMapping("/like")
    public ResponseEntity<StringResponse> likePost(@RequestBody Map<String,String> map, HttpServletRequest httpServletRequest) {
        String id=map.get("id");
        boolean status= Boolean.parseBoolean(map.get("status"));
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null)
            throw new BaseException("用户不存在");
        boolean res = postService.likePost(String.valueOf(id),user,status);
        String response = res ? "点赞成功" : "点赞失败";
        if (res && status){
            //点赞成功
            addNoticeService.addLikePostNotice(id);
        }
        return ResponseEntity.ok(new StringResponse(response));
    }


    /*
    转发动态
     */
    @PostMapping("repost")
    public ResponseEntity<Map<String, Object>> repost(@RequestBody Map<String,String> map,
                                                      HttpServletRequest httpServletRequest) {
        String id=map.get("id");
        String title = map.get("title");
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


    @PostMapping("/user")//个人动态
    public ResponseEntity<Map<String, Object>> getUser(@RequestBody Map<String, String> userIdRequest, HttpServletRequest httpServletRequest) {
//        String id=map.get("id");
        String userId = userIdRequest.get("userId");
        User user=userService.getUserInfo(userId);
        if(user == null){
            throw new BaseException("用户不存在");
        }
        List<Post> postList = postService.getPostsByUser(user);

        Map<String, Object> res = new HashMap<>();
        List<Map<String,Object>> mapList = new ArrayList<>();
        for(Post postOrigin: postList){
            // post
            Map<String, Object> post = postService.getPostInfoMapById(postOrigin.getPostId());

            // isParent  &&  parent
            postParent(post);

            // isLike
            postLike(post, userId);

            mapList.add(post);
        }

        //author
        Map<String, Object> userInfoMap = userService.getUserInfoById(userId);
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(userId);
        userInfoMap.put("epId",enterpriseUser.getEpId());
        String epName = enterpriseService.getEnterpriseByEpId(enterpriseUser.getEpId()).getEpName();
        userInfoMap.put("epName",epName);

        res.put("author",userInfoMap);
        res.put("posts", mapList);

        return ResponseEntity.ok(res);
    }
    @GetMapping("")//动态详情
    public ResponseEntity<Map<String,Object>> getPost(@RequestParam int id,HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null) {
            throw new BaseException("用户不存在");
        }
        Map<String,Object> res=postService.getPost(String.valueOf(id),user);
        res.put("comments",postService.getAllComments(String.valueOf(id)));
        return ResponseEntity.ok(res);
    }

    //企业动态 展示
    @PostMapping("/enterprise")
    public ResponseEntity<List<Map<String,Object>>>getEnterprisePost(@RequestBody ShowEnterprisePostRequest showEnterprisePostRequest){
        String epId = showEnterprisePostRequest.getEpId();
        List<Map<String,Object>> mapList = new ArrayList<>();
        List<String> postIdList = postService.getEnterprisePosts(epId);
        for(String postId: postIdList){
            // post
            Map<String, Object> post = postService.getPostInfoMapById(postId);
            if(post == null || post.isEmpty()){
                continue;
            }

            //author
            String userId = String.valueOf(post.get("userId"));
            Map<String, Object> userInfoMap = userService.getUserInfoById(userId);
            Enterprise enterprise = enterpriseService.getEnterpriseByEpId(epId);
            userInfoMap.put("epId", epId);
            userInfoMap.put("epName", enterprise.getEpName());
            post.put("author",userInfoMap);

            // isParent  &&  parent
            postParent(post);

            // isLike
            postLike(post, userId);

            mapList.add(post);
        }
        return ResponseEntity.ok(mapList);
    }

    //关注的用户动态和企业动态
    @PostMapping("/following")
    public ResponseEntity<List<Map<String,Object>>> getFollowingPosts(@RequestBody Map<String, String> typeRequest, HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null) {
            throw new BaseException("用户不存在");
        }
        List<Map<String,Object>> mapList = new ArrayList<>();
        String type = typeRequest.get("type");
        if(type.equals("user")){
            System.out.println("here");
            mapList = postService.getUsersPost(user.getUserId());

        } else {
            mapList = postService.getEnterprisePost(user.getUserId());
//            System.out.println(mapList);
        }

        for(Map<String, Object> post: mapList){
            // isParent  &&  parent
            postParent(post);

            // author
            String userId = String.valueOf(post.get("userId"));
            Map<String, Object> userMap = userService.getUserInfoById(userId);
            post.put("author",userMap);

            // enterprise
            postEnterprise(post, userId);

            // isLiked
            postLike(post, userId);
        }

        return ResponseEntity.ok(mapList);

    }

    private void postParent(Map<String, Object> post){
        // isParent  &&  parent
        if(post.get("postParentId") == null){
            post.put("isParent", true);
            post.put("parent", null);
        } else {
            System.out.println(post.get("postParentId"));
            post.put("isParent", false);
            String parentId = String.valueOf(post.get("postParentId")) ;
            Map<String, Object> postMap = postService.getPostInfoMapById(parentId);
            if(postMap == null || postMap.isEmpty()){
                post.put("parent", null);
            } else {
                User user1 = userService.getUserInfo(String.valueOf(postMap.get("userId")) );
                Map<String, Object> parentMap = new HashMap<>();
                parentMap.put("parentUserId", user1.getUserId());
                parentMap.put("parentUserName", user1.getUserName());
                parentMap.put("parentUserNickname", user1.getUserNickname());
                parentMap.put("parentUserAvatarUrl", user1.getUserAvatarUrl());
                parentMap.put("parentUserEmail", user1.getUserEmail());
                parentMap.put("parentPostCreatedAt", postMap.get("postCreateAt"));

                Map<String, Object> tmp = new HashMap<>();
                postEnterprise(tmp, user1.getUserId());
//                System.out.println(tmp);
                Enterprise enterprise = (Enterprise) tmp.get("enterprise");
                parentMap.put("parentUserEpId", enterprise.getEpId());
                parentMap.put("parentUserEpName", enterprise.getEpName());
                post.put("parent", parentMap);
            }
        }
    }

    private void postEnterprise(Map<String, Object> post, String userId){
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(userId);
        if(enterpriseUser == null){
            post.put("enterprise", null);
        } else {
            Enterprise enterpriseMap = enterpriseService.getEnterpriseByEpId(enterpriseUser.getEpId());
            post.put("enterprise",enterpriseMap);
        }
    }

    private void postLike(Map<String, Object> post, String userId){
        LikePost likePost = postService.likePostById(userId, String.valueOf(post.get("postId")) );
        if(likePost == null){
            post.put("isLiked", false);
        } else {
            post.put("isLiked", true);
        }
    }

    @GetMapping("/following/users")
    public ResponseEntity<List<Map<String,Object>>>getUsersPost(HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null) {
            throw new BaseException("用户不存在");
        }
        return ResponseEntity.ok(postService.getUsersPost(user.getUserId()));
    }


    @GetMapping("/following/enterprises")
    public ResponseEntity<List<Map<String,Object>>>geEnterprisePost(HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null) {
            throw new BaseException("用户不存在");
        }
        return ResponseEntity.ok(postService.getEnterprisePost(user.getUserId()));
    }
    //管理员 管理企业动态


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

        //通知被评论用户
        addNoticeService.addCommentedNotice(comment.getCommentId());

        Map<String, Object> responseData = new HashMap<>();
        responseData.putAll(comment.toDict());
        responseData.put("author", user.toDict());
        return ResponseEntity.ok(responseData);
    }
    @PostMapping("/comments/delete")
    public ResponseEntity<StringResponse> delComment(@RequestBody Map<String,String> map, HttpServletRequest httpServletRequest) {
        String id=map.get("id");
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null)
            throw new BaseException("用户不存在");
        boolean res = postService.delComment(String.valueOf(id), user);
        String response = res ? "删除成功" : "删除失败";
        return ResponseEntity.ok(new StringResponse(response));
    }
    @GetMapping("/comments")
    public ResponseEntity<List<Map<String, Object>>> getAllComment(@RequestParam int id,HttpServletRequest httpServletRequest){
//        String id=map.get("id");
        return ResponseEntity.ok(postService.getAllComments(String.valueOf(id)));
    }
    @PostMapping("/comments/like")
    public ResponseEntity<StringResponse> likeComment(@RequestBody Map<String,String> map, HttpServletRequest httpServletRequest) {
        String id=map.get("id");
        boolean status= Boolean.parseBoolean(map.get("status"));
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null)
            throw new BaseException("用户不存在");
        boolean res = postService.likeComment(String.valueOf(id),user,status);
        String response = res ? "评论点赞成功" : "评论点赞失败";
        if (res && status){
            //点赞成功
            addNoticeService.addLikeCommentNotice(id);
        }
        return ResponseEntity.ok(new StringResponse(response));
    }

    @PostMapping("/adminAdd")
    public ResponseEntity<StringResponse> adminAddPost(@RequestBody AddEnterprisePostRequest addEnterprisePostRequest,
                                                       HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getAttribute("user");
        String id = user.getUserId();

        String userId = addEnterprisePostRequest.getUserId();
        String epId = addEnterprisePostRequest.getEpId();
        String postId = addEnterprisePostRequest.getPostId();

        if(enterpriseService.isAdmin(id, epId) == 0){
            throw new BaseException("权限不足");
        }

        EnterprisePost enterprisePost1 = postService.getEnterprisePostByEpPost(epId, postId);
        if(enterprisePost1 == null){
            EnterprisePost enterprisePost = new EnterprisePost();
            enterprisePost.setEpId(epId);
            enterprisePost.setPostId(postId);
            enterprisePost.setIsDelete(0);
            int res = postService.insertEnterprisePost(enterprisePost);

            if(res == 0)
                throw new BaseException("添加失败");
        } else {
            int isDelete = enterprisePost1.getIsDelete();
            if(isDelete == 0){
                throw new BaseException("已经存在");
            } else {
                postService.updateEnterprisePost(enterprisePost1.getEpPostId(), 0);
            }
        }

        return ResponseEntity.ok(new StringResponse("添加成功"));
    }

    @PostMapping("/remove")
    public ResponseEntity<StringResponse> adminRemovePost(@RequestBody DeleteEnterprisePostRequest deleteEnterprisePostRequest,
                                                          HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getAttribute("user");
        String id = user.getUserId();

        String epId = deleteEnterprisePostRequest.getEpId();
        String postId = deleteEnterprisePostRequest.getPostId();

        if(enterpriseService.isAdmin(id, epId) == 0){
            throw new BaseException("权限不足");
        }

        EnterprisePost enterprisePost1 = postService.getEnterprisePostByEpPost(epId, postId);
        if(enterprisePost1 == null){
            throw new BaseException("删除失败");
        } else {
            int isDelete = enterprisePost1.getIsDelete();
            if(isDelete == 1){
                throw new BaseException("已经删除");
            } else {
                postService.updateEnterprisePost(enterprisePost1.getEpPostId(), 1);
            }
        }

        return ResponseEntity.ok(new StringResponse("删除成功"));
    }
}
