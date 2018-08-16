package main.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Rows {
    private List<Object[]> rows = new ArrayList<>();
    private Map<String, ColumnMeta> columnMeta;

    public void setColumnMeta(Map<String, ColumnMeta> columnMeta) {
        this.columnMeta = columnMeta;
    }

    public Map<String, ColumnMeta> getColumnMeta() {
        return columnMeta;
    }

    public void add(Object[] row) {
        rows.add(row);
    }

    public Iterable<? extends Object[]> getData() {
        return rows;
    }
}
