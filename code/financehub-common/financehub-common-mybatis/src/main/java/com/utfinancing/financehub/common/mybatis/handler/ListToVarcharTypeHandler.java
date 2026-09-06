package com.utfinancing.financehub.common.mybatis.handler;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;


/**
 * @Author : lixin
 * @Date : Create in 19/12/2023
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(List.class)
public class ListToVarcharTypeHandler implements TypeHandler<List<String>>{


    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, List<String> strings, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, StrUtil.join(",", strings));
    }

    @Override
    public List<String> getResult(ResultSet resultSet, String s) throws SQLException {
        String resultString = resultSet.getString(s);
        if (StringUtils.isNotEmpty(resultString)) {
            return Arrays.asList(resultString.split(","));
        }
        return null;
    }

    @Override
    public List<String> getResult(ResultSet resultSet, int i) throws SQLException {
        String resultString = resultSet.getString(i);
        if (StringUtils.isNotEmpty(resultString)) {
            return Arrays.asList(resultString.split(","));
        }
        return null;
    }

    @Override
    public List<String> getResult(CallableStatement callableStatement, int i) throws SQLException {
        String resultString = callableStatement.getString(i);
        if (StringUtils.isNotEmpty(resultString)) {
            return Arrays.asList(resultString.split(","));
        }
        return null;
    }
}
