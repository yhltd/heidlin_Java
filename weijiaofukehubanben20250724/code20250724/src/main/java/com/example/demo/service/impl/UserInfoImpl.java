package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.TextRestrictions;
import com.example.demo.entity.UserInfo;
import com.example.demo.mapper.TextRestrictionsMapper;
import com.example.demo.mapper.UserInfoMapper;
import com.example.demo.service.UserInfoService;
import com.example.demo.util.GsonUtil;
import com.example.demo.util.ResultInfo;
import com.example.demo.util.StringUtils;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.Result;
import java.util.*;

/**
 * @author hui
 * @date 2022/8/19 10:16
 */
@Service
public class UserInfoImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    TextRestrictionsMapper textRestrictionsMapper;

    @Override
    public Map<String, Object> login(String username, String password) {
        //条件构造器
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        //账号
        queryWrapper.eq("username", username);
        //密码
        queryWrapper.eq("password", password);
        //获取User
        UserInfo userInfo = this.getOne(queryWrapper);
        //如果不为空
        String data = StringUtils.EMPTY;
        if (StringUtils.isNotNull(userInfo)) {
            //转JSON
            data = GsonUtil.toJson(userInfo);

            Map<String, Object> map = new HashMap<>();
            map.put("token", data);
            String this_power = "";
            return map;
        }
        return null;
    }

    @Override
    public List<UserInfo> getList() {
        return userInfoMapper.getList();
    }

    @Override
    public List<UserInfo> getUserList() {
        return userInfoMapper.getUserList();
    }

    @Override
    public List<UserInfo> getListid() {
        return userInfoMapper.getListid();
    }

    @Override
    public List<UserInfo> queryList(String name, String department) {
        return userInfoMapper.queryList(name, department);
    }

    @Override
    public UserInfo add(UserInfo userInfo) {
        return save(userInfo) ? userInfo : null;
    }

    @Override
    public UserInfo useradd(UserInfo userInfo) {
        return null;
    }

    //
//    @Override
//    public boolean useradd(String add_username, String add_password, String add_name, String add_department, String add_power) {
//        return false;
//    }
    @Override
public boolean useradd(String add_username, String add_password, String add_name, String add_department, String add_power, String add_change) {
        return false;
    }

    @Override
    public List<UserInfo> getUserAll() {
        return userInfoMapper.getUserAll();
    }

    @Override
    public List<TextRestrictions> getTextR() {
        return userInfoMapper.getTextR();
    }

    @Override
    public List<TextRestrictions> getTextCpById(String id) {
        return userInfoMapper.getTextCpById(id);
    }

    @Override
    public List<TextRestrictions> getCpById(String id) {
        return userInfoMapper.getTextCpById(id);
    }

    //删除该用户产品
    @Override
    public boolean isDelete(int id) {
        return textRestrictionsMapper.deleteById(id);
    }

    @Override
    public ResultInfo info(String ids, Integer id) {
        if (id != null){
                if (!StringUtils.isEmpty(ids)){
                    String[] split = ids.replace("[", "").replace("]", "").split(",");
                    for (String s : split) {
                        if (!"".equals(s)){
                            textRestrictionsMapper.insertToAcount(s, id, textRestrictionsMapper.selectById(id).getProduct());
                        }
                    }
                    return ResultInfo.success("添加成功");
                }else {
                    return ResultInfo.error("添加失败");
                }
        }
       return ResultInfo.error("添加失败");
    }

    @Override
    public boolean deleteCp(String ids, Integer id) {
        if (!StringUtils.isEmpty(ids)){
            String[] split = ids.replace("[", "").replace("]", "").split(",");
            for (String s : split) {
                textRestrictionsMapper.deleteByFoundId(s, id);
            }
            return true;

        }else {
            return false;
        }
    }

    @Override
    public List<TextRestrictions> getCpByUserId(Integer userId) {
        List<TextRestrictions> textRestrictions = textRestrictionsMapper.selectPcByUserId(userId.toString());
        return textRestrictions;
    }

    //根据admin产品查询
    @Override
    public ResultInfo getAccountProduct(Integer id) {
        List<UserInfo> userList = new ArrayList<>();
        List<TextRestrictions> textRestrictions = userInfoMapper.selectByTextId(id);
        List<UserInfo> userInfoList = userInfoMapper.getUserList();
        if (textRestrictions.size() == 0){
            Map<String, List<UserInfo>> map = new HashMap<>();
            map.put("userInfoList", userInfoList);
            return ResultInfo.success(map);
        }
        for (int i = 0; i < textRestrictions.size(); i++) {
            UserInfo userInfo = userInfoMapper.selectXxByUserId(Integer.valueOf(textRestrictions.get(i).getFounder()));
            if (userInfo != null && !userList.contains(userInfo)){
                userList.add(userInfo);
            }
        }
        for (int i = 0; i < userList.size(); i++) {
            if (userInfoList.contains(userList.get(i))){
                userInfoList.remove(userList.get(i));
            }
        }

        Map<String, List<UserInfo>> map = new HashMap<>();
        map.put("userList", userList);
        map.put("userInfoList", userInfoList);
        return ResultInfo.success(map);
    }

    @Override
    public List<UserInfo> getUserXx() {
        return userInfoMapper.selectUserByAccount();
    }

    //通过子账号查产品
    @Override
    public List<TextRestrictions> getCpByUserName(String userName) {
        if (!StringUtils.isEmpty(userName)){
            UserInfo userInfo = userInfoMapper.selectUserIdByUserName(userName);
            if (userInfo == null){
                return null;
            }else {
                if (!"管理员".equals(userInfo.getPower())){
                    List<TextRestrictions> textRestrictions = textRestrictionsMapper.selectPcByUserId(userInfo.getId().toString());
                    return textRestrictions;
                }else {
                    return null;
                }
            }
        }else {
            return null;
        }


    }



    @Override
    public boolean useradd(String add_username, String add_password, String add_name, String add_department, String add_change) {
        return userInfoMapper.useradd(add_username, add_password, add_name, add_department, add_change);
    }


    //    boolena useradd(String add_username, String add_password, String add_name, String add_department) {
//        return userInfoMapper.useradd(add_username,add_password,add_name,add_department);
//    }
    @Override
    public boolean update(UserInfo userInfo) {
        return updateById(userInfo);
    }

    @Override
    public boolean delete(List<Integer> idList) {
        return removeByIds(idList);
    }

    @Override
    public boolean deleteid(int id) {
        return userInfoMapper.deleteid(id);
    }
}
