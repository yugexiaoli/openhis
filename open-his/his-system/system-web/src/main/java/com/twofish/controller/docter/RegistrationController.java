package com.twofish.controller.docter;

import cn.hutool.core.date.DateUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.twofish.aspectj.annotation.Log;
import com.twofish.aspectj.enums.BusinessType;
import com.twofish.constants.Constants;
import com.twofish.controller.BaseController;
import com.twofish.domain.Dept;
import com.twofish.domain.Patient;
import com.twofish.domain.Registration;
import com.twofish.dto.PatientDto;
import com.twofish.dto.RegistrationDto;
import com.twofish.dto.RegistrationFormDto;
import com.twofish.dto.RegistrationQueryDto;
import com.twofish.service.DeptService;
import com.twofish.service.PatientService;
import com.twofish.service.RegistrationService;
import com.twofish.service.SchedulingService;
import com.twofish.utils.IdGeneratorSnowflake;
import com.twofish.utils.ShiroSecurityUtils;
import com.twofish.vo.AjaxResult;
import com.twofish.vo.DataGridView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author ：ChenChangYu
 * @date ：Created in 2021/3/9 21:51
 * @description：挂号
 */
@RestController
@RequestMapping("/doctor/registration/")
@Api(value = "挂号控制层",tags = "挂号")
@Log4j2
public class RegistrationController extends BaseController {

    @Reference
    private SchedulingService schedulingService;
    @Resource
    private DeptService deptService;

    @Reference
    private RegistrationService registrationService;

    @Reference
    private PatientService patientService;
    /**
     * 根据条件查询有号的部门
     * 1,先根据条件查询排班表，查出部门id集合
     * 2，再根据部门id集合查询部门表，查出部门集合
     * @param registrationQueryDto
     * @return
     */
    @GetMapping("listDeptForScheduling")
    @ApiOperation(value = "根据条件查询有号的部门",notes = "根据条件查询有号的部门")
    @HystrixCommand
    public AjaxResult listDeptForScheduling(@Validated RegistrationQueryDto registrationQueryDto){

        Long deptId = registrationQueryDto.getDeptId();
        String schedulingDay = registrationQueryDto.getSchedulingDay().substring(0,10);
        String schedulingType = registrationQueryDto.getSchedulingType();
        String subsectionType = registrationQueryDto.getSubsectionType();
        List<Long> deptIds=this.schedulingService.queryHasSchedulingForDeptId(deptId,schedulingDay,schedulingType,subsectionType);
        if(null==deptIds ||deptIds.size()==0){
            return AjaxResult.success(Collections.EMPTY_LIST);
        }else {
            List<Dept> depts=this.deptService.queryDeptByIds(deptIds);
            return AjaxResult.success(depts);
        }
    }

    /**
     * 根据身份证号查询患者信息
     */
    @GetMapping("getPatientByIdCard/{idCard}")
    @ApiOperation(value = "根据身份证号查询患者信息",notes = "根据身份证号查询患者信息")
    @HystrixCommand
    public AjaxResult getPatientByIdCard(@PathVariable String idCard){
        Patient patient=this.patientService.getPatientByIdCard(idCard);
        if(null==patient){
            return AjaxResult.fail("患者库中没有["+idCard+"]的患者,请在下方填写新增患者信息");
        }else {
            return AjaxResult.success(patient);
        }
    }




    /**
     * 患者PC端挂号
     * 两种情况，如果是已经存在的患者编号，直接插入挂号表，然后修改科室的号数
     * 如果是不存在临时加的患者，需要先吧患者入库，再挂号
     */
    @PostMapping("addRegistration")
    @ApiOperation(value = "患者PC端挂号",notes = "患者PC端挂号")
    @Log(title = "患者PC端挂号",businessType = BusinessType.INSERT)
    @HystrixCommand
    public AjaxResult addRegistration(@RequestBody @Validated RegistrationFormDto registrationFormDto){
        PatientDto patientDto = registrationFormDto.getPatientDto();
        RegistrationDto registrationDto = registrationFormDto.getRegistrationDto();
        Patient patient=null;
        if(StringUtils.isBlank(patientDto.getPatientId())){
            //没有患者编号，是新输入的患者，需要先入库
            patientDto.setPatientId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PROFIX_HZ));
            patientDto.setIsFinal(Constants.IS_FINAL_FALSE);
            patientDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
            patient=this.patientService.addPatient(patientDto);
        }else {
            //患者库里有，直接进行挂号
            patient= this.patientService.getPatientById(patientDto.getPatientId());
        }
        if(patient==null){
            return  AjaxResult.fail("患者库里没有此患者，请检查后提交挂号单");
        }
        //开始插入挂号
        registrationDto.setRegistrationId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PROFIX_GH));
        registrationDto.setPatientId(patient.getPatientId());
        registrationDto.setPatientName(patient.getName());
        //查科室号
        Dept dept= this.deptService.getOne(registrationDto.getDeptId());
        registrationDto.setRegistrationNumber( (dept.getRegNumber()+1) );
        registrationDto.setRegistrationStatus(Constants.REG_STATUS_0);
        registrationDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        registrationDto.setVisitDate(registrationDto.getVisitDate().substring(0,10));
//        System.out.println("=========getVisitDate"+registrationDto.getVisitDate());
        this.registrationService.addRegistration(registrationDto);
        //更新科室号数
        this.deptService.updateDeptbyDeptId(registrationDto.getDeptId(),registrationDto.getRegistrationNumber());
        return AjaxResult.success("",registrationDto.getRegistrationId());
    }

    /**
     * 挂号收费（现金方式）
     * 直接更改挂号状态为待就诊，
     * 挂号状态必须是0才能收费
     * @param regId
     * @return
     */
    @PostMapping("collectFee/{regId}")
    @ApiOperation(value = "挂号现金收费",notes = "挂号现金收费")
    @HystrixCommand
    @Log(title = "挂号现金收费",businessType = BusinessType.UPDATE)
    public AjaxResult collectFee(@PathVariable String regId){
        Registration registration= this.registrationService.selectRgisionById(regId);
        if(null==registration){
            return AjaxResult.fail("当前查询挂号单【"+regId+"】不存在，请重新核对提交");
        }
        if(!registration.getRegistrationStatus().equals(Constants.REG_STATUS_0)){
            return AjaxResult.fail("当前挂号单【"+regId+"】已缴费，不能再次进行收费");
        }
        registration.setRegistrationStatus(Constants.REG_STATUS_1);
        return AjaxResult.toAjax(this.registrationService.updateRegisByRigId(registration));
    }

    /**
     * 分页加载挂号列表【默认当天的】
     * 如果查询的挂号列表的开始时间&结束时间为空，就默认查询当天的挂号列表
     */
    @GetMapping("queryRegistrationForPage")
    @ApiOperation(value = "分页加载挂号列表",notes = "分页加载挂号列表")
    @HystrixCommand
    public AjaxResult queryRegistrationForPage(RegistrationDto registrationDto){
        if(registrationDto.getBeginTime()==null && registrationDto.getEndTime()==null && StringUtils.isBlank(registrationDto.getVisitDate()) ){
            registrationDto.setVisitDate(DateUtil.format(DateUtil.date(),"yyyy-MM-dd"));
        }
        DataGridView dataGridView= this.registrationService.selectRegForPage(registrationDto);
        return AjaxResult.success("查询挂号列表成功",dataGridView.getData(),dataGridView.getTotal());
    }


    /**
     * 作废挂号单
     * 必须是未收费状态才能作废挂号单
     * @param regId
     * @return
     */
    @PostMapping("doInvalid/{regId}")
    @ApiOperation(value = "作废挂号单",notes = "作废挂号单")
    @Log(title = "作废挂号单",businessType = BusinessType.UPDATE)
    @HystrixCommand
    public AjaxResult doInvalid(@PathVariable String regId){
        Registration registration = this.registrationService.selectRgisionById(regId);
        if(null==registration){
            return AjaxResult.fail("当前查询挂号单【"+regId+"】不存在，请重新核对提交");
        }

        if(!registration.getRegistrationStatus().equals(Constants.REG_STATUS_0)){
            return AjaxResult.fail("不能进行作废,此挂号单已收费");
        }
        registration.setRegistrationStatus(Constants.REG_STATUS_5);
        return AjaxResult.toAjax(this.registrationService.updateRegisByRigId(registration));
    }


    /**
     * 退号
     * 必须是待就诊状态才能退号
     * @param regId
     * @return
     */
    @PostMapping("doReturn/{regId}")
    @ApiOperation(value = "退号",notes = "退号")
    @Log(title = "退号",businessType = BusinessType.UPDATE)
    @HystrixCommand
    public AjaxResult doReturn(@PathVariable String regId){
        Registration registration = this.registrationService.selectRgisionById(regId);
        if(null==registration){
            return AjaxResult.fail("当前查询挂号单【"+regId+"】不存在，请重新核对提交");
        }

        if(!registration.getRegistrationStatus().equals(Constants.REG_STATUS_1)){
            return AjaxResult.fail("不能进行退号,此挂号单状态不是待就诊");
        }
        registration.setRegistrationStatus(Constants.REG_STATUS_4);
        return AjaxResult.toAjax(this.registrationService.updateRegisByRigId(registration));
    }






}
