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
        return textRestrictionsMapper.insert(textRestrictions.getColumntext(),textRestrictions.getNum(),textRestrictions.getFounder(),textRestrictions.getProduct());
    }

    @Override
    public boolean insertById(TextRestrictions textRestrictions) {
        return textRestrictionsMapper.insertById(textRestrictions.getColumntext(),textRestrictions.getNum(),textRestrictions.getFounder(),textRestrictions.getTextId(),textRestrictions.getProduct());
    }

    @Override
    public boolean update(String columntext,String num,int id,String product) {
        return textRestrictionsMapper.update(columntext,num,id,product);
    }

    @Override
    public boolean updateById(String columntext,String num,int id,String product) {
        return textRestrictionsMapper.updateById(columntext,num,id,product);
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
