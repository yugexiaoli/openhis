package com.bjsxt.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bjsxt.domain.Scheduling;
import com.bjsxt.dto.SchedulingFormDto;
import com.bjsxt.dto.SchedulingQueryDto;
import com.bjsxt.mapper.SchedulingMapper;
import com.bjsxt.service.SchedulingService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
* @Author: 尚学堂 雷哥
*/

@Service
public class SchedulingServiceImpl implements SchedulingService{

    @Autowired
    private SchedulingMapper schedulingMapper;


    @Override
    public List<Scheduling> queryScheduling(SchedulingQueryDto schedulingQueryDto) {
        QueryWrapper<Scheduling> qw=new QueryWrapper<>();
        qw.eq(null!=schedulingQueryDto.getUserId(),Scheduling.COL_USER_ID,schedulingQueryDto.getUserId());
        qw.eq(null!=schedulingQueryDto.getDeptId(),Scheduling.COL_DEPT_ID,schedulingQueryDto.getDeptId());
        qw.ge(StringUtils.isNotBlank(schedulingQueryDto.getBeginDate()),Scheduling.COL_SCHEDULING_DAY,schedulingQueryDto.getBeginDate());
        qw.le(StringUtils.isNotBlank(schedulingQueryDto.getEndDate()),Scheduling.COL_SCHEDULING_DAY,schedulingQueryDto.getEndDate());
        return this.schedulingMapper.selectList(qw);
    }

    /**
     * 保存排班数据
     * @param schedulingFormDto
     * @return
     */
    @Override
    public int saveScheduling(SchedulingFormDto schedulingFormDto) {
        //保存的条件
        if(null!=schedulingFormDto.getData()&&schedulingFormDto.getData().size()>0){
            DateTime dateTime = DateUtil.parse(schedulingFormDto.getBeginDate(), "yyyy-MM-dd");
            //得到dateTime所在周的周一
            DateTime b = DateUtil.beginOfWeek(dateTime);
            DateTime e = DateUtil.endOfWeek(dateTime);
            String beginDate=DateUtil.format(b,"yyyy-MM-dd");
            String endDate = DateUtil.format(e, "yyyy-MM-dd");
            //得到用户名和科室
            SchedulingFormDto.SchedulingData schedulingData = schedulingFormDto.getData().get(0);
            Long userId = schedulingData.getUserId();
            Long deptId = schedulingData.getDeptId();
            if(null!=userId){
                //删除原来这个用户的当前周的所有排班数据
                QueryWrapper<Scheduling> qw=new QueryWrapper<>();
                qw.eq(Scheduling.COL_USER_ID,userId);
                qw.eq(Scheduling.COL_DEPT_ID,deptId);
                qw.between(Scheduling.COL_SCHEDULING_DAY,beginDate,endDate);
                this.schedulingMapper.delete(qw);
                //再进行保存添加新排班
//                初始化当前周 周一到周日的日期数据
                List<String> schedulingDays=initSchedulingDay(b);
                for (SchedulingFormDto.SchedulingData datump : schedulingFormDto.getData()) {
                    Scheduling scheduling=null;
                    int i=0;//记录循环次数取日期值
                    for (String s : datump.getSchedulingType()) {
                        if(StringUtils.isNotBlank(s)){
                            scheduling=new Scheduling(userId,deptId,schedulingDays.get(i),datump.getSubsectionType(),s,DateUtil.date(),schedulingFormDto.getSimpleUser().getUserName());
                            //保存
                            this.schedulingMapper.insert(scheduling);
                        }
                        i++;
                    }
                }
                return 1;//保存成功
            }else{
                return 0;
            }
        }
        return 0;
    }



    /**
     *根据一周的开始时间根据周一到周日的日期数组
     * @param date
     * @return
     */
    private List<String> initSchedulingDay(DateTime date) {
        List<String> list=new ArrayList<>();
        for (int i = 0; i < 7 ; i++) {
            DateTime dateTime = DateUtil.offsetDay(date, i);
            list.add(DateUtil.format(dateTime,"yyyy-MM-dd"));
        }
        return list;
    }

    /**
     * 根据条件查询有号的部门编号
     * @param deptId
     * @param schedulingDay
     * @param schedulingType
     * @param subsectionType
     * @return
     */
    @Override
    public List<Long> queryHasSchedulingDeptIds(Long deptId, String schedulingDay, String schedulingType, String subsectionType) {
        return this.schedulingMapper.queryHasSchedulingDeptIds(deptId,schedulingDay,schedulingType,subsectionType);
    }
}
