package com.asciugano.engine.handlers.mouse;

public class MouseHandler {
    public static float scroll;
    public static float dx, dy;
    public static float mouseX;
    public static float mouseY;

    public static boolean RIGHT_PRESSED = false;
    public static boolean LEFT_PRESSED = false;
    public static boolean WHEEL_PRESSED = false;

    public static void reset() {
        scroll = 0;
        dx = 0;
        dy = 0;

        RIGHT_PRESSED = false;
        LEFT_PRESSED = false;
        WHEEL_PRESSED = false;
    }
}
