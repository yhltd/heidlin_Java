package com.example.demo.controller;

import com.example.demo.entity.Infringement;
import com.example.demo.entity.TextRestrictions;
import com.example.demo.entity.UserInfo;
import com.example.demo.service.InfringementService;
import com.example.demo.service.TextRestrictionsService;
import com.example.demo.service.UserInfoService;
import com.example.demo.util.DecodeUtil;
import com.example.demo.util.GsonUtil;
import com.example.demo.util.ResultInfo;
import com.example.demo.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

/**
 * @author hui
 * @date 2022/8/19 10:20
 */
@Slf4j
@RestController
@RequestMapping("/infringement")
public class InfringementController {
    @Autowired
    private InfringementService infringementService;
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
            List<Infringement> getList = infringementService.getList();
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
            List<Infringement> list = infringementService.queryList(this_column);
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
    public ResultInfo add(@RequestBody HashMap map, HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        GsonUtil gsonUtil = new GsonUtil(GsonUtil.toJson(map));
        try {
            Infringement infringement = GsonUtil.toEntity(gsonUtil.get("addInfo"), Infringement.class);
            String text = infringement.getText();
            String replaceText = infringement.getReplaceText();
            String[] textArr = text.split("<br><br>");
            String[] replaceTextArr = replaceText.split("<br><br>");
            int thisLen = textArr.length;
            for(int i=0; i<thisLen; i++){
                Infringement infringementItem = new Infringement();
                infringementItem.setText(textArr[i]);
                if(i <= replaceTextArr.length-1){
                    infringementItem.setReplaceText(replaceTextArr[i]);
                }else{
                    infringementItem.setReplaceText("");
                }
                infringementService.insert(infringementItem);
            }
            return ResultInfo.success("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("添加失败：{}", e.getMessage());
            log.error("参数：{}", map);
            return ResultInfo.error("添加失败");
        }
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultInfo update(@RequestBody String updateJson,HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        Infringement infringement = null;
        try {
            infringement = DecodeUtil.decodeToJson(updateJson, Infringement.class);
            infringementService.update(infringement.getText(),infringement.getReplaceText(),infringement.getId());
            return ResultInfo.success("修改成功", infringement);
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
            if (infringementService.delete(idList)) {
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

}