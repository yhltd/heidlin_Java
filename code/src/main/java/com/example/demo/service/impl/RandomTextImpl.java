package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.RandomText;
import com.example.demo.entity.TextRestrictions;
import com.example.demo.mapper.RandomTextMapper;
import com.example.demo.mapper.TextRestrictionsMapper;
import com.example.demo.service.RandomTextService;
import com.example.demo.service.TextRestrictionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hui
 * @date 2022/8/19 10:16
 */
@Service
public class RandomTextImpl extends ServiceImpl<RandomTextMapper, RandomText> implements RandomTextService {
    @Autowired
    RandomTextMapper randomTextMapper;

    @Override
    public List<RandomText> getList() {
        return randomTextMapper.getList();
    }

    @Override
    public List<RandomText> getListByUser(String founder) {
        return randomTextMapper.getListByUser(founder);
    }

    @Override
    public List<RandomText> queryList(String this_column) {
        return randomTextMapper.queryList(this_column);
    }

    @Override
    public List<RandomText> queryListByName(String this_column, String founder) {
        return randomTextMapper.queryListByUser(this_column,founder);
    }

    @Override
    public List<RandomText> getMaxId() {
        return randomTextMapper.getMaxId();
    }

    @Override
    public RandomText add(RandomText randomText) {
        return save(randomText) ? randomText : null;
    }

    @Override
    public boolean insert(RandomText randomText) {
        return randomTextMapper.insert(randomText.getKeyword(),randomText.getLongword(),randomText.getScenario(),randomText.getUser(),randomText.getProp1(),randomText.getProp2(),randomText.getProp3(),randomText.getProp4(),randomText.getProp5(),randomText.getLuckword(),randomText.getPoint1(),randomText.getPoint2(),randomText.getPoint3(),randomText.getPoint4(),randomText.getPoint5(),randomText.getDescription(),randomText.getProp6(),randomText.getProp7(),randomText.getProp8(),randomText.getProp9(),randomText.getProp10(),randomText.getProp11(),randomText.getProp12(),randomText.getProp13(),randomText.getProp14(),randomText.getProp15(),randomText.getProp16(),randomText.getProp17(),randomText.getProp18(),randomText.getProp19(),randomText.getProp20(),randomText.getFounder(),randomText.getProduct());
    }

    @Override
    public boolean insertById(RandomText randomText) {
        return randomTextMapper.insertById(randomText.getKeyword(),randomText.getLongword(),randomText.getScenario(),randomText.getUser(),randomText.getProp1(),randomText.getProp2(),randomText.getProp3(),randomText.getProp4(),randomText.getProp5(),randomText.getLuckword(),randomText.getPoint1(),randomText.getPoint2(),randomText.getPoint3(),randomText.getPoint4(),randomText.getPoint5(),randomText.getDescription(),randomText.getProp6(),randomText.getProp7(),randomText.getProp8(),randomText.getProp9(),randomText.getProp10(),randomText.getProp11(),randomText.getProp12(),randomText.getProp13(),randomText.getProp14(),randomText.getProp15(),randomText.getProp16(),randomText.getProp17(),randomText.getProp18(),randomText.getProp19(),randomText.getProp20(),randomText.getFounder(),randomText.getTextId(),randomText.getProduct());
    }

    @Override
    public boolean update(String keyword,String longword,String scenario,String user,String prop1,String prop2,String prop3,String prop4,String prop5,String luckword,String point1,String point2,String point3,String point4,String point5,String description,String prop6,String prop7,String prop8,String prop9,String prop10,String prop11,String prop12,String prop13,String prop14,String prop15,String prop16,String prop17,String prop18,String prop19,String prop20,int id,String product) {
        return randomTextMapper.update(keyword,longword,scenario,user,prop1,prop2,prop3,prop4,prop5,luckword,point1,point2,point3,point4,point5,description,prop6,prop7,prop8,prop9,prop10,prop11,prop12,prop13,prop14,prop15,prop16,prop17,prop18,prop19,prop20,id,product);
    }

    @Override
    public boolean updateById(String keyword,String longword,String scenario,String user,String prop1,String prop2,String prop3,String prop4,String prop5,String luckword,String point1,String point2,String point3,String point4,String point5,String description,String prop6,String prop7,String prop8,String prop9,String prop10,String prop11,String prop12,String prop13,String prop14,String prop15,String prop16,String prop17,String prop18,String prop19,String prop20,int id,String product) {
        return randomTextMapper.updateById(keyword,longword,scenario,user,prop1,prop2,prop3,prop4,prop5,luckword,point1,point2,point3,point4,point5,description,prop6,prop7,prop8,prop9,prop10,prop11,prop12,prop13,prop14,prop15,prop16,prop17,prop18,prop19,prop20,id,product);
    }

    @Override
    public boolean delete(List<Integer> idList) {
        return removeByIds(idList);
    }

    @Override
    public boolean deleteid(int id) {
        return randomTextMapper.deleteid(id);
    }
}
