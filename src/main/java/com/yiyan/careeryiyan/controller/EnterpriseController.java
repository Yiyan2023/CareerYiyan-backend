package com.yiyan.careeryiyan.controller;

import com.yiyan.careeryiyan.config.OSSConfig;
import com.yiyan.careeryiyan.exception.BaseException;
import com.yiyan.careeryiyan.model.domain.*;
import com.yiyan.careeryiyan.model.request.*;
import com.yiyan.careeryiyan.model.response.*;
import com.yiyan.careeryiyan.service.*;
import com.yiyan.careeryiyan.utils.MapUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @Resource
    AddNoticeService addNoticeService;
    @Resource
    OSSConfig ossConfig;

    //refactor completed 2024年6月28日22点47分
    //simple test passed 2024年6月29日01点46分
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
            rsp.put("epId",enterprise.getEpId());
            return ResponseEntity.ok(rsp);

        }
        throw new BaseException("创建企业失败！");
    }

    //refactor completed 2024年6月28日22点47分
    //simple test passed 2024年6月29日01点42分
    @PostMapping("/getInfo")
    public ResponseEntity getInfo(@RequestBody GetEnterpriseInfoRequest rq) {
        String userId = rq.getUserId();
        Enterprise enterprise = enterpriseService.getEnterpriseByEpId(rq.getEpId());
        if (enterprise == null) {
            throw new BaseException("企业不存在");
        }
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(userId);
        Map map = enterprise.toDict();
        int auth=0;
        if(enterpriseUser == null|| !Objects.equals(enterpriseUser.getEpId(), enterprise.getEpId())) {
            auth=0;
        }else{
            if (enterpriseUser.getEpUserAuth() == 1 && Objects.equals(enterpriseUser.getEpId(), enterprise.getEpId()) ) {
                auth=1;
            }else{
                if (enterpriseUser.getEpUserAuth() == 0 && Objects.equals(enterpriseUser.getEpId(), enterprise.getEpId() )) {
                    auth=2;
                }
            }
        }
        map.put("auth",auth);
        return ResponseEntity.ok(map);
    }

    //refactor completed 2024年6月28日22点47分
    //simple test passed 2024年6月29日01点41分
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
            //推送岗位给用户
            addNoticeService.addNewRecruitmentNotice(rcId);
            return ResponseEntity.ok(Map.of("rcId", rcId));
        }
        throw new BaseException("发布失败");
    }

    //refactor completed 2024年6月28日22点47分
    //simple test passed 2024年6月29日01点37分
    @PostMapping("/getRecruitmentList")
    public ResponseEntity getRecruitmentList(@RequestBody GetRecruitmentListRequest getRecruitmentListRequest) {
        String enterpriseId = getRecruitmentListRequest.getEpId();
        List<Map<String, Object>> recruitmentList = recruitmentService.getRecruitmentList(enterpriseId);
        if (recruitmentList == null) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        return ResponseEntity.ok(MapUtil.convertKeysToCamelCase(recruitmentList));
    }

    //refactor completed 2024年6月28日22点47分
    //simple test passed 2024年6月29日01点36分
    @PostMapping("/getRecruitmentInfo")
    public ResponseEntity getRecruitmentInfo(@RequestBody GetRecruitmentInfoRequest getRecruitmentInfoRequest) {
        String rcId = getRecruitmentInfoRequest.getRcId();
        String userId = getRecruitmentInfoRequest.getUserId();
        Recruitment recruitment = recruitmentService.getRecruitmentById(rcId);
        Map<String,Object> map = MapUtil.convertKeysToCamelCase(recruitmentService.getRecruitmentInfo(rcId));
        if (map == null) {
            throw new BaseException("岗位不存在");
        }
        EnterpriseUser enterpriseAdmin = enterpriseService.getEnterpriseAdminByEnterpriseId(recruitment.getEpId());
        User admin = userService.getUserInfo(enterpriseAdmin.getUserId());
        int auth=0;
        if(enterpriseAdmin.getEpUserAuth() == 0 && Objects.equals(userId, enterpriseAdmin.getUserId())){
            auth = 2;//管理员
        }else {
            Apply apply = recruitmentService.getApplyByUserIdAndRecruitmentId(userId, rcId);
            if (apply != null) {
                auth = 1;
            }
        }
        map.put("auth",auth);
        return ResponseEntity.ok(MapUtil.convertKeysToCamelCase(map));
    }

    //refactor completed 2024年6月28日22点47分
    //simple test passed 2024年6月29日01点34分
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

    //refactor completed 2024年6月28日22点47分
    // 被废弃 at 2024年6月29日01点29分

//    @PostMapping("/deleteRecruitment")
//    public ResponseEntity deleteRecruitment(@RequestBody DeleteRecruitmentRequest deleteRecruitmentRequest, HttpServletRequest request) {
//        User user = (User) request.getAttribute("user");
//        String id = deleteRecruitmentRequest.getRcId();
//        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(user.getUserId());
//        Recruitment recruitment = recruitmentService.getRecruitmentById(id);
//        if (enterpriseUser == null || enterpriseUser.getEpUserAuth() != 0 ||
//                !Objects.equals(enterpriseUser.getEpId(), recruitment.getEpId())) {
//            throw new BaseException("用户不是企业管理员");
//        }
//        if (recruitmentService.deleteRecruitment(id) > 0) {
//            return ResponseEntity.ok(new StringResponse("修改成功"));
//        }
//
//        throw new BaseException("修改失败");
//    }

    //refactor completed 2024年6月28日22点48分
    //simple test passed 2024年6月29日00点24分
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

    //refactor completed 2024年6月28日22点48分
    //simple test passed 2024年6月28日23点44分
    @PostMapping("/addEmployee")
    public ResponseEntity addEmployee(@RequestBody AddEmployeeRequest addEmployeeRequest, HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        System.out.println("userId = "+ user.getUserId());
        System.out.println("epId = " + addEmployeeRequest.getEpId());
        EnterpriseUser adminUser = enterpriseService.getEnterpriseUserByUserId(user.getUserId());
        if(adminUser == null||adminUser.getEpUserAuth() != 0|| !Objects.equals(adminUser.getEpId(), addEmployeeRequest.getEpId())){
            throw new BaseException("你不是此企业的管理员");
        }
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(addEmployeeRequest.getUserId());
        if(enterpriseUser!=null){
            throw new BaseException("此用户已有所属企业");
        }
        Enterprise enterprise = enterpriseService.getEnterpriseByEpId(addEmployeeRequest.getEpId());
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

    //refactor completed 2024年6月28日23点40分
    //simple test passed 2024年6月28日23点40分
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
        System.out.println("epId is "+recruitment.getEpId());
        Enterprise enterprise = enterpriseService.getEnterpriseByEpId(recruitment.getEpId());
        if (enterprise == null){
            throw new BaseException("企业不存在");
        }
        List<Apply> applyList = recruitmentService.getApplyByRecruitmentId(recruitmentId);
        List<Map<String,Object>> responseList = new ArrayList<>();
        for (Apply apply : applyList) {
            User user = userService.getUserInfo(apply.getUserId());
            List<UserRecruitmentPreferences> userRecruitmentPreferences = userService.getUserRecruitmentPreferences(apply.getUserId());
            Map<String, Object> map = new HashMap<>();
            map.put("applyId", apply.getApplyId());
            map.put("applyCvUrl", apply.getApplyCvUrl());
            map.put("applyStatus", apply.getApplyStatus());
            map.put("applyCreateAt", apply.getApplyCreateAt());
            map.put("applyUpdateAt",apply.getApplyUpdateAt());
            map.put("rcId",apply.getRcId());

            map.put("userName", user.getUserNickname());
            map.put("userAvatarUrl", user.getUserAvatarUrl());
            map.put("userEdu", user.getUserEdu());
            map.put("userId", user.getUserId());
            map.put("userInterest", user.getUserInterest());

            map.put("userRecruitmentPreferences", userRecruitmentPreferences.stream());
            responseList.add(map);
        }
        return ResponseEntity.ok(responseList);
    }


    //refactor completed 2024年6月28日22点48分
    //simple test passed 2024年6月29日01点21分
    //用户获取自己的投递列表
    @PostMapping("/getUserApplyList")
    public ResponseEntity getApplyList(HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getAttribute("user");
        List<Map<String, Object>> applyList = recruitmentService.getUserApplyList(user.getUserId());
        applyList = MapUtil.convertKeysToCamelCase(applyList);
        return ResponseEntity.ok(applyList);
    }

    //refactor completed 2024年6月28日22点48分
    //simple test passed 2024年6月29日00点45分
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
            //通知用户招聘状态
            addNoticeService.addApplyStatusNotice(apply);
            return ResponseEntity.ok(new StringResponse("修改成功"));
        }
        throw new BaseException("修改失败");
    }

    //refactor completed 2024年6月28日22点48分
    //simple test passed 2024年6月29日01点06分
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
        int status = isAccept==1?3:4;
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
                return ResponseEntity.ok(new StringResponse("您已成功加入企业"));
            }
        }
        //修改apply(accept:status=3 offer被接收   not accept:status=4 offer被拒绝
        if(recruitmentService.changeState(applyId,status)<=0){
            throw new BaseException("处理失败");
        }
        return ResponseEntity.ok(new StringResponse("处理成功"));
        //throw new BaseException("处理失败");
    }

    //refactor completed 2024年6月28日22点55分
    //simple test passed 2024年6月29日00点42分
    @PostMapping("/getEmployeeList")
    public ResponseEntity getEmployeeList(@RequestBody Map<String,String> requestBody, HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getAttribute("user");
        String enterpriseId = requestBody.get("epId");
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(user.getUserId());
        if(enterpriseUser == null || !Objects.equals(enterpriseUser.getEpId(),enterpriseId)){

            return ResponseEntity.ok(new ArrayList<>());
        }
        List<Map<String, Object>> employeeList = enterpriseService.getEmployeeListByEnterpriseId(enterpriseId);
        return ResponseEntity.ok(employeeList);
    }

    // refactor completed 2024年6月28日22点49分
    // simple test passed 2024年6月29日00点37分
    @PostMapping("/getAdmin")
    public ResponseEntity getAdmin(@RequestBody Map<String,String> requestBody){
        String enterpriseId = requestBody.get("epId");
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseAdminByEnterpriseId(enterpriseId);
        if(enterpriseUser == null){
            throw new BaseException("企业不存在");
        }
        User admin = userService.getUserInfo(enterpriseUser.getUserId());
        return ResponseEntity.ok(admin);
    }

    @PostMapping("/transferAdmin")
    public ResponseEntity transferAdmin(@RequestBody Map<String,String> requestBody,HttpServletRequest httpServletRequest){
        User admin = (User) httpServletRequest.getAttribute("user");
        String epId = requestBody.get("epId");
        String newAdminId = requestBody.get("userId");
        System.out.println("epId = "+epId);
        System.out.println("newAdminId = "+newAdminId);
        EnterpriseUser oldAdmin = enterpriseService.getEnterpriseUserByUserId(admin.getUserId());
        if(oldAdmin == null || oldAdmin.getEpUserAuth() != 0 || !Objects.equals(oldAdmin.getEpId(),epId)){
            throw new BaseException("你不是此企业的管理员");
        }
        EnterpriseUser newAdmin = enterpriseService.getEnterpriseUserByUserId(newAdminId);
        if(newAdmin == null || !Objects.equals(newAdmin.getEpId(),epId)){
            throw new BaseException("新管理员不在此企业");
        }
        if(enterpriseService.transferAdmin(oldAdmin.getEpUserId(),newAdmin.getEpUserId())>0){
            addNoticeService.addTransferEnterpriseNotice(newAdmin.getUserId(), epId);
            return ResponseEntity.ok(new StringResponse("转让成功"));
        }
        throw new BaseException("转让失败");
    }


    @PostMapping("quitEnterprise")
    public ResponseEntity quitEnterprise(@RequestBody Map<String,String> requestBody,HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getAttribute("user");
        String epId = requestBody.get("epId");
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(user.getUserId());
        if(enterpriseUser == null){
            throw new BaseException("你不在任何企业中");
        }
        if(!Objects.equals(enterpriseUser.getEpId(),epId)){
            throw new BaseException("你不在此企业中");
        }
        //管理员不能退出企业
        if(enterpriseUser.getEpUserAuth() == 0){
            throw new BaseException("管理员不能退出企业");
        }
        if(enterpriseService.quitEnterprise(enterpriseUser.getEpUserId())>0){
            addNoticeService.addQuitEnterpriseNotice(user.getUserName(),epId);
            return ResponseEntity.ok(new StringResponse("退出成功"));
        }
        throw new BaseException("退出失败");
    }

    //用户取消投递简历,传入的是岗位id,需要查询apply
    @PostMapping("/cancelApply")
    public ResponseEntity cancelApply(@RequestBody Map<String,String> requestBody,HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getAttribute("user");
        String rcId = requestBody.get("rcId");
        Apply apply = recruitmentService.getApplyByUserIdAndRecruitmentId(user.getUserId(),rcId);
        if(apply == null){
            throw new BaseException("你没有投递过该岗位");
        }
        if(recruitmentService.cancelApply(apply.getApplyId())>0){
            return ResponseEntity.ok(new StringResponse("取消成功"));
        }
        throw new BaseException("取消失败");

    }

    @PostMapping("/uploadAvatar")
    public ResponseEntity uploadAvatar(@RequestParam("epId") String epId, @RequestParam("avatar") MultipartFile avatar, HttpServletRequest httpServletRequest) throws IOException {
        User user = (User) httpServletRequest.getAttribute("user");
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(user.getUserId());
        if(enterpriseUser == null || !Objects.equals(enterpriseUser.getEpId(),epId)){
            throw new BaseException("你不是此企业的管理员");
        }
        if (ObjectUtils.isEmpty(avatar) || avatar.getSize() <= 0) {
            throw new BaseException("File is empty");
        }
        if (!avatar.getContentType().equals("image/jpeg") && !avatar.getContentType().equals("image/png")) {
            throw new BaseException("File must be a jpg/png");
        }
        String name = "ep"+enterpriseUser.getEpId()+".jpg";
        String url=ossConfig.upload(avatar, "avatar", name);
        if(url == null){
            throw new BaseException("上传失败");
        }
        if(enterpriseService.updateAvatar(epId,url)>0){
            return ResponseEntity.ok(Map.of("epAvatarUrl",url));
        }
        throw new BaseException("上传失败");
    }



    @PostMapping("/editEnterpriseInfo")
    public ResponseEntity editEnterpriseInfo(@RequestBody EditEnterpriseRequest editEnterpriseRequest,HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getAttribute("user");
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserByUserId(user.getUserId());
        if(enterpriseUser == null || enterpriseUser.getEpUserAuth() != 0){
            throw new BaseException("你不是此企业的管理员");
        }
        if(enterpriseService.editEnterprise(editEnterpriseRequest)>0){
            return ResponseEntity.ok(new StringResponse("修改成功"));
        }
        throw new BaseException("修改失败");
    }

}
