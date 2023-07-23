package com.freedom.model.dao;

import com.freedom.model.TUserBalanceTcc;

public interface TUserBalanceTccDao {
    int deleteByPrimaryKey(Long id);

    int insert(TUserBalanceTcc record);

    int insertSelective(TUserBalanceTcc record);

    TUserBalanceTcc selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TUserBalanceTcc record);

    int updateByPrimaryKey(TUserBalanceTcc record);
}