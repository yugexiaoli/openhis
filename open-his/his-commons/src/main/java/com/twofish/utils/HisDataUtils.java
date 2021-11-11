package com.twofish.utils;

import cn.hutool.core.date.DateUtil;
import com.twofish.constants.Constants;

/**
 * @author ：ChenChangYu
 * @date ：Created in 2021/10/28 17:05
 * @description：
 */
public class HisDataUtils {

    /**
     * 获取当前时间类型
     * @return 上午1  下午2  晚上3
     */
    public static String getCurrentTimeType(){
        String subsectionType="1";
        int hours = DateUtil.date().getHours();
        if (hours < 6) {
            subsectionType= Constants.subsection_type_evening;
        } else if (hours >= 6 && hours < 12) {
            subsectionType= Constants.subsection_type_morning;
        } else if (hours >= 12 && hours < 18) {
            subsectionType= Constants.subsection_type_afternoon;
        } else if (hours >= 18 && hours < 24) {
            subsectionType= Constants.subsection_type_evening;
        }
        return subsectionType;
    }
}
