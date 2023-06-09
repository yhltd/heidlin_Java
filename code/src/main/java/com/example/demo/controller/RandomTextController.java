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
import com.google.gson.Gson;
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
    public ResultInfo getList(HttpSession session,int id) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        try {
            List<TextRestrictions> getList = textRestrictionsService.getMuBan(id);
            return ResultInfo.success("获取成功", getList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取失败：{}", e.getMessage());
            return ResultInfo.error("错误!");
        }
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultInfo update(@RequestBody String updateJson,HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        TextRestrictions textRestrictions = null;
        try {
            textRestrictions = DecodeUtil.decodeToJson(updateJson, TextRestrictions.class);
            if(userInfo.getPower().equals("管理员")){
                textRestrictionsService.updateMuBan(textRestrictions);
                textRestrictionsService.updateMuBanById(textRestrictions);
            }else{
                textRestrictionsService.updateMuBan(textRestrictions);
            }
            return ResultInfo.success("修改成功", textRestrictions);
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
     * @return ResultInfo
     */
    @PostMapping("/upload")
    public ResultInfo upload(@RequestBody HashMap map, HttpSession session) {
        try {
            UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
            List<TextRestrictions> randomTextList = null;
            List<TextRestrictions> textRestrictionsList = null;
            GsonUtil gsonUtil = new GsonUtil(GsonUtil.toJson(map));
            String data = gsonUtil.toJson();

            JSONObject jsonObject = new JSONObject(data);
            String data2 = jsonObject.get("addInfo").toString();
            jsonObject = new JSONObject(data2);
            String name = jsonObject.get("name").toString();
            String excel = jsonObject.get("excel").toString();
            String houzhui = jsonObject.get("houzhui").toString();
            int mubanId = Integer.parseInt(jsonObject.get("id").toString());

            List<Infringement> infringementList = infringementService.getList();

            textRestrictionsList = textRestrictionsService.getListById(mubanId);
            randomTextList = textRestrictionsList;

            Map<Integer, Integer> textNumMap=new HashMap<>();
            for(int i=0; i<textRestrictionsList.size(); i++){
                if( !textRestrictionsList.get(i).getColumntext1().equals("") && textRestrictionsList.get(i).getColumntext1() != null && !textRestrictionsList.get(i).getNum1().equals("") && textRestrictionsList.get(i).getNum1() != null){
                    textNumMap.put(CTransformation(textRestrictionsList.get(i).getColumntext1()),Integer.parseInt(textRestrictionsList.get(i).getNum1()));
                }
                if( !textRestrictionsList.get(i).getColumntext2().equals("") && textRestrictionsList.get(i).getColumntext2() != null && !textRestrictionsList.get(i).getNum2().equals("") && textRestrictionsList.get(i).getNum2() != null){
                    textNumMap.put(CTransformation(textRestrictionsList.get(i).getColumntext2()),Integer.parseInt(textRestrictionsList.get(i).getNum2()));
                }
                if( !textRestrictionsList.get(i).getColumntext3().equals("") && textRestrictionsList.get(i).getColumntext3() != null && !textRestrictionsList.get(i).getNum3().equals("") && textRestrictionsList.get(i).getNum3() != null){
                    textNumMap.put(CTransformation(textRestrictionsList.get(i).getColumntext3()),Integer.parseInt(textRestrictionsList.get(i).getNum3()));
                }
                if( !textRestrictionsList.get(i).getColumntext4().equals("") && textRestrictionsList.get(i).getColumntext4() != null && !textRestrictionsList.get(i).getNum4().equals("") && textRestrictionsList.get(i).getNum4() != null){
                    textNumMap.put(CTransformation(textRestrictionsList.get(i).getColumntext4()),Integer.parseInt(textRestrictionsList.get(i).getNum4()));
                }
                if( !textRestrictionsList.get(i).getColumntext5().equals("") && textRestrictionsList.get(i).getColumntext5() != null && !textRestrictionsList.get(i).getNum5().equals("") && textRestrictionsList.get(i).getNum5() != null){
                    textNumMap.put(CTransformation(textRestrictionsList.get(i).getColumntext5()),Integer.parseInt(textRestrictionsList.get(i).getNum5()));
                }
                if( !textRestrictionsList.get(i).getColumntext6().equals("") && textRestrictionsList.get(i).getColumntext6() != null && !textRestrictionsList.get(i).getNum6().equals("") && textRestrictionsList.get(i).getNum6() != null){
                    textNumMap.put(CTransformation(textRestrictionsList.get(i).getColumntext6()),Integer.parseInt(textRestrictionsList.get(i).getNum6()));
                }
                if( !textRestrictionsList.get(i).getColumntext7().equals("") && textRestrictionsList.get(i).getColumntext7() != null && !textRestrictionsList.get(i).getNum7().equals("") && textRestrictionsList.get(i).getNum7() != null){
                    textNumMap.put(CTransformation(textRestrictionsList.get(i).getColumntext7()),Integer.parseInt(textRestrictionsList.get(i).getNum7()));
                }
                if( !textRestrictionsList.get(i).getColumntext8().equals("") && textRestrictionsList.get(i).getColumntext8() != null && !textRestrictionsList.get(i).getNum8().equals("") && textRestrictionsList.get(i).getNum8() != null){
                    textNumMap.put(CTransformation(textRestrictionsList.get(i).getColumntext8()),Integer.parseInt(textRestrictionsList.get(i).getNum8()));
                }
                if( !textRestrictionsList.get(i).getColumntext9().equals("") && textRestrictionsList.get(i).getColumntext9() != null && !textRestrictionsList.get(i).getNum9().equals("") && textRestrictionsList.get(i).getNum9() != null){
                    textNumMap.put(CTransformation(textRestrictionsList.get(i).getColumntext9()),Integer.parseInt(textRestrictionsList.get(i).getNum9()));
                }
                if( !textRestrictionsList.get(i).getColumntext10().equals("") && textRestrictionsList.get(i).getColumntext10() != null && !textRestrictionsList.get(i).getNum10().equals("") && textRestrictionsList.get(i).getNum10() != null){
                    textNumMap.put(CTransformation(textRestrictionsList.get(i).getColumntext10()),Integer.parseInt(textRestrictionsList.get(i).getNum10()));
                }
            }

            Map<String, String> qinquanMap=new HashMap<>();
            for(int i=0; i<infringementList.size(); i++){
                if(!infringementList.get(i).getText().equals("") && infringementList.get(i).getText() != null && infringementList.get(i).getReplaceText() != null){
                    qinquanMap.put(infringementList.get(i).getText(),infringementList.get(i).getReplaceText());
                }
            }

            String keywordStr = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getKeyword().equals("") && randomTextList.get(k).getKeyword() != null ){
                    String[] randomTextArr = randomTextList.get(k).getKeyword().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(keywordStr.equals("")){
                                keywordStr = randomTextArr[l];
                            }else{
                                keywordStr = keywordStr + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String longwordStr = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getLongword().equals("") && randomTextList.get(k).getLongword() != null ){
                    String[] randomTextArr = randomTextList.get(k).getLongword().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(longwordStr.equals("")){
                                longwordStr = randomTextArr[l];
                            }else{
                                longwordStr = longwordStr + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String scenarioStr = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getScenario().equals("") && randomTextList.get(k).getScenario() != null ){
                    String[] randomTextArr = randomTextList.get(k).getScenario().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(scenarioStr.equals("")){
                                scenarioStr = randomTextArr[l];
                            }else{
                                scenarioStr = scenarioStr + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String userStr = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getUser().equals("") && randomTextList.get(k).getUser() != null ){
                    String[] randomTextArr = randomTextList.get(k).getUser().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(userStr.equals("")){
                                userStr = randomTextArr[l];
                            }else{
                                userStr = userStr + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop1Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp1().equals("") && randomTextList.get(k).getProp1() != null ){
                    String[] randomTextArr = randomTextList.get(k).getProp1().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop1Str.equals("")){
                                prop1Str = randomTextArr[l];
                            }else{
                                prop1Str = prop1Str + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop2Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp2().equals("") && randomTextList.get(k).getProp2() != null ){
                    String[] randomTextArr = randomTextList.get(k).getProp2().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop2Str.equals("")){
                                prop2Str = randomTextArr[l];
                            }else{
                                prop2Str = prop2Str + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop3Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp3().equals("") && randomTextList.get(k).getProp3() != null ){
                    String[] randomTextArr = randomTextList.get(k).getProp3().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop3Str.equals("")){
                                prop3Str = randomTextArr[l];
                            }else{
                                prop3Str = prop3Str + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop4Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp4().equals("") && randomTextList.get(k).getProp4() != null ){
                    String[] randomTextArr = randomTextList.get(k).getProp4().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop4Str.equals("")){
                                prop4Str = randomTextArr[l];
                            }else{
                                prop4Str = prop4Str + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop5Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp5().equals("") && randomTextList.get(k).getProp5() != null ){
                    String[] randomTextArr = randomTextList.get(k).getProp5().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop5Str.equals("")){
                                prop5Str = randomTextArr[l];
                            }else{
                                prop5Str = prop5Str + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String luckwordStr = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getLuckword().equals("") && randomTextList.get(k).getLuckword() != null ){
                    String[] randomTextArr = randomTextList.get(k).getLuckword().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(luckwordStr.equals("")){
                                luckwordStr = randomTextArr[l];
                            }else{
                                luckwordStr = luckwordStr + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String point1Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getPoint1().equals("") && randomTextList.get(k).getPoint1() != null ){
                    String[] randomTextArr = randomTextList.get(k).getPoint1().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(point1Str.equals("")){
                                point1Str = randomTextArr[l];
                            }else{
                                point1Str = point1Str + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String point2Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getPoint2().equals("") && randomTextList.get(k).getPoint2() != null ){
                    String[] randomTextArr = randomTextList.get(k).getPoint2().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(point2Str.equals("")){
                                point2Str = randomTextArr[l];
                            }else{
                                point2Str = point2Str + "``" + randomTextArr[l];
                            }
                        }

                    }
                }
            }

            String point3Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getPoint3().equals("") && randomTextList.get(k).getPoint3() != null ){
                    String[] randomTextArr = randomTextList.get(k).getPoint3().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(point3Str.equals("")){
                                point3Str = randomTextArr[l];
                            }else{
                                point3Str = point3Str + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String point4Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getPoint4().equals("") && randomTextList.get(k).getPoint4() != null ){
                    String[] randomTextArr = randomTextList.get(k).getPoint4().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(point4Str.equals("")){
                                point4Str = randomTextArr[l];
                            }else{
                                point4Str = point4Str + "``" + randomTextArr[l];
                            }
                        }

                    }
                }
            }

            String point5Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getPoint5().equals("") && randomTextList.get(k).getPoint5() != null ){
                    String[] randomTextArr = randomTextList.get(k).getPoint5().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(point5Str.equals("")){
                                point5Str = randomTextArr[l];
                            }else{
                                point5Str = point5Str + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String descriptionStr = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getDescription().equals("") && randomTextList.get(k).getDescription() != null ){
                    String[] randomTextArr = randomTextList.get(k).getDescription().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(descriptionStr.equals("")){
                                descriptionStr = randomTextArr[l];
                            }else{
                                descriptionStr = descriptionStr + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop6Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp6().equals("") && randomTextList.get(k).getProp6() != null ){
                    String[] randomTextArr = randomTextList.get(k).getProp6().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop6Str.equals("")){
                                prop6Str = randomTextArr[l];
                            }else{
                                prop6Str = prop6Str + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop7Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp7().equals("") && randomTextList.get(k).getProp7() != null ){
                    String[] randomTextArr = randomTextList.get(k).getProp7().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop7Str.equals("")){
                                prop7Str = randomTextArr[l];
                            }else{
                                prop7Str = prop7Str + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop8Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp8().equals("") && randomTextList.get(k).getProp8() != null ){
                    String[] randomTextArr = randomTextList.get(k).getProp8().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop8Str.equals("")){
                                prop8Str = randomTextArr[l];
                            }else{
                                prop8Str = prop8Str + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop9Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp9().equals("") && randomTextList.get(k).getProp9() != null ){
                    String[] randomTextArr = randomTextList.get(k).getProp9().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop9Str.equals("")){
                                prop9Str = randomTextArr[l];
                            }else{
                                prop9Str = prop9Str + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop10Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp10().equals("") && randomTextList.get(k).getProp10() != null ){
                    String[] randomTextArr = randomTextList.get(k).getProp10().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop10Str.equals("")){
                                prop10Str = randomTextArr[l];
                            }else{
                                prop10Str = prop10Str + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop11Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp11().equals("") && randomTextList.get(k).getProp11() != null ){
                    String[] randomTextArr = randomTextList.get(k).getProp11().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop11Str.equals("")){
                                prop11Str = randomTextArr[l];
                            }else{
                                prop11Str = prop11Str + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop12Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp12().equals("") && randomTextList.get(k).getProp12() != null ){
                    String[] randomTextArr = randomTextList.get(k).getProp12().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop12Str.equals("")){
                                prop12Str = randomTextArr[l];
                            }else{
                                prop12Str = prop12Str + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop13Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp13().equals("") && randomTextList.get(k).getProp13() != null ){
                    String[] randomTextArr = randomTextList.get(k).getProp13().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop13Str.equals("")){
                                prop13Str = randomTextArr[l];
                            }else{
                                prop13Str = prop13Str + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop14Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp14().equals("") && randomTextList.get(k).getProp14() != null ){
                    String[] randomTextArr = randomTextList.get(k).getProp14().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop14Str.equals("")){
                                prop14Str = randomTextArr[l];
                            }else{
                                prop14Str = prop14Str + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop15Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp15().equals("") && randomTextList.get(k).getProp15() != null ){
                    String[] randomTextArr = randomTextList.get(k).getProp15().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop15Str.equals("")){
                                prop15Str = randomTextArr[l];
                            }else{
                                prop15Str = prop15Str + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop16Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp16().equals("") && randomTextList.get(k).getProp16() != null ){
                    String[] randomTextArr = randomTextList.get(k).getProp16().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop16Str.equals("")){
                                prop16Str = randomTextArr[l];
                            }else{
                                prop16Str = prop16Str + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop17Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp17().equals("") && randomTextList.get(k).getProp17() != null ){
                    String[] randomTextArr = randomTextList.get(k).getProp17().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop17Str.equals("")){
                                prop17Str = randomTextArr[l];
                            }else{
                                prop17Str = prop17Str + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop18Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp18().equals("") && randomTextList.get(k).getProp18() != null ){
                    String[] randomTextArr = randomTextList.get(k).getProp18().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop18Str.equals("")){
                                prop18Str = randomTextArr[l];
                            }else{
                                prop18Str = prop18Str + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop19Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp19().equals("") && randomTextList.get(k).getProp19() != null ){
                    String[] randomTextArr = randomTextList.get(k).getProp19().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop19Str.equals("")){
                                prop19Str = randomTextArr[l];
                            }else{
                                prop19Str = prop19Str + "``" + randomTextArr[l];
                            }
                        }
                    }
                }
            }

            String prop20Str = "";
            for(int k=0; k<randomTextList.size(); k++){
                if(!randomTextList.get(k).getProp20().equals("") && randomTextList.get(k).getProp20() != null ){
                    String[] randomTextArr = randomTextList.get(k).getProp20().split("<br><br>");
                    for(int l=0; l<randomTextArr.length; l++){
                        if(!randomTextArr[l].equals("")){
                            if(prop20Str.equals("")){
                                prop20Str = randomTextArr[l];
                            }else{
                                prop20Str = prop20Str + "``" + randomTextArr[l];
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
                    str = str.replace("｛","{");
                    str = str.replace("｝","}");
                    if(str.indexOf("{keyword}") != -1) {
                        if(!keywordStr.equals("")){
                            String[] replaceArr = keywordStr.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = longwordStr.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = scenarioStr.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = userStr.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop1Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop2Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop3Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop4Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop5Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = luckwordStr.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = point1Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = point2Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = point3Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = point4Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = point5Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = descriptionStr.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop6Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop7Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop8Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop9Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop10Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop11Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop12Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop13Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop14Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop15Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop16Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop17Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop18Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop19Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop20Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = keywordStr.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = longwordStr.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = scenarioStr.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = userStr.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop1Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop2Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop3Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop4Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop5Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = luckwordStr.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = point1Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = point2Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = point3Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = point4Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = point5Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = descriptionStr.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop6Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop7Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop8Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop9Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop10Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop11Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop12Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop13Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop14Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop15Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop16Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop17Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop18Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop19Str.split("``");
                            int maxNum = replaceArr.length;
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
                            String[] replaceArr = prop20Str.split("``");
                            int maxNum = replaceArr.length;
                            Random r = new Random();
                            int randomNum = 0;
                            if(maxNum > 0){
                                randomNum = r.nextInt(maxNum);
                            }
                            str = str.replace("{prop20}",replaceArr[randomNum]);
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

                    for (String key : qinquanMap.keySet()) {
                        boolean panduan = false;
                        if(str.toUpperCase().indexOf(key.toUpperCase()) != -1){
                            if(str.toUpperCase().indexOf(key.toUpperCase()) == 0){
                                if(key.length() == str.length()){
                                    panduan = true;
                                }else if(str.toUpperCase().indexOf(key.toUpperCase() + " ") != -1){
                                    panduan = true;
                                }
                            }else if( str.toUpperCase().indexOf(" " + key.toUpperCase() + " ") != -1 ){
                                panduan = true;
                            }else if( str.toUpperCase().indexOf(" " + key.toUpperCase()) + key.length() == str.length() ){
                                panduan = true;
                            }
                        }
                        if(panduan){
                            str = str.replaceAll("(?i)" + key,qinquanMap.get(key));
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