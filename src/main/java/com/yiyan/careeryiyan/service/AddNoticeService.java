package com.yiyan.careeryiyan.service;

import com.yiyan.careeryiyan.mapper.NoticeMapper;
import com.yiyan.careeryiyan.mapper.PostMapper;
import com.yiyan.careeryiyan.mapper.RecommandMapper;
import com.yiyan.careeryiyan.mapper.RecruitmentMapper;
import com.yiyan.careeryiyan.model.domain.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddNoticeService {
    @Resource
    private NoticeMapper noticeMapper;
    @Resource
    private PostMapper postMapper;
    @Resource
    private RecruitmentMapper recruitmentMapper;
    @Resource
    private RecommandMapper recommandMapper;

    @Resource
    MyWebSocket myWebSocket;
    public void insertNotice(Notice notice) {
        noticeMapper.insertNotice(notice);
        myWebSocket.sendNotice(notice);
    }

    public void insertNotice(List<Notice> noticeList){
        for (Notice notice : noticeList) {
            noticeMapper.insertNotice(notice);
            myWebSocket.sendNotice(notice);
        }
    }

    //notice必填：userId,noticeType,noticeContent
    //选填：postId,epId
    //1，动态被点赞，通知动态作者，postId
    public void addLikePostNotice(String likePostId){
        LikePost likePost = postMapper.getLikePostByLikePostId(likePostId);
        Post post = postMapper.getPostById(likePost.getPostId());

        Notice notice = new Notice();
        notice.setUserId(post.getUserId());
        notice.setNoticeType("1");
        notice.setNoticeContent("您的动态被点赞了");
        notice.setPostId(post.getPostId());

        insertNotice(notice);
    }

    //1，评论被点赞，通知评论作者,postId
    public void addLikeCommentNotice(String commentId) {
        Comment comment = postMapper.getCommentById(commentId);
        Post post = postMapper.getPostById(comment.getPostId());

        Notice notice = new Notice();
        notice.setUserId(comment.getUserId());
        notice.setNoticeType("1");
        notice.setNoticeContent("您的评论被点赞了");
        notice.setPostId(post.getPostId());

        insertNotice(notice);
    }

    //2.动态被评论,通知动态作者，postId
    public void addCommentedNotice(String commentId) {
        Comment comment = postMapper.getCommentById(commentId);
        Post post = postMapper.getPostById(comment.getPostId());

        Notice notice = new Notice();
        notice.setUserId(post.getUserId());
        notice.setNoticeType("2");
        notice.setNoticeContent("您的动态被评论了");
        notice.setPostId(post.getPostId());
        insertNotice(notice);

    }

    //3.通知申请变化,epId
    public void addApplyStatusNotice(Apply apply) {
        Recruitment recruitment = recruitmentMapper.getRecruitmentById(apply.getRcId());
        Notice notice = new Notice();
        notice.setUserId(apply.getUserId());
        notice.setNoticeType("3");
        notice.setNoticeContent("您的岗位投递状态发生了变化");
        notice.setEpId(recruitment.getEpId());
        insertNotice(notice);

    }

    //3.通知新岗位,epId
    public void addNewRecruitmentNotice(String rcId) {
        Recruitment recruitment = recruitmentMapper.getRecruitmentById(rcId);
        List<User> userList = recommandMapper.getRelatedUserIdsByRcTag(recruitment.getRcTag());
        List<Notice> noticeList = new ArrayList<>();
        for (User user : userList) {
            Notice notice = new Notice();
            notice.setUserId(user.getUserId());
            notice.setNoticeType("3");
            notice.setNoticeContent("有新的"+recruitment.getRcTag()+"相关岗位发布了");
            notice.setEpId(recruitment.getEpId());
        }
        insertNotice(noticeList);
    }
}
