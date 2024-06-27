package com.yiyan.careeryiyan.service;

import com.yiyan.careeryiyan.config.OSSConfig;
import com.yiyan.careeryiyan.exception.BaseException;
import com.yiyan.careeryiyan.mapper.PostMapper;
import com.yiyan.careeryiyan.mapper.UserMapper;
import com.yiyan.careeryiyan.model.domain.Comment;
import com.yiyan.careeryiyan.model.domain.Like;
import com.yiyan.careeryiyan.model.domain.Post;
import com.yiyan.careeryiyan.model.domain.User;
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
    
    public Post addPost(String content,String photos, User user) throws IOException {
        Post post=new Post(content, user.getId(),photos);
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
        if(!post.getUserId().equals(user.getId()))
            return false;
        postMapper.deletePost(id);
        //企业相关
        return true;
    }

    
    public Map<String,Object> getPostsByUser(User user) {
        List<Post> postlist=postMapper.getPostByUser(user.getId());
        List<Map<String,Object>>posts=new ArrayList<Map<String,Object>>();
        Map<String,Object>res=new HashMap<String,Object>();
        for(Post post : postlist){
            Map<String,Object>postDict=post.toDict();
            if(!post.getParentId().equals("0")){
                Post parent = postMapper.getPostById(post.getParentId());
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
        if(!post.getParentId().equals("0")){
            Post parent=postMapper.getPostById(post.getParentId());
            User origin=userMapper.getUserById(parent.getUserId());
            postDict.put("origin", origin.toDict());
        }
        return postDict;
    }
    
    public boolean like(String id, User user,boolean status,int type) {
        Post post=postMapper.getPostById(id);
        if(post==null)
            return false;
        //先查找like
        System.out.println(user.getId()+id+type);
        Like like=postMapper.getLike(user.getId(),id,type);
        //点赞
        if(status && like==null){
            like=new Like(user.getId(), String.valueOf(type),id);
            postMapper.insertLike(like);
            System.out.println("点赞");
        }
        else if(!status&&like!=null){
            postMapper.deleteLike(like.getId());
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

        Comment comment=new Comment(req.getPostId(), user.getId(), req.getContent(),req.getParentId());
        postMapper.insertComment(comment);
        return comment;
    }

    
    public boolean delComment(String id, User user) {
        Comment comment=postMapper.getCommentById(id);
        if(comment==null||!comment.getUserId().equals(user.getId()))
            return false;

        postMapper.delComment(id);
        return true;
    }

    
    public List<Map<String,Object>> getAllComments(String postId) {
        List<Comment>comments=postMapper.getAllComments(postId,"0");
        List<Map<String,Object>> res=new ArrayList<Map<String,Object>>();
        for(Comment comment : comments){
            Map<String,Object> c=comment.toDict();
            User user=userMapper.getUserById(comment.getUserId());
            c.put("author",user.toDict());
            List<Comment>replyList=postMapper.getAllComments(postId,comment.getId());
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
        if(!post.getParentId().equals("0")){
            post=postMapper.getPostById(post.getParentId());
        }
        User origin=userMapper.getUserById(post.getUserId());
        Post newPost=post;
        newPost.setCreatedAt(new Date());
        newPost.setUserId(user.getId());
        newPost.setParentId(postId);
        newPost.setTitle(title);
        postMapper.insertPost(post);
        res.put("posts",post.toDict());
        res.put("author",user.toDict());
        res.put("origin",origin.toDict());
        return res;
    }

}
