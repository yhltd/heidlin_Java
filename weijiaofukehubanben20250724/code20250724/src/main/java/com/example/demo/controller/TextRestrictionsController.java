package com.example.demo.controller;

import com.example.demo.entity.PageInfo;
import com.example.demo.entity.TextRestrictions;
import com.example.demo.entity.UserInfo;
import com.example.demo.service.TextRestrictionsService;
import com.example.demo.service.UserInfoService;
import com.example.demo.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.Result;

import javax.servlet.http.HttpSession;
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

    //----------------------------------------


    /**
     * 管理员分页查询
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param orderBy 排序字段
     * @return 分页结果
     */
    /**
     * 管理员分页查询
     * @return
     */
    @RequestMapping("/admin/list")
    public Result<PageInfo<TextRestrictions>> getAdminListByPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "50") int pageSize,
            @RequestParam(defaultValue = "id") String orderBy) {

        // 参数校验
        if (pageNum < 1) pageNum = 1;
        if (pageSize < 1 || pageSize >= 100) pageSize = 50;

        return Result.success(
                textRestrictionsService.getAdminListByPage(pageNum, pageSize, orderBy)
        );
    }

    /**
     * 普通用户分页查询
     * @return
     */
    @RequestMapping("/user/list")
    public Result<PageInfo<TextRestrictions>> getListByUserPage(
            @RequestParam String userId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "50") int pageSize) {

        // 参数校验
        if (pageNum < 1) pageNum = 1;
        if (pageSize < 1 || pageSize >= 100) pageSize = 50;

        return Result.success(
                textRestrictionsService.getListByUserPage(userId, pageNum, pageSize)
        );
    }

//----------------------------------------

    @PostMapping("/admin/listAll")
    public Result listAll(@RequestParam(required = false) String orderBy) {
        List<TextRestrictions> list = textRestrictionsService.getAllList(orderBy);
        return Result.success(list);
    }


    //----------------------------------------

    @RequestMapping("/processDuplicates")
    public ResultInfo processDuplicates(@RequestBody HashMap<String, Object> map, HttpSession session) {
        try {
            // 获取重复设置配置
            List<Map<String, Object>> duplicateSettings = (List<Map<String, Object>>) map.get("duplicateSettings");
            // 获取要检查的数据
            List<TextRestrictions> dataList = GsonUtil.toList(GsonUtil.toJson(map.get("dataList")), TextRestrictions.class);

            // 按列分组处理
            Map<String, Map<String, Integer>> columnValueCounts = new HashMap<>();

            // 1. 先统计所有值的出现次数
            for (TextRestrictions item : dataList) {
                for (Map<String, Object> setting : duplicateSettings) {
                    String column = (String) setting.get("column");
                    int maxAllowed = (int) setting.get("maxAllowed");
                    String value = getColumnValue(item, column);

                    if (value == null || value.isEmpty()) continue;

                    columnValueCounts.putIfAbsent(column, new HashMap<>());
                    Map<String, Integer> valueCounts = columnValueCounts.get(column);
                    valueCounts.put(value, valueCounts.getOrDefault(value, 0) + 1);
                }
            }

            // 2. 处理超过限制的值
            List<TextRestrictions> resultList = new ArrayList<>();
            for (TextRestrictions item : dataList) {
                TextRestrictions newItem = new TextRestrictions();
                BeanUtils.copyProperties(item, newItem);

                for (Map<String, Object> setting : duplicateSettings) {
                    String column = (String) setting.get("column");
                    int maxAllowed = (int) setting.get("maxAllowed");
                    String value = getColumnValue(item, column);

                    if (value == null || value.isEmpty()) continue;

                    int currentCount = columnValueCounts.get(column).get(value);
                    if (currentCount > maxAllowed) {
                        // 超过限制，清空该值
                        setColumnValue(newItem, column, "");
                        log.warn("值 {} 在列 {} 中超过最大允许重复次数 {}，已清空", value, column, maxAllowed);
                    }
                }
                resultList.add(newItem);
            }

            return ResultInfo.success("处理成功", resultList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("处理重复项失败：{}", e.getMessage());
            return ResultInfo.error("处理重复项失败");
        }
    }

    // 辅助方法：获取指定列的值
    private String getColumnValue(TextRestrictions item, String column) {
        switch (column) {
            case "product": return item.getProduct();
            case "columntext1": return item.getColumntext1();
            case "columntext2": return item.getColumntext2();
            // 添加其他列...
            default: return null;
        }
    }

    // 辅助方法：设置指定列的值
    private void setColumnValue(TextRestrictions item, String column, String value) {
        switch (column) {
            case "product": item.setProduct(value); break;
            case "columntext1": item.setColumntext1(value); break;
            case "columntext2": item.setColumntext2(value); break;
            // 添加其他列...
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