package com.tourneynizer.tourneynizer.helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class JDBCHelper {

    public static void setNullable(PreparedStatement preparedStatement, int pos, Long x) throws SQLException {
        if (x == null) {
            preparedStatement.setNull(pos, Types.BIGINT);
        }
        else {
            preparedStatement.setLong(pos, x);
        }
    }

    public static Long getNullableLong(ResultSet resultSet, int pos) throws SQLException {
        Long x = resultSet.getLong(pos);
        if (resultSet.wasNull()) { return null; }
        return x;
    }
}
