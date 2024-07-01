package com.yiyan.careeryiyan.service;

import com.yiyan.careeryiyan.mapper.*;
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
    UserMapper userMapper;
    @Resource
    EnterpriseMapper enterpriseMapper;

    @Resource
    MyWebSocket myWebSocket;

    private void insertNotice(Notice notice) {
        noticeMapper.insertNotice(notice);
        myWebSocket.sendNotice(notice);
    }

    private void insertNotice(List<Notice> noticeList) {
        for (Notice notice : noticeList) {
            noticeMapper.insertNotice(notice);
            myWebSocket.sendNotice(notice);
        }
    }

    //notice必填：userId,noticeType,noticeContent,avatarUrl
    //选填：postId,epId
    //1，动态被点赞，通知动态作者，postId,点赞者头像
    public void addLikePostNotice(String likePostId) {
        LikePost likePost = postMapper.getLikePostByLikePostId(likePostId);
        Post post = postMapper.getPostById(likePost.getPostId());
        User user = userMapper.getUserById(likePost.getUserId());

        Notice notice = new Notice();
        notice.setUserId(post.getUserId());
        notice.setNoticeType("1");
        notice.setNoticeContent(String.format("%s 点赞了您的动态: %s", user.getUserName(), post.getPostTitle()));
        notice.setAvatarUrl(user.getUserAvatarUrl());
        notice.setPostId(post.getPostId());

        insertNotice(notice);
    }

    //1，评论被点赞，通知评论作者,postId，点赞者头像
    public void addLikeCommentNotice(String commentId) {
        Comment comment = postMapper.getCommentById(commentId);
        Post post = postMapper.getPostById(comment.getPostId());
        User user = userMapper.getUserById(comment.getUserId());

        Notice notice = new Notice();
        notice.setUserId(comment.getUserId());
        notice.setNoticeType("1");
        notice.setNoticeContent(String.format("%s 点赞了您的评论: %s", user.getUserName(), comment.getCommentContent()));
        notice.setAvatarUrl(user.getUserAvatarUrl());
        notice.setPostId(post.getPostId());

        insertNotice(notice);
    }

    //2.动态被评论,通知动态作者，postId,评论者头像
    public void addCommentedNotice(String commentId) {
        Comment comment = postMapper.getCommentById(commentId);
        Post post = postMapper.getPostById(comment.getPostId());
        User user = userMapper.getUserById(comment.getUserId());

        Notice notice = new Notice();
        notice.setUserId(post.getUserId());
        notice.setNoticeType("2");
        notice.setAvatarUrl(user.getUserAvatarUrl());
        notice.setNoticeContent(String.format("%s 评论了您的动态: %s", user.getUserName(), post.getPostTitle()));

        notice.setPostId(post.getPostId());
        insertNotice(notice);

    }

    //3.通知申请变化,epId,企业头像
    public void addApplyStatusNotice(Apply apply) {
        Recruitment recruitment = recruitmentMapper.getRecruitmentById(apply.getRcId());
        Enterprise enterprise = enterpriseMapper.getEnterpriseByEpId(recruitment.getEpId());

        Notice notice = new Notice();
        notice.setUserId(apply.getUserId());
        notice.setNoticeType("3");
        notice.setAvatarUrl(enterprise.getEpAvatarUrl());
        String hrRes = apply.getApplyStatus() == 1 ? "通过" : "拒绝";
        notice.setNoticeContent(String.format(" %s %s您对 %s的申请", enterprise.getEpName(), hrRes, recruitment.getRcName()));
        notice.setEpId(recruitment.getEpId());
        insertNotice(notice);

    }

    //3.通知新岗位,epId,企业头像
    public void addNewRecruitmentNotice(String rcId) {
        Recruitment recruitment = recruitmentMapper.getRecruitmentById(rcId);
        Enterprise enterprise = enterpriseMapper.getEnterpriseByEpId(recruitment.getEpId());
        List<User> userList = recommandMapper.getRelatedUserIdsByRcTag(recruitment.getRcTag());
        List<Notice> noticeList = new ArrayList<>();
        for (User user : userList) {
            Notice notice = new Notice();
            notice.setUserId(user.getUserId());
            notice.setNoticeType("3");
            notice.setAvatarUrl(enterprise.getEpAvatarUrl());
            notice.setNoticeContent(String.format("%s 发布了 %s相关的新岗位%s ", enterprise.getEpName(),
                    recruitment.getRcTag(), recruitment.getRcName()));
            notice.setEpId(recruitment.getEpId());
        }
        insertNotice(noticeList);
    }

    //0，员工退出企业向企业管理员通知
    public void addQuitEnterpriseNotice(String userName, String epId) {
        //查询企业管理员
        User admin = enterpriseMapper.getEnterpriseAdmin(epId);
        Notice notice = new Notice();
        notice.setUserId(admin.getUserId());
        notice.setNoticeType("0");
        notice.setAvatarUrl(admin.getUserAvatarUrl());
        notice.setNoticeContent(String.format("%s 退出了您的企业", userName));
        insertNotice(notice);
    }

    //0.管理员转让，通知新管理员
    public void addTransferEnterpriseNotice(String adminId, String epId) {
        Enterprise enterprise = enterpriseMapper.getEnterpriseByEpId(epId);

        Notice notice = new Notice();
        notice.setUserId(adminId);
        notice.setNoticeType("0");
        notice.setAvatarUrl(epId);
        notice.setNoticeContent(String.format("您成为了 %s 的新管理员", enterprise.getEpName()));
        insertNotice(notice);
    }

}
