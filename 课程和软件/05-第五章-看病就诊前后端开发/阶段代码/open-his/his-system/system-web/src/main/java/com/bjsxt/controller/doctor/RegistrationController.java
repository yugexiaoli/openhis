package com.bjsxt.controller.doctor;

import com.bjsxt.constants.Constants;
import com.bjsxt.controller.BaseController;
import com.bjsxt.domain.Dept;
import com.bjsxt.domain.Patient;
import com.bjsxt.domain.Registration;
import com.bjsxt.dto.PatientDto;
import com.bjsxt.dto.RegistrationDto;
import com.bjsxt.dto.RegistrationFormDto;
import com.bjsxt.dto.RegistrationQueryDto;
import com.bjsxt.service.DeptService;
import com.bjsxt.service.PatientService;
import com.bjsxt.service.RegistrationService;
import com.bjsxt.service.SchedulingService;
import com.bjsxt.utils.IdGeneratorSnowflake;
import com.bjsxt.utils.ShiroSecurityUtils;
import com.bjsxt.vo.AjaxResult;
import com.bjsxt.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * @Author: 尚学堂 雷哥
 */
@RestController
@RequestMapping("doctor/registration")
public class RegistrationController extends BaseController {


    @Reference
    private SchedulingService schedulingService;

    @Autowired
    private DeptService deptService;

    @Reference
    private PatientService patientService;

    @Reference
    private RegistrationService registrationService;
    /**
     * 查询出所有的排班的部门列表
     * 1.从排班表里面查询有排班的部门编号集合
     * 2.根据查询出来的部门编号集合再去查询部门
     */
    @GetMapping("listDeptForScheduling")
    @HystrixCommand
    public AjaxResult listDeptForScheduling(@Validated RegistrationQueryDto registrationQueryDto){
        Long deptId = registrationQueryDto.getDeptId();
        String subsectionType = registrationQueryDto.getSubsectionType();
        String schedulingType = registrationQueryDto.getSchedulingType();//挂号类型
        String schedulingDay = registrationQueryDto.getSchedulingDay().substring(0,10);//时间
        List<Long> deptIds=schedulingService.queryHasSchedulingDeptIds(deptId,schedulingDay,schedulingType,subsectionType);

        if(null==deptIds||deptIds.size()==0){
            return AjaxResult.success(Collections.EMPTY_LIST);
        }else{
            List<Dept> list=this.deptService.listDeptByDeptIds(deptIds);
            return AjaxResult.success(list);
        }
    }


    /**
     * 根据身份证号查询患者信息
     */
    @GetMapping("getPatientByIdCard/{idCard}")
    public AjaxResult getPatientByIdCard(@PathVariable String idCard){

        Patient patient=this.patientService.getPatientByIdCard(idCard);
        if(null==patient){
            return AjaxResult.fail("【"+idCard+"】对应的患者不存在，请在下面新建患者信息");
        }else{
            return AjaxResult.success(patient);
        }

    }

    /**
     * 挂号
     */
    @PostMapping("addRegistration")
    @HystrixCommand
    public AjaxResult addRegistration(@RequestBody @Validated RegistrationFormDto registrationFormDto){
        PatientDto patientDto = registrationFormDto.getPatientDto();
        RegistrationDto registrationDto = registrationFormDto.getRegistrationDto();
        Patient patient=null;
        if(StringUtils.isBlank(patientDto.getPatientId())){//说明要新添加患者
            patientDto.setPatientId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PROFIX_HZ));
            patientDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
            patient=patientService.addPatient(patientDto);
        }else{
            //如果有患者编号，就把患者信息查询出来
            patient=this.patientService.getPatientById(patientDto.getPatientId());
        }
        if(null==patient){
            return AjaxResult.fail("当前患者ID不存在，请确认后再提交");
        }
        //查询部门信息
        Dept dept=this.deptService.getOne(registrationDto.getDeptId());
        //保存患者信息
        registrationDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        registrationDto.setRegId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PROFIX_GH));
        registrationDto.setPatientId(patient.getPatientId());
        registrationDto.setPatientName(patient.getName());
        registrationDto.setRegNumber(dept.getRegNumber()+1);//编号+1
        this.registrationService.addRegistration(registrationDto);
        //更新当天的科室号段
        this.deptService.updateDeptRegNumber(dept.getDeptId(),dept.getRegNumber()+1);
        //返回挂号编号给前端
        return AjaxResult.success("",registrationDto.getRegId());
    }

    /**
     * 收费
     */
    @PostMapping("collectFee/{regId}")
    @HystrixCommand
    public AjaxResult collectFee(@PathVariable String regId){
        //查询挂号单
        Registration registration=this.registrationService.queryRegistrationByRegId(regId);
        if(null==registration){
            return AjaxResult.fail("当前挂号单【"+regId+"】对应的挂号单不存在，请核对后再查询");
        }
        //如果挂号单的状态不是未收费
        if(!registration.getRegStatus().equals(Constants.REG_STATUS_0)){
            return AjaxResult.fail("当前挂号单【"+regId+"】的状态不是未收费状态，不能进行收费");
        }
        //收费，更新挂号单的状态
        registration.setRegStatus(Constants.REG_STATUS_1);
        return AjaxResult.toAjax(this.registrationService.updateRegistrationByRegId(registration));
    }


    /**
     * 分页查询挂号信息
     */
    @GetMapping("queryRegistrationForPage")
    @HystrixCommand
    public AjaxResult queryRegistrationForPage(RegistrationDto registrationDto){
        DataGridView gridView=this.registrationService.queryRegistrationForPage(registrationDto);
        return AjaxResult.success("查询成功",gridView.getData(),gridView.getTotal());
    }



    /**
     * 作废
     */
    @PostMapping("doInvalid/{regId}")
    @HystrixCommand
    public AjaxResult doInvalid(@PathVariable String regId){
        Registration registration=this.registrationService.queryRegistrationByRegId(regId);
        if(null==registration){
            return AjaxResult.fail("当前挂号单【"+regId+"】对应的挂号单不存在，请核对后再查询");
        }
        //如果挂号单的状态不是未收费
        if(!registration.getRegStatus().equals(Constants.REG_STATUS_0)){
            return AjaxResult.fail("当前挂号单【"+regId+"】的状态不是未收费状态，不能作废");
        }
        //收费，更新挂号单的状态
        registration.setRegStatus(Constants.REG_STATUS_5);
        return AjaxResult.toAjax(this.registrationService.updateRegistrationByRegId(registration));
    }

    /**
     * 退号
     */
    @PostMapping("doReturn/{regId}")
    @HystrixCommand
    public AjaxResult doReturn(@PathVariable String regId){
        Registration registration=this.registrationService.queryRegistrationByRegId(regId);
        if(null==registration){
            return AjaxResult.fail("当前挂号单【"+regId+"】对应的挂号单不存在，请核对后再查询");
        }
        //如果挂号单的状态不是未收费
        if(!registration.getRegStatus().equals(Constants.REG_STATUS_1)){
            return AjaxResult.fail("当前挂号单【"+regId+"】的状态不是待就诊状态，不能退号");
        }
        //收费，更新挂号单的状态
        registration.setRegStatus(Constants.REG_STATUS_4);
        return AjaxResult.toAjax(this.registrationService.updateRegistrationByRegId(registration));
    }

}
