package com.yiyan.careeryiyan.controller;

import com.yiyan.careeryiyan.exception.BaseException;
import com.yiyan.careeryiyan.model.domain.*;
import com.yiyan.careeryiyan.model.request.*;
import com.yiyan.careeryiyan.model.response.*;
import com.yiyan.careeryiyan.service.EnterpriseService;
import com.yiyan.careeryiyan.service.RecruitmentService;
import com.yiyan.careeryiyan.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

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
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(user.getUserId());
        if (enterpriseUser != null) {
            throw new BaseException("用户已创建过企业或已加入企业！");
        }
        //检查企业重名
        if (enterpriseService.getEnterpriseByName(addEnterpriseRequest.getEpName()) != null) {
            throw new BaseException("企业名已存在");
        }
        Enterprise enterprise = new Enterprise();
        enterprise.setEpAddr(addEnterpriseRequest.getEpAddr());
        enterprise.setEpName(addEnterpriseRequest.getEpName());
        enterprise.setEpDesc(addEnterpriseRequest.getEpDesc());
        enterprise.setEpLicense(addEnterpriseRequest.getEpLicense());
        enterprise.setEpType(addEnterpriseRequest.getEpType());
        enterprise.setEpAvatarUrl(addEnterpriseRequest.getEpAvatarUrl());
        enterprise.setEpCreateAt(LocalDateTime.now());

        if (enterpriseService.addEnterprise(enterprise) > 0) {
            System.out.println(enterprise.getEpId());
            //enterprise.setId(String.valueOf(id));
            enterpriseUser = new EnterpriseUser();
            enterpriseUser.setUserId(user.getUserId());
            enterpriseUser.setEpId(enterprise.getEpId());
            enterpriseUser.setEpUserAuth(0);
            enterpriseUser.setEpUserCreateAt(LocalDateTime.now());
            if (enterpriseService.addEnterpriseUser(enterpriseUser) <= 0) {
                throw new BaseException("创建企业失败！");
            }
            //System.out.println(enterprise.getId());
            Map<String ,String> rsp = new HashMap<>();
            rsp.put("enterpriseId",enterprise.getEpId());
            return ResponseEntity.ok(rsp);

        }
        throw new BaseException("创建企业失败！");
    }

    @PostMapping("/getInfo")
    public ResponseEntity getInfo(@RequestBody GetEnterpriseInfoRequest rq) {
        String userId = rq.getUserId();
        Enterprise enterprise = enterpriseService.getEnterpriseById(rq.getEpId());
        //System.out.println(enterpriseId);
        if (enterprise == null) {
            throw new BaseException("企业不存在");
        }
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(userId);
        EnterpriseInfoResponse response= new EnterpriseInfoResponse();
        if(enterpriseUser == null|| !Objects.equals(enterpriseUser.getEpId(), enterprise.getEpId())) {
            response = new EnterpriseInfoResponse(enterprise, 0);
        }else{
            if (enterpriseUser.getEpUserAuth() == 1 && Objects.equals(enterpriseUser.getEpId(), enterprise.getEpId()) ) {
                response = new EnterpriseInfoResponse(enterprise, 1);
            }else{
                if (enterpriseUser.getEpUserAuth() == 0 && Objects.equals(enterpriseUser.getEpId(), enterprise.getEpId() )) {
                    response = new EnterpriseInfoResponse(enterprise, 2);
                }
            }
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addRecruitment")
    public ResponseEntity addRecruitment(@RequestBody AddRecruitmentRequest addRecruitmentRequest, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(user.getUserId());
        if (enterpriseUser == null || enterpriseUser.getEpUserAuth() != 0 || !Objects.equals(enterpriseUser.getEpId(), addRecruitmentRequest.getEpId())) {
            throw new BaseException("用户不是企业管理员");
        }
        addRecruitmentRequest.setRcCreateAt(LocalDateTime.now());
        String rcId = recruitmentService.addRecruitment(addRecruitmentRequest);
        if (Integer.parseInt(rcId) > 0) {
            return ResponseEntity.ok(Map.of("recruitmentId",String.valueOf(rcId)));
        }

        throw new BaseException("发布失败");

    }

    @PostMapping("/getRecruitmentList")
    public ResponseEntity getRecruitmentList(@RequestBody GetRecruitmentListRequest getRecruitmentListRequest) {
        String enterpriseId = getRecruitmentListRequest.getEpId();
        List<Recruitment> recruitmentList = recruitmentService.getRecruitmentList(enterpriseId);
        if (recruitmentList == null) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        EnterpriseUser enterpriseAdmin = enterpriseService.getEnterpriseAdminByEnterpriseId(enterpriseId);
        if(enterpriseAdmin == null){
            return ResponseEntity.ok(new ArrayList<>());
        }
        User admin = userService.getUserInfo(enterpriseAdmin.getUserId());
        List<GetRecruitmentListResponse> recruitmentResponseList = new ArrayList<>();
        for (Recruitment recruitment : recruitmentList) {
            recruitmentResponseList.add(new GetRecruitmentListResponse(recruitment, admin));
        }
        return ResponseEntity.ok(recruitmentResponseList);
    }

    @PostMapping("/getRecruitmentInfo")
    public ResponseEntity getRecruitmentInfo(@RequestBody GetRecruitmentInfoRequest getRecruitmentInfoRequest) {
        String recruitmentId = getRecruitmentInfoRequest.getRcId();
        String userId = getRecruitmentInfoRequest.getUserId();
        Recruitment recruitment = recruitmentService.getRecruitmentById(recruitmentId);
        if(recruitment==null){
            throw new BaseException("岗位不存在");
        }
        EnterpriseUser enterpriseAdmin = enterpriseService.getEnterpriseAdminByEnterpriseId(recruitment.getEpId());
        User admin = userService.getUserInfo(enterpriseAdmin.getUserId());
        int auth=0;
        if(enterpriseAdmin.getEpUserAuth() == 0 && Objects.equals(userId, enterpriseAdmin.getUserId())){
            auth = 2;//管理员
        }else {
            Apply apply = recruitmentService.getApplyByUserIdAndRecruitmentId(userId, recruitmentId);
            if (apply != null) {
                auth = 1;
            }
        }
        return ResponseEntity.ok(new GetRecruitmentInfoResponse(recruitment, admin, auth));
    }

    @PostMapping("/editRecruitment")
    public ResponseEntity editRecruitment(@RequestBody EditRecruitmentRequest editRecruitmentRequest, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(user.getUserId());
        if (enterpriseUser == null || enterpriseUser.getEpUserAuth() != 0 ||
                !Objects.equals(enterpriseUser.getEpId(), editRecruitmentRequest.getEpId())) {
            throw new BaseException("用户不是企业管理员");
        }
        Recruitment recruitment = recruitmentService.getRecruitmentById(editRecruitmentRequest.getRcId());
        if(recruitment.getRcOfferCount() > editRecruitmentRequest.getRcTotalCount()){
            throw new BaseException("招聘人数不能小于已发offer人数");
        }

        if (recruitmentService.updateRecruitment(editRecruitmentRequest) > 0) {
            return ResponseEntity.ok(new StringResponse("修改成功"));
        }

        throw new BaseException("修改失败");
    }

    @PostMapping("/deleteRecruitment")
    public ResponseEntity deleteRecruitment(@RequestBody DeleteRecruitmentRequest deleteRecruitmentRequest, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        String id = deleteRecruitmentRequest.getRcId();
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(user.getUserId());
        Recruitment recruitment = recruitmentService.getRecruitmentById(id);
        if (enterpriseUser == null || enterpriseUser.getEpUserAuth() != 0 ||
                !Objects.equals(enterpriseUser.getEpId(), recruitment.getEpId())) {
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
        Apply apply = new Apply(user.getUserId(), addApplyRequest.getRcId(), 0, user.getUserCvUrl());
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(user.getUserId());
        if(enterpriseUser!=null&&enterpriseUser.getEpUserAuth()==0){
            throw new BaseException("企业管理员不能申请其他职位");
        }
        Recruitment recruitment = recruitmentService.getRecruitmentById(addApplyRequest.getRcId());
        if (recruitment == null) {
            throw new BaseException("职位不存在");
        } else if (recruitmentService.getApplyByUserIdAndRecruitmentId(user.getUserId(), addApplyRequest.getRcId()) != null) {
            throw new BaseException("已经申请过该职位");
        } else if (recruitment.getRcOfferCount() >= recruitment.getRcTotalCount()) {
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
        EnterpriseUser adminUser = enterpriseService.getEnterpriseUserByUserId(user.getUserId());
        if(adminUser == null||adminUser.getEpUserAuth() != 0|| !Objects.equals(adminUser.getEpId(), addEmployeeRequest.getEpId())){
            throw new BaseException("你不是此企业的管理员");
        }
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(addEmployeeRequest.getUserId());
        if(enterpriseUser!=null){
            throw new BaseException("此用户已有所属企业");
        }
        Enterprise enterprise = enterpriseService.getEnterpriseById(addEmployeeRequest.getEpId());
        if (enterprise == null){
            throw new BaseException("企业不存在");
        }
        User employee = userService.getUserInfo(addEmployeeRequest.getUserId());
        if(employee==null){
            throw new BaseException("用户不存在");
        }
        enterpriseUser = new EnterpriseUser();
        enterpriseUser.setUserId(addEmployeeRequest.getUserId());
        enterpriseUser.setEpUserAuth(1);
        enterpriseUser.setEpId(addEmployeeRequest.getEpId());
        enterpriseUser.setEpUserCreateAt(LocalDateTime.now());
        enterpriseService.addEnterpriseUser(enterpriseUser);

        return ResponseEntity.ok("添加员工成功");
    }

    @PostMapping("/getApplicationList")
    public ResponseEntity getApplicationList(@RequestBody GetApplicationListRequest getApplicationListRequest, HttpServletRequest request) {
        User admin = (User) request.getAttribute("user");
        String recruitmentId = getApplicationListRequest.getRcId();
        Recruitment recruitment  = recruitmentService.getRecruitmentById(recruitmentId);
        if(recruitment==null){
            throw new BaseException("职位不存在");
        }
        EnterpriseUser adminUser = enterpriseService.getEnterpriseUserByUserId(admin.getUserId());
        if(adminUser == null||adminUser.getEpUserAuth() != 0|| !Objects.equals(adminUser.getEpId(), recruitment.getEpId())) {
            throw new BaseException("你不是此企业的管理员");
        }
        Enterprise enterprise = enterpriseService.getEnterpriseById(recruitment.getEpId());
        if (enterprise == null){
            throw new BaseException("企业不存在");
        }
        List<Apply> applyList = recruitmentService.getApplyByRecruitmentId(recruitmentId);
        List<GetApplicationListResponse> responseList = new ArrayList<>();
        for (Apply apply : applyList) {
            User user = userService.getUserInfo(apply.getUserId());
            List<UserJobPreferences> userJobPreferences = userService.getUserJobPreferences(apply.getUserId());
            responseList.add(new GetApplicationListResponse(apply, user, userJobPreferences));
        }
        return ResponseEntity.ok(responseList);
    }


    //用户获取自己的投递列表
    @PostMapping("/getUserApplyList")
    public ResponseEntity getApplyList(HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getAttribute("user");
        List<Map<String, Object>> applyList = recruitmentService.getUserApplyList(user.getUserId());
        return ResponseEntity.ok(applyList);
    }

    @PostMapping("/changeState")
    public ResponseEntity changeState(@RequestBody Map<String,String> map,HttpServletRequest httpServletRequest){
        String applyId = map.get("applyId");
        int status = Integer.parseInt(map.get("status"));
        User user = (User) httpServletRequest.getAttribute("user");
        //查询apply
        Apply apply = recruitmentService.getApplyById(applyId);
        //通过apply查询recruitment
        Recruitment recruitment = recruitmentService.getRecruitmentById(apply.getRcId());
        //通过userId和recruitment中的enterpriseId查询enterpriseUser
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(user.getUserId());
        //不是管理员，报错
        if(enterpriseUser == null || enterpriseUser.getEpUserAuth() != 0 || !Objects.equals(enterpriseUser.getEpId(),recruitment.getEpId())){
            throw new BaseException("你不是此企业的管理员");
        }

        if(recruitmentService.changeState(applyId,status)>0){
            return ResponseEntity.ok(new StringResponse("修改成功"));
        }
        throw new BaseException("修改失败");
    }

    @PostMapping("/acceptOffer")
    public ResponseEntity acceptOffer(@RequestBody Map<String,String> map,HttpServletRequest httpServletRequest){
        String applyId = map.get("applyId");
        int isAccept = Integer.parseInt(map.get("isAccept"));
        User user = (User) httpServletRequest.getAttribute("user");
        Apply apply = recruitmentService.getApplyById(applyId);
        if(apply == null){
            throw new BaseException("申请不存在");
        }

        //判断是否是申请人
        if(!Objects.equals(apply.getUserId(),user.getUserId())){
            throw new BaseException("你不是申请人");
        }
        //判断是否发offer(status为1)
        if(apply.getApplyStatus()!=1){
            throw new BaseException("该岗位没有向你发放offer");
        }

        //判断相应recruitment是否招满
        Recruitment recruitment = recruitmentService.getRecruitmentById(apply.getRcId());
        if(recruitment.getRcOfferCount()>=recruitment.getRcTotalCount()){
            throw new BaseException("该岗位已经招满");
        }
        //修改apply(accept:status=3 offer被接收   not accept:status=4 offer被拒绝
        int status = isAccept==1?3:4;
        if(recruitmentService.changeState(applyId,status)<=0){
            throw new BaseException("处理失败");
        }

        if (status == 3){
            //已经加入企业不能再加入
            EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(user.getUserId());
            //判断是否已经加入企业
            if(enterpriseUser != null){
                throw new BaseException("您已在企业中");
            }
            //将用户加入企业
            int res=enterpriseService.addUserToEnterprise(apply.getUserId(),recruitment.getEpId());
            if (res > 0){
                return ResponseEntity.ok(new StringResponse("您已加入企业"));
            }
        }

        throw new BaseException("处理失败");
    }

    @PostMapping("/getEmployeeList")
    public ResponseEntity getEmployeeList(@RequestBody Map<String,String> requestBody, HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getAttribute("user");
        String enterpriseId = requestBody.get("enterpriseId");
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(user.getUserId());
        if(enterpriseUser == null || !Objects.equals(enterpriseUser.getEpId(),enterpriseId)){
            throw new BaseException("无权查看企业员工列表");
        }

        List<EmployeeListResponse> employeeList = enterpriseService.getEmployeeListByEnterpriseId(enterpriseId);
        return ResponseEntity.ok(employeeList);
    }

    @PostMapping("/getAdmin")
    public ResponseEntity getAdmin(@RequestBody Map<String,String> requestBody){
        String enterpriseId = requestBody.get("enterpriseId");
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseAdminByEnterpriseId(enterpriseId);
        if(enterpriseUser == null){
            throw new BaseException("企业不存在");
        }
        User admin = userService.getUserInfo(enterpriseUser.getUserId());
        return ResponseEntity.ok(admin);
    }
}


