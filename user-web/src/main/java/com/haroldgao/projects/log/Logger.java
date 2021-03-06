package com.haroldgao.projects.log;

/**
 * Logger
 *
 * @since v4
 * @author Harold Gao
 */
public class Logger {
    private static final String INFO = "[" +
            Colors.colorize("INFO", Colors.BOLD, Colors.BLUE) + "] ";
    private static final String WARNING = "[" +
            Colors.colorize("WARNING", Colors.BOLD, Colors.YELLOW) + "] ";
    private static final String ERROR = "[" +
            Colors.colorize("ERROR", Colors.BOLD, Colors.RED) + "]";

    public static void info(String msg) {
        System.out.println(INFO + msg);
    }

    public static void error(String msg) {
        System.out.println(ERROR + msg);
    }

    public static void warning(String msg) {
        System.out.println(WARNING + msg);
    }
}
