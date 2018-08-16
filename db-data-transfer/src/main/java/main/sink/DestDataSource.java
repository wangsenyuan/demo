package main.sink;

import main.Job;
import main.common.ColumnMeta;
import main.common.DataSourceUtil;
import main.common.Rows;
import main.common.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.Consumer;

public class DestDataSource implements Job<Rows, Void>, DataSourceUtil {
    private static final Logger logger = LoggerFactory.getLogger(DestDataSource.class);
    private final DataSource dataSource;
    private final Table table;
    private final String sql;

    public DestDataSource(DataSource dataSource, Table table) {
        this.dataSource = dataSource;
        this.table = table;
        this.sql = table.getInsertSql();
    }

    @Override
    public void apply(Rows rows, Consumer<Void> consumer) {
        //        logger.info("rows column meta {}", rows.getColumnMeta());
        try (Connection connection = dataSource.getConnection()) {
            Map<String, ColumnMeta> columnsMeta = getTableMetaData(connection, table.getName());

            execute(connection, rows, columnsMeta);
            consumer.accept(null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void execute(Connection connection, Rows rows, Map<String, ColumnMeta> columnsMeta) throws SQLException {
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (Object[] row : rows.getData()) {
                    if (row == null || row.length == 0) {
                        logger.error("null row got");
                        continue;
                    }

                    for (int i = 0; i < table.getColumns().size(); i++) {
                        String column = table.getColumns().get(i);
                        ColumnMeta meta = columnsMeta.get(column);
                        Object value = row[i];
                        if (value == null) {
                            statement.setNull(i + 1, meta.getColumnType());
                        } else {
                            statement.setObject(i + 1, value, meta.getColumnType());
                        }
                    }

                    statement.addBatch();
                }
                statement.executeBatch();
            }
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        }
    }
}
