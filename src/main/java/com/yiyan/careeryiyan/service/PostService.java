package com.yiyan.careeryiyan.service;

import com.yiyan.careeryiyan.config.OSSConfig;
import com.yiyan.careeryiyan.exception.BaseException;
import com.yiyan.careeryiyan.mapper.PostMapper;
import com.yiyan.careeryiyan.mapper.UserMapper;
import com.yiyan.careeryiyan.model.domain.*;
import com.yiyan.careeryiyan.model.request.AddCommentRequest;
import com.yiyan.careeryiyan.model.request.AddPostRequest;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class  PostService {
    @Resource
    PostMapper postMapper;

    @Resource
    UserMapper userMapper;
    @Resource
    OSSConfig ossConfig;
    
    public Post addPost(String content,String photos, User user)  {
        Post post=new Post(content, user.getUserId(),photos,null,null);
        int res=postMapper.insertPost(post);
        return post;
    }

    
    public boolean delPost(String id, User user) {
        Post post=postMapper.getPostById(id);
        if(post==null)
            return false;
        if(post.getUserId()==null){
            System.out.println(post);
        }
        if(!post.getUserId().equals(user.getUserId()))
            return false;
        postMapper.deletePost(id);
        //企业相关
        return true;
    }

    
    public Map<String,Object> getPostsByUser(User user) {
        List<Post> postlist=postMapper.getPostByUser(user.getUserId());
        List<Map<String,Object>>posts=new ArrayList<Map<String,Object>>();
        Map<String,Object>res=new HashMap<String,Object>();
        for(Post post : postlist){
            Map<String,Object>postDict=post.toDict();
            if(post.getPostParentId()!=1){
                Post parent = postMapper.getPostById(String.valueOf(post.getPostParentId()));
                User origin=userMapper.getUserById(parent.getUserId());
                postDict.put("origin",origin.toDict());
            }
            posts.add(postDict);
        }
        res.put("posts",posts);
        res.put("author",user.toDict());
        return res;
    }

    public Map<String, Object> getPost(String id, User user) {
        Post post = postMapper.getPostById(id);
        User author=userMapper.getUserById(post.getUserId());
        Map<String, Object> postDict = post.toDict();
        postDict.put("author", author.toDict());
        if(post.getPostParentId()!=1){
            Post parent = postMapper.getPostById(String.valueOf(post.getPostParentId()));
            User origin=userMapper.getUserById(parent.getUserId());
            postDict.put("origin", origin.toDict());
        }
        return postDict;
    }
    
    public boolean likePost(String id, User user,boolean status) {
        Post post=postMapper.getPostById(id);
        if(post==null){

            System.out.println(id);
            return false;
            //
        }
        System.out.println(1);
        LikePost like=postMapper.getLikePost(user.getUserId(),id);
        //点赞
        if(status && like==null){
            like=new LikePost(user.getUserId(),id);
            postMapper.insertLikePost(like);
            System.out.println("点赞");
        }
        else if(!status&&like!=null){
            postMapper.deleteLikePost(like.getLikePostId());
        }
        else
            return  false;
        return true;
    }

    public boolean likeComment(String id, User user,boolean status) {
        Comment comment=postMapper.getCommentById(id);
        if(comment==null){
            System.out.println(id);
            return false;
        }

        LikeComment like=postMapper.getLikeComment(user.getUserId(),id);
        //点赞
        if(status && like==null){
            like=new LikeComment(user.getUserId(),id);
            postMapper.insertLikeComment(like);
            System.out.println("点赞");
        }
        else if(!status&&like!=null){
            System.out.println("取消点赞");
            postMapper.deleteLikeComment(like.getLikeCommentId());
        }
        else
            return  false;
        return true;
    }
    
    public Comment addComment(AddCommentRequest req, User user) {
        System.out.println("nihao:"+req.getParentId());
        Comment parent=postMapper.getCommentById(req.getParentId());
        String parentId=req.getParentId();
        if(req.getContent()==null||req.getContent().equals("")||parentId==null||(!parentId.equals("0")&&parent==null)){
            return null;
        }
        Post post=postMapper.getPostById(req.getPostId());
        if(post==null)
            return null;
        Comment comment=new Comment(req.getPostId(), user.getUserId(), req.getContent(),parentId.equals("0")?null:parentId);
        postMapper.insertComment(comment);
        return comment;
    }

    
    public boolean delComment(String id, User user) {
        Comment comment=postMapper.getCommentById(id);
//        System.out.println(comment==null);
        if(comment==null||!comment.getUserId().equals(user.getUserId()))
            return false;

        postMapper.deleteComment(id);
        return true;
    }

    
    public List<Map<String,Object>> getAllComments(String postId) {
        List<Comment>comments=postMapper.getAllComments(postId,null);
        System.out.println(comments.size());
        List<Map<String,Object>> res=new ArrayList<Map<String,Object>>();
        for(Comment comment : comments){
            Map<String,Object> c=comment.toDict();
            User user=userMapper.getUserById(comment.getUserId());
            c.put("author",user.toDict());
            List<Comment>replyList=postMapper.getAllComments(postId,comment.getCommentId());
            List<Map<String,Object>> replies=new ArrayList<>();
            for(Comment reply : replyList){
                Map<String,Object>r=reply.toDict();
                User rU=userMapper.getUserById(reply.getUserId());
                r.put("author",rU.toDict());
                replies.add(r);
            }
            c.put("replies",replies);
            res.add(c);
        }
        return res;
    }
    public Map<String, Object> repost(String postId,User user,String title){
        Map<String, Object>res=new HashMap<String, Object>();
        Post post=postMapper.getPostById(postId);
        if(post ==null){
            System.out.println("帖子Id: "+postId+" is null");
            return null;
        }
        if(post.getPostParentId()!=1){
            System.out.println("有父帖 "+post.getPostParentId());
            post=postMapper.getPostById(String.valueOf(post.getPostParentId()));
        }
        if(post ==null){
            System.out.println("帖子"+" is null");
            return null;
        }
        User origin=userMapper.getUserById(post.getUserId());
        Post newPost=post;
        newPost.setPostCreateAt(new Date());
        newPost.setUserId(user.getUserId());
        newPost.setPostParentId(Integer.parseInt(post.getPostId()));
        newPost.setPostTitle(title);
        postMapper.insertPost(post);
        res.put("posts",post.toDict());
        res.put("author",user.toDict());
        res.put("origin",origin.toDict());
        return res;
    }

}
