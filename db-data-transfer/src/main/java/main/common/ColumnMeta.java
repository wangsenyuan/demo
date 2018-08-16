package main.common;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class ColumnMeta {
    private String columnName;
    private int columnType;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getColumnType() {
        return columnType;
    }

    public void setColumnType(int columnType) {
        this.columnType = columnType;
    }

    public Object getColumnValue(ResultSet rs) throws SQLException {
        switch (columnType) {
            case Types.DATE:
            case Types.TIME:
            case Types.TIME_WITH_TIMEZONE:
            case Types.TIMESTAMP:
            case Types.TIMESTAMP_WITH_TIMEZONE:
                return rs.getTimestamp(columnName);
            case Types.VARCHAR:
            case Types.CHAR:
                return rs.getString(columnName);
            case Types.INTEGER:
            case Types.BIGINT:
                return rs.getLong(columnName);
            case Types.BIT:
            case Types.BOOLEAN:
                return rs.getBoolean(columnName);
            default:
                return rs.getObject(columnName);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("columnName", columnName).append("columnType", columnType).toString();
    }
}
