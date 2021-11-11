package com.twofish.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.twofish.vo.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author ：ChenChangYu
 * @date ：Created in 2021/10/20 20:31
 * @description：
 */
@ApiModel(value="com-twofish-dto-RegistrationDto")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto extends BaseDto {

    /**
     * 挂号流水
     */
    @NotBlank(message = "挂号流水号不能为空")
    @ApiModelProperty(value="挂号流水")
    private String registrationId;

    /**
     * 患者ID
     */
    @NotBlank(message = "患者ID不能为空")
    @ApiModelProperty(value="患者ID")
    private String patientId;

    /**
     * 患者姓名
     */
    @NotBlank(message = "患者姓名不能为空")
    @ApiModelProperty(value="患者姓名")
    private String patientName;

    /**
     * 接诊医生ID
     */
    @ApiModelProperty(value="接诊医生ID")
    private Long userId;

    /**
     * 接诊医生姓名
     */
    @ApiModelProperty(value="接诊医生姓名")
    private String doctorName;



    /**
     * 科室ID
     */
    @ApiModelProperty(value="科室ID")
    @NotNull(message = "科室不能为空")
    private Long deptId;

    /**
     * 挂号项目ID
     */

    @ApiModelProperty(value="挂号项目ID")
    @NotNull(message = "挂号项目不能为空")
    @JsonProperty("regItemId")
    private Long registrationItemId;

    /**
     * 挂号总金额
     */
    @ApiModelProperty(value="挂号总金额")
    @NotNull(message = "挂号费不能为空")
    @JsonProperty("regItemAmount")
    private BigDecimal registrationAmount;

    /**
     * 挂号编号
     */
    @NotNull(message = "挂号编号不能为空")
    @ApiModelProperty(value="挂号编号")
    private Integer registrationNumber;

    /**
     * 挂号状态0未收费,1待就诊，2,就诊中，3，就诊完成，4，已退号，5 作废
     */
    @NotBlank(message = "挂号状态不能为空")
    @ApiModelProperty(value="挂号状态0未收费,1待就诊，2,就诊中，3，就诊完成，4，已退号，5 作废")
    private String registrationStatus;

    /**
     * 就诊日期
     */
    @ApiModelProperty(value="就诊日期")
    @NotBlank(message = "就诊日期不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String visitDate;

    /**
     * 挂号类型1 门诊 2 急诊 字典表数据翻译
     */
    @ApiModelProperty(value="挂号类型1 门诊 2 急诊 字典表数据翻译")
    @NotBlank(message = "挂号类型不能为空")
    private String schedulingType;

    /**
     * 挂号时段1上午  2下午 3晚上 字典表数据翻译
     */
    @ApiModelProperty(value="挂号时段1上午  2下午 3晚上 字典表数据翻译")
    @NotBlank(message = "挂号时段不能为空")
    private String subsectionType;

}
