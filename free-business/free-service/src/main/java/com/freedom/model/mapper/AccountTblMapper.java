package com.freedom.model.mapper;

import com.freedom.model.AccountTbl;

public interface AccountTblMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AccountTbl record);

    int insertSelective(AccountTbl record);

    AccountTbl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AccountTbl record);

    int updateByPrimaryKey(AccountTbl record);


}