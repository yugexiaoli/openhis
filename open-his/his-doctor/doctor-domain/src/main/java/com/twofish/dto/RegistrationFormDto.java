package com.twofish.dto;

import com.twofish.vo.BaseDto;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author ：ChenChangYu
 * @date ：Created in 2021/10/20 20:28
 * @description：挂号使用的参数
 */
@ApiModel(value="com-twofish-dto-RegistrationFormDto")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationFormDto extends BaseDto {

    private PatientDto patientDto;

    private RegistrationDto registrationDto;


}
