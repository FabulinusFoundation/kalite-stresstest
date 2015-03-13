package org.fabulinus.logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Timon on 13.03.2015.
 */
public class LogEntry {
    private final DateFormat dateFormat;
    private final LogLevel level;
    private final Date timestamp;
    private final String message;
    private final String tooltip;

    public LogEntry(LogLevel level, Date timestamp, String message, String tooltip) {
        this.dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        this.level = level;
        this.timestamp = timestamp;
        this.message = message;
        this.tooltip = tooltip;
    }

    public LogLevel getLogLevel() {
        return level;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getTooltip() {
        return tooltip;
    }

    @Override
    public String toString() {
        String timestamp = dateFormat.format(this.timestamp);
        return String.format("[%s](%s): %s", timestamp, level, message);
    }
}
