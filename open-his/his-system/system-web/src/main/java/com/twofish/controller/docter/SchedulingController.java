package com.twofish.controller.docter;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.twofish.aspectj.annotation.Log;
import com.twofish.aspectj.enums.BusinessType;
import com.twofish.controller.BaseController;
import com.twofish.domain.Scheduling;
import com.twofish.domain.User;
import com.twofish.dto.SchedulingFormDto;
import com.twofish.dto.SchedulingQueryDto;
import com.twofish.dto.TableDateItem;
import com.twofish.service.SchedulingService;
import com.twofish.service.UserService;
import com.twofish.utils.ShiroSecurityUtils;
import com.twofish.vo.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author ：ChenChangYu
 * @date ：Created in 2021/1/20 11:52
 * @description：
 */
@RestController
@RequestMapping("/doctor/scheduling/")
@Log4j2
@Api(value = "排班接口", tags = "排班接口")
public class SchedulingController extends BaseController {

    @Resource
    private UserService userService;

    @Reference
    private SchedulingService schedulingService;

    /**
     * 查询要排版的医生信息
     *
     * @param userId
     * @param deptId
     * @return
     */
    @GetMapping("queryUsersNeedScheduling")
    @ApiOperation(value = "查询要排版的医生信息", notes = "查询要排版的医生信息")
    public AjaxResult queryUsersNeedScheduling(Long userId, Long deptId) {
        List<User> users = this.userService.queryUsersNeedScheduling(userId, deptId);
        return AjaxResult.success("查询成功", users);
    }

    /**
     * 查询要排班的医生的排班信息
     *
     * @param schedulingQueryDto
     * @return
     */
    @GetMapping("queryScheduling")
    @ApiOperation(value = "查询要排班的医生的排班信息", notes = "查询要排班的医生的排班信息")
    @HystrixCommand
    public AjaxResult queryScheduling(SchedulingQueryDto schedulingQueryDto) {
        //先查询出需要排班的所有医生
        List<User> users = this.userService.queryUsersNeedScheduling(schedulingQueryDto.getUserId(), schedulingQueryDto.getDeptId());
        //再根据需要排班用户和排班周queryDate条件 查询排班信息
        return queryForScheduling(schedulingQueryDto, users);
    }

    /**
     * 查询当前登录的医生的排班信息
     *
     * @param schedulingQueryDto
     * @return
     */
    @GetMapping("queryMyScheduling")
    @ApiOperation(value = "查询当前登录的医生的排班信息", notes = "查询当前登录的医生的排班信息")
    @HystrixCommand
    public AjaxResult queryMyScheduling(SchedulingQueryDto schedulingQueryDto) {
        //先查询出需要排班的所有医生
        List<User> users = this.userService.queryUsersNeedScheduling(ShiroSecurityUtils.getCurrentUser().getUserId(), schedulingQueryDto.getDeptId());
        //再根据需要排班用户和排班周queryDate条件 查询排班信息
        return queryForScheduling(schedulingQueryDto, users);
    }



    /**
     * 根据queryDate第几周去查询排班核心业务
     *
     * @param schedulingQueryDto
     * @param users
     * @return
     */
    private AjaxResult queryForScheduling(SchedulingQueryDto schedulingQueryDto, List<User> users) {
        Date date = new Date();//当前时间 （当前周）
        if (StringUtils.isNotBlank(schedulingQueryDto.getQueryDate())) {//传入的是上一周或下一周，需要将queryDate计算成周一周日日期
            date = DateUtil.parse(schedulingQueryDto.getQueryDate(), "yyyy-MM-dd");
            int i = DateUtil.dayOfWeek(date);//1表示周日，2表示周一
            if (i == 1) {
                //下一周 +1天为下一周的周一
                date = DateUtil.offsetDay(date, 1);
            } else {
                //上一周 -1天为上一周的周日
                date = DateUtil.offsetDay(date, -1);
            }
        }
        //获取选中周时间的周一beginDate和周日日期endDate
        schedulingQueryDto.setBeginDate(DateUtil.format(DateUtil.beginOfWeek(date), "yyyy-MM-dd"));
        schedulingQueryDto.setEndDate(DateUtil.format(DateUtil.endOfWeek(date), "yyyy-MM-dd"));
        List<Scheduling> list = this.schedulingService.queryForScheduling(schedulingQueryDto);
        //再组装数据返回给前端
        //先封装表格的数据=> tableData
        List<TableDateItem> tableData = new ArrayList<>();
        String beginDate = schedulingQueryDto.getBeginDate();
        for (User user : users) {
            //每个用户封装三次 TableDateItem，分别为上午下午晚上的班
            TableDateItem tableDateItem1 = new TableDateItem(user.getUserId(), user.getDeptId(), "1", initMap(beginDate));
            TableDateItem tableDateItem2 = new TableDateItem(user.getUserId(), user.getDeptId(), "2", initMap(beginDate));
            TableDateItem tableDateItem3 = new TableDateItem(user.getUserId(), user.getDeptId(), "3", initMap(beginDate));
            //装载records
            for (Scheduling scheduling : list) {
                Integer userId = scheduling.getUserId();
                String subsectionType = scheduling.getSubsectionType();
                String schedulingDay = scheduling.getSchedulingDay();
                if (user.getUserId().intValue()==userId) {
                    switch (subsectionType) {
                        case "1":
                            tableDateItem1.getRecords().put(schedulingDay, scheduling.getSchedulingType());
                            break;
                        case "2":
                            tableDateItem2.getRecords().put(schedulingDay, scheduling.getSchedulingType());
                            break;
                        case "3":
                            tableDateItem3.getRecords().put(schedulingDay, scheduling.getSchedulingType());
                            break;
                    }
                }
            }
            //装载records到schedulingType
            tableDateItem1.setSchedulingType(tableDateItem1.getRecords().values());
            tableDateItem2.setSchedulingType(tableDateItem2.getRecords().values());
            tableDateItem3.setSchedulingType(tableDateItem3.getRecords().values());
            tableData.add(tableDateItem1);
            tableData.add(tableDateItem2);
            tableData.add(tableDateItem3);
        }
        //封装 schedulingDate
        Map<String, String> schedulingDate = new HashMap<>();
        schedulingDate.put("startDatethisweek", schedulingQueryDto.getBeginDate());
        schedulingDate.put("endDatethisweek", schedulingQueryDto.getEndDate());
        //封装 一周时间 labelNames
        String[] labelNames = new String[7];
        for (int i = 0; i < 7; i++) {
            DateTime d = DateUtil.parse(schedulingQueryDto.getBeginDate(), "yyyy-MM-dd");
            labelNames[i] = DateUtil.format(DateUtil.offsetDay(d, i), "yyyy-MM-dd") + formatterweek(i);
        }
        Map<String,Object> res=new HashMap<>();
        res.put("labelNames",labelNames);
        res.put("schedulingDate",schedulingDate);
        res.put("tableData",tableData);
        return AjaxResult.success("查询成功",res);
    }

    //判断周几的方法
    private String formatterweek(int i) {
        String label = "(周一)";
        switch (i) {
            case 0:
                label = "(周一)";
                break;
            case 1:
                label = "(周二)";
                break;
            case 2:
                label = "(周三)";
                break;
            case 3:
                label = "(周四)";
                break;
            case 4:
                label = "(周五)";
                break;
            case 5:
                label = "(周六)";
                break;
            default:
                label = "(周日)";
                break;
        }
        return label;
    }

    //初始化封装排班的map
    private TreeMap<String, String> initMap(String beginDate) {
        DateTime dateTime = DateUtil.parse(beginDate, "yyyy-MM-dd");
        TreeMap<String, String> records = new TreeMap<>();
        for (int i = 0; i < 7; i++) {
            records.put((DateUtil.format(DateUtil.offsetDay(dateTime, i), "yyyy-MM-dd")), "");
        }
        return records;
    }

    /**
     * 修改排班信息
     *先删后插入
     */
    @PostMapping("saveScheduling")
    @Log(title = "修改排班信息",businessType = BusinessType.UPDATE)
    @ApiOperation(value = "修改排班信息",notes = "修改排班信息")
    @HystrixCommand
    public AjaxResult saveScheduling(@RequestBody SchedulingFormDto formDto){
        formDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.schedulingService.saveScheduling(formDto));
    }




}
