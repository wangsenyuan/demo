package main.common;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public interface DataSourceUtil {

    default Map<String, ColumnMeta> getTableMetaData(Connection connection, String table) throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        Map<String, ColumnMeta> result = new HashMap<>();
        try (ResultSet rs = databaseMetaData.getColumns(null, null, table, null)) {
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                int columnType = rs.getInt("DATA_TYPE");
                ColumnMeta meta = new ColumnMeta();
                meta.setColumnName(columnName);
                meta.setColumnType(columnType);
                result.put(columnName, meta);
            }
        }
        return result;
    }
}
