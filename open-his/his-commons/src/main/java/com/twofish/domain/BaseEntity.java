package com.twofish.domain;


import java.io.Serializable;

/**
 * 所有实体类的基类:因为rpc远程调用需要实体类实现序列化接口，所以编写一个基类来实现，以后每个实体类都继承这个类就能实现序列化
 * @author ccy
 *
 */
public class BaseEntity implements Serializable {
    private static final long serialVersionUID=1L;
}
