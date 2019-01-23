package bpa_project;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @file Logger.java
 * @author Dakota Taylor
 * @createdOn Tuesday, 22 January, 2019
 */

public class Logging {
    private static FileHandler fileHandler = null;

    private static void configurateLogger(Logger logger) {
        try {
            if (fileHandler == null) {
                fileHandler = new FileHandler(new File("test.log").getAbsolutePath(), true);
                SimpleFormatter formatter = new SimpleFormatter();
                fileHandler.setFormatter(formatter);
            }
            logger.addHandler(fileHandler);
        } catch (IOException ex) {
        }
    }

    public static Logger getLogger(String name) {
        Logger logger = Logger.getLogger(name);
        configurateLogger(logger);
        return logger;
    }
}