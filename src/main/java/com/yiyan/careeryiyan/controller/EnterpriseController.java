package com.yiyan.careeryiyan.controller;

import com.yiyan.careeryiyan.exception.BaseException;
import com.yiyan.careeryiyan.model.domain.*;
import com.yiyan.careeryiyan.model.request.*;
import com.yiyan.careeryiyan.model.response.StringResponse;
import com.yiyan.careeryiyan.model.response.UserApplyDetailResponse;
import com.yiyan.careeryiyan.service.EnterpriseService;
import com.yiyan.careeryiyan.service.RecruitmentService;
import com.yiyan.careeryiyan.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/enterprise")
public class EnterpriseController {
    @Resource
    private EnterpriseService enterpriseService;
    @Resource
    private RecruitmentService recruitmentService;
    @Resource
    private UserService userService;

    @PostMapping("/addEnterprise")
    public ResponseEntity addEnterprise(@RequestBody AddEnterpriseRequest addEnterpriseRequest, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(user.getId());
        if (enterpriseUser != null) {
            throw new BaseException("用户已创建过企业或已加入企业！");
        }
        Enterprise enterprise = new Enterprise();
        enterprise.setEnterpriseAddress(addEnterpriseRequest.getEnterpriseAddress());
        enterprise.setEnterpriseName(addEnterpriseRequest.getEnterpriseName());
        enterprise.setEnterpriseDescription(addEnterpriseRequest.getEnterpriseDescription());
        enterprise.setEnterpriseLicense(addEnterpriseRequest.getEnterpriseLicense());
        enterprise.setEnterpriseType(addEnterpriseRequest.getEnterpriseType());
        enterprise.setAvatarUrl(addEnterpriseRequest.getAvatarUrl());
        enterprise.setCreateTime(LocalDateTime.now());
        int id = enterpriseService.addEnterprise(enterprise);
        if (id > 0) {
            enterprise.setId(String.valueOf(id));
            enterpriseUser = new EnterpriseUser();
            enterpriseUser.setUserId(user.getId());
            enterpriseUser.setEnterpriseId(enterprise.getId());
            enterpriseUser.setRole(0);
            enterpriseUser.setCreateTime(LocalDateTime.now());
            if (enterpriseService.addEnterpriseUser(enterpriseUser) <= 0) {
                throw new BaseException("创建企业失败！");
            }
            return ResponseEntity.ok("add enterprise success");
        }
        throw new BaseException("创建企业失败！");
    }

    @PostMapping("/getInfo")
    public ResponseEntity getInfo(@RequestBody GetEnterpriseInfoRequest rq) {

        Enterprise enterprise = enterpriseService.getEnterpriseById(rq.getEnterpriseId());
        //System.out.println(enterpriseId);
        if (enterprise == null) {
            throw new BaseException("企业不存在");
        }
        return ResponseEntity.ok(enterprise);
    }

    @PostMapping("/addRecruitment")
    public ResponseEntity addRecruitment(@RequestBody AddRecruitmentRequest addRecruitmentRequest, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(user.getId());
        if (enterpriseUser == null || enterpriseUser.getRole() != 0 || !Objects.equals(enterpriseUser.getEnterpriseId(), addRecruitmentRequest.getEnterpriseId())) {
            throw new BaseException("用户不是企业管理员");
        }
        addRecruitmentRequest.setCreateTime(LocalDateTime.now());
        if (recruitmentService.addRecruitment(addRecruitmentRequest) > 0) {
            return ResponseEntity.ok("发布成功");
        }

        throw new BaseException("发布失败");

    }

    @PostMapping("/getRecruitmentList")
    public ResponseEntity getRecruitmentList(@RequestBody GetRecruitmentListRequest getRecruitmentListRequest) {
        List<Recruitment> recruitmentList = recruitmentService.getRecruitmentList(getRecruitmentListRequest.getEnterpriseId());
        if (recruitmentList == null) {
            return ResponseEntity.ok(new ArrayList<>());
        }

        return ResponseEntity.ok(recruitmentList);
    }

    @PostMapping("/editRecruitment")
    public ResponseEntity editRecruitment(@RequestBody EditRecruitmentRequest editRecruitmentRequest, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(user.getId());
        if (enterpriseUser == null || enterpriseUser.getRole() != 0 ||
                !Objects.equals(enterpriseUser.getEnterpriseId(), editRecruitmentRequest.getEnterpriseId())) {
            throw new BaseException("用户不是企业管理员");
        }
        if (recruitmentService.updateRecruitment(editRecruitmentRequest) > 0) {
            return ResponseEntity.ok(new StringResponse("修改成功"));
        }

        throw new BaseException("修改失败");
    }

    @PostMapping("/deleteRecruitment")
    public ResponseEntity deleteRecruitment(@RequestBody DeleteRecruitmentRequest deleteRecruitmentRequest, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        String id = deleteRecruitmentRequest.getId();
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(user.getId());
        Recruitment recruitment = recruitmentService.getRecruitmentById(id);
        if (enterpriseUser == null || enterpriseUser.getRole() != 0 ||
                !Objects.equals(enterpriseUser.getEnterpriseId(), recruitment.getEnterpriseId())) {
            throw new BaseException("用户不是企业管理员");
        }
        if (recruitmentService.deleteRecruitment(id) > 0) {
            return ResponseEntity.ok(new StringResponse("修改成功"));
        }

        throw new BaseException("修改失败");
    }

    @PostMapping("/apply")
    public ResponseEntity addApply(@RequestBody AddApplyRequest addApplyRequest, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        Apply apply = new Apply(user.getId(), addApplyRequest.getRecruitmentId(), "applied", user.getCv());

        Recruitment recruitment = recruitmentService.getRecruitmentById(addApplyRequest.getRecruitmentId());
        if (recruitment == null) {
            throw new BaseException("职位不存在");
        } else if (recruitmentService.getApplyByUserIdAndRecruitmentId(user.getId(), addApplyRequest.getRecruitmentId()) != null) {
            throw new BaseException("已经申请过该职位");
        } else if (recruitment.getOfferCount() >= recruitment.getHeadCount()) {
            throw new BaseException("该职位已经招满");
        }
        if (recruitmentService.addApply(apply) > 0) {
            return ResponseEntity.ok(new StringResponse("申请成功"));
        }
        throw new BaseException("申请失败");
    }

    @PostMapping("/addEmployee")
    public ResponseEntity addEmployee(@RequestBody AddEmployeeRequest addEmployeeRequest, HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        EnterpriseUser adminUser = enterpriseService.getEnterpriseUserByUserId(user.getId());
        if(adminUser == null||adminUser.getRole() != 0|| !Objects.equals(adminUser.getEnterpriseId(), addEmployeeRequest.getEnterpriseId())){
            throw new BaseException("你不是此企业的管理员");
        }
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(addEmployeeRequest.getUserId());
        if(enterpriseUser!=null){
            throw new BaseException("此用户已有所属企业");
        }
        Enterprise enterprise = enterpriseService.getEnterpriseById(addEmployeeRequest.getEnterpriseId());
        if (enterprise == null){
            throw new BaseException("企业不存在");
        }
        User employee = userService.getUserInfo(addEmployeeRequest.getUserId());
        if(employee==null){
            throw new BaseException("用户不存在");
        }
        enterpriseUser = new EnterpriseUser();
        enterpriseUser.setUserId(addEmployeeRequest.getUserId());
        enterpriseUser.setRole(1);
        enterpriseUser.setEnterpriseId(addEmployeeRequest.getEnterpriseId());
        enterpriseUser.setCreateTime(LocalDateTime.now());
        enterpriseService.addEnterpriseUser(enterpriseUser);

        return ResponseEntity.ok("添加员工成功");
    }

    @PostMapping("getApplicationList")
    public ResponseEntity getApplicationList(@RequestBody GetApplicationListRequest getApplicationListRequest, HttpServletRequest request) {
        User admin = (User) request.getAttribute("user");
        String recruitmentId = getApplicationListRequest.getRecruitmentId();
        Recruitment recruitment  = recruitmentService.getRecruitmentById(recruitmentId);
        if(recruitment==null){
            throw new BaseException("职位不存在");
        }
        EnterpriseUser adminUser = enterpriseService.getEnterpriseUserByUserId(admin.getId());
        if(adminUser == null||adminUser.getRole() != 0|| !Objects.equals(adminUser.getEnterpriseId(), recruitment.getEnterpriseId())) {
            throw new BaseException("你不是此企业的管理员");
        }
        Enterprise enterprise = enterpriseService.getEnterpriseById(recruitment.getEnterpriseId());
        if (enterprise == null){
            throw new BaseException("企业不存在");
        }
        List<Apply> applyList = recruitmentService.getApplyByRecruitmentId(recruitmentId);
        return ResponseEntity.ok(applyList);
    }


    //用户获取自己的投递列表
    @PostMapping("getUserApplyList")
    public ResponseEntity getApplyList(HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getAttribute("user");
        List<UserApplyDetailResponse> applyList = recruitmentService.getUserApplyList(user.getId());
        return ResponseEntity.ok(applyList);
    }

    @PostMapping("changeState")
    public ResponseEntity changeState(@RequestBody Map<String,String> map,HttpServletRequest httpServletRequest){
        String applyId = map.get("applyId");
        String state = map.get("state");
        User user = (User) httpServletRequest.getAttribute("user");
        //查询apply
        Apply apply = recruitmentService.getApplyById(applyId);
        //通过apply查询recruitment
        Recruitment recruitment = recruitmentService.getRecruitmentById(apply.getRecruitmentId());
        //通过userId和recruitment中的enterpriseId查询enterpriseUser
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(user.getId());
        //不是管理员，报错
        if(enterpriseUser == null || enterpriseUser.getRole() != 0 || !Objects.equals(enterpriseUser.getEnterpriseId(),recruitment.getEnterpriseId())){
            throw new BaseException("你不是此企业的管理员");
        }

        if(recruitmentService.changeState(applyId,state)>0){
            return ResponseEntity.ok(new StringResponse("修改成功"));
        }
        throw new BaseException("修改失败");
    }
}


