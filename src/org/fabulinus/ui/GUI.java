package org.fabulinus.ui;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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

    private IntegerProperty userNumber = new SimpleIntegerProperty(20);

    public static void main(String[] args) {
        launch(args);
    }

    public GUI(){
        this.logger = new Logger("KA Lite");
        this.userThreads = new ArrayList<>();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox vBox = new VBox(10);
        LoggingControl loggingControl = new LoggingControl();
        logger.addListener(loggingControl);
        loggingControl.setPadding(new Insets(20));

        vBox.setPadding(new Insets(20));
        vBox.getChildren().add(loggingControl);
        vBox.getChildren().add(createStartStopButton());
        vBox.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(vBox, 500, 500);
        primaryStage.setTitle("KA Lite Stress Test");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
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
            Thread user = new User("192.168.0.101", 8008, logger);
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
