package main.source;

import main.Job;
import main.common.ColumnMeta;
import main.common.DataSourceUtil;
import main.common.Rows;
import main.common.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Map;
import java.util.function.Consumer;

public class SourceExtractor implements Job<Void, Rows>, DataSourceUtil {
    private static final Logger logger = LoggerFactory.getLogger(SourceExtractor.class);
    private final DataSource dataSource;
    private final Table table;
    private final int start;
    private final int end;
    private final String querySql;

    public SourceExtractor(DataSource dataSource, Table table, int start, int end) {
        this.dataSource = dataSource;
        this.table = table;
        this.start = start;
        this.end = end;
        this.querySql = table.getSelectSql();
    }

    @Override
    public void apply(Void nothing, Consumer<Rows> consumer) {
        // fetch data from database, and provide them to consumer
        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setAutoCommit(false);
                connection.setReadOnly(true);
                Map<String, ColumnMeta> columnMeta = getTableMetaData(connection, table.getName());
                query(connection, columnMeta, consumer);
            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void query(Connection connection, Map<String, ColumnMeta> columnMeta, Consumer<Rows> consumer)
        throws SQLException {
        int offset = start;
        int limit = 1000;

        while (offset < end) {
            if (end - offset < limit) {
                limit = end - offset;
            }
            connection.setAutoCommit(false);

            queryPage(connection, columnMeta, consumer, offset, limit);

            connection.commit();
            offset += limit;
        }
    }

    private void queryPage(Connection connection, Map<String, ColumnMeta> columnMeta, Consumer<Rows> consumer,
        int offset, int limit) throws SQLException {
        logger.info("queryPage: {} with {}, {}", querySql, offset, limit);
        try (PreparedStatement ps = connection.prepareStatement(querySql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            ps.setFetchSize(100);
            ps.setFetchDirection(ResultSet.FETCH_FORWARD);
            try (ResultSet rs = ps.executeQuery()) {
                Rows rows = new Rows();
                ResultSetMetaData resultSetMetaData = null;
                while (rs.next()) {
                    if (resultSetMetaData == null) {
                        resultSetMetaData = rs.getMetaData();
                    }
                    Object[] row = fetchData(rs, columnMeta, resultSetMetaData);
                    rows.add(row);
                }
                rows.setColumnMeta(columnMeta);
                consumer.accept(rows);
            }
        }
    }

    private Object[] fetchData(ResultSet rs, Map<String, ColumnMeta> columnMetaMap, ResultSetMetaData resultSetMetaData)
        throws SQLException {

        Object[] row = new Object[resultSetMetaData.getColumnCount()];

        for (int i = 0; i < table.getColumns().size(); i++) {
            String column = table.getColumns().get(i);
            ColumnMeta meta = columnMetaMap.get(column);
            Object value = meta.getColumnValue(rs);
            if (!rs.wasNull()) {
                row[i] = value;
            }
        }

        return row;
    }


}
