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
    private final List<LogListener> listeners;

    public Logger(){
        this.listeners = new ArrayList<>();
    }

    public void log(String message, String tooltip, LogLevel logLevel){
        for (LogListener listener : listeners) {
            LogEntry entry = new LogEntry(logLevel, new Date(), message, tooltip);
            listener.log(entry);
        }
    }

    public void addListener(LogListener listener){
        this.listeners.add(listener);
    }
}
