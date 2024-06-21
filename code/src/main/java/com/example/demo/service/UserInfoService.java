package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.TextRestrictions;
import com.example.demo.entity.UserInfo;
import com.example.demo.util.ResultInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author hui
 * @date 2022/8/19 10:14
 */
@Service
public interface UserInfoService extends IService<UserInfo> {
    /**
     * 登陆
     *
     * @param username 用户名
     * @param password 密码
     * @return 转Json后的用户信息
     */
    Map<String, Object> login(String username, String password);

    /**
     * 查询所有
     */
    List<UserInfo> getList();

    /**
     * 查询所有普通用户
     */
    List<UserInfo> getUserList();

    /**
     * 查询id
     */
    List<UserInfo> getListid();

    /**
     * 根据姓名和部门查询
     */
    List<UserInfo> queryList(String name, String department);

    /**
     * 添加
     */
    UserInfo add(UserInfo userInfo);

    /**
     * 添加
     */
//    boolean useradd(String add_username, String add_password, String add_name, String add_department, String add_power);
    UserInfo useradd(UserInfo userInfo);

    boolean useradd(String add_username, String add_password, String add_name, String add_department, String add_change);

    /**
     * 修改
     */
    boolean update(UserInfo userInfo);

    /**
     * 删除
     *
     * @param idList 根据id集合删除
     * @return 是否删除成功
     */
    boolean delete(List<Integer> idList);

    boolean deleteid(int id);

    boolean useradd(@Param("addUsername") String addUsername, String addPassword, String addName, String addDepartment, String addPower, String addChange);

    List<UserInfo> getUserAll();

    //查询有关用户的产品
    List<TextRestrictions> getTextR();

    List<TextRestrictions> getTextCpById(String id);

    List<TextRestrictions> getCpById(String id);

    //删除该用户产品
    boolean isDelete(int id);

    ResultInfo info(String ids, Integer userId);

    List<UserInfo> getUserXx();

    List<TextRestrictions> getCpByUserName(String userName);

    boolean deleteCp(String ids, Integer id);

    List<TextRestrictions> getCpByUserId(Integer userId);

    ResultInfo getAccountProduct(Integer id);
}
