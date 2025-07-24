package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Infringement;
import com.example.demo.entity.TextRestrictions;
import com.example.demo.mapper.InfringementMapper;
import com.example.demo.mapper.TextRestrictionsMapper;
import com.example.demo.service.InfringementService;
import com.example.demo.service.TextRestrictionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hui
 * @date 2022/8/19 10:16
 */
@Service
public class InfringementImpl extends ServiceImpl<InfringementMapper, Infringement> implements InfringementService {
    @Autowired
    InfringementMapper infringementMapper;

    @Override
    public List<Infringement> getList() {
        return infringementMapper.getList();
    }

    @Override
    public List<Infringement> queryList(String this_column) {
        return infringementMapper.queryList(this_column);
    }

    @Override
    public Infringement add(Infringement infringement) {
        return save(infringement) ? infringement : null;
    }

    @Override
    public boolean insert(Infringement infringement) {
        return infringementMapper.insert(infringement.getText(),infringement.getReplaceText());
    }

    @Override
    public boolean update(String text,String replaceText,int id) {
        return infringementMapper.update(text,replaceText,id);
    }

    @Override
    public boolean delete(List<Integer> idList) {
        return removeByIds(idList);
    }
}
