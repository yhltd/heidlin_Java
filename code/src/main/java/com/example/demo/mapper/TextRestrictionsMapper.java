package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.TextRestrictions;
import com.example.demo.entity.UserInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hui
 * @date 2022/8/19 10:17
 */
@Mapper
@Repository
public interface TextRestrictionsMapper extends BaseMapper<TextRestrictions> {
    @Select("select * from textRestrictions where founder = '管理员'")
    List<TextRestrictions> getList();

    @Select("select * from textRestrictions where founder = #{founder}")
    List<TextRestrictions> getListByUser(String founder);

    @Insert("insert into textRestrictions(columntext,num,founder,text_id,product) values(#{this_column},#{num},#{founder},#{textId},#{product})")
    boolean insertById(String this_column,String num,String founder,int textId,String product);

    @Insert("insert into textRestrictions(columntext,num,founder,product) output inserted.id values(#{this_column},#{num},#{founder},#{product})")
    boolean insert(String this_column,String num,String founder,String product);

    @Select("select * from textRestrictions where product like '%'+#{this_column}+'%' and founder = '管理员'")
    List<TextRestrictions> queryList(String this_column);

    @Select("select * from textRestrictions where product like '%'+#{this_column}+'%' and founder = #{founder}")
    List<TextRestrictions> queryListByUser(String this_column,String founder);

    @Select("SELECT max(id) as id from textRestrictions")
    List<TextRestrictions> getMaxId();

    @Select("SELECT product from textRestrictions where founder = #{founder} and product = #{product}")
    List<TextRestrictions> getName(String founder,String product);

    @Update("update textRestrictions set columntext = #{columntext},num = #{num},product = #{product} where id= #{id}")
    boolean update(String columntext,String num,int id,String product);

    @Update("update textRestrictions set columntext = #{columntext},num = #{num},product = #{product} where text_id = #{id}")
    boolean updateById(String columntext,String num,int id,String product);

    @Delete("delete from textRestrictions where text_id=#{id}")
    boolean deleteid(int id);
}
