package com.bjsxt.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: 尚学堂 雷哥
 * 登陆用户的数据传输对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleUser implements Serializable {
    private Serializable userId;
    private String userName;
}
