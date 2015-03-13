package org.fabulinus.logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Timon on 12.03.2015.
 */
public class Logger {
    private final DateFormat dateFormat;
    private final List<LogListener> listeners;
    private final String prefix;

    public Logger(String prefix){
        this.dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        this.listeners = new ArrayList<>();
        this.prefix = prefix;
    }

    public void log(String message, LogLevel logLevel){
        for (LogListener listener : listeners) {
            String timestamp = dateFormat.format(new Date());
            String content = String.format("%s [%s]: %s", prefix, timestamp, message);
            listener.log(content, logLevel);
        }
    }

    public void addListener(LogListener listener){
        this.listeners.add(listener);
    }
}
