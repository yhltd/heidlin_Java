package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface TextRestrictionsMapper extends BaseMapper<TextRestrictions> {
    @Select("select * from textRestrictions where founder = '管理员'")
    List<TextRestrictions> getList();

    @Select("select * from textRestrictions where id = #{id}")
    List<TextRestrictions> getMuBan(int id);

    @Select("select * from textRestrictions where founder = #{founder}")
    List<TextRestrictions> getListByUser(String founder);

    @Select("select id, product, text_id from textRestrictions where founder = #{founder}")
    List<TextRestrictions> selectPcByUserId(String founder);

    @Select("select * from textRestrictions where id = #{id}")
    List<TextRestrictions> getListById(int id);

    @Insert("insert into textRestrictions(founder,text_id,columntext1,num1,columntext2,num2,columntext3,num3,columntext4,num4,columntext5,num5,columntext6,num6,columntext7,num7,columntext8,num8,columntext9,num9,columntext10,num10,keyword,longword,scenario,[user],prop1,prop2,prop3,prop4,prop5,luckword,point1,point2,point3,point4,point5,description,prop6,prop7,prop8,prop9,prop10,prop11,prop12,prop13,prop14,prop15,prop16,prop17,prop18,prop19,prop20,product,title,title_color) values(#{founder},#{textId},#{columntext1},#{num1},#{columntext2},#{num2},#{columntext3},#{num3},#{columntext4},#{num4},#{columntext5},#{num5},#{columntext6},#{num6},#{columntext7},#{num7},#{columntext8},#{num8},#{columntext9},#{num9},#{columntext10},#{num10},#{keyword},#{longword},#{scenario},#{user},#{prop1},#{prop2},#{prop3},#{prop4},#{prop5},#{luckword},#{point1},#{point2},#{point3},#{point4},#{point5},#{description},#{prop6},#{prop7},#{prop8},#{prop9},#{prop10},#{prop11},#{prop12},#{prop13},#{prop14},#{prop15},#{prop16},#{prop17},#{prop18},#{prop19},#{prop20},#{product},#{title},#{titleColor})")
    boolean insertById(String founder,int textId,String columntext1,String num1,String columntext2,String num2,String columntext3,String num3,String columntext4,String num4,String columntext5,String num5,String columntext6,String num6,String columntext7,String num7,String columntext8,String num8,String columntext9,String num9,String columntext10,String num10,String keyword,String longword,String scenario,String user,String prop1,String prop2,String prop3,String prop4,String prop5,String luckword,String point1,String point2,String point3,String point4,String point5,String description,String prop6,String prop7,String prop8,String prop9,String prop10,String prop11,String prop12,String prop13,String prop14,String prop15,String prop16,String prop17,String prop18,String prop19,String prop20,String product,String title,String titleColor);

    @Insert("insert into textRestrictions(founder,text_id) values(#{founder},#{textId})")
    boolean insertById2(String founder,int textId);

    @Insert("insert into textRestrictions(founder,text_id,product) values(#{founder},#{textId},#{product})")
    boolean insertToAcount(String founder,int textId, String product);

    @Insert("insert into textRestrictions(founder) output inserted.id values(#{founder})")
    boolean insert(String founder);

    @Select("select * from textRestrictions where product like '%'+#{this_column}+'%' and founder = '管理员'")
    List<TextRestrictions> queryList(String this_column);

    @Select("select * from textRestrictions where product like '%'+#{this_column}+'%' and founder = #{founder}")
    List<TextRestrictions> queryListByUser(String this_column,String founder);

    @Select("SELECT max(id) as id from textRestrictions")
    List<TextRestrictions> getMaxId();

    @Select("SELECT product from textRestrictions where founder = #{founder} and product = #{product}")
    List<TextRestrictions> getName(String founder,String product);

    @Update("update textRestrictions set ${column} = #{value} where id= #{id}")
    boolean update(String column,String value,int id);

    @Update("update textRestrictions set ${column} = #{value} where text_id = #{textId}")
    boolean updateById(String column,String value,int textId);

    @Update("update textRestrictions set product= #{product},title= #{title},title_color= #{titleColor},keyword= #{keyword},longword= #{longword},scenario= #{scenario},[user]= #{user},prop1= #{prop1},prop2= #{prop2},prop3= #{prop3},prop4= #{prop4},prop5= #{prop5},luckword= #{luckword},point1= #{point1},point2= #{point2},point3= #{point3},point4= #{point4},point5= #{point5},description= #{description},prop6= #{prop6},prop7= #{prop7},prop8= #{prop8},prop9= #{prop9},prop10= #{prop10},prop11= #{prop11},prop12= #{prop12},prop13= #{prop13},prop14= #{prop14},prop15= #{prop15},prop16= #{prop16},prop17= #{prop17},prop18= #{prop18},prop19= #{prop19},prop20= #{prop20} where id= #{id}")
    boolean updateMuBan(String keyword,String longword,String scenario,String user,String prop1,String prop2,String prop3,String prop4,String prop5,String luckword,String point1,String point2,String point3,String point4,String point5,String description,String prop6,String prop7,String prop8,String prop9,String prop10,String prop11,String prop12,String prop13,String prop14,String prop15,String prop16,String prop17,String prop18,String prop19,String prop20,int id,String product,String title,String titleColor);

    @Update("update textRestrictions set product= #{product},title= #{title},title_color= #{titleColor},keyword= #{keyword},longword= #{longword},scenario= #{scenario},[user]= #{user},prop1= #{prop1},prop2= #{prop2},prop3= #{prop3},prop4= #{prop4},prop5= #{prop5},luckword= #{luckword},point1= #{point1},point2= #{point2},point3= #{point3},point4= #{point4},point5= #{point5},description= #{description},prop6= #{prop6},prop7= #{prop7},prop8= #{prop8},prop9= #{prop9},prop10= #{prop10},prop11= #{prop11},prop12= #{prop12},prop13= #{prop13},prop14= #{prop14},prop15= #{prop15},prop16= #{prop16},prop17= #{prop17},prop18= #{prop18},prop19= #{prop19},prop20= #{prop20} where text_id = #{id}")
    boolean updateMuBanById(String keyword,String longword,String scenario,String user,String prop1,String prop2,String prop3,String prop4,String prop5,String luckword,String point1,String point2,String point3,String point4,String point5,String description,String prop6,String prop7,String prop8,String prop9,String prop10,String prop11,String prop12,String prop13,String prop14,String prop15,String prop16,String prop17,String prop18,String prop19,String prop20,int id,String product,String title,String titleColor);

    @Delete("delete from textRestrictions where text_id=#{id}")
    boolean deleteid(int id);

    @Delete("delete from textRestrictions where id=#{id}")
    boolean deleteById(int id);

    @Insert("insert into textRestrictions(founder,text_id,columntext1,num1,columntext2,num2,columntext3,num3,columntext4,num4,columntext5,num5,columntext6,num6,columntext7,num7,columntext8,num8,columntext9,num9,columntext10,num10,keyword,longword,scenario,[user],prop1,prop2,prop3,prop4,prop5,luckword,point1,point2,point3,point4,point5,description,prop6,prop7,prop8,prop9,prop10,prop11,prop12,prop13,prop14,prop15,prop16,prop17,prop18,prop19,prop20,product,title,title_color) values(#{founder},#{textId},#{columntext1},#{num1},#{columntext2},#{num2},#{columntext3},#{num3},#{columntext4},#{num4},#{columntext5},#{num5},#{columntext6},#{num6},#{columntext7},#{num7},#{columntext8},#{num8},#{columntext9},#{num9},#{columntext10},#{num10},#{keyword},#{longword},#{scenario},#{user},#{prop1},#{prop2},#{prop3},#{prop4},#{prop5},#{luckword},#{point1},#{point2},#{point3},#{point4},#{point5},#{description},#{prop6},#{prop7},#{prop8},#{prop9},#{prop10},#{prop11},#{prop12},#{prop13},#{prop14},#{prop15},#{prop16},#{prop17},#{prop18},#{prop19},#{prop20},#{product},#{title},#{titleColor})")
    boolean insertShare(String founder,int textId,String columntext1,String num1,String columntext2,String num2,String columntext3,String num3,String columntext4,String num4,String columntext5,String num5,String columntext6,String num6,String columntext7,String num7,String columntext8,String num8,String columntext9,String num9,String columntext10,String num10,String keyword,String longword,String scenario,String user,String prop1,String prop2,String prop3,String prop4,String prop5,String luckword,String point1,String point2,String point3,String point4,String point5,String description,String prop6,String prop7,String prop8,String prop9,String prop10,String prop11,String prop12,String prop13,String prop14,String prop15,String prop16,String prop17,String prop18,String prop19,String prop20,String product,String title,String titleColor);

    @Select("select * from textRestrictions where founder = #{founder}")
    List<TextRestrictions> getShareByFounder(String founder);

    @Delete("delete from textRestrictions where founder = #{founder} and text_id = #{id}")
    boolean deleteByFoundId(String founder, Integer id);
}
