package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import main.common.Spec;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Paths;

public class App {
    //    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
        if (!validArgs(args)) {
            System.err.println("[usage] transfer -config file");
            System.err.println(specExample());
            return;
        }

        Spec spec = readSpecFromFile(args[1]);

        for (String driver : spec.getDrivers()) {
            Class.forName(driver);
        }

        JobRunner jobRunner = new JobRunner();
        jobRunner.runJob(spec);
    }

    private static String specExample() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, Spec.example());
        return writer.getBuffer().toString();
    }

    private static Spec readSpecFromFile(String filePath) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(Paths.get(filePath).toFile(), Spec.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static boolean validArgs(String[] args) {
        if (args.length < 2) {
            return false;
        }

        if (!StringUtils.equals("-config", args[0])) {
            return false;
        }

        return true;
    }
}
