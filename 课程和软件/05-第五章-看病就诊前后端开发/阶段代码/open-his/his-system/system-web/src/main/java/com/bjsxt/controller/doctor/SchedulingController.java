package com.bjsxt.controller.doctor;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.bjsxt.controller.BaseController;
import com.bjsxt.domain.Scheduling;
import com.bjsxt.domain.User;
import com.bjsxt.dto.SchedulingDto;
import com.bjsxt.dto.SchedulingFormDto;
import com.bjsxt.dto.SchedulingQueryDto;
import com.bjsxt.service.SchedulingService;
import com.bjsxt.service.UserService;
import com.bjsxt.utils.ShiroSecurityUtils;
import com.bjsxt.vo.AjaxResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Author: 尚学堂 雷哥
 */
@RestController
@RequestMapping("doctor/scheduling")
public class SchedulingController extends BaseController {


    @Reference
    private SchedulingService schedulingService;

    @Autowired
    private UserService userService;

    /**
     * 查询要排班的医生信息
     * 如果部门ID为空，那么查询所有要排班的用户信息
     */
    @GetMapping("queryUsersNeedScheduling")
    public AjaxResult queryUsersNeedScheduling(Long deptId) {
        List<User> users = this.userService.queryUsersNeedScheduling(null, deptId);
        return AjaxResult.success(users);
    }

    /**
     * 查询要排班的医生的排班信息
     */
    @GetMapping("queryScheduling")
    @HystrixCommand
    public AjaxResult queryScheduling(SchedulingQueryDto schedulingQueryDto) {
        //根据部门ID用户ID查询用户信息 如果用户ID和部门ID都为空，那么就查询所有要排班的用户信息
        List<User> users = userService.queryUsersNeedScheduling(schedulingQueryDto.getUserId(), schedulingQueryDto.getDeptId());
        return getSchedulingAjaxResult(schedulingQueryDto, users);
    }

    /**
     * 查询当前登陆用户的的排班信息
     */
    @GetMapping("queryMyScheduling")
    @HystrixCommand
    public AjaxResult queryMyScheduling(SchedulingQueryDto schedulingQueryDto) {
        //根据部门ID用户ID查询用户信息 如果用户ID和部门ID都为空，那么就查询所有要排班的用户信息
        List<User> users = userService.queryUsersNeedScheduling(Long.valueOf(ShiroSecurityUtils.getCurrentSimpleUser().getUserId().toString()), schedulingQueryDto.getDeptId());
        return getSchedulingAjaxResult(schedulingQueryDto, users);
    }

    /**
     * 核心的构造页面数据的方法
     *
     * @param schedulingQueryDto
     * @param users
     * @return
     */
    private AjaxResult getSchedulingAjaxResult(SchedulingQueryDto schedulingQueryDto, List<User> users) {
        //得到当前时间
        Date date = DateUtil.date();
        if (StringUtils.isNotBlank(schedulingQueryDto.getQueryDate())) {
            date = DateUtil.parse(schedulingQueryDto.getQueryDate(), "yyyy-MM-dd");
            //根据传过来的日期判断是周几
            int i = DateUtil.dayOfWeek(date);//1表示周日，2表示周一
            if (i == 1) {
                date = DateUtil.offsetDay(date, 1);//下一周的周一的日期
            } else {
                date = DateUtil.offsetDay(date, -1);//上一周的周天的日期
            }
        }
        //计算一周的开始日期和结束日期
        DateTime beginTime = DateUtil.beginOfWeek(date);
        DateTime endTime = DateUtil.endOfWeek(date);
        //设置开始日期和结束日期到schedulingQueryDto
        schedulingQueryDto.setBeginDate(DateUtil.format(beginTime, "yyyy-MM-dd"));
        schedulingQueryDto.setEndDate(DateUtil.format(endTime, "yyyy-MM-dd"));
        //根据开始时间和结束时间去查询数据库里面存在的数据
        List<Scheduling> list = this.schedulingService.queryScheduling(schedulingQueryDto);
        //存放页面需要的数据的对象
        List<SchedulingDto> schedulingDtos = new ArrayList<>();
        //根据用户来循环
        for (User user : users) {
            SchedulingDto schedulingDto1 = new SchedulingDto(user.getUserId(), user.getDeptId(), "1", initMap(beginTime));
            SchedulingDto schedulingDto2 = new SchedulingDto(user.getUserId(), user.getDeptId(), "2", initMap(beginTime));
            SchedulingDto schedulingDto3 = new SchedulingDto(user.getUserId(), user.getDeptId(), "3", initMap(beginTime));
            //一个用户的循环放三条数据
            schedulingDtos.add(schedulingDto1);
            schedulingDtos.add(schedulingDto2);
            schedulingDtos.add(schedulingDto3);
            for (Scheduling scheduling : list) {
                Long userId = scheduling.getUserId();//获取数据库的用户ID
                String subsectionType = scheduling.getSubsectionType();//得到早中晚的类型
                String schedulingDay = scheduling.getSchedulingDay();//值班日期
                if (user.getUserId().equals(userId)) {
                    switch (subsectionType) {
                        case "1":
                            Map<String, String> record1 = schedulingDto1.getRecord();
                            record1.put(schedulingDay, scheduling.getSchedulingType());
                            break;
                        case "2":
                            Map<String, String> record2 = schedulingDto2.getRecord();
                            record2.put(schedulingDay, scheduling.getSchedulingType());
                            break;
                        case "3":
                            Map<String, String> record3 = schedulingDto3.getRecord();
                            record3.put(schedulingDay, scheduling.getSchedulingType());
                            break;
                    }
                }

            }
            //把map转成数组数据放到schedulingType
            schedulingDto1.setSchedulingType(schedulingDto1.getRecord().values());
            schedulingDto2.setSchedulingType(schedulingDto2.getRecord().values());
            schedulingDto3.setSchedulingType(schedulingDto3.getRecord().values());
        }
        //组装返回的对象
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("tableData", schedulingDtos);

        Map<String, Object> schedulingData = new HashMap<>();
        schedulingData.put("startTimeThisWeek", schedulingQueryDto.getBeginDate());
        schedulingData.put("endTimeThisWeek", schedulingQueryDto.getEndDate());
        resMap.put("schedulingData", schedulingData);
        resMap.put("labelNames", initLabel(beginTime));
        return AjaxResult.success(resMap);
    }

    /**
     * 初始化labelNames
     *
     * @param beginTime
     * @return
     */
    private String[] initLabel(DateTime beginTime) {
        String[] labelNames = new String[7];
        for (int i = 0; i < 7; i++) {
            DateTime d = DateUtil.offsetDay(beginTime, i);
            labelNames[i] = DateUtil.format(d, "yyyy-MM-dd") + formatterWeek(i);
        }
        return labelNames;
    }

    /**
     * 翻译周
     *
     * @param i
     * @return
     */
    private String formatterWeek(int i) {
        switch (i) {
            case 0:
                return "(周一)";
            case 1:
                return "(周二)";
            case 2:
                return "(周三)";
            case 3:
                return "(周四)";
            case 4:
                return "(周五)";
            case 5:
                return "(周六)";
            default:
                return "(周日)";
        }
    }

    /**
     * 初始化值班记录
     *
     * @param beginTime 当周的第一天
     * @return
     */
    private Map<String, String> initMap(DateTime beginTime) {
        Map<String, String> map = new TreeMap<>();//按顺序放 2020-08-03 -  2020-08-09
        for (int i = 0; i < 7; i++) {
            DateTime d = DateUtil.offsetDay(beginTime, i);
            String key = DateUtil.format(d, "yyyy-MM-dd");
            map.put(key, "");
        }
        return map;
    }


    /**
     * 保存排班数据
     */
    @PostMapping("saveScheduling")
//    @HystrixCommand
    public AjaxResult saveScheduling(@RequestBody SchedulingFormDto schedulingFormDto){
        schedulingFormDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.schedulingService.saveScheduling(schedulingFormDto));
    }



}
