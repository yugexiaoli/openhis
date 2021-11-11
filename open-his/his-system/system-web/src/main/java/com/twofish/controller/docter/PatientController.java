package com.twofish.controller.docter;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.twofish.controller.BaseController;
import com.twofish.domain.Patient;
import com.twofish.domain.PatientFile;
import com.twofish.dto.PatientDto;
import com.twofish.service.PatientService;
import com.twofish.vo.AjaxResult;
import com.twofish.vo.DataGridView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * @author ：ChenChangYu
 * @date ：Created in 2021/1/20 11:52
 * @description：患者
 */
@RestController
@RequestMapping("/doctor/patient/")
@Log4j2
@Api(value = "患者接口", tags = "患者接口")
public class PatientController extends BaseController {

    @Reference
    private PatientService patientService;

    /**
     * 分页查询所有患者信息
     */
    @GetMapping("listPatientForPage")
    @ApiOperation(value = "分页查询所有患者信息",notes = "分页查询所有患者信息")
    @HystrixCommand
    public AjaxResult listPatientForPage(PatientDto patientDto){
        DataGridView dataGridView=this.patientService.listPatientForPage(patientDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 根据患者ID查询患者信息
     */
    @GetMapping("getPatientById/{patientId}")
    @ApiOperation(value = "根据患者ID查询患者信息",notes = "根据患者ID查询患者信息")
    @HystrixCommand
    public AjaxResult getPatientById(@PathVariable @Validated @NotNull(message = "患者id为空") String patientId){
        Patient patient=this.patientService.getPatientById(patientId);
        return AjaxResult.success(patient);
    }

    /**
     * 根据患者ID查询患者的档案信息
     */
    @GetMapping("getPatientFileById/{patientId}")
    @ApiOperation(value = "根据患者ID查询患者的档案信息",notes = "根据患者ID查询患者的档案信息")
    @HystrixCommand
    public AjaxResult getPatientFileById(@PathVariable @Validated @NotNull(message = "患者id为空") String patientId){
        PatientFile patientFile=this.patientService.getPatientFileById(patientId);
        return AjaxResult.success(patientFile);
    }

    /**
     * 根据患者ID查询患者所有信息【基础，档案，病例】 未完成
     */
    @GetMapping("getPatientAllMessageByPatientId/{patientId}")
    @ApiOperation(value = "根据患者ID查询患者所有信息",notes = "根据患者ID查询患者所有信息")
    @HystrixCommand
    public AjaxResult getPatientAllMessageByPatientId(@PathVariable @Validated @NotNull(message = "患者id为空") String patientId){
        return AjaxResult.success(null);
    }


}
