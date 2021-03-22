package com.haroldgao.log;

/**
 * ANSI color, see <a href="https://misc.flogisoft.com/bash/tip_colors_and_formatting">
 *     misc.flogisoft.com</a>
 *
 * @since v4
 * @author Harold Gao
 */
public enum Colors {
    BOLD(1), DIM(2), UNDERLINE(4),
    RED(31), GREEN(32), YELLOW(33), BLUE(34),
    END(0);

    Colors(int val) {
        this.val = "\033[" + val + "m";
    }

    private final String val;

    public String value() {
        return val;
    }

    public static String colorize(String s, Colors...colors) {
        StringBuilder sb = new StringBuilder();
        for (Colors color : colors) {
            sb.append(color.value());
        }
        sb.append(s);
        sb.append(Colors.END.value());
        return sb.toString();
    }
}
