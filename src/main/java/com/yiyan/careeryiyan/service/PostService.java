package com.yiyan.careeryiyan.service;

import com.yiyan.careeryiyan.model.domain.Post;
import com.yiyan.careeryiyan.model.domain.User;
import com.yiyan.careeryiyan.model.request.AddPostRequest;

import java.util.List;
import java.util.Map;

public abstract class PostService {
    public abstract Post addPost(AddPostRequest req, User user);
    public abstract boolean delPost(String id, User user);
    public abstract List<Post> getPostsByUser(User user);
//    public abstract List<Post> getPostsByEn(User user);
}
