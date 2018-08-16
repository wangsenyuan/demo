package main;

import main.common.Config;
import main.common.Spec;
import main.common.Table;
import main.sink.DestDataSource;
import main.source.SourceExtractor;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JobManager {
    public List<Job<Void, Void>> createJobs(Spec spec) {
        return spec.getTables().stream().flatMap(table -> createJobForTable(spec, table)).collect(Collectors.toList());
    }

    private Stream<? extends Job<Void, Void>> createJobForTable(Spec spec, Table table) {
        DataSource source = createDataSource(spec.getSource());
        DataSource sink = createDataSource(spec.getSink());

        DestDataSource destDataSource = new DestDataSource(sink, table);

        String countSql = table.getCountSql();

        int total = count(source, countSql);

        if (total == 0) {
            return Stream.empty();
        }

        int batchSize = spec.getJobBatchSize();

        List<Job<Void, Void>> jobs = new ArrayList<>();
        int offset = 0;
        while (offset < total) {
            int end = offset + batchSize;
            if (end > total) {
                end = total;
            }
            SourceExtractor job = new SourceExtractor(source, table, offset, end);
            jobs.add(job.pipe(destDataSource));
            offset = end;
        }

        return jobs.stream();
    }


    private int count(DataSource source, String countSql) {
        try (Connection connection = source.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(countSql);
            ResultSet rs = preparedStatement.executeQuery();) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private DataSource createDataSource(Config config) {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(config.getUrl());
        ds.setUsername(config.getUser());
        ds.setPassword(config.getPassword());
        ds.setMinIdle(3);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
        return ds;
    }
}
