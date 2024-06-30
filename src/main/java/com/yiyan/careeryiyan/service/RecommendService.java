package com.yiyan.careeryiyan.service;

import com.yiyan.careeryiyan.mapper.RecommandMapper;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RecommendService {
    @Resource
    private RecommandMapper recommandMapper ;
    public List<Map<String, Object>> getHotRecruitmentList() {
        return recommandMapper.getHotRecruitmentList();
    }

    public List<Map<String, Object>> getHotEnterpriseList() {
        return recommandMapper.getHotEnterpriseList();
    }

    public List<Map<String, Object>> getHotUserList() {
        return recommandMapper.getHotUserList();
    }

    public List<Map<String, Object>> getHotPostList() {
        return recommandMapper.getHotPostList();
    }
}
