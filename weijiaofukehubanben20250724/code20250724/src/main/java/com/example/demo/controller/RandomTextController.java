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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
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
    public ResultInfo getList(HttpSession session, int id) {
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
    public ResultInfo update(@RequestBody String updateJson, HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        TextRestrictions textRestrictions = null;
        try {
            textRestrictions = DecodeUtil.decodeToJson(updateJson, TextRestrictions.class);
            if (userInfo.getPower().equals("管理员")) {
                textRestrictionsService.updateMuBan(textRestrictions);
                textRestrictionsService.updateMuBanById(textRestrictions);
            } else {
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
    public ResultInfo delete(@RequestBody HashMap map, HttpSession session) {
        UserInfo userInfo = GsonUtil.toEntity(SessionUtil.getToken(session), UserInfo.class);
        GsonUtil gsonUtil = new GsonUtil(GsonUtil.toJson(map));
        List<Integer> idList = GsonUtil.toList(gsonUtil.get("idList"), Integer.class);
        try {
            for (int i = 0; i < idList.size(); i++) {
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
            GsonUtil gsonUtil = new GsonUtil(GsonUtil.toJson(map));
            String data = gsonUtil.toJson();

            JSONObject jsonObject = new JSONObject(data);
            String data2 = jsonObject.get("addInfo").toString();
            jsonObject = new JSONObject(data2);
            String name = jsonObject.get("name").toString();
            String excel = jsonObject.get("excel").toString();
            String houzhui = jsonObject.get("houzhui").toString();
            int mubanId = Integer.parseInt(jsonObject.get("id").toString());

            // 1. 预编译正则表达式
            Pattern specialCharPattern = Pattern.compile(".*[[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\\n|\\r|\\t].*");

            // 2. 使用更高效的方式构建textNumMap
            Map<Integer, Integer> textNumMap = buildTextNumMap(textRestrictionsService.getListById(mubanId));
            Map<String, String> qinquanMap = buildQinquanMap(infringementService.getList());
            Map<String, String[]> replacementMap = buildReplacementMap(textRestrictionsService.getListById(mubanId));
            Map<String, Integer> wordRepeatLimitMap = buildWordRepeatLimitMap(textRestrictionsService.getListById(mubanId));

            FileInputStream fis = new FileInputStream(StringUtils.base64ToFile(excel));
            Workbook wb = houzhui.equals(".xlsx") ? new XSSFWorkbook(fis) : new HSSFWorkbook(fis);
            Sheet sheet = wb.getSheet("Template");
            int lastColumn = sheet.getRow(1).getLastCellNum();

            // 3. 创建线程安全的随机数生成器
            ThreadLocalRandom random = ThreadLocalRandom.current();

            // 4. 预编译侵权词的正则表达式
            Map<String, Pattern> qinquanPatterns = new ConcurrentHashMap<>();
            for (Map.Entry<String, String> entry : qinquanMap.entrySet()) {
                String key = entry.getKey();
                Matcher matcher = specialCharPattern.matcher(key);
                if (matcher.matches()) {
                    qinquanPatterns.put(key, Pattern.compile("(?i)" + Pattern.quote(key)));
                } else {
                    qinquanPatterns.put(key, Pattern.compile("\\b(?i)" + Pattern.quote(key) + "\\b"));
                }
            }

            // 5. 使用更高效的行迭代方式
            for (int i = 3; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                for (int j = 0; j < lastColumn; j++) {
                    Cell cell = row.getCell(j);
                    if (cell == null) continue;

                    String str = getCellValueAsString(cell);
                    str = str.replace("｛", "{").replace("｝", "}");

                    // 6. 使用StringBuilder进行高效字符串操作
                    StringBuilder sb = new StringBuilder(str);

                    // 7. 优化替换逻辑
                    processPlaceholders(sb, replacementMap, random);

                    // 2. 检查单词重复（新增）
                    String columnLetter = getColumnLetter(j); // 新增方法获取列字母
                    applyWordRepeatLimit(sb, columnLetter, wordRepeatLimitMap);


                    // 8. 应用长度限制
                    applyLengthLimit(sb, j, textNumMap);

                    // 9. 应用侵权词替换
                    applyInfringementReplacements(sb, qinquanMap, qinquanPatterns);

                    cell.setCellValue(sb.toString());

                }

                if (i % 100 == 0) {
                    log.info("处理进度: {}/{}", i, sheet.getLastRowNum());
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
            log.error("上传失败，请查看数据是否正确：{}", e.getMessage(), e);
            return ResultInfo.error("上传失败，请查看数据是否正确");
        }

}

        // ============== 辅助方法 ==============

        private Map<Integer, Integer> buildTextNumMap(List<TextRestrictions> restrictions) {
            Map<Integer, Integer> map = new HashMap<>();
            for (TextRestrictions tr : restrictions) {
                addToMapIfPresent(map, tr.getColumntext1(), tr.getNum1());
                addToMapIfPresent(map, tr.getColumntext2(), tr.getNum2());
                addToMapIfPresent(map, tr.getColumntext3(), tr.getNum3());
                addToMapIfPresent(map, tr.getColumntext4(), tr.getNum4());
                addToMapIfPresent(map, tr.getColumntext5(), tr.getNum5());
                addToMapIfPresent(map, tr.getColumntext6(), tr.getNum6());
                addToMapIfPresent(map, tr.getColumntext7(), tr.getNum7());
                addToMapIfPresent(map, tr.getColumntext8(), tr.getNum8());
                addToMapIfPresent(map, tr.getColumntext9(), tr.getNum9());
                addToMapIfPresent(map, tr.getColumntext10(), tr.getNum10());
            }
            return map;
        }

        private void addToMapIfPresent(Map<Integer, Integer> map, String columnText, String num) {
            if (columnText != null && !columnText.isEmpty() && num != null && !num.isEmpty()) {
                map.put(CTransformation(columnText), Integer.parseInt(num));
            }
        }

        private Map<String, String> buildQinquanMap(List<Infringement> infringements) {
            Map<String, String> map = new HashMap<>();
            for (Infringement infringement : infringements) {
                if (infringement.getText() != null && !infringement.getText().isEmpty() &&
                        infringement.getReplaceText() != null) {
                    map.put(infringement.getText(), infringement.getReplaceText());
                }
            }
            return map;
        }

        private Map<String, String[]> buildReplacementMap(List<TextRestrictions> restrictions) {
            Map<String, String[]> map = new HashMap<>();

            map.put("{keyword}", buildArray(restrictions, TextRestrictions::getKeyword));
            map.put("{longword}", buildArray(restrictions, TextRestrictions::getLongword));
            map.put("{scenario}", buildArray(restrictions, TextRestrictions::getScenario));
            map.put("{user}", buildArray(restrictions, TextRestrictions::getUser));
            map.put("{prop1}", buildArray(restrictions, TextRestrictions::getProp1));
            map.put("{prop2}", buildArray(restrictions, TextRestrictions::getProp2));
            map.put("{prop3}", buildArray(restrictions, TextRestrictions::getProp3));
            map.put("{prop4}", buildArray(restrictions, TextRestrictions::getProp4));
            map.put("{prop5}", buildArray(restrictions, TextRestrictions::getProp5));
            map.put("{luckyword}", buildArray(restrictions, TextRestrictions::getLuckword));
            map.put("{point1}", buildArray(restrictions, TextRestrictions::getPoint1));
            map.put("{point2}", buildArray(restrictions, TextRestrictions::getPoint2));
            map.put("{point3}", buildArray(restrictions, TextRestrictions::getPoint3));
            map.put("{point4}", buildArray(restrictions, TextRestrictions::getPoint4));
            map.put("{point5}", buildArray(restrictions, TextRestrictions::getPoint5));
            map.put("{description}", buildArray(restrictions, TextRestrictions::getDescription));
            map.put("{prop6}", buildArray(restrictions, TextRestrictions::getProp6));
            map.put("{prop7}", buildArray(restrictions, TextRestrictions::getProp7));
            map.put("{prop8}", buildArray(restrictions, TextRestrictions::getProp8));
            map.put("{prop9}", buildArray(restrictions, TextRestrictions::getProp9));
            map.put("{prop10}", buildArray(restrictions, TextRestrictions::getProp10));
            map.put("{prop11}", buildArray(restrictions, TextRestrictions::getProp11));
            map.put("{prop12}", buildArray(restrictions, TextRestrictions::getProp12));
            map.put("{prop13}", buildArray(restrictions, TextRestrictions::getProp13));
            map.put("{prop14}", buildArray(restrictions, TextRestrictions::getProp14));
            map.put("{prop15}", buildArray(restrictions, TextRestrictions::getProp15));
            map.put("{prop16}", buildArray(restrictions, TextRestrictions::getProp16));
            map.put("{prop17}", buildArray(restrictions, TextRestrictions::getProp17));
            map.put("{prop18}", buildArray(restrictions, TextRestrictions::getProp18));
            map.put("{prop19}", buildArray(restrictions, TextRestrictions::getProp19));
            map.put("{prop20}", buildArray(restrictions, TextRestrictions::getProp20));

            return map;
        }

        private String[] buildArray(List<TextRestrictions> restrictions,
                                    Function<TextRestrictions, String> extractor) {
            List<String> result = new ArrayList<>();
            for (TextRestrictions tr : restrictions) {
                String value = extractor.apply(tr);
                if (value != null && !value.isEmpty()) {
                    String[] parts = value.split("<br><br>");
                    for (String part : parts) {
                        if (!part.isEmpty()) {
                            result.add(part);
                        }
                    }
                }
            }
            return result.toArray(new String[0]);
        }

        private String getCellValueAsString(Cell cell) {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return String.valueOf(cell.getNumericCellValue());
                case BLANK:
                    return "";
                default:
                    return cell.getStringCellValue();
            }
        }

        private void processPlaceholders(StringBuilder sb, Map<String, String[]> replacementMap,
                                         ThreadLocalRandom random) {
            for (Map.Entry<String, String[]> entry : replacementMap.entrySet()) {
                String placeholder = entry.getKey();
                String[] replacements = entry.getValue();

                if (replacements.length == 0) continue;

                int index;
                while ((index = sb.indexOf(placeholder)) != -1) {
                    int replacementIndex = random.nextInt(replacements.length);
                    sb.replace(index, index + placeholder.length(), replacements[replacementIndex]);
                }
            }
        }

        private void applyLengthLimit(StringBuilder sb, int columnIndex, Map<Integer, Integer> textNumMap) {
            Integer maxLength = textNumMap.get(columnIndex + 1);
            if (maxLength != null && sb.length() > maxLength) {
                // 优化截断逻辑
                int endIndex = maxLength;
                if (sb.charAt(maxLength) != ' ') {
                    int lastSpace = sb.lastIndexOf(" ", maxLength);
                    if (lastSpace > 0) {
                        endIndex = lastSpace;
                    }
                }
                sb.setLength(endIndex);
            }
        }

        private void applyInfringementReplacements(StringBuilder sb, Map<String, String> qinquanMap,
                                                   Map<String, Pattern> qinquanPatterns) {
            String temp = sb.toString();
            for (Map.Entry<String, Pattern> entry : qinquanPatterns.entrySet()) {
                String key = entry.getKey();
                if (temp.contains(key)) {
                    Pattern pattern = entry.getValue();
                    String replacement = qinquanMap.get(key);
                    Matcher matcher = pattern.matcher(sb);
                    // 使用StringBuffer避免创建多个字符串
                    StringBuffer buffer = new StringBuffer();
                    while (matcher.find()) {
                        matcher.appendReplacement(buffer, replacement);
                    }
                    matcher.appendTail(buffer);
                    sb.setLength(0);
                    sb.append(buffer);
                    temp = sb.toString(); // 更新临时字符串
                }
            }
        }


//
    private String getColumnLetter(int columnIndex) {
        return String.valueOf((char) ('A' + columnIndex));
    }

    private void applyWordRepeatLimit(StringBuilder sb, String columnLetter,
                                      Map<String, Integer> wordRepeatLimitMap) {
        // 1. 参数检查
        if (wordRepeatLimitMap == null || columnLetter == null || sb == null) {
            return;
        }

        // 2. 获取重复限制次数
        Integer maxRepeats = wordRepeatLimitMap.get(columnLetter);
        if (maxRepeats == null || maxRepeats <= 0) return;

        // 3. 获取文本内容
        String text = sb.toString();
        if (text == null || text.isEmpty()) return;

        // 4. 分割单词
        String[] words = text.split("[\\s+,;.!?]+");
        if (words.length == 0) return;

        // 5. 统计单词出现次数
        Map<String, Integer> wordCounts = new HashMap<>();
        List<String> keptWords = new ArrayList<>();

        for (String word : words) {
            if (word == null || word.isEmpty()) continue;

            int count = wordCounts.getOrDefault(word, 0) + 1;
            if (count <= maxRepeats) {
                keptWords.add(word);
                wordCounts.put(word, count);
            }
        }

        // 6. 如果去重后有变化，则更新单元格内容
        if (keptWords.size() < words.length) {
            sb.setLength(0);
            sb.append(String.join(" ", keptWords));
        }
    }




    private Map<String, Integer> buildWordRepeatLimitMap(List<TextRestrictions> restrictions) {
        Map<String, Integer> map = new HashMap<>();
        if (restrictions == null || restrictions.isEmpty()) return map;

        for (TextRestrictions tr : restrictions) {
            if (tr == null) continue;

            try {
                processColumnLimit(map, tr.getColumntext1(), tr.getQuchong1());
                processColumnLimit(map, tr.getColumntext2(), tr.getQuchong2());
                processColumnLimit(map, tr.getColumntext3(), tr.getQuchong3());
                processColumnLimit(map, tr.getColumntext4(), tr.getQuchong4());
                processColumnLimit(map, tr.getColumntext5(), tr.getQuchong5());
                processColumnLimit(map, tr.getColumntext6(), tr.getQuchong6());
                processColumnLimit(map, tr.getColumntext7(), tr.getQuchong7());
                processColumnLimit(map, tr.getColumntext8(), tr.getQuchong8());
                processColumnLimit(map, tr.getColumntext9(), tr.getQuchong9());
                processColumnLimit(map, tr.getColumntext10(), tr.getQuchong10());
            } catch (Exception e) {
                log.error("Error processing text restrictions", e);
            }
        }
        return map;
    }

    private void processColumnLimit(Map<String, Integer> map, String columnText, String quchongValue) {
        if (columnText != null && quchongValue != null) {
            String columnLetter = columnText.trim();
            if (!columnLetter.isEmpty()) {
                try {
                    map.put(columnLetter, Integer.parseInt(quchongValue.trim()));
                } catch (NumberFormatException e) {
                    log.warn("Invalid number format for Quchong value: {}", quchongValue);
                }
            }
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