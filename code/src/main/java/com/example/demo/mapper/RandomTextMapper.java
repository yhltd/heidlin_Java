package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.RandomText;
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
public interface RandomTextMapper extends BaseMapper<RandomText> {
    @Select("select * from randomText where founder = '管理员'")
    List<RandomText> getList();

    @Select("select * from randomText where founder = #{founder}")
    List<RandomText> getListByUser(String founder);

    @Insert("insert into randomText(keyword,longword,scenario,[user],prop1,prop2,prop3,prop4,prop5,luckword,point1,point2,point3,point4,point5,description,prop6,prop7,prop8,prop9,prop10,prop11,prop12,prop13,prop14,prop15,prop16,prop17,prop18,prop19,prop20,founder,text_id,product) " +
            " values(#{keyword},#{longword},#{scenario},#{user},#{prop1},#{prop2},#{prop3},#{prop4},#{prop5},#{luckword},#{point1},#{point2},#{point3},#{point4},#{point5},#{description},#{prop6},#{prop7},#{prop8},#{prop9},#{prop10},#{prop11},#{prop12},#{prop13},#{prop14},#{prop15},#{prop16},#{prop17},#{prop18},#{prop19},#{prop20},#{founder},#{textId},#{product})")
    boolean insertById(String keyword,String longword,String scenario,String user,String prop1,String prop2,String prop3,String prop4,String prop5,String luckword,String point1,String point2,String point3,String point4,String point5,String description,String prop6,String prop7,String prop8,String prop9,String prop10,String prop11,String prop12,String prop13,String prop14,String prop15,String prop16,String prop17,String prop18,String prop19,String prop20,String founder,int textId,String product);

    @Insert("insert into randomText(keyword,longword,scenario,[user],prop1,prop2,prop3,prop4,prop5,luckword,point1,point2,point3,point4,point5,description,prop6,prop7,prop8,prop9,prop10,prop11,prop12,prop13,prop14,prop15,prop16,prop17,prop18,prop19,prop20,founder,product) " +
            " values(#{keyword},#{longword},#{scenario},#{user},#{prop1},#{prop2},#{prop3},#{prop4},#{prop5},#{luckword},#{point1},#{point2},#{point3},#{point4},#{point5},#{description},#{prop6},#{prop7},#{prop8},#{prop9},#{prop10},#{prop11},#{prop12},#{prop13},#{prop14},#{prop15},#{prop16},#{prop17},#{prop18},#{prop19},#{prop20},#{founder},#{product})")
    boolean insert(String keyword,String longword,String scenario,String user,String prop1,String prop2,String prop3,String prop4,String prop5,String luckword,String point1,String point2,String point3,String point4,String point5,String description,String prop6,String prop7,String prop8,String prop9,String prop10,String prop11,String prop12,String prop13,String prop14,String prop15,String prop16,String prop17,String prop18,String prop19,String prop20,String founder,String product);

    @Select("select * from randomText where product like '%'+#{this_column}+'%' and founder = '管理员'")
    List<RandomText> queryList(String this_column);

    @Select("select * from randomText where product like '%'+#{this_column}+'%' and founder = #{founder}")
    List<RandomText> queryListByUser(String this_column,String founder);

    @Select("SELECT max(id) as id from randomText")
    List<RandomText> getMaxId();

    @Select("SELECT product from randomText where founder = '#{founder} and product = #{product}")
    List<RandomText> getName(String founder,String product);

    @Update("update randomText set keyword= #{keyword},longword= #{longword},scenario= #{scenario},[user]= #{user},prop1= #{prop1},prop2= #{prop2},prop3= #{prop3},prop4= #{prop4},prop5= #{prop5},luckword= #{luckword},point1= #{point1},point2= #{point2},point3= #{point3},point4= #{point4},point5= #{point5},description= #{description},prop6= #{prop6},prop7= #{prop7},prop8= #{prop8},prop9= #{prop9},prop10= #{prop10},prop11= #{prop11},prop12= #{prop12},prop13= #{prop13},prop14= #{prop14},prop15= #{prop15},prop16= #{prop16},prop17= #{prop17},prop18= #{prop18},prop19= #{prop19},prop20= #{prop20},product= #{product} where id= #{id}")
    boolean update(String keyword,String longword,String scenario,String user,String prop1,String prop2,String prop3,String prop4,String prop5,String luckword,String point1,String point2,String point3,String point4,String point5,String description,String prop6,String prop7,String prop8,String prop9,String prop10,String prop11,String prop12,String prop13,String prop14,String prop15,String prop16,String prop17,String prop18,String prop19,String prop20,int id,String product);

    @Update("update randomText set keyword= #{keyword},longword= #{longword},scenario= #{scenario},[user]= #{user},prop1= #{prop1},prop2= #{prop2},prop3= #{prop3},prop4= #{prop4},prop5= #{prop5},luckword= #{luckword},point1= #{point1},point2= #{point2},point3= #{point3},point4= #{point4},point5= #{point5},description= #{description},prop6= #{prop6},prop7= #{prop7},prop8= #{prop8},prop9= #{prop9},prop10= #{prop10},prop11= #{prop11},prop12= #{prop12},prop13= #{prop13},prop14= #{prop14},prop15= #{prop15},prop16= #{prop16},prop17= #{prop17},prop18= #{prop18},prop19= #{prop19},prop20= #{prop20},product= #{product} where text_id = id")
    boolean updateById(String keyword,String longword,String scenario,String user,String prop1,String prop2,String prop3,String prop4,String prop5,String luckword,String point1,String point2,String point3,String point4,String point5,String description,String prop6,String prop7,String prop8,String prop9,String prop10,String prop11,String prop12,String prop13,String prop14,String prop15,String prop16,String prop17,String prop18,String prop19,String prop20,int id,String product);

    @Delete("delete from randomText where text_id=#{id}")
    boolean deleteid(int id);
}
