package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("randomText")
public class RandomText {
    /**
     * id自增列
     */
    @TableId(value = "id" , type = IdType.AUTO)
    private Integer id;
    /**
     * keyword
     */
    private String keyword;
    /**
     * longword
     */
    private String longword;
    /**
     * scenario
     */
    private String scenario;
    /**
     * user
     */
    private String user;
    /**
     * prop1
     */
    private String prop1;
    /**
     * prop2
     */
    private String prop2;
    /**
     * prop3
     */
    private String prop3;
    /**
     * prop4
     */
    private String prop4;
    /**
     * prop5
     */
    private String prop5;
    /**
     * luckword
     */
    private String luckword;
    /**
     * point1
     */
    private String point1;
    /**
     * point2
     */
    private String point2;
    /**
     * point3
     */
    private String point3;
    /**
     * point4
     */
    private String point4;
    /**
     * point5
     */
    private String point5;
    /**
     * description
     */
    private String description;
    /**
     * prop6
     */
    private String prop6;
    /**
     * prop7
     */
    private String prop7;
    /**
     * prop8
     */
    private String prop8;
    /**
     * prop9
     */
    private String prop9;
    /**
     * prop10
     */
    private String prop10;
    /**
     * prop11
     */
    private String prop11;
    /**
     * prop12
     */
    private String prop12;
    /**
     * prop13
     */
    private String prop13;
    /**
     * prop14
     */
    private String prop14;
    /**
     * prop15
     */
    private String prop15;
    /**
     * prop16
     */
    private String prop16;
    /**
     * prop17
     */
    private String prop17;
    /**
     * prop18
     */
    private String prop18;
    /**
     * prop19
     */
    private String prop19;
    /**
     * prop20
     */
    private String prop20;
    /**
     * 创建人
     */
    private String founder;
    /**
     * 匹配id
     */
    private int textId;
    /**
     * 产品名称
     */
    private String product;

}
