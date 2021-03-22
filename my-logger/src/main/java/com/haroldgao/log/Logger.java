package com.haroldgao.log;

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
            Colors.colorize("ERROR", Colors.BOLD, Colors.RED) + "] ";

    /**
     * Simple information
     * @param msg
     */
    public static void info(Object msg) {
        System.out.println(INFO + msg);
    }

    /**
     * Error information
     * @param msg
     */
    public static void error(Object msg) {
        System.out.println(ERROR + msg);
    }

    /**
     * Warning information
     * @param msg
     */
    public static void warning(Object msg) {
        System.out.println(WARNING + msg);
    }

    /**
     * Highlight message in log
     * @param msg
     */
    public static void highlight(String msg) {
        System.out.println("## " + Colors.colorize(msg, Colors.BOLD, Colors.GREEN));
    }
}
