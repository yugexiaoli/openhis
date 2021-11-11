package com.twofish.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.twofish.domain.Scheduling;
import com.twofish.dto.SchedulingFormDto;
import com.twofish.dto.SchedulingQueryDto;
import com.twofish.dto.TableDateItem;
import com.twofish.mapper.SchedulingMapper;
import com.twofish.service.SchedulingService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SchedulingServiceImpl  implements SchedulingService{

    @Resource
    private SchedulingMapper schedulingMapper;

    @Override
    public List<Scheduling> queryForScheduling(SchedulingQueryDto schedulingQueryDto) {
        QueryWrapper<Scheduling> qw=new QueryWrapper<>();
        qw.eq(null!=schedulingQueryDto.getDeptId(),Scheduling.COL_DEPT_ID,schedulingQueryDto.getDeptId());
        qw.eq(null!=schedulingQueryDto.getUserId(),Scheduling.COL_USER_ID,schedulingQueryDto.getUserId());
        qw.ge(StringUtils.isNotBlank(schedulingQueryDto.getBeginDate()),Scheduling.COL_SCHEDULING_DAY,schedulingQueryDto.getBeginDate());
        qw.le(StringUtils.isNotBlank(schedulingQueryDto.getEndDate()),Scheduling.COL_SCHEDULING_DAY,schedulingQueryDto.getEndDate());
        qw.orderByAsc(Scheduling.COL_SCHEDULING_DAY);
        return this.schedulingMapper.selectList(qw);


    }

    @Override
    public int saveScheduling(SchedulingFormDto formDto) {
        if(null!=formDto.getData() && formDto.getData().size()>0){
            //先删除这个医生这周的排班
            TableDateItem item = formDto.getData().get(0);
            Long userId = item.getUserId();
            Long deptId = item.getDeptId();
            String startDay = formDto.getStartDate();
            DateTime startTime = DateUtil.parse(startDay, "yyyy-MM-dd");
            String endDay = DateUtil.format(DateUtil.endOfWeek(startTime), "yyyy-MM-dd");
            if(null!=userId){
                QueryWrapper<Scheduling> qw=new QueryWrapper<>();
                qw.eq(Scheduling.COL_USER_ID,userId);
                qw.eq(Scheduling.COL_DEPT_ID,deptId);
                qw.between(Scheduling.COL_SCHEDULING_DAY,startDay,endDay);
                this.schedulingMapper.delete(qw);
                //再插入这一周排班
                //一周的日期
                List<String> schedulingdays=initdays(startTime);
                for (TableDateItem datum : formDto.getData()) {
                    Scheduling scheduling=null;
                    int i=0;
                    for (String s : datum.getSchedulingType()) {
                        if(StringUtils.isNotBlank(s)){
                            scheduling=new Scheduling(userId.intValue(),deptId.intValue(),schedulingdays.get(i),datum.getSubsectionType(),s,DateUtil.date(),formDto.getSimpleUser().getUserName());
                            this.schedulingMapper.insert(scheduling);
                        }
                        i++;
                    }
                }
                return 1;
            }else {
                return 0;
            }
        }
        return 0;
    }

    ////根据条件查询排班表，查出部门id集合
    @Override
    public List<Long> queryHasSchedulingForDeptId(Long deptId, String schedulingDay, String schedulingType, String subsectionType) {
        return this.schedulingMapper.queryHasSchedulingForDeptId(deptId,schedulingDay,schedulingType,subsectionType);
    }

    //根据周一时间获取这一周的日期，方便插入数据库schedulingday字段
    private List<String> initdays(DateTime startTime) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i <7 ; i++) {
            String format = DateUtil.format(DateUtil.offsetDay(startTime, i),"yyyy-MM-dd");
            list.add(format);
        }
        return list;
    }


}
