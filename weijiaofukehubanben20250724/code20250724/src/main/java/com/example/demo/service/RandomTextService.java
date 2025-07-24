package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.RandomText;
import com.example.demo.entity.TextRestrictions;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hui
 * @date 2022/8/19 10:14
 */
@Service
public interface RandomTextService extends IService<RandomText> {

    /**
     * 查询所有
     */
    List<RandomText> getList();

    /**
     * 查询所有
     */
    List<RandomText> getListByUser(String founder);

    /**
     * 根据列名查询
     */
    List<RandomText> queryList(String this_column);

    /**
     * 根据列名查询
     */
    List<RandomText> queryListByName(String this_column,String founder);

    /**
     * 根据列名查询
     */
    List<RandomText> getMaxId();

    /**
     * 查询相同名称
     */
    List<RandomText> getName(String founder,String product);

    /**
     * 添加
     */
    RandomText add(RandomText randomText);

    /**
     * 添加
     */
    boolean insert(RandomText randomText);

    /**
     * 添加
     */
    boolean insertById(RandomText randomText);

    /**
     * 修改
     */
    boolean update(String keyword,String longword,String scenario,String user,String prop1,String prop2,String prop3,String prop4,String prop5,String luckword,String point1,String point2,String point3,String point4,String point5,String description,String prop6,String prop7,String prop8,String prop9,String prop10,String prop11,String prop12,String prop13,String prop14,String prop15,String prop16,String prop17,String prop18,String prop19,String prop20,int id,String product);

    /**
     * 修改
     */
    boolean updateById(String keyword,String longword,String scenario,String user,String prop1,String prop2,String prop3,String prop4,String prop5,String luckword,String point1,String point2,String point3,String point4,String point5,String description,String prop6,String prop7,String prop8,String prop9,String prop10,String prop11,String prop12,String prop13,String prop14,String prop15,String prop16,String prop17,String prop18,String prop19,String prop20,int id,String product);

    /**
     * 删除
     *
     * @param idList 根据id集合删除
     * @return 是否删除成功
     */
    boolean delete(List<Integer> idList);

    boolean deleteid(int id);
}
