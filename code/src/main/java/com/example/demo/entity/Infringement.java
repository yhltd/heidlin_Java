package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("infringement")
public class Infringement {
    /**
     * id自增列
     */
    @TableId(value = "id" , type = IdType.AUTO)
    private Integer id;
    /**
     * 产品名称
     */
    private String product;
    /**
     * 侵权词
     */
    private String text;
    /**
     * 替换侵权词
     */
    private String replaceText;

}
