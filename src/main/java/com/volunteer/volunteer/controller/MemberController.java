package com.volunteer.volunteer.controller;

import com.volunteer.volunteer.enums.DepartmentEnum;
import com.volunteer.volunteer.model.UserInformation;
import com.volunteer.volunteer.service.UserInformationService;
import com.volunteer.volunteer.util.ExcelUtil;
import com.volunteer.volunteer.util.ToolSupport.UniversalResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/pc/member")
@Slf4j
public class MemberController {


    @Resource
    private UserInformationService userInformationService;

    /**
     * @Description: PC端：我的部员
     * @Param: [mainId,request]
     * @return: UniversalResponseBody
     */
    @GetMapping(value = "/")
    public UniversalResponseBody myMembers(@RequestParam("department")int departmentCode){
        List<UserInformation> list = userInformationService.findMemberByDepartment(DepartmentEnum.getDepartment(departmentCode));
        if (list != null) {
            return new UniversalResponseBody<>(0, "成功",list);
        } else {
            return new UniversalResponseBody(-1, "失败");
        }
    }


    /**
     * @Description: PC端：退部
     * @Param: [mainId]
     * @return: UniversalResponseBody
     */
    @PostMapping(value = "/DROP")
    public UniversalResponseBody dropOut(int mainId){
        if (userInformationService.updateDropOut(mainId)) {
            return new UniversalResponseBody<>(0, "成功");
        } else {
            return new UniversalResponseBody(-1, "失败");
        }
    }

    /**
     * @Description: 导出部员名单
     * @Param: [response]
     * @return: void
     */
    @GetMapping(value = "/export/list")
    public void exportMyMembersList(HttpServletResponse response,HttpServletRequest request){
        String department = (String)request.getSession().getAttribute("department");
        List<UserInformation> list = userInformationService.findMemberByDepartment(department);
        ExcelUtil.exportExcel(list,department+"部员名单","花名册",UserInformation.class,department+"名单.xls",response);
    }

    /**
     * @Description: 导出考勤表
     * @Param: [response]
     * @return: void
     */
    @GetMapping(value = "/export/attendance")
    public void exportAttendance(HttpServletResponse response,HttpServletRequest request){
        request.getSession().getAttribute("department");
        //TODO 未完
    }
}
