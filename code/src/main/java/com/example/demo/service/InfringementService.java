package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Infringement;
import com.example.demo.entity.TextRestrictions;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hui
 * @date 2022/8/19 10:14
 */
@Service
public interface InfringementService extends IService<Infringement> {

    /**
     * 查询所有
     */
    List<Infringement> getList();

    /**
     * 根据列名查询
     */
    List<Infringement> queryList(String this_column);

    /**
     * 添加
     */
    Infringement add(Infringement infringement);

    /**
     * 添加
     */
    boolean insert(Infringement infringement);

    /**
     * 修改
     */
    boolean update(String product,String text,String replaceText,int id);

    /**
     * 删除
     *
     * @param idList 根据id集合删除
     * @return 是否删除成功
     */
    boolean delete(List<Integer> idList);
}
