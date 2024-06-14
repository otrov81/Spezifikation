package app.toolbox;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerClass {
    private static final Logger LOGGER = Logger.getLogger(LoggerClass.class.getName());

    public static void logInfo(String message) {
        LOGGER.log(Level.INFO, message);
    }

    public static void logWarning(String message) {
        LOGGER.log(Level.WARNING, message);
    }

    public static void logError(String message, Throwable throwable) {
        LOGGER.log(Level.SEVERE, message, throwable);
    }

    //vor use -> LoggerClass.logWarning("User not found: " + username);
}
