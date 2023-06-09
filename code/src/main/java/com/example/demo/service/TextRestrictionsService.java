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
    List<TextRestrictions> getListById(int id);

    /**
     * 查询模板
     */
    List<TextRestrictions> getMuBan(int id);

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
     * 查询产品名称
     */
    List<TextRestrictions> getName(String founder,String product);

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
    boolean update(String column,String value,int id);

    /**
     * 修改
     */
    boolean updateMuBan(TextRestrictions textRestrictions);


    /**
     * 修改
     */
    boolean updateMuBanById(TextRestrictions textRestrictions);

    /**
     * 修改
     */
    boolean updateById(String column,String value,int textId);

    /**
     * 删除
     *
     * @param idList 根据id集合删除
     * @return 是否删除成功
     */
    boolean delete(List<Integer> idList);

    boolean deleteid(int id);
}
