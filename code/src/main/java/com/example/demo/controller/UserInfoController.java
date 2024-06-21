package com.example.demo.controller;

import com.example.demo.entity.RandomText;
import com.example.demo.entity.TextRestrictions;
import com.example.demo.entity.UserInfo;
import com.example.demo.service.RandomTextService;
import com.example.demo.service.TextRestrictionsService;
import com.example.demo.service.UserInfoService;
import com.example.demo.util.*;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hui
 * @date 2022/8/19 10:20
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RandomTextService randomTextService;
    @Autowired
    private TextRestrictionsService textRestrictionsService;

    @RequestMapping("/login")
    public ResultInfo login(HttpSession session, String username, String password) {
        try {
            //获取user
            Map<String, Object> map = userInfoService.login(username, password);

            //为Null则查询不到
            if (StringUtils.isEmpty(map)) {
                SessionUtil.remove(session, "token");
                SessionUtil.remove(session, "power");
                return ResultInfo.error(-1, "用户名密码错误");
            } else {
                SessionUtil.setToken(session, map.get("token").toString());
                return ResultInfo.success("登陆成功");
            }
        } catch (Exception e) {
            log.error("登陆失败：{}", e.getMessage());
            log.error("参数：{}", username);
            log.error("参数：{}", password);
            return ResultInfo.error("错误!");
        }
    }

    /**
     * 查询所有
     *
     * @return ResultInfo
     */
    @RequestMapping("/getList")
    public ResultInfo getList(HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        if(!userInfo.getPower().equals("管理员")){
            return ResultInfo.error(401, "无权限");
        }
        try {
            List<UserInfo> getList = userInfoService.getList();
            return ResultInfo.success("获取成功", getList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取失败：{}", e.getMessage());
            return ResultInfo.error("错误!");
        }
    }

    /**
     * 获取当前账号权限
     *
     * @return ResultInfo
     */
    @RequestMapping("/getPower")
    public ResultInfo getPower(HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        try {
            return ResultInfo.success("获取成功", userInfo.getPower());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取失败：{}", e.getMessage());
            return ResultInfo.error("错误!");
        }
    }

    /**
     * 查询所有普通用户
     *
     * @return ResultInfo
     */
    @RequestMapping("/getUserList")
    public ResultInfo getUserList(HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        try {
            List<UserInfo> getList = userInfoService.getUserList();
            return ResultInfo.success("获取成功", getList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取失败：{}", e.getMessage());
            return ResultInfo.error("错误!");
        }
    }

    /**
     * 查询所有普通用户
     *
     * @return ResultInfo
     */
    @RequestMapping("/getCpById")
    public ResultInfo getCpById(String id) {
        try {
            List<TextRestrictions> getList = userInfoService.getCpById(id);
            return ResultInfo.success("获取成功", getList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取失败：{}", e.getMessage());
            return ResultInfo.error("错误!");
        }
    }



    /**
     * 查询所有管理员权限产品
     *
     * @return ResultInfo
     */
    @RequestMapping("/getTextR")
    public ResultInfo getTextR(HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        try {
            List<TextRestrictions> getList = userInfoService.getTextR();
            return ResultInfo.success("获取成功", getList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取失败：{}", e.getMessage());
            return ResultInfo.error("错误!");
        }
    }

    @RequestMapping("/getCpByUserName")
    public ResultInfo getCpByUserName(String userName) {
        try {
            List<TextRestrictions> getList = userInfoService.getCpByUserName(userName);
            if (getList == null){
                return ResultInfo.error("查无结果");
            }
            return ResultInfo.success("获取成功", getList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取失败：{}", e.getMessage());
            return ResultInfo.error("错误!");
        }
    }

    /**
     * 查询所有普通用户对应产品
     *
     * @return ResultInfo
     */
    @RequestMapping("/getUserAll")
    public ResultInfo getUserAll(HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        try {
            List<UserInfo> getList = userInfoService.getUserAll();
            return ResultInfo.success("获取成功", getList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取失败：{}", e.getMessage());
            return ResultInfo.error("错误!");
        }
    }

    /**
     * 查询所有
     *
     * @return ResultInfo
     */
    @RequestMapping("/getSelect")
    public ResultInfo getSelect(HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        if(!userInfo.getPower().equals("管理员")){
            return ResultInfo.error(401, "无权限");
        }
        try {
            List<UserInfo> getList = userInfoService.getList();
            return ResultInfo.success("获取成功", getList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取失败：{}", e.getMessage());
            return ResultInfo.error("错误!");
        }
    }



    /**
     * 根据姓名和部门查询
     *
     * @return ResultInfo
     */
    @RequestMapping("/queryList")
    public ResultInfo queryList(String name, String department, HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        if(!userInfo.getPower().equals("管理员")){
            return ResultInfo.error(401, "无权限");
        }
        try {
            List<UserInfo> list = userInfoService.queryList(name, department);
            return ResultInfo.success("获取成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取失败：{}", e.getMessage());
            return ResultInfo.error("错误!");
        }
    }

    /**
     * 添加
     */
    @RequestMapping("/add")
    public ResultInfo add(@RequestBody HashMap map, HttpSession session,String idd) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        if(!userInfo.getPower().equals("管理员")){
            return ResultInfo.error(401, "无权限");
        }
        GsonUtil gsonUtil = new GsonUtil(GsonUtil.toJson(map));
        try {
            UserInfo userInfo2 = GsonUtil.toEntity(gsonUtil.get("addInfo"), UserInfo.class);
            userInfo2 = userInfoService.add(userInfo2);
            List<UserInfo> userInfoList = userInfoService.getListid();
            List<TextRestrictions> textRestrictionsList = GsonUtil.toList(gsonUtil.get("textList"), TextRestrictions.class);
            for(int i=0; i<textRestrictionsList.size(); i++){
                if(textRestrictionsList.get(i).getFounder().equals("管理员")){
                    textRestrictionsList.get(i).setFounder(userInfoList.get(0).getId().toString());
                    textRestrictionsList.get(i).setTextId(textRestrictionsList.get(i).getId());
                    textRestrictionsService.insertById(textRestrictionsList.get(i));
                }
            }
            return ResultInfo.success("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("添加失败：{}", e.getMessage());
            log.error("参数：{}", map);
            return ResultInfo.error("添加失败");
        }
    }

    @RequestMapping("/info")
    public ResultInfo info(String ids, Integer id) throws JSONException {
        try {
            ResultInfo info = userInfoService.info(ids, id);
            return info;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取失败：{}", e.getMessage());
            return ResultInfo.error("添加失败");
        }
    }

    @RequestMapping("/getAccountProduct")
    public ResultInfo getAccountProduct(Integer id) throws JSONException {
        try {
            ResultInfo info = userInfoService.getAccountProduct(id);
            return info;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取失败：{}", e.getMessage());
            return ResultInfo.error("添加失败");
        }
    }

    @RequestMapping("/deleteCp")
    public ResultInfo deleteCp(String ids, Integer id) throws JSONException {
        try {
            boolean info = userInfoService.deleteCp(ids, id);
            if (info){
                return ResultInfo.success("删除成功");
            }else {
                return ResultInfo.success("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取失败：{}", e.getMessage());
            return ResultInfo.error("删除失败");
        }
    }


    /**
     * 添加
     */
    @RequestMapping("/useradd")
    public ResultInfo useradd( HttpSession session, String add_username, String add_password, String add_name, String add_department, String add_power,String add_change) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        try {
            if(userInfoService.useradd(add_username,add_password,add_name,add_department,add_power,add_change)) {
                return ResultInfo.success("添加成功");
            }else{
                return ResultInfo.error("添加失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("添加失败：{}", e.getMessage());
            return ResultInfo.error("添加失败");
        }
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultInfo update(@RequestBody String updateJson,HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        if(!userInfo.getPower().equals("管理员")){
            return ResultInfo.error(401, "无权限");
        }
        UserInfo userInfo2 = null;
        try {
            userInfo2 = DecodeUtil.decodeToJson(updateJson, UserInfo.class);
            if (userInfoService.update(userInfo2)) {
                return ResultInfo.success("修改成功", userInfo2);
            } else {
                return ResultInfo.success("修改失败", userInfo2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("修改失败：{}", e.getMessage());
            log.error("参数：{}", userInfo);
            return ResultInfo.error("修改失败");
        }
    }





    @RequestMapping(value = "/update2", method = RequestMethod.POST)
    public ResultInfo update2(@RequestBody String updateJson,HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        if(!userInfo.getChange().equals("否")){
            return ResultInfo.error(401, "无权限");
        }
        UserInfo userInfo2 = null;
        try {
            userInfo2 = DecodeUtil.decodeToJson(updateJson, UserInfo.class);
            if (userInfoService.update(userInfo2)) {
                return ResultInfo.success("修改成功", userInfo2);
            } else {
                return ResultInfo.success("修改失败", userInfo2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("修改失败：{}", e.getMessage());
            log.error("参数：{}", userInfo);
            return ResultInfo.error("修改失败");
        }
    }

    /**
     * 删除
     *
     * @param map
     * @return ResultInfo
     */
    @RequestMapping("/delete")
    public ResultInfo delete(@RequestBody HashMap map,HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        if(!userInfo.getPower().equals("管理员")){
            return ResultInfo.error(401, "无权限");
        }
        GsonUtil gsonUtil = new GsonUtil(GsonUtil.toJson(map));
        List<Integer> idList = GsonUtil.toList(gsonUtil.get("idList"), Integer.class);

        try {
            if (userInfoService.delete(idList)) {
                return ResultInfo.success("删除成功", idList);
            } else {
                return ResultInfo.success("删除失败", idList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除失败：{}", e.getMessage());
            log.error("参数：{}", idList);
            return ResultInfo.error("删除失败");
        }
    }

    /**
     * 删除该用户产品
     *
     * @param id
     * @return ResultInfo
     */
    @RequestMapping("/isDelete")
    public ResultInfo isDelete(int id) {
        try {
            if (userInfoService.isDelete(id)) {
                return ResultInfo.success("删除成功", id);
            } else {
                return ResultInfo.success("删除失败", id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除失败：{}", e.getMessage());
            log.error("参数：{}", id);
            return ResultInfo.error("删除失败");
        }
    }

    //查询用户id和账号
    @RequestMapping("/userXx")
    public ResultInfo userXx() {
        try {
            List<UserInfo> getList = userInfoService.getUserXx();
            return ResultInfo.success("获取成功", getList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取失败：{}", e.getMessage());
            return ResultInfo.error("错误!");
        }
    }

    //通过userid查询产品
    @RequestMapping("/getCpByUserId")
    public ResultInfo getCpByUserId(Integer userId) {
        try {
            List<TextRestrictions> getList = userInfoService.getCpByUserId(userId);
            if (getList == null){
                return ResultInfo.error("查无结果");
            }
            return ResultInfo.success("获取成功", getList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取失败：{}", e.getMessage());
            return ResultInfo.error("错误!");
        }
    }

}