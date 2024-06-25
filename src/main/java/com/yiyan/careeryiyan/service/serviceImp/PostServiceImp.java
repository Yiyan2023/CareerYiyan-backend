package com.yiyan.careeryiyan.service.serviceImp;

import com.yiyan.careeryiyan.mapper.PostMapper;
import com.yiyan.careeryiyan.model.domain.Post;
import com.yiyan.careeryiyan.model.domain.User;
import com.yiyan.careeryiyan.model.request.AddPostRequest;
import com.yiyan.careeryiyan.service.PostService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class PostServiceImp extends PostService {
    @Resource
    PostMapper postMapper;
    @Override
    public Post addPost(AddPostRequest req, User user) {
        String title=req.getTitle();
        String content=req.getContent();
        Post post=new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUserId(user.getId());
        post.setCreatedAt(new Date());
        int res=postMapper.insertPost(post);
        return post;
    }

    @Override
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

    @Override
    public List<Post> getPostsByUser(User user) {
        return postMapper.getPostByUser(user.getId());
    }

//    @Override
//    public List<Post> getPostsByEn(User user) {
//        return null;
//    }
}
