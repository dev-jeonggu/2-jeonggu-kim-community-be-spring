package com.board.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {
    private static final Logger log = LoggerFactory.getLogger(LoggerUtil.class);

    // Debug 로그 출력
    public static void debug(String message, Object... args) {
        log.debug(message, args);
    }

    // Info 로그 출력
    public static void info(String message, Object... args) {
        log.info(message, args);
    }

    // Warn 로그 출력
    public static void warn(String message, Object... args) {
        log.warn(message, args);
    }

    // Error 로그 출력
    public static void error(String message, Object... args) {
        log.error(message, args);
    }
}
