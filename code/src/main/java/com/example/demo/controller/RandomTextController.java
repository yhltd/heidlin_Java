package com.example.demo.controller;

import com.example.demo.entity.Infringement;
import com.example.demo.entity.RandomText;
import com.example.demo.entity.TextRestrictions;
import com.example.demo.entity.UserInfo;
import com.example.demo.service.InfringementService;
import com.example.demo.service.RandomTextService;
import com.example.demo.service.TextRestrictionsService;
import com.example.demo.service.UserInfoService;
import com.example.demo.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

/**
 * @author hui
 * @date 2022/8/19 10:20
 */
@Slf4j
@RestController
@RequestMapping("/randomText")
public class RandomTextController {
    @Autowired
    private RandomTextService randomTextService;
    @Autowired
    private TextRestrictionsService textRestrictionsService;
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
            if(userInfo.getPower().equals("管理员")){
                List<RandomText> getList = randomTextService.getList();
                return ResultInfo.success("获取成功", getList);
            }else{
                List<RandomText> getList = randomTextService.getListByUser(userInfo.getId().toString());
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
    @RequestMapping("/getSelect")
    public ResultInfo getSelect(HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        try {
            List<RandomText> getList = randomTextService.getList();
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
                List<RandomText> list = randomTextService.queryList(this_column);
                return ResultInfo.success("获取成功", list);
            }else{
                List<RandomText> list = randomTextService.queryListByName(this_column,userInfo.getId().toString());
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
    public ResultInfo add(@RequestBody HashMap map, HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        GsonUtil gsonUtil = new GsonUtil(GsonUtil.toJson(map));
        try {
            RandomText randomText = GsonUtil.toEntity(gsonUtil.get("addInfo"), RandomText.class);
            if(userInfo.getPower().equals("管理员")){
                randomText.setFounder("管理员");
            }else{
                randomText.setFounder(userInfo.getId().toString());
            }
            List<RandomText> nameList = randomTextService.getName(randomText.getFounder(),randomText.getProduct());
            if(nameList.size() == 0){
                randomTextService.insert(randomText);
                if(userInfo.getPower().equals("管理员")){
                    List<RandomText> idList = randomTextService.getMaxId();
                    List<UserInfo> userList = userInfoService.getUserList();
                    if(userList.size() > 0){
                        for(int i=0; i<userList.size(); i++){
                            randomText.setFounder(userList.get(i).getId().toString());
                            randomText.setTextId(idList.get(0).getId());
                            randomTextService.insertById(randomText);
                        }
                    }
                }
                return ResultInfo.success("添加成功");
            }else{
                return ResultInfo.error("已有相同产品名称的配置，请检查");
            }
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
        RandomText randomText = null;
        try {
            randomText = DecodeUtil.decodeToJson(updateJson, RandomText.class);
            if(userInfo.getPower().equals("管理员")){
                randomTextService.update(randomText.getKeyword(),randomText.getLongword(),randomText.getScenario(),randomText.getUser(),randomText.getProp1(),randomText.getProp2(),randomText.getProp3(),randomText.getProp4(),randomText.getProp5(),randomText.getLuckword(),randomText.getPoint1(),randomText.getPoint2(),randomText.getPoint3(),randomText.getPoint4(),randomText.getPoint5(),randomText.getDescription(),randomText.getProp6(),randomText.getProp7(),randomText.getProp8(),randomText.getProp9(),randomText.getProp10(),randomText.getProp11(),randomText.getProp12(),randomText.getProp13(),randomText.getProp14(),randomText.getProp15(),randomText.getProp16(),randomText.getProp17(),randomText.getProp18(),randomText.getProp19(),randomText.getProp20(),randomText.getId(),randomText.getProduct());
                randomTextService.updateById(randomText.getKeyword(),randomText.getLongword(),randomText.getScenario(),randomText.getUser(),randomText.getProp1(),randomText.getProp2(),randomText.getProp3(),randomText.getProp4(),randomText.getProp5(),randomText.getLuckword(),randomText.getPoint1(),randomText.getPoint2(),randomText.getPoint3(),randomText.getPoint4(),randomText.getPoint5(),randomText.getDescription(),randomText.getProp6(),randomText.getProp7(),randomText.getProp8(),randomText.getProp9(),randomText.getProp10(),randomText.getProp11(),randomText.getProp12(),randomText.getProp13(),randomText.getProp14(),randomText.getProp15(),randomText.getProp16(),randomText.getProp17(),randomText.getProp18(),randomText.getProp19(),randomText.getProp20(),randomText.getId(),randomText.getProduct());
            }else{
                randomTextService.update(randomText.getKeyword(),randomText.getLongword(),randomText.getScenario(),randomText.getUser(),randomText.getProp1(),randomText.getProp2(),randomText.getProp3(),randomText.getProp4(),randomText.getProp5(),randomText.getLuckword(),randomText.getPoint1(),randomText.getPoint2(),randomText.getPoint3(),randomText.getPoint4(),randomText.getPoint5(),randomText.getDescription(),randomText.getProp6(),randomText.getProp7(),randomText.getProp8(),randomText.getProp9(),randomText.getProp10(),randomText.getProp11(),randomText.getProp12(),randomText.getProp13(),randomText.getProp14(),randomText.getProp15(),randomText.getProp16(),randomText.getProp17(),randomText.getProp18(),randomText.getProp19(),randomText.getProp20(),randomText.getId(),randomText.getProduct());
            }
            return ResultInfo.success("修改成功", randomText);
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
                randomTextService.deleteid(idList.get(i));
            }
            if (randomTextService.delete(idList)) {
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
     * 上传excel
     *
     * @param data data
     * @return ResultInfo
     */
    @PostMapping("/upload")
    public ResultInfo upload(@RequestBody HashMap map, HttpSession session) {
        try {
            UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
            List<RandomText> randomTextList = null;
            List<TextRestrictions> textRestrictionsList = null;
            GsonUtil gsonUtil = new GsonUtil(GsonUtil.toJson(map));
            String data = gsonUtil.toJson();

            JSONObject jsonObject = new JSONObject(data);
            String data2 = jsonObject.get("addInfo").toString();
            jsonObject = new JSONObject(data2);
            String name = jsonObject.get("name").toString();
            String excel = jsonObject.get("excel").toString();
            String houzhui = jsonObject.get("houzhui").toString();

            List<Infringement> infringementList = infringementService.queryList(name);
            if(userInfo.getPower().equals("管理员")){
                randomTextList = randomTextService.queryList(name);
                textRestrictionsList = textRestrictionsService.queryList(name);
            }else{
                randomTextList = randomTextService.queryListByName(name,userInfo.getId().toString());
                textRestrictionsList = textRestrictionsService.queryListByName(name,userInfo.getId().toString());
            }

            Map<Integer, Integer> textNumMap=new HashMap<>();
            for(int i=0; i<textRestrictionsList.size(); i++){
                if(textRestrictionsList.get(i).getProduct().equals(name) && !textRestrictionsList.get(i).getColumntext().equals("") && textRestrictionsList.get(i).getColumntext() != null && !textRestrictionsList.get(i).getNum().equals("") && textRestrictionsList.get(i).getNum() != null){
                    String[] columnTextArr = textRestrictionsList.get(i).getColumntext().split("<br><br>");
                    String[] numArr = textRestrictionsList.get(i).getNum().split("<br><br>");
                    for(int j=0; j<columnTextArr.length; j++){
                        if(j <= numArr.length-1){
                            textNumMap.put(CTransformation(columnTextArr[j]),Integer.parseInt(numArr[j]));
                        }
                    }
                }
            }

            Map<String, String> qinquanMap=new HashMap<>();
            for(int i=0; i<infringementList.size(); i++){
                if(!infringementList.get(i).getText().equals("") && infringementList.get(i).getText() != null && !infringementList.get(i).getReplaceText().equals("") && infringementList.get(i).getReplaceText() != null){
                    qinquanMap.put(infringementList.get(i).getText(),infringementList.get(i).getReplaceText());
                }
            }

            String keywordStr = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getKeyword().equals("") && randomTextList.get(k).getKeyword() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getKeyword().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(keywordStr.equals("")){
                                keywordStr = randomTextArr[l];
                            }else{
                                keywordStr = keywordStr + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String longwordStr = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getLongword().equals("") && randomTextList.get(k).getLongword() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getLongword().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(longwordStr.equals("")){
                                longwordStr = randomTextArr[l];
                            }else{
                                longwordStr = longwordStr + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String scenarioStr = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getScenario().equals("") && randomTextList.get(k).getScenario() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getScenario().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(scenarioStr.equals("")){
                                scenarioStr = randomTextArr[l];
                            }else{
                                scenarioStr = scenarioStr + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String userStr = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getUser().equals("") && randomTextList.get(k).getUser() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getUser().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(userStr.equals("")){
                                userStr = randomTextArr[l];
                            }else{
                                userStr = userStr + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop1Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp1().equals("") && randomTextList.get(k).getProp1() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getProp1().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop1Str.equals("")){
                                prop1Str = randomTextArr[l];
                            }else{
                                prop1Str = prop1Str + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop2Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp2().equals("") && randomTextList.get(k).getProp2() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getProp2().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop2Str.equals("")){
                                prop2Str = randomTextArr[l];
                            }else{
                                prop2Str = prop2Str + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop3Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp3().equals("") && randomTextList.get(k).getProp3() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getProp3().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop3Str.equals("")){
                                prop3Str = randomTextArr[l];
                            }else{
                                prop3Str = prop3Str + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop4Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp4().equals("") && randomTextList.get(k).getProp4() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getProp4().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop4Str.equals("")){
                                prop4Str = randomTextArr[l];
                            }else{
                                prop4Str = prop4Str + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop5Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp5().equals("") && randomTextList.get(k).getProp5() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getProp5().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop5Str.equals("")){
                                prop5Str = randomTextArr[l];
                            }else{
                                prop5Str = prop5Str + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String luckwordStr = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getLuckword().equals("") && randomTextList.get(k).getLuckword() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getLuckword().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(luckwordStr.equals("")){
                                luckwordStr = randomTextArr[l];
                            }else{
                                luckwordStr = luckwordStr + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String point1Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getPoint1().equals("") && randomTextList.get(k).getPoint1() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getPoint1().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(point1Str.equals("")){
                                point1Str = randomTextArr[l];
                            }else{
                                point1Str = point1Str + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String point2Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getPoint2().equals("") && randomTextList.get(k).getPoint2() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getPoint2().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(point2Str.equals("")){
                                point2Str = randomTextArr[l];
                            }else{
                                point2Str = point2Str + "," + randomTextArr[l];
                            }
                        }

                    }
                }
            }

            String point3Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getPoint3().equals("") && randomTextList.get(k).getPoint3() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getPoint3().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(point3Str.equals("")){
                                point3Str = randomTextArr[l];
                            }else{
                                point3Str = point3Str + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String point4Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getPoint4().equals("") && randomTextList.get(k).getPoint4() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getPoint4().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(point4Str.equals("")){
                                point4Str = randomTextArr[l];
                            }else{
                                point4Str = point4Str + "," + randomTextArr[l];
                            }
                        }

                    }
                }
            }

            String point5Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getPoint5().equals("") && randomTextList.get(k).getPoint5() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getPoint5().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(point5Str.equals("")){
                                point5Str = randomTextArr[l];
                            }else{
                                point5Str = point5Str + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String descriptionStr = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getDescription().equals("") && randomTextList.get(k).getDescription() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getDescription().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(descriptionStr.equals("")){
                                descriptionStr = randomTextArr[l];
                            }else{
                                descriptionStr = descriptionStr + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop6Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp6().equals("") && randomTextList.get(k).getProp6() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getProp6().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop6Str.equals("")){
                                prop6Str = randomTextArr[l];
                            }else{
                                prop6Str = prop6Str + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop7Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp7().equals("") && randomTextList.get(k).getProp7() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getProp7().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop7Str.equals("")){
                                prop7Str = randomTextArr[l];
                            }else{
                                prop7Str = prop7Str + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop8Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp8().equals("") && randomTextList.get(k).getProp8() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getProp8().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop8Str.equals("")){
                                prop8Str = randomTextArr[l];
                            }else{
                                prop8Str = prop8Str + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop9Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp9().equals("") && randomTextList.get(k).getProp9() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getProp9().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop9Str.equals("")){
                                prop9Str = randomTextArr[l];
                            }else{
                                prop9Str = prop9Str + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop10Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp10().equals("") && randomTextList.get(k).getProp10() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getProp10().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop10Str.equals("")){
                                prop10Str = randomTextArr[l];
                            }else{
                                prop10Str = prop10Str + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop11Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp11().equals("") && randomTextList.get(k).getProp11() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getProp11().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop11Str.equals("")){
                                prop11Str = randomTextArr[l];
                            }else{
                                prop11Str = prop11Str + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop12Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp12().equals("") && randomTextList.get(k).getProp12() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getProp12().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop12Str.equals("")){
                                prop12Str = randomTextArr[l];
                            }else{
                                prop12Str = prop12Str + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop13Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp13().equals("") && randomTextList.get(k).getProp13() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getProp13().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop13Str.equals("")){
                                prop13Str = randomTextArr[l];
                            }else{
                                prop13Str = prop13Str + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop14Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp14().equals("") && randomTextList.get(k).getProp14() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getProp14().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop14Str.equals("")){
                                prop14Str = randomTextArr[l];
                            }else{
                                prop14Str = prop14Str + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop15Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp15().equals("") && randomTextList.get(k).getProp15() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getProp15().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop15Str.equals("")){
                                prop15Str = randomTextArr[l];
                            }else{
                                prop15Str = prop15Str + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop16Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp16().equals("") && randomTextList.get(k).getProp16() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getProp16().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop16Str.equals("")){
                                prop16Str = randomTextArr[l];
                            }else{
                                prop16Str = prop16Str + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop17Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp17().equals("") && randomTextList.get(k).getProp17() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getProp17().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop17Str.equals("")){
                                prop17Str = randomTextArr[l];
                            }else{
                                prop17Str = prop17Str + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop18Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp18().equals("") && randomTextList.get(k).getProp18() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getProp18().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop18Str.equals("")){
                                prop18Str = randomTextArr[l];
                            }else{
                                prop18Str = prop18Str + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop19Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp19().equals("") && randomTextList.get(k).getProp19() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getProp19().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop19Str.equals("")){
                                prop19Str = randomTextArr[l];
                            }else{
                                prop19Str = prop19Str + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop20Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp20().equals("") && randomTextList.get(k).getProp20() != null && randomTextList.get(k).getProduct().equals(name)){
                    String[] randomTextArr = randomTextList.get(k).getProp20().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop20Str.equals("")){
                                prop20Str = randomTextArr[l];
                            }else{
                                prop20Str = prop20Str + "," + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            FileInputStream fis = new FileInputStream(StringUtils.base64ToFile(excel));

            //循环Excel文件的i=1行开始
            Workbook wb = null;
            //创建2007版本Excel工作簿对象
            if(houzhui.equals(".xlsx")){
                wb = new XSSFWorkbook(fis);
            }else{
                wb = new HSSFWorkbook(fis);
            }
            //获取基本信息工作表
            Sheet sheet = wb.getSheet("Template");
            int lastColumn = sheet.getRow(1).getLastCellNum();
            for (int i = 3; i <= sheet.getLastRowNum(); i++) {
                //获取第i行
                Row row = sheet.getRow(i);
                //循环每一列
                for(int j=0; j<lastColumn; j++){
                    Cell cell = row.getCell(j);
                    if(cell == null){
                        continue;
                    }
                    String str = cell.getStringCellValue();
                    String[] columnName = new String[] {"{keyword}","{longword}","{scenario}","{user}","{prop1}","{prop2}","{prop3}","{prop4}","{prop5}","{luckword}","{point1}","{point2}","{point3}","{point4}","{point5}","{description}","{prop6}","{prop7}","{prop8}","{prop9}","{prop10}","{prop11}","{prop12}","{prop13}","{prop14}","{prop15}","{prop16}","{prop17}","{prop18}","{prop19}","{prop20}"};
                    str = str.replace("｛","{");
                    str = str.replace("｝","}");
                    if(str.indexOf("{keyword}") != -1) {
                        if(!keywordStr.equals("")){
                            String[] replaceArr = keywordStr.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{keyword}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{longword}") != -1) {
                        if(!longwordStr.equals("")){
                            String[] replaceArr = longwordStr.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{longword}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{scenario}") != -1) {
                        if(!scenarioStr.equals("")){
                            String[] replaceArr = scenarioStr.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{scenario}",replaceArr[randomNum]);
                        }
                    }



                    if(str.indexOf("{user}") != -1) {
                        if(!userStr.equals("")){
                            String[] replaceArr = userStr.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{user}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop1}") != -1) {
                        if(!prop1Str.equals("")){
                            String[] replaceArr = prop1Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop1}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop2}") != -1) {
                        if(!prop2Str.equals("")){
                            String[] replaceArr = prop2Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop2}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop3}") != -1) {
                        if(!prop3Str.equals("")){
                            String[] replaceArr = prop3Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop3}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop4}") != -1) {
                        if(!prop4Str.equals("")){
                            String[] replaceArr = prop4Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop4}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop5}") != -1) {
                        if(!prop5Str.equals("")){
                            String[] replaceArr = prop5Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop5}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{luckyword}") != -1) {
                        if(!luckwordStr.equals("")){
                            String[] replaceArr = luckwordStr.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{luckyword}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{point1}") != -1) {
                        if(!point1Str.equals("")){
                            String[] replaceArr = point1Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{point1}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{point2}") != -1) {
                        if(!point2Str.equals("")){
                            String[] replaceArr = point2Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{point2}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{point3}") != -1) {
                        if(!point3Str.equals("")){
                            String[] replaceArr = point3Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{point3}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{point4}") != -1) {
                        if(!point4Str.equals("")){
                            String[] replaceArr = point4Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{point4}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{point5}") != -1) {
                        if(!point5Str.equals("")){
                            String[] replaceArr = point5Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{point5}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{description}") != -1) {
                        if(!descriptionStr.equals("")){
                            String[] replaceArr = descriptionStr.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{description}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop6}") != -1) {
                        if(!prop6Str.equals("")){
                            String[] replaceArr = prop6Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop6}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop7}") != -1) {
                        if(!prop7Str.equals("")){
                            String[] replaceArr = prop7Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop7}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop8}") != -1) {
                        if(!prop8Str.equals("")){
                            String[] replaceArr = prop8Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop8}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop9}") != -1) {
                        if(!prop9Str.equals("")){
                            String[] replaceArr = prop9Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop9}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop10}") != -1) {
                        if(!prop10Str.equals("")){
                            String[] replaceArr = prop10Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop10}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop11}") != -1) {
                        if(!prop11Str.equals("")){
                            String[] replaceArr = prop11Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop11}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop12}") != -1) {
                        if(!prop12Str.equals("")){
                            String[] replaceArr = prop12Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop12}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop13}") != -1) {
                        if(!prop13Str.equals("")){
                            String[] replaceArr = prop13Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop13}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop14}") != -1) {
                        if(!prop14Str.equals("")){
                            String[] replaceArr = prop14Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop14}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop15}") != -1) {
                        if(!prop15Str.equals("")){
                            String[] replaceArr = prop15Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop15}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop16}") != -1) {
                        if(!prop16Str.equals("")){
                            String[] replaceArr = prop16Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop16}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop17}") != -1) {
                        if(!prop17Str.equals("")){
                            String[] replaceArr = prop17Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop17}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop18}") != -1) {
                        if(!prop18Str.equals("")){
                            String[] replaceArr = prop18Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop18}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop19}") != -1) {
                        if(!prop19Str.equals("")){
                            String[] replaceArr = prop19Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop19}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop20}") != -1) {
                        if(!prop20Str.equals("")){
                            String[] replaceArr = prop20Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop20}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{keyword}") != -1) {
                        if(!keywordStr.equals("")){
                            String[] replaceArr = keywordStr.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{keyword}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{longword}") != -1) {
                        if(!longwordStr.equals("")){
                            String[] replaceArr = longwordStr.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{longword}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{scenario}") != -1) {
                        if(!scenarioStr.equals("")){
                            String[] replaceArr = scenarioStr.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{scenario}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{user}") != -1) {
                        if(!userStr.equals("")){
                            String[] replaceArr = userStr.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{user}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop1}") != -1) {
                        if(!prop1Str.equals("")){
                            String[] replaceArr = prop1Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop1}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop2}") != -1) {
                        if(!prop2Str.equals("")){
                            String[] replaceArr = prop2Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop2}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop3}") != -1) {
                        if(!prop3Str.equals("")){
                            String[] replaceArr = prop3Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop3}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop4}") != -1) {
                        if(!prop4Str.equals("")){
                            String[] replaceArr = prop4Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop4}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop5}") != -1) {
                        if(!prop5Str.equals("")){
                            String[] replaceArr = prop5Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop5}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{luckword}") != -1) {
                        if(!luckwordStr.equals("")){
                            String[] replaceArr = luckwordStr.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{luckword}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{point1}") != -1) {
                        if(!point1Str.equals("")){
                            String[] replaceArr = point1Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{point1}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{point2}") != -1) {
                        if(!point2Str.equals("")){
                            String[] replaceArr = point2Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{point2}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{point3}") != -1) {
                        if(!point3Str.equals("")){
                            String[] replaceArr = point3Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{point3}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{point4}") != -1) {
                        if(!point4Str.equals("")){
                            String[] replaceArr = point4Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{point4}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{point5}") != -1) {
                        if(!point5Str.equals("")){
                            String[] replaceArr = point5Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{point5}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{description}") != -1) {
                        if(!descriptionStr.equals("")){
                            String[] replaceArr = descriptionStr.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{description}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop6}") != -1) {
                        if(!prop6Str.equals("")){
                            String[] replaceArr = prop6Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop6}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop7}") != -1) {
                        if(!prop7Str.equals("")){
                            String[] replaceArr = prop7Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop7}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop8}") != -1) {
                        if(!prop8Str.equals("")){
                            String[] replaceArr = prop8Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop8}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop9}") != -1) {
                        if(!prop9Str.equals("")){
                            String[] replaceArr = prop9Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop9}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop10}") != -1) {
                        if(!prop10Str.equals("")){
                            String[] replaceArr = prop10Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop10}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop11}") != -1) {
                        if(!prop11Str.equals("")){
                            String[] replaceArr = prop11Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop11}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop12}") != -1) {
                        if(!prop12Str.equals("")){
                            String[] replaceArr = prop12Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop12}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop13}") != -1) {
                        if(!prop13Str.equals("")){
                            String[] replaceArr = prop13Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop13}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop14}") != -1) {
                        if(!prop14Str.equals("")){
                            String[] replaceArr = prop14Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop14}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop15}") != -1) {
                        if(!prop15Str.equals("")){
                            String[] replaceArr = prop15Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop15}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop16}") != -1) {
                        if(!prop16Str.equals("")){
                            String[] replaceArr = prop16Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop16}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop17}") != -1) {
                        if(!prop17Str.equals("")){
                            String[] replaceArr = prop17Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop17}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop18}") != -1) {
                        if(!prop18Str.equals("")){
                            String[] replaceArr = prop18Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop18}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop19}") != -1) {
                        if(!prop19Str.equals("")){
                            String[] replaceArr = prop19Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop19}",replaceArr[randomNum]);
                        }
                    }

                    if(str.indexOf("{prop20}") != -1) {
                        if(!prop20Str.equals("")){
                            String[] replaceArr = prop20Str.split(",");
                            int maxNum = replaceArr.length - 1;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop20}",replaceArr[randomNum]);
                        }
                    }

                    for (String key : qinquanMap.keySet()) {
                        if(str.indexOf(key) != -1){
                            str = str.replace(key,qinquanMap.get(key));
                        }
                    }

                    if(textNumMap.containsKey(j + 1)){
                        int len = str.length();
                        int endLen = 0;
                        if(len > textNumMap.get(j + 1)){
                            String subStr = str.substring(textNumMap.get(j + 1), textNumMap.get(j + 1) + 1);
                            if(!subStr.equals(" ")){
                                for(int k=textNumMap.get(j + 1)-1; k>=0; k--){
                                    String thisStr = str.substring(k, k + 1);
                                    if(thisStr.equals(" ")){
                                        endLen = k;
                                        break;
                                    }
                                }
                                str = str.substring(0,endLen);
                            }else{
                                str = str.substring(0,textNumMap.get(j + 1));
                            }
                        }
                    }

                    //修改列内容
                    sheet.getRow(i).getCell(j).setCellValue(str);
                }
            }

            File PDFFilePath = new File("codeExecl.xls");
            FileOutputStream exportXls = new FileOutputStream(PDFFilePath);
            wb.write(exportXls);
            exportXls.close();
            //自定义方法  文件转base64流
            String strResult = PDFToBase64(PDFFilePath);
            strResult = strResult.replaceAll("\r\n", "");
            //删除创建好的无用文件
            deleteFile("codeExecl.xls");
            return ResultInfo.success("成功",strResult);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传失败，请查看数据是否正确：{}", e.getMessage());
            return ResultInfo.error("上传失败，请查看数据是否正确");
        }
    }

    /**
     * 方法说明: 列号转数字
     **/
    public int CTransformation(String s){
        char[] chars = s.toUpperCase(Locale.ROOT).toCharArray();
        int i = 0;
        for (char c : chars) {
            i = i * 26 + (c - 'A' + 1);
        }
        return i;
    }

    /**
     * 方法说明: 数字转列号
     **/
    public String ITransformation(Integer integer){
        int len = 0, power = 1, pre = 0;
        for (; pre < integer; pre += power) {
            power *= 26;
            len++;
        }
        char[] excelNum = new char[len];
        integer -= pre - power;
        integer--;
        for (int i = 0; i < len; i++) {
            power /= 26;
            excelNum[i] = (char) (integer / power + 'A');
            integer %= power;
        }
        return String.valueOf(excelNum);
    }


    /**
     * 文件转为base64字符串
     * @param document
     * @return
     */
    public static String PDFToBase64(File document) {
        BASE64Encoder encoder = new BASE64Encoder();
        FileInputStream fin = null;
        BufferedInputStream bin = null;
        ByteArrayOutputStream baos = null;
        BufferedOutputStream bout = null;
        try {
            fin = new FileInputStream(document);
            bin = new BufferedInputStream(fin);
            baos = new ByteArrayOutputStream();
            bout = new BufferedOutputStream(baos);
            byte[] buffer = new byte[1024];
            int len = bin.read(buffer);
            while (len != -1) {
                bout.write(buffer, 0, len);
                len = bin.read(buffer);
            }
            //刷新此输出流并强制写出所有缓冲的输出字节
            bout.flush();
            byte[] bytes = baos.toByteArray();
            return encoder.encodeBuffer(bytes).trim();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fin.close();
                bin.close();
                bout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param pathname
     * @return
     * @throws IOException
     * @author HF .HS
     */
    public static boolean deleteFile(String pathname) {
        boolean result = false;
        File file = new File(pathname);
        if (file.exists()) {
            file.delete();
            result = true;
        }
        return result;
    }

}