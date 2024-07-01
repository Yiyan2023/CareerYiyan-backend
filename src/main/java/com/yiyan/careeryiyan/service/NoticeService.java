package com.yiyan.careeryiyan.service;

import com.yiyan.careeryiyan.mapper.NoticeMapper;
import com.yiyan.careeryiyan.mapper.PostMapper;
import com.yiyan.careeryiyan.mapper.RecommandMapper;
import com.yiyan.careeryiyan.mapper.RecruitmentMapper;
import com.yiyan.careeryiyan.model.domain.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import scala.App;

import java.util.List;

@Service
public class NoticeService {
    @Resource
    private NoticeMapper noticeMapper;

    public List<Notice> getNotices(String userId) {
        return noticeMapper.getNotices(userId);
    }

    public void markAsRead(String userId, List<String> noticeIds) {
        noticeMapper.markAsReadBatch(userId, noticeIds);
    }
}
