package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.TextRestrictions;
import com.example.demo.entity.UserInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hui
 * @date 2022/8/19 10:17
 */
@Mapper
@Repository
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    @Select("select * from userInfo")
    List<UserInfo> getList();

    @Select("select * from userInfo where power = '用户'")
    List<UserInfo> getUserList();

    @Select("select MAX(id) as id from userInfo")
    List<UserInfo> getListid();

//    @Insert("insert into userinfo(username,password,name,department,power) values(#{add_username},#{add_password},#{add_name},#{add_department},#{power})")
//    boolean useradd(String add_username, String add_password, String add_name, String add_department, String add_power);
//
    @Insert("insert into userinfo(username,password,name,department,power) values(#{add_username},#{add_password},#{add_name},#{add_department},#{power},#{add_change})")
    boolean useradd(String add_username, String add_password, String add_name, String add_department, String add_power, String add_change);
    @Select("select * from userInfo where name like '%'+#{name}+'%' and department like '%'+#{department}+'%' ")
    List<UserInfo> queryList(String name,String department);

    @Delete("delete from userInfo where id=#{id}")
    boolean deleteid(int id);

    boolean useradd(String addUsername, String addPassword, String addName, String addDepartment, String addChange);
}
