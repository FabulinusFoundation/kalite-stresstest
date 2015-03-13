package org.fabulinus.ui;

import javafx.scene.control.ListCell;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import org.fabulinus.logging.LogEntry;
import org.fabulinus.logging.LogLevel;

/**
 * Created by Timon on 13.03.2015.
 */
public class LogEntryCell extends ListCell<LogEntry> {

    @Override
    protected void updateItem(LogEntry item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setText(item.toString());
            if (item.getLogLevel() == LogLevel.ERROR) {
                setTextFill(Paint.valueOf("#F70202"));
            } else if (item.getLogLevel() == LogLevel.WARN){
                setTextFill(Paint.valueOf("#F7E705"));
            } else {
                setTextFill(Paint.valueOf("#28AB03"));
            }
            setFont(new Font("Arial bold", 11));
        }
    }
}
