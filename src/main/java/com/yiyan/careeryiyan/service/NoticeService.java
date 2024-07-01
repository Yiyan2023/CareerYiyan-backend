package com.yiyan.careeryiyan.service;

import com.yiyan.careeryiyan.mapper.NoticeMapper;
import com.yiyan.careeryiyan.model.domain.Notice;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    @Resource
    private NoticeMapper noticeMapper;

    public List<Notice> getNotices(String userId) {
        return noticeMapper.getNotices(userId);
    }
    public void insertNotice(Notice notice) {
        noticeMapper.insertNotice(notice);
        //myWebSocket.sendNotice(notice);
    }

    public void markAsRead(String userId,List<String> noticeIds) {
        noticeMapper.markAsReadBatch(userId,noticeIds);
    }
}
