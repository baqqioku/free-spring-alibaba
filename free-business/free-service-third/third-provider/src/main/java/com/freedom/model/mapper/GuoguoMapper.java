package com.freedom.model.mapper;

import com.freedom.model.Guoguo;

public interface GuoguoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Guoguo record);

    int insertSelective(Guoguo record);

    Guoguo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Guoguo record);

    int updateByPrimaryKey(Guoguo record);
}