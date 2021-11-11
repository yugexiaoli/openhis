package com.twofish.controller.docter;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.twofish.aspectj.annotation.Log;
import com.twofish.aspectj.enums.BusinessType;
import com.twofish.constants.Constants;
import com.twofish.controller.BaseController;
import com.twofish.domain.*;
import com.twofish.dto.CareHistoryDto;
import com.twofish.dto.CareOrderSaveDto;
import com.twofish.service.*;
import com.twofish.utils.HisDataUtils;
import com.twofish.utils.IdGeneratorSnowflake;
import com.twofish.utils.ShiroSecurityUtils;
import com.twofish.vo.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：ChenChangYu
 * @date ：Created in 2021/10/27 9:51
 * @description：看病就诊控制器
 */

@RestController
@RequestMapping("/doctor/care/")
@Log4j2
@Api(value = "就诊看病接口", tags = "就诊看病接口")
public class CareController extends BaseController {

    @Reference
    private CareHistoryService careHistoryService;
    @Reference
    private CareOrderService careOrderService;
    @Reference
    private CareOrderItemService careOrderItemService;
    @Reference
    private RegistrationService registrationService;
    @Reference
    private PatientService patientService;
    @Resource
    private DeptService deptService;


    /**
     * 查询待就诊的挂号信息
     * @param scheudlingType
     * @return
     */
    @GetMapping("queryToBeSeenRegistration/{scheudlingType}")
    @ApiOperation(value = "查询待就诊的挂号信息",notes = "查询待就诊的挂号信息")
    @HystrixCommand
    public AjaxResult queryToBeSeenRegistration(@PathVariable String scheudlingType){
        Long deptId = ShiroSecurityUtils.getCurrentUser().getDeptId();
        String registrationStatus= Constants.REG_STATUS_1;
        Long userId=null;
        String subsectionType= HisDataUtils.getCurrentTimeType();
        List<Registration> list= this.registrationService.queryRegstrationBySatatus(scheudlingType,deptId,registrationStatus,userId,subsectionType);
        return AjaxResult.success(list);
    }


    /**
     * 查询就诊中的挂号信息
     * @param scheudlingType
     * @return
     */
    @GetMapping("queryVisitingRegistration/{scheudlingType}")
    @ApiOperation(value = "查询就诊中的挂号信息",notes = "查询就诊中的挂号信息")
    @HystrixCommand
    public AjaxResult queryVisitingRegistration(@PathVariable String scheudlingType){
        Long deptId = ShiroSecurityUtils.getCurrentUser().getDeptId();
        String registrationStatus= Constants.REG_STATUS_2;
        Long userId=ShiroSecurityUtils.getCurrentUser().getUserId();
//        String subsectionType= HisDataUtils.getCurrentTimeType();
        List<Registration> list= this.registrationService.queryRegstrationBySatatus(scheudlingType,deptId,registrationStatus,userId,null);
        return AjaxResult.success(list);
    }


    /**
     * 查询就诊完成的挂号信息
     * @param scheudlingType
     * @return
     */
    @GetMapping("queryVisitCompletedRegistration/{scheudlingType}")
    @ApiOperation(value = "查询就诊完成的挂号信息",notes = "查询就诊完成的挂号信息")
    @HystrixCommand
    public AjaxResult queryVisitCompletedRegistration(@PathVariable String scheudlingType){
        Long deptId = ShiroSecurityUtils.getCurrentUser().getDeptId();
        String registrationStatus= Constants.REG_STATUS_3;
        Long userId=ShiroSecurityUtils.getCurrentUser().getUserId();
//        String subsectionType= HisDataUtils.getCurrentTimeType();
        List<Registration> list= this.registrationService.queryRegstrationBySatatus(scheudlingType,deptId,registrationStatus,userId,null);
        return AjaxResult.success(list);
    }


    /**
     * 接诊，挂号单状态必须是待就诊
     * @param regId
     * @return
     */
    @PostMapping("receivePatient/{regId}")
    @ApiOperation(value = "接诊",notes = "接诊")
    @Log(title = "接诊",businessType = BusinessType.UPDATE)
    @HystrixCommand
    public AjaxResult receivePatient(@PathVariable String regId){
        Registration registration = this.registrationService.selectRgisionById(regId);
        if(null==registration){
            return AjaxResult.fail("挂号单【"+regId+"】不存在，请核实 后提交");
        }
        if(!registration.getRegistrationStatus().equals(Constants.REG_STATUS_1)){
            return AjaxResult.fail("挂号单【"+regId+"】的状态不是待就诊，不能接诊");
        }
        registration.setRegistrationStatus(Constants.REG_STATUS_2);
        registration.setUserId(ShiroSecurityUtils.getCurrentUser().getUserId());
        registration.setDoctorName(ShiroSecurityUtils.getCurrentUser().getUserName());
        return AjaxResult.toAjax(this.registrationService.updateRegisByRigId(registration));
    }


    /**
     * 根据患者ID获取患者信息、档案信息、病历信息
     * @param patientId
     * @return
     */
    @GetMapping("getPatientAllMessageByPatientId/{patientId}")
    @ApiOperation(value = "根据患者ID获取患者信息、档案信息、病历信息",notes = "根据患者ID获取患者信息、档案信息、病历信息")
    @HystrixCommand
    public  AjaxResult getPatientAllMessageByPatientId(@PathVariable String patientId){
        Patient patient = this.patientService.getPatientById(patientId);
        PatientFile patientFile = this.patientService.getPatientFileById(patientId);
        List<CareHistory> careHistoryList= this.careHistoryService.queryCareHistoryByPatientId(patientId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("patient",patient);
        map.put("careHistoryList",careHistoryList);
        map.put("patientFile",patientFile);
        return AjaxResult.success(map);
    }


    /**
     * 保存病例：有chid就是修改病例，没有就是新增一个carehistory，前端就诊时间是string ，要parse成时间类型
     * @param careHistoryDto
     * @return
     */
    @PostMapping("saveCareHistory")
    @ApiOperation(value = "保存病例",notes = "保存病例")
    @Log(title = "保存病例",businessType = BusinessType.OTHER)
    @HystrixCommand
    public AjaxResult saveCareHistory(@RequestBody CareHistoryDto careHistoryDto) {
        CareHistory ch;
        if(StringUtils.isNotBlank(careHistoryDto.getChId())){
            //病例号不为空，做修改
            ch=this.careHistoryService.updateHistory(careHistoryDto);
        }else {
            //做新增
            careHistoryDto.setUserId(ShiroSecurityUtils.getCurrentUser().getUserId());
            careHistoryDto.setUserName(ShiroSecurityUtils.getCurrentUser().getUserName());
            Long deptId = ShiroSecurityUtils.getCurrentUser().getDeptId();
            Dept dept = this.deptService.getDeptById(deptId);
            careHistoryDto.setDeptId(deptId);
            careHistoryDto.setDeptName(dept.getDeptName());
            ch=this.careHistoryService.addHistory(careHistoryDto);
        }
        return AjaxResult.success(ch);
    }

    /**
     * 根据挂号ID查询病历信息
     * @param regId
     * @return
     */
    @GetMapping("getCareHistoryByRegId/{regId}")
    @ApiOperation(value = "根据挂号ID查询病历信息",notes = "根据挂号ID查询病历信息")
    @HystrixCommand
    public AjaxResult getCareHistoryByRegId(@PathVariable String regId){
       return AjaxResult.success( this.careHistoryService.getCareHistoryByRegId(regId));
    }


    /**
     * 根据病例ID查询处方列表及详情
     * @param chId
     * @return
     */
    @GetMapping("queryCareOrdersByChId/{chId}")
    @ApiOperation(value = "根据病例ID查询处方列表及详情",notes = "根据病例ID查询处方列表及详情")
    @HystrixCommand
    public AjaxResult queryCareOrdersByChId(@PathVariable String chId){
        List<CareOrder> careOrders=this.careOrderService.queryCoBychId(chId);
        List<Map<String, Object>> res=new ArrayList<>();
        for (CareOrder careOrder : careOrders) {
            Map<String, Object> map = new HashMap<>();
            List<CareOrderItem> careOrderItems=this.careOrderItemService.queryCoItemByCoId(careOrder.getCoId());
            map.put("careOrder",careOrder);
            map.put("careOrderItems",careOrderItems);
            res.add(map);
        }
        return AjaxResult.success(res);
    }


    @PostMapping("saveCareOrderItem")
    @Log(title = "添加处方和详情",businessType = BusinessType.INSERT)
    @ApiOperation(value = "添加处方和详情",notes = "添加处方和详情")
    @HystrixCommand
    public AjaxResult saveCareOrderItem(@RequestBody CareOrderSaveDto careOrderSaveDto ){
       try {
           CareOrder careOrderDto = careOrderSaveDto.getCareOrder();
           careOrderDto.setCreateBy(ShiroSecurityUtils.getCurrentUser().getUserName());
           careOrderDto.setCoId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PROFIX_CO_ID));
           careOrderDto.setUserId(ShiroSecurityUtils.getCurrentUser().getUserId());
           this.careOrderService.saveCareOrder(careOrderDto);
           List<CareOrderItem> careOrderItems = careOrderSaveDto.getCareOrderItems();
           for (CareOrderItem careOrderItemDto : careOrderItems) {
               careOrderItemDto.setCoId(careOrderDto.getCoId());
               this.careOrderItemService.saveCareOrderItem(careOrderItemDto);
           }
           return AjaxResult.success("添加成功");
       }catch (Exception e){
           return AjaxResult.fail("添加失败");
       }

    }

    /**
     * 根据处方详情ID删除处方详情【只能删除未支付的】
     */
    @DeleteMapping("deleteCareOrderItemById/{itemId}")
    @Log(title = "根据处方详情ID删除处方详情",businessType = BusinessType.DELETE)
    @ApiOperation(value = "根据处方详情ID删除处方详情",notes = "根据处方详情ID删除处方详情")
    @HystrixCommand
    public  AjaxResult deleteCareOrderItemById(@PathVariable String itemId){
        CareOrderItem careOrderItem=this.careOrderItemService.queryOrderItemByItemId(itemId);
        if(null==careOrderItem){
            return AjaxResult.fail("编号为：【"+itemId+"】的处方详情不存在");
        }
        if(!careOrderItem.getStatus().equals(Constants.ORDER_DETAILS_STATUS_0)){
            return AjaxResult.fail("当前处方状态不是未支付，不能删除");
        }
        return AjaxResult.toAjax(this.careOrderItemService.deleteOrderItemByItemId(itemId));
    }

    /**
     * 就诊完成
     */
    @PostMapping("visitComplete/{regId}")
    @Log(title = "就诊完成",businessType = BusinessType.UPDATE)
    @ApiOperation(value = "就诊完成",notes = "就诊完成")
    @HystrixCommand
    public AjaxResult visitComplete(@PathVariable String regId){
        Registration registration = this.registrationService.selectRgisionById(regId);
        if(null==registration){
            return AjaxResult.fail("挂号单不存在");
        }
        if(!registration.getRegistrationStatus().equals(Constants.REG_STATUS_2)){
            return AjaxResult.fail("挂号单状态不是就诊中，不能完成就诊");
        }
        if(null==this.careHistoryService.getCareHistoryByRegId(regId)){
            return AjaxResult.fail("尚未填写病例信息，请先填写信息后完成就诊");
        }
        registration.setRegistrationStatus(Constants.REG_STATUS_3);
        return AjaxResult.toAjax(this.registrationService.updateRegisByRigId(registration));
    }



}
