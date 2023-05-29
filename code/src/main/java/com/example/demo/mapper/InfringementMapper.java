package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Infringement;
import com.example.demo.entity.TextRestrictions;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hui
 * @date 2022/8/19 10:17
 */
@Mapper
@Repository
public interface InfringementMapper extends BaseMapper<Infringement> {
    @Select("select * from infringement")
    List<Infringement> getList();

    @Insert("insert into infringement(text,replace_text) values(#{text},#{replaceText})")
    boolean insert(String text,String replaceText);

    @Select("select * from infringement where product like '%'+#{this_column}+'%'")
    List<Infringement> queryList(String this_column);

    @Update("update infringement set text = #{text},replace_text = #{replaceText} where id= #{id}")
    boolean update(String text,String replaceText,int id);
}
