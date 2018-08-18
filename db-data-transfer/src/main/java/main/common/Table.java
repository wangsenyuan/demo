package main.common;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class Table {
    private String name;
    private List<String> columns;
    private String keyColumn;

    public static Table example(String name) {
        List<String> columns = Arrays.asList("a", "b", "c", "e");
        Table table = new Table();
        table.setName(name);
        table.setColumns(columns);
        table.setKeyColumn("c");
        return table;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public String getInsertSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ");
        sb.append(name);
        sb.append(" ( ");
        for (int i = 0; i < columns.size(); i++) {
            sb.append(columns.get(i));
            if (i < columns.size() - 1) {
                sb.append(", ");
            } else {
                sb.append(" ) ");
            }
        }
        sb.append(" values (");

        for (int i = 0; i < columns.size(); i++) {
            sb.append("?");
            if (i < columns.size() - 1) {
                sb.append(", ");
            } else {
                sb.append(" ) ");
            }
        }

        return sb.toString();
    }

    public String getSelectSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        for (int i = 0; i < columns.size(); i++) {
            sb.append(columns.get(i));
            if (i < columns.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(" from ");
        sb.append(name);
        if (StringUtils.isNotEmpty(keyColumn)) {
            sb.append(" order by ").append(keyColumn).append(" asc ");
        }
        sb.append(" offset ? limit ? ");
        return sb.toString();
    }

    public String getCountSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(1) from ");
        sb.append(name);
        return sb.toString();
    }

    public String getKeyColumn() {
        return keyColumn;
    }

    public void setKeyColumn(String keyColumn) {
        this.keyColumn = keyColumn;
    }
}
