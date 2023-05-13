package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.TextRestrictions;
import com.example.demo.entity.UserInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author hui
 * @date 2022/8/19 10:14
 */
@Service
public interface TextRestrictionsService extends IService<TextRestrictions> {

    /**
     * 查询所有
     */
    List<TextRestrictions> getList();

    /**
     * 查询所有
     */
    List<TextRestrictions> getListByUser(String founder);

    /**
     * 根据列名查询
     */
    List<TextRestrictions> queryList(String this_column);

    /**
     * 根据列名查询
     */
    List<TextRestrictions> queryListByName(String this_column,String founder);

    /**
     * 根据列名查询
     */
    List<TextRestrictions> getMaxId();

    /**
     * 添加
     */
    TextRestrictions add(TextRestrictions textRestrictions);

    /**
     * 添加
     */
    boolean insert(TextRestrictions textRestrictions);

    /**
     * 添加
     */
    boolean insertById(TextRestrictions textRestrictions);

    /**
     * 修改
     */
    boolean update(String columntext,String num,int id,String product);

    /**
     * 修改
     */
    boolean updateById(String columntext,String num,int id,String product);

    /**
     * 删除
     *
     * @param idList 根据id集合删除
     * @return 是否删除成功
     */
    boolean delete(List<Integer> idList);

    boolean deleteid(int id);
}
