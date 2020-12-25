package com.twofish.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 添加采购表单dto对象
 *封装了采购单dto和单据详情dto对象
 */
@ApiModel(value="com-twofish-dto-PurchaseFormDto")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseFormDto implements Serializable {

    private PurchaseDto purchaseDto;

    private List<PurchaseItemDto> purchaseItemDtos;
}
