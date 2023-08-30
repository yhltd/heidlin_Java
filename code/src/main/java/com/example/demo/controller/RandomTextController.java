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
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            Pattern compile = Pattern.compile(".*[[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\\n|\\r|\\t].*");

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

            String[] replaceArr1 = keywordStr.split("``");
            String[] replaceArr2 = longwordStr.split("``");
            String[] replaceArr3 = scenarioStr.split("``");
            String[] replaceArr4 = userStr.split("``");
            String[] replaceArr5 = prop1Str.split("``");
            String[] replaceArr6 = prop2Str.split("``");
            String[] replaceArr7 = prop3Str.split("``");
            String[] replaceArr8 = prop4Str.split("``");
            String[] replaceArr9 = prop5Str.split("``");
            String[] replaceArr10 = luckwordStr.split("``");
            String[] replaceArr11 = point1Str.split("``");
            String[] replaceArr12 = point2Str.split("``");
            String[] replaceArr13 = point3Str.split("``");
            String[] replaceArr14 = point4Str.split("``");
            String[] replaceArr15 = point5Str.split("``");
            String[] replaceArr16 = descriptionStr.split("``");
            String[] replaceArr17 = prop6Str.split("``");
            String[] replaceArr18 = prop7Str.split("``");
            String[] replaceArr19 = prop8Str.split("``");
            String[] replaceArr20 = prop9Str.split("``");
            String[] replaceArr21 = prop10Str.split("``");
            String[] replaceArr22 = prop11Str.split("``");
            String[] replaceArr23 = prop12Str.split("``");
            String[] replaceArr24 = prop13Str.split("``");
            String[] replaceArr25 = prop14Str.split("``");
            String[] replaceArr26 = prop15Str.split("``");
            String[] replaceArr27 = prop16Str.split("``");
            String[] replaceArr28 = prop17Str.split("``");
            String[] replaceArr29 = prop18Str.split("``");
            String[] replaceArr30 = prop19Str.split("``");
            String[] replaceArr31 = prop20Str.split("``");

            for (int i = 3; i <= sheet.getLastRowNum(); i++) {
                //获取第i行
                Row row = sheet.getRow(i);
                //循环每一列
                for(int j=0; j<lastColumn; j++){
                    Cell cell = row.getCell(j);
                    if(cell == null){
                        continue;
                    }

                    String str = "";
                    if(cell.getCellType() == 0){
                        str = String.valueOf(cell.getNumericCellValue());
                    }else if(cell.getCellType() == 5){
                        str = "";
                    }else{
                        str = cell.getStringCellValue();
                    }

                    str = str.replace("｛","{");
                    str = str.replace("｝","}");

                    while(str.indexOf("{keyword}") != -1 && !keywordStr.equals("")){
                        int maxNum = replaceArr1.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{keyword\\}",replaceArr1[randomNum]);
                    }



                    while(str.indexOf("{longword}") != -1 && !longwordStr.equals("")) {

                        int maxNum = replaceArr2.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{longword\\}",replaceArr2[randomNum]);
                    }

                    while(str.indexOf("{scenario}") != -1 && !scenarioStr.equals("")) {
                        int maxNum = replaceArr3.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{scenario\\}",replaceArr3[randomNum]);
                    }



                    while(str.indexOf("{user}") != -1 && !userStr.equals("")) {
                        int maxNum = replaceArr4.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{user\\}",replaceArr4[randomNum]);
                    }

                    while(str.indexOf("{prop1}") != -1 && !prop1Str.equals("")) {
                        int maxNum = replaceArr5.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop1\\}",replaceArr5[randomNum]);
                    }

                    while(str.indexOf("{prop2}") != -1 && !prop2Str.equals("")) {
                        int maxNum = replaceArr6.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop2\\}",replaceArr6[randomNum]);
                    }

                    while(str.indexOf("{prop3}") != -1 && !prop3Str.equals("")) {
                        int maxNum = replaceArr7.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop3\\}",replaceArr7[randomNum]);
                    }

                    while(str.indexOf("{prop4}") != -1 && !prop4Str.equals("")) {
                        int maxNum = replaceArr8.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop4\\}",replaceArr8[randomNum]);
                    }

                    while(str.indexOf("{prop5}") != -1 && !prop5Str.equals("")) {
                        int maxNum = replaceArr9.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop5\\}",replaceArr9[randomNum]);
                    }

                    while(str.indexOf("{luckyword}") != -1 && !luckwordStr.equals("")) {
                        int maxNum = replaceArr10.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{luckyword\\}",replaceArr10[randomNum]);
                    }

                    while(str.indexOf("{point1}") != -1 && !point1Str.equals("")) {
                        int maxNum = replaceArr11.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{point1\\}",replaceArr11[randomNum]);
                    }

                    while(str.indexOf("{point2}") != -1 && !point2Str.equals("")) {
                        int maxNum = replaceArr12.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{point2\\}",replaceArr12[randomNum]);
                    }

                    while(str.indexOf("{point3}") != -1) {
                        int maxNum = replaceArr13.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{point3\\}",replaceArr13[randomNum]);
                    }

                    while(str.indexOf("{point4}") != -1 && !point4Str.equals("")) {
                        int maxNum = replaceArr14.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{point4\\}",replaceArr14[randomNum]);
                    }

                    while(str.indexOf("{point5}") != -1 && !point5Str.equals("")) {
                        int maxNum = replaceArr15.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{point5\\}",replaceArr15[randomNum]);
                    }

                    while(str.indexOf("{description}") != -1 && !descriptionStr.equals("")) {
                        int maxNum = replaceArr16.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{description\\}",replaceArr16[randomNum]);
                    }

                    while(str.indexOf("{prop6}") != -1 && !prop6Str.equals("")) {
                        int maxNum = replaceArr17.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop6\\}",replaceArr17[randomNum]);
                    }

                    while(str.indexOf("{prop7}") != -1 && !prop7Str.equals("")) {
                        int maxNum = replaceArr18.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop7\\}",replaceArr18[randomNum]);
                    }

                    while(str.indexOf("{prop8}") != -1 && !prop8Str.equals("")) {
                        int maxNum = replaceArr19.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop8\\}",replaceArr19[randomNum]);
                    }

                    while(str.indexOf("{prop9}") != -1 && !prop9Str.equals("")) {
                        int maxNum = replaceArr20.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop9\\}",replaceArr20[randomNum]);
                    }

                    while(str.indexOf("{prop10}") != -1 && !prop10Str.equals("")) {
                        int maxNum = replaceArr21.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop10\\}",replaceArr21[randomNum]);
                    }

                    while(str.indexOf("{prop11}") != -1 && !prop11Str.equals("")) {
                        int maxNum = replaceArr22.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop11\\}",replaceArr22[randomNum]);
                    }

                    while(str.indexOf("{prop12}") != -1 && !prop12Str.equals("")) {
                        int maxNum = replaceArr23.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop12\\}",replaceArr23[randomNum]);
                    }

                    while(str.indexOf("{prop13}") != -1 && !prop13Str.equals("")) {
                        int maxNum = replaceArr24.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop13\\}",replaceArr24[randomNum]);
                    }

                    while(str.indexOf("{prop14}") != -1 && !prop14Str.equals("")) {
                        int maxNum = replaceArr25.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop14\\}",replaceArr25[randomNum]);
                    }

                    while(str.indexOf("{prop15}") != -1 && !prop15Str.equals("")) {
                        int maxNum = replaceArr26.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop15\\}",replaceArr26[randomNum]);
                    }

                    while(str.indexOf("{prop16}") != -1 && !prop16Str.equals("")) {
                        int maxNum = replaceArr27.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop16\\}",replaceArr27[randomNum]);
                    }

                    while(str.indexOf("{prop17}") != -1 && !prop17Str.equals("")) {
                        int maxNum = replaceArr28.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop17\\}",replaceArr28[randomNum]);
                    }

                    while(str.indexOf("{prop18}") != -1 && !prop18Str.equals("")) {
                        int maxNum = replaceArr29.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop18\\}",replaceArr29[randomNum]);
                    }

                    while(str.indexOf("{prop19}") != -1 && !prop19Str.equals("")) {
                        int maxNum = replaceArr30.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop19\\}",replaceArr30[randomNum]);
                    }

                    while(str.indexOf("{prop20}") != -1 && !prop20Str.equals("")) {
                        int maxNum = replaceArr31.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop20\\}",replaceArr31[randomNum]);
                    }

                    while(str.indexOf("{keyword}") != -1 && !keywordStr.equals("")){
                        int maxNum = replaceArr1.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{keyword\\}",replaceArr1[randomNum]);
                    }



                    while(str.indexOf("{longword}") != -1 && !longwordStr.equals("")) {

                        int maxNum = replaceArr2.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{longword\\}",replaceArr2[randomNum]);
                    }

                    while(str.indexOf("{scenario}") != -1 && !scenarioStr.equals("")) {
                        int maxNum = replaceArr3.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{scenario\\}",replaceArr3[randomNum]);
                    }



                    while(str.indexOf("{user}") != -1 && !userStr.equals("")) {
                        int maxNum = replaceArr4.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{user\\}",replaceArr4[randomNum]);
                    }

                    while(str.indexOf("{prop1}") != -1 && !prop1Str.equals("")) {
                        int maxNum = replaceArr5.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop1\\}",replaceArr5[randomNum]);
                    }

                    while(str.indexOf("{prop2}") != -1 && !prop2Str.equals("")) {
                        int maxNum = replaceArr6.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop2\\}",replaceArr6[randomNum]);
                    }

                    while(str.indexOf("{prop3}") != -1 && !prop3Str.equals("")) {
                        int maxNum = replaceArr7.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop3\\}",replaceArr7[randomNum]);
                    }

                    while(str.indexOf("{prop4}") != -1 && !prop4Str.equals("")) {
                        int maxNum = replaceArr8.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop4\\}",replaceArr8[randomNum]);
                    }

                    while(str.indexOf("{prop5}") != -1 && !prop5Str.equals("")) {
                        int maxNum = replaceArr9.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop5\\}",replaceArr9[randomNum]);
                    }

                    while(str.indexOf("{luckyword}") != -1 && !luckwordStr.equals("")) {
                        int maxNum = replaceArr10.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{luckyword\\}",replaceArr10[randomNum]);
                    }

                    while(str.indexOf("{point1}") != -1 && !point1Str.equals("")) {
                        int maxNum = replaceArr11.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{point1\\}",replaceArr11[randomNum]);
                    }

                    while(str.indexOf("{point2}") != -1 && !point2Str.equals("")) {
                        int maxNum = replaceArr12.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{point2\\}",replaceArr12[randomNum]);
                    }

                    while(str.indexOf("{point3}") != -1) {
                        int maxNum = replaceArr13.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{point3\\}",replaceArr13[randomNum]);
                    }

                    while(str.indexOf("{point4}") != -1 && !point4Str.equals("")) {
                        int maxNum = replaceArr14.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{point4\\}",replaceArr14[randomNum]);
                    }

                    while(str.indexOf("{point5}") != -1 && !point5Str.equals("")) {
                        int maxNum = replaceArr15.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{point5\\}",replaceArr15[randomNum]);
                    }

                    while(str.indexOf("{description}") != -1 && !descriptionStr.equals("")) {
                        int maxNum = replaceArr16.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{description\\}",replaceArr16[randomNum]);
                    }

                    while(str.indexOf("{prop6}") != -1 && !prop6Str.equals("")) {
                        int maxNum = replaceArr17.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop6\\}",replaceArr17[randomNum]);
                    }

                    while(str.indexOf("{prop7}") != -1 && !prop7Str.equals("")) {
                        int maxNum = replaceArr18.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop7\\}",replaceArr18[randomNum]);
                    }

                    while(str.indexOf("{prop8}") != -1 && !prop8Str.equals("")) {
                        int maxNum = replaceArr19.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop8\\}",replaceArr19[randomNum]);
                    }

                    while(str.indexOf("{prop9}") != -1 && !prop9Str.equals("")) {
                        int maxNum = replaceArr20.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop9\\}",replaceArr20[randomNum]);
                    }

                    while(str.indexOf("{prop10}") != -1 && !prop10Str.equals("")) {
                        int maxNum = replaceArr21.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop10\\}",replaceArr21[randomNum]);
                    }

                    while(str.indexOf("{prop11}") != -1 && !prop11Str.equals("")) {
                        int maxNum = replaceArr22.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop11\\}",replaceArr22[randomNum]);
                    }

                    while(str.indexOf("{prop12}") != -1 && !prop12Str.equals("")) {
                        int maxNum = replaceArr23.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop12\\}",replaceArr23[randomNum]);
                    }

                    while(str.indexOf("{prop13}") != -1 && !prop13Str.equals("")) {
                        int maxNum = replaceArr24.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop13\\}",replaceArr24[randomNum]);
                    }

                    while(str.indexOf("{prop14}") != -1 && !prop14Str.equals("")) {
                        int maxNum = replaceArr25.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop14\\}",replaceArr25[randomNum]);
                    }

                    while(str.indexOf("{prop15}") != -1 && !prop15Str.equals("")) {
                        int maxNum = replaceArr26.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop15\\}",replaceArr26[randomNum]);
                    }

                    while(str.indexOf("{prop16}") != -1 && !prop16Str.equals("")) {
                        int maxNum = replaceArr27.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop16\\}",replaceArr27[randomNum]);
                    }

                    while(str.indexOf("{prop17}") != -1 && !prop17Str.equals("")) {
                        int maxNum = replaceArr28.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop17\\}",replaceArr28[randomNum]);
                    }

                    while(str.indexOf("{prop18}") != -1 && !prop18Str.equals("")) {
                        int maxNum = replaceArr29.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop18\\}",replaceArr29[randomNum]);
                    }

                    while(str.indexOf("{prop19}") != -1 && !prop19Str.equals("")) {
                        int maxNum = replaceArr30.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop19\\}",replaceArr30[randomNum]);
                    }

                    while(str.indexOf("{prop20}") != -1 && !prop20Str.equals("")) {
                        int maxNum = replaceArr31.length;
                        Random r = new Random();
                        int randomNum = 0;
                        if(maxNum > 0){
                            randomNum = r.nextInt(maxNum);
                        }
                        str = str.replaceFirst("\\{prop20\\}",replaceArr31[randomNum]);
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
                            panduan = true;
                        }

                        Matcher matcher = compile.matcher(key);
                        if(panduan){
                            if(matcher.matches()){
                                str = str.replaceAll("(?i)" + key.replace("{","\\{").replace("}","\\}") , qinquanMap.get(key) );
                            }else{
                                str = str.replaceAll("\\b(?i)" + key + "\\b", qinquanMap.get(key) );
                            }
                        }
                    }

                    //修改列内容
                    sheet.getRow(i).getCell(j).setCellValue(str);
                }

                System.out.println(i + " - " + sheet.getLastRowNum());
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