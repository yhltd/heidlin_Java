package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.TextRestrictions;
import com.example.demo.mapper.TextRestrictionsMapper;
import com.example.demo.service.TextRestrictionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hui
 * @date 2022/8/19 10:16
 */
@Service
public class TextRestrictionsImpl extends ServiceImpl<TextRestrictionsMapper, TextRestrictions> implements TextRestrictionsService {
    @Autowired
    TextRestrictionsMapper textRestrictionsMapper;

    @Override
    public List<TextRestrictions> getList() {
        return textRestrictionsMapper.getList();
    }

    @Override
    public List<TextRestrictions> getListById(int id) {
        return textRestrictionsMapper.getListById(id);
    }

    @Override
    public List<TextRestrictions> getMuBan(int id) {
        return textRestrictionsMapper.getMuBan(id);
    }

    @Override
    public List<TextRestrictions> getListByUser(String founder) {
        return textRestrictionsMapper.getListByUser(founder);
    }

    @Override
    public List<TextRestrictions> queryList(String this_column) {
        return textRestrictionsMapper.queryList(this_column);
    }

    @Override
    public List<TextRestrictions> queryListByName(String this_column, String founder) {
        return textRestrictionsMapper.queryListByUser(this_column,founder);
    }

    @Override
    public List<TextRestrictions> getMaxId() {
        return textRestrictionsMapper.getMaxId();
    }

    @Override
    public List<TextRestrictions> getName(String founder,String product) {
        return textRestrictionsMapper.getName(founder,product);
    }

    @Override
    public TextRestrictions add(TextRestrictions textRestrictions) {
        return save(textRestrictions) ? textRestrictions : null;
    }

    @Override
    public boolean insert(TextRestrictions textRestrictions) {
        return textRestrictionsMapper.insert(textRestrictions.getFounder());
    }

    @Override
    public boolean insertById(TextRestrictions textRestrictions) {
        return textRestrictionsMapper.insertById(textRestrictions.getFounder(),textRestrictions.getTextId(),textRestrictions.getColumntext1(),textRestrictions.getNum1(),textRestrictions.getColumntext2(),textRestrictions.getNum2(),textRestrictions.getColumntext3(),textRestrictions.getNum3(),textRestrictions.getColumntext4(),textRestrictions.getNum4(),textRestrictions.getColumntext5(),textRestrictions.getNum5(),textRestrictions.getColumntext6(),textRestrictions.getNum6(),textRestrictions.getColumntext7(),textRestrictions.getNum7(),textRestrictions.getColumntext8(),textRestrictions.getNum8(),textRestrictions.getColumntext9(),textRestrictions.getNum9(),textRestrictions.getColumntext10(),textRestrictions.getNum10(),textRestrictions.getKeyword(),textRestrictions.getLongword(),textRestrictions.getScenario(),textRestrictions.getUser(),textRestrictions.getProp1(),textRestrictions.getProp2(),textRestrictions.getProp3(),textRestrictions.getProp4(),textRestrictions.getProp5(),textRestrictions.getLuckword(),textRestrictions.getPoint1(),textRestrictions.getPoint2(),textRestrictions.getPoint3(),textRestrictions.getPoint4(),textRestrictions.getPoint5(),textRestrictions.getDescription(),textRestrictions.getProp6(),textRestrictions.getProp7(),textRestrictions.getProp8(),textRestrictions.getProp9(),textRestrictions.getProp10(),textRestrictions.getProp11(),textRestrictions.getProp12(),textRestrictions.getProp13(),textRestrictions.getProp14(),textRestrictions.getProp15(),textRestrictions.getProp16(),textRestrictions.getProp17(),textRestrictions.getProp18(),textRestrictions.getProp19(),textRestrictions.getProp20(),textRestrictions.getProduct(),textRestrictions.getTitle(),textRestrictions.getTitleColor());
    }

    @Override
    public boolean insertById2(TextRestrictions textRestrictions) {
        return textRestrictionsMapper.insertById2(textRestrictions.getFounder(),textRestrictions.getTextId());
    }

    @Override
    public boolean update(String column,String value,int id) {
        return textRestrictionsMapper.update(column,value,id);
    }

    @Override
    public boolean updateMuBan(TextRestrictions textRestrictions) {
        return textRestrictionsMapper.updateMuBan(textRestrictions.getKeyword(),textRestrictions.getLongword(),textRestrictions.getScenario(),textRestrictions.getUser(),textRestrictions.getProp1(),textRestrictions.getProp2(),textRestrictions.getProp3(),textRestrictions.getProp4(),textRestrictions.getProp5(),textRestrictions.getLuckword(),textRestrictions.getPoint1(),textRestrictions.getPoint2(),textRestrictions.getPoint3(),textRestrictions.getPoint4(),textRestrictions.getPoint5(),textRestrictions.getDescription(),textRestrictions.getProp6(),textRestrictions.getProp7(),textRestrictions.getProp8(),textRestrictions.getProp9(),textRestrictions.getProp10(),textRestrictions.getProp11(),textRestrictions.getProp12(),textRestrictions.getProp13(),textRestrictions.getProp14(),textRestrictions.getProp15(),textRestrictions.getProp16(),textRestrictions.getProp17(),textRestrictions.getProp18(),textRestrictions.getProp19(),textRestrictions.getProp20(),textRestrictions.getId(),textRestrictions.getProduct(),textRestrictions.getTitle(),textRestrictions.getTitleColor());
    }

    @Override
    public boolean updateMuBanById(TextRestrictions textRestrictions) {
        return textRestrictionsMapper.updateMuBanById(textRestrictions.getKeyword(),textRestrictions.getLongword(),textRestrictions.getScenario(),textRestrictions.getUser(),textRestrictions.getProp1(),textRestrictions.getProp2(),textRestrictions.getProp3(),textRestrictions.getProp4(),textRestrictions.getProp5(),textRestrictions.getLuckword(),textRestrictions.getPoint1(),textRestrictions.getPoint2(),textRestrictions.getPoint3(),textRestrictions.getPoint4(),textRestrictions.getPoint5(),textRestrictions.getDescription(),textRestrictions.getProp6(),textRestrictions.getProp7(),textRestrictions.getProp8(),textRestrictions.getProp9(),textRestrictions.getProp10(),textRestrictions.getProp11(),textRestrictions.getProp12(),textRestrictions.getProp13(),textRestrictions.getProp14(),textRestrictions.getProp15(),textRestrictions.getProp16(),textRestrictions.getProp17(),textRestrictions.getProp18(),textRestrictions.getProp19(),textRestrictions.getProp20(),textRestrictions.getId(),textRestrictions.getProduct(),textRestrictions.getTitle(),textRestrictions.getTitleColor());
    }

    @Override
    public boolean updateById(String column,String value,int textId) {
        return textRestrictionsMapper.updateById(column,value,textId);
    }

    @Override
    public boolean delete(List<Integer> idList) {
        return removeByIds(idList);
    }

    @Override
    public boolean deleteid(int id) {
        return textRestrictionsMapper.deleteid(id);
    }
}
