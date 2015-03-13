package org.fabulinus.ui;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.scene.control.ListSpinner;
import org.fabulinus.client.User;
import org.fabulinus.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Timon on 12.03.2015.
 */
public class GUI extends Application {
    private final Logger logger;
    private final List<Thread> userThreads;

    private final int USER_DEFAULT = 20;
    private IntegerProperty userNumber = new SimpleIntegerProperty(USER_DEFAULT);

    public static void main(String[] args) {
        launch(args);
    }

    public GUI(){
        this.logger = new Logger("KA Lite");
        this.userThreads = new ArrayList<>();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LoggingControl loggingControl = new LoggingControl();
        logger.addListener(loggingControl);
        loggingControl.setPadding(new Insets(10));

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(10));
        vBox.getChildren().add(loggingControl);
        HBox hBox = new HBox(10);
        hBox.getChildren().add(new Label("Number of Users:"));
        hBox.getChildren().add(createUserNumberSpinner());
        hBox.getChildren().add(createStartStopButton());
        hBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(hBox);
        vBox.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(vBox, 500, 500);
        primaryStage.setTitle("KA Lite Stress Test");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private ListSpinner createUserNumberSpinner(){
        ObservableList<Integer> list = createUserNumberList();
        ListSpinner spinner = new ListSpinner(list);
        spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            userNumber.setValue((int)newValue);
        });
        spinner.setPrefWidth(75);
        spinner.setValue(USER_DEFAULT);
        return spinner;
    }

    private ObservableList createUserNumberList() {
        ObservableList<Integer> result = FXCollections.observableArrayList();
        for (int i=1; i<50; i++) {
            result.add(i);
        }
        return result;
    }

    public Button createStartStopButton() {
        Button btn = new Button("Start");
        btn.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
            btn.setDisable(true);
            if (btn.getText().equals("Start")){
                startUserThreads();
                btn.setText("Stop");
            } else {
                stopUserThreads();
                btn.setText("Start");
            }
            btn.setDisable(false);
        });
        return btn;
    }

    private void startUserThreads() {
        for (int i=0; i<userNumber.get(); i++){
            Thread user = new User("192.168.0.101", 8008, logger, 2000);
            userThreads.add(user);
        }
        userThreads.forEach(user -> user.start());
    }

    private void stopUserThreads() {
        userThreads.forEach(user -> user.interrupt());
        userThreads.clear();
    }

    @Override
    public void stop() throws Exception {
        stopUserThreads();
    }
}
