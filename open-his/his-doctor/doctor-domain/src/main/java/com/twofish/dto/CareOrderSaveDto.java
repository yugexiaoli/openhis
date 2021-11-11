package com.twofish.dto;

import com.twofish.domain.CareOrder;
import com.twofish.domain.CareOrderItem;
import com.twofish.vo.BaseDto;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ：ChenChangYu
 * @date ：Created in 2021/11/10 22:47
 * @description：
 */
@ApiModel(value="com-twofish-dto-CareOrderSaveDto")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class CareOrderSaveDto extends BaseDto {
    private CareOrder careOrder;
    private List<CareOrderItem> careOrderItems;
}
