package org.fabulinus.logging;

/**
 * Created by Timon on 12.03.2015.
 */
public enum LogLevel {
    DEBUG(0), INFO(1), WARN(2), ERROR(3), OFF(4);

    private final int level;

    private LogLevel(int level){
        this.level = level;
    }

    public int value() {
        return level;
    }
}
