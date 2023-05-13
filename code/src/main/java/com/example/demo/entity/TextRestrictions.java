package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("textRestrictions")
public class TextRestrictions {
    /**
     * id自增列
     */
    @TableId(value = "id" , type = IdType.AUTO)
    private Integer id;
    /**
     * 列标
     */
    private String columntext;
    /**
     * 数量规定
     */
    private String num;
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
