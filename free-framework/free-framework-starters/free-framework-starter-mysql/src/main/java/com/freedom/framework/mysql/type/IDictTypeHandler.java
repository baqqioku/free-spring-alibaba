package com.freedom.framework.mysql.type;


import com.freedom.common.model.IDict;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

/**
 * 字典接口类型处理器
 *
 * @author R
 * @date 2022-11-28
 */
public class IDictTypeHandler<K, T extends IDict<K>> extends BaseTypeHandler<IDict<K>> {
    private final Class<T> type;
    private final Class<K> genericType;

    @SuppressWarnings("unchecked")
    public IDictTypeHandler(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
        this.genericType = Stream.of(type.getGenericInterfaces())
                .map(ParameterizedTypeImpl.class::cast)
                .filter(genericInterface -> genericInterface.getRawType() == IDict.class)
                .findFirst()
                .map(genericInterface -> (Class<K>) genericInterface.getActualTypeArguments()[0])
                .orElseThrow(() -> new RuntimeException("orm中使用的枚举类必须实现IDict接口"));

    }


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, IDict<K> parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter.getCode());
    }

    @Override
    public IDict<K> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        K code = rs.getObject(columnName, genericType);
        return IDict.getByCode(type, code);
    }

    @Override
    public IDict<K> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        K code = rs.getObject(columnIndex, genericType);
        return IDict.getByCode(type, code);
    }

    @Override
    public IDict<K> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        K code = cs.getObject(columnIndex, genericType);
        return IDict.getByCode(type, code);
    }
}
