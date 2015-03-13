package org.fabulinus.ui;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.fabulinus.logging.LogEntry;
import org.fabulinus.logging.LogLevel;
import org.fabulinus.logging.LogListener;

/**
 * Created by Timon on 13.03.2015.
 */
public class LoggingControl extends VBox implements LogListener {
    private final ListView<LogEntry> logListView;
    private final ObservableList<LogEntry> logEntries;
    private final Label logLevelLabel;
    private final ChoiceBox<LogLevel> logLevelChoiceBox;
    private final CheckBox autoScroll;

    private BooleanProperty scroll;
    private LogLevel logLevel;

    public LoggingControl() {
        logListView = new ListView<>();
        logEntries = FXCollections.observableArrayList();
        initLogListView();
        logLevelLabel = new Label("Logging Level:");
        logLevelChoiceBox = new ChoiceBox<>();
        addLogLevelChoiceListener();
        autoScroll = new CheckBox();
        addAutoScrollListener();
        setPositions();
    }

    private void initLogListView(){
        logListView.itemsProperty().setValue(logEntries);
        logListView.setCellFactory(
                (param) -> new LogEntryCell()
        );
    }

    private void addLogLevelChoiceListener(){
        logLevelChoiceBox.getItems().addAll(LogLevel.values());
        logLevelChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            logLevel = newValue;
        });
        logLevelChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {

        });
        logLevelChoiceBox.setValue(LogLevel.DEBUG);
    }

    private void addAutoScrollListener(){
        scroll = new SimpleBooleanProperty();
        scroll.bind(autoScroll.selectedProperty());
        logEntries.addListener((ListChangeListener.Change<?extends LogEntry> change) -> {
            if (scroll.get()) {
                int index = logListView.getItems().size()-1;
                logListView.scrollTo(index);
            }
        });
        autoScroll.selectedProperty().setValue(true);
    }

    private void setPositions(){
        this.setSpacing(5);
        this.getChildren().add(logListView);
        HBox hbox = new HBox(20);
        hbox.getChildren().add(new Label("Automatic scrolling:"));
        hbox.getChildren().add(autoScroll);
        hbox.getChildren().add(logLevelLabel);
        hbox.getChildren().add(logLevelChoiceBox);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        this.getChildren().add(hbox);
    }

    @Override
    public void log(LogEntry entry) {
        if (logLevel.value() <= entry.getLogLevel().value()){
            Platform.runLater(() -> {
                logEntries.add(entry);
            });
        }
    }
}
