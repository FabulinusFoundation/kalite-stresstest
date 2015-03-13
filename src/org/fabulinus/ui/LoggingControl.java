package org.fabulinus.ui;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.fabulinus.logging.LogLevel;
import org.fabulinus.logging.LogListener;

/**
 * Created by Timon on 13.03.2015.
 */
public class LoggingControl extends VBox implements LogListener {
    private final ListView<String> logListView;
    private final ObservableList<String> logEntries;
    private final ChoiceBox<LogLevel> logLevelChoiceBox;
    private final CheckBox autoScroll;

    private BooleanProperty scroll;
    private LogLevel logLevel;

    public LoggingControl() {
        logListView = new ListView<>();
        logEntries = FXCollections.observableArrayList();
        logListView.itemsProperty().setValue(logEntries);
        logLevelChoiceBox = new ChoiceBox<>();
        addLogLevelChoiceListener();
        autoScroll = new CheckBox("Automatic scrolling");
        addAutoScrollListener();
        setPositions();
    }

    private void addLogLevelChoiceListener(){
        logLevelChoiceBox.getItems().addAll(LogLevel.values());
        logLevelChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            logLevel = newValue;
        });
        logLevelChoiceBox.setValue(LogLevel.OFF);
    }

    private void addAutoScrollListener(){
        scroll = new SimpleBooleanProperty();
        scroll.bind(autoScroll.selectedProperty());
        logEntries.addListener((ListChangeListener.Change<?extends String> change) -> {
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
        hbox.getChildren().add(autoScroll);
        hbox.getChildren().add(logLevelChoiceBox);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        this.getChildren().add(hbox);
    }

    @Override
    public void log(String content, LogLevel level) {
        if (logLevel.getLevel() < level.getLevel()){
            Platform.runLater(() -> {
                logEntries.add(content);
                //TODO set color
            });
        }
    }

    private String getColor(LogLevel level){
        switch (level) {
            case WARN: return "yellow";
            case ERROR: return "red";
            default: return "green";
        }
    }
}
