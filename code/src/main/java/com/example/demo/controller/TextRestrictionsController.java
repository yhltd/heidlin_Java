package com.example.demo.controller;

import com.example.demo.entity.TextRestrictions;
import com.example.demo.entity.UserInfo;
import com.example.demo.service.TextRestrictionsService;
import com.example.demo.service.UserInfoService;
import com.example.demo.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author hui
 * @date 2022/8/19 10:20
 */
@Slf4j
@RestController
@RequestMapping("/textRestrictions")
public class TextRestrictionsController {
    @Autowired
    private TextRestrictionsService textRestrictionsService;
    @Autowired
    private UserInfoService userInfoService;

    /**
     * 查询所有
     *
     * @return ResultInfo
     */
    @RequestMapping("/getList")
    public ResultInfo getList(HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        try {
            if(userInfo.getPower().equals("管理员")){
                List<TextRestrictions> getList = textRestrictionsService.getList();
                return ResultInfo.success("获取成功", getList);
            }else{
                List<TextRestrictions> getList = textRestrictionsService.getListByUser(userInfo.getId().toString());
                return ResultInfo.success("获取成功", getList);
            }
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
    @RequestMapping("/getListByUser")
    public ResultInfo getListById(String this_id,HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        try {
            List<TextRestrictions> getList = textRestrictionsService.getListByUser(this_id);
            return ResultInfo.success("获取成功", getList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取失败：{}", e.getMessage());
            return ResultInfo.error("错误!");
        }
    }

    @RequestMapping("/getListFounder")
    public ResultInfo getListFounder(HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        try {
            List<TextRestrictions> getList = textRestrictionsService.getList();
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
     * 查询所有
     *
     * @return ResultInfo
     */
    @RequestMapping("/getSelect")
    public ResultInfo getSelect(HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        try {
            List<TextRestrictions> getList = textRestrictionsService.getList();
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
    public ResultInfo queryList(String this_column, HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        try {
            if(userInfo.getPower().equals("管理员")){
                List<TextRestrictions> list = textRestrictionsService.queryList(this_column);
                return ResultInfo.success("获取成功", list);
            }else{
                List<TextRestrictions> list = textRestrictionsService.queryListByName(this_column,userInfo.getId().toString());
                return ResultInfo.success("获取成功", list);
            }
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
    public ResultInfo add(@RequestBody HashMap map, HttpSession session ) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        GsonUtil gsonUtil = new GsonUtil(GsonUtil.toJson(map));
        try {
            List<UserInfo> userList = GsonUtil.toList(gsonUtil.get("addInfo"),UserInfo.class);
            TextRestrictions textRestrictions = new TextRestrictions();
            if(userInfo.getPower().equals("管理员")){
                textRestrictions.setFounder("管理员");
            }else{
                textRestrictions.setFounder(userInfo.getId().toString());
            }

            textRestrictionsService.insert(textRestrictions);
            if(userInfo.getPower().equals("管理员")){
                List<TextRestrictions> idList = textRestrictionsService.getMaxId();
//                List<UserInfo> userList =  userInfoService.getUserList();
                if(userList.size() > 0){
                    for(int i=0; i<userList.size(); i++){
                        textRestrictions.setFounder(userList.get(i).getId().toString());
                        textRestrictions.setTextId(idList.get(0).getId());
                        textRestrictionsService.insertById2(textRestrictions);
                    }
                }

//                ArrayList<UserInfo> arrayList = new ArrayList<>();
//                for(field field : userInfo.getClass().getDeclaredFields());
//                field.setAccessible(true);
//                userInfo value = field.get



            }
            return ResultInfo.success("添加成功");

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
    public ResultInfo update(HttpSession session,String column,int id,String value) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        try {
            if(userInfo.getPower().equals("管理员")){
                textRestrictionsService.update(column,value,id);
                textRestrictionsService.updateById(column,value,id);
            }else{
                textRestrictionsService.update(column,value,id);
            }
            return ResultInfo.success("修改成功",column);
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
        GsonUtil gsonUtil = new GsonUtil(GsonUtil.toJson(map));
        List<Integer> idList = GsonUtil.toList(gsonUtil.get("idList"), Integer.class);
        try {
            for(int i=0; i<idList.size(); i++){
                textRestrictionsService.deleteid(idList.get(i));
            }
            if (textRestrictionsService.delete(idList)) {
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
     * 删除
     *
     * @param map
     * @return ResultInfo
     */
    @RequestMapping("/deleteById")
    public ResultInfo deleteByFounder(@RequestBody HashMap map,HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        GsonUtil gsonUtil = new GsonUtil(GsonUtil.toJson(map));
        List<Integer> idList = GsonUtil.toList(gsonUtil.get("idList"), Integer.class);
        try {
            if (textRestrictionsService.deleteById(idList)) {
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
     * 添加
     */
//    @RequestMapping("/insertShare")
//    public ResultInfo insertShare(@RequestBody HashMap map, HttpSession session) {
//        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
//        GsonUtil gsonUtil = new GsonUtil(GsonUtil.toJson(map));
//        try {
////            List<UserInfo> userInfoList = userInfoService.getListid();
//            TextRestrictions textRestrictions = new TextRestrictions();
//            List<UserInfo> userList = GsonUtil.toList(gsonUtil.get("textList"),UserInfo.class);
//
//            List<TextRestrictions> textRestrictionsList = GsonUtil.toList(gsonUtil.get("textList"), TextRestrictions.class);
////            List<TextRestrictions> founder = GsonUtil.toList(gsonUtil.get("id"), TextRestrictions.class);
//            for(int i=0; i<textRestrictionsList.size(); i++){
//                if(textRestrictionsList.get(i).getFounder().equals("管理员")){
//                    textRestrictionsList.get(i).setFounder(textRestrictionsList.get(i).getFounder());
//                    textRestrictionsList.get(i).setTextId(textRestrictionsList.get(i).getId());
//                    textRestrictionsService.insertShare(textRestrictionsList.get(i));
//                }
//            }
//            return ResultInfo.success("添加成功");
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("添加失败：{}", e.getMessage());
//            log.error("参数：{}", map);
//            return ResultInfo.error("添加失败");
//        }
//    }

    @RequestMapping("/insertShare")
    public ResultInfo insertShare(@RequestBody HashMap map, HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        GsonUtil gsonUtil = new GsonUtil(GsonUtil.toJson(map));

        GsonUtil gsonUtil1 = new GsonUtil(GsonUtil.toJson(map));
        List<Integer> id = GsonUtil.toList(gsonUtil1.get("id"), Integer.class);
        try {
            TextRestrictions textRestrictions = new TextRestrictions();
            if(userInfo.getPower().equals("管理员")){
                textRestrictions.setFounder("管理员");
            }else{
                textRestrictions.setFounder(userInfo.getId().toString());
            }
            if(userInfo.getPower().equals("管理员")){
                List<UserInfo> userList =  userInfoService.getUserList();
                List<TextRestrictions> textRestrictionsList = GsonUtil.toList(gsonUtil.get("textList"), TextRestrictions.class);
                    for(int i=0; i<textRestrictionsList.size(); i++){
                        textRestrictionsList.get(i).setFounder(id.get(0).toString());
                        textRestrictionsList.get(i).setTextId(textRestrictionsList.get(i).getId());
                        textRestrictionsService.insertShare(textRestrictionsList.get(i));
                    }
            }
            return ResultInfo.success("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("添加失败：{}", e.getMessage());
            return ResultInfo.error("添加失败");
        }
    }

    @RequestMapping("/getShareByFounder")
    public ResultInfo getShareByFounder(String founder,HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        try {
            List<TextRestrictions> getShareByFounder = textRestrictionsService.getShareByFounder(founder);

            return ResultInfo.success("获取成功", getShareByFounder);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取失败：{}", e.getMessage());
            return ResultInfo.error("错误!");
        }
    }

}