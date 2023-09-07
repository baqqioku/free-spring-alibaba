package com.freedom.model.mapper;

import com.freedom.model.AccountTbl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountTblNextMapper extends AccountTblMapper{

    int batchInsert(@Param("list") List<AccountTbl> list);



}