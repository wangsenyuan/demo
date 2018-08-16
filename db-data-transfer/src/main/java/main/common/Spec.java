package main.common;

import java.util.Arrays;
import java.util.List;

public class Spec {
    private Config source;
    private Config sink;

    private List<Table> tables;

    private List<String> drivers;

    private int jobBatchSize;

    public static Spec example() {
        Config source = Config.example();
        Config sink = Config.example();
        Table a = Table.example("A");
        Table b = Table.example("B");
        Spec spec = new Spec();
        spec.setSource(source);
        spec.setSink(sink);
        spec.setTables(Arrays.asList(a, b));
        spec.setDrivers(Arrays.asList("com.mysql.jdbc.Driver", "org.postgresql.Driver"));
        return spec;
    }

    public Config getSource() {
        return source;
    }

    public void setSource(Config source) {
        this.source = source;
    }

    public Config getSink() {
        return sink;
    }

    public void setSink(Config sink) {
        this.sink = sink;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public int getJobBatchSize() {
        return jobBatchSize;
    }

    public void setJobBatchSize(int jobBatchSize) {
        this.jobBatchSize = jobBatchSize;
    }

    public List<String> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<String> drivers) {
        this.drivers = drivers;
    }
}
