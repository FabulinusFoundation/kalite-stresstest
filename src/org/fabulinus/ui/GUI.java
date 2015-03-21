package org.fabulinus.ui;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.scene.control.ListSpinner;
import org.fabulinus.client.Client;
import org.fabulinus.input.ResourceFileReader;
import org.fabulinus.input.Resources;
import org.fabulinus.logging.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timon on 12.03.2015.
 */
public class GUI extends Application {
    private final Logger logger;
    private final Resources resources;
    private final List<Thread> clientThreads;
    private final StringProperty hostProperty;
    private final StringProperty portProperty;
    private final IntegerProperty clientNumberProperty;

    private static final String IPADDRESS_PATTERN = "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$";
    private static final String PORT_PATTERN = "^\\d{1,5}$";

    public static void main(String[] args) {
        launch(args);
    }

    public GUI(){
        this.logger = new Logger();
        this.resources = new ResourceFileReader("resource-input", logger)
                .readResources();
        this.clientThreads = new ArrayList<>();
        this.clientNumberProperty = new SimpleIntegerProperty(20);
        this.hostProperty = new SimpleStringProperty();
        this.portProperty = new SimpleStringProperty();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LoggingControl loggingControl = new LoggingControl();
        logger.addListener(loggingControl);
        loggingControl.setPadding(new Insets(10));

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(10));
        vBox.getChildren().add(loggingControl);

        HBox settings = new HBox(10);
        settings.setAlignment(Pos.CENTER);
        settings.getChildren().add(createHostInputFields());
        settings.getChildren().add(new Label("Clients:"));
        settings.getChildren().add(createClientNumberSpinner());
        settings.getChildren().add(createStartStopButton());
        vBox.getChildren().add(settings);
        vBox.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(vBox, 500, 500);
        primaryStage.setTitle("KA Lite Stress Test");
        Image img = new Image("file:img/leaf-green.png");
        primaryStage.getIcons().add(img);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createHostInputFields(){
        HBox result = new HBox(10);
        result.setAlignment(Pos.CENTER);
        Label ipLabel = new Label("IP:");
        Label portLabel = new Label("Port:");
        TextField ipTextField = new TextField("192.168.0.101");
        ipTextField.setPrefWidth(100);
        hostProperty.bind(ipTextField.textProperty());
        addIpValidator(ipTextField);
        TextField portTextField = new TextField("8008");
        portTextField.setPrefWidth(50);
        portProperty.bind(portTextField.textProperty());
        addPortValidator(portTextField);
        result.getChildren().addAll(ipLabel, ipTextField, portLabel, portTextField);
        return result;
    }

    private void addIpValidator(TextField ipTextField){
        ipTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches(IPADDRESS_PATTERN)) {
                ipTextField.setStyle("-fx-border-color: cornflowerblue");
            } else {
                ipTextField.setStyle("-fx-border-color: red");
            }
        });
    }

    private void addPortValidator(TextField portTextField){
        portTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches(PORT_PATTERN)) {
                portTextField.setStyle("-fx-border-color: cornflowerblue");
            } else {
                portTextField.setStyle("-fx-border-color: red");
            }
        });
    }

    private ListSpinner createClientNumberSpinner(){
        ObservableList<Integer> list = createClientNumberList();
        ListSpinner spinner = new ListSpinner(list);
        spinner.setValue(20);
        spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            clientNumberProperty.setValue((int) newValue);
        });
        spinner.setPrefWidth(65);
        return spinner;
    }

    private ObservableList createClientNumberList() {
        ObservableList<Integer> result = FXCollections.observableArrayList();
        for (int i=1; i<101; i++) {
            result.add(i);
        }
        return result;
    }

    public Button createStartStopButton() {
        Button btn = new Button("Start");
        btn.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
            btn.setDisable(true);
            if (btn.getText().equals("Start")){
                startClientThreads();
                btn.setText("Stop");
            } else {
                stopClientThreads();
                btn.setText("Start");
            }
            btn.setDisable(false);
        });
        btn.setPrefWidth(75);
        return btn;
    }

    private void startClientThreads() {
        String host = hostProperty.get();
        int port = Integer.parseInt(portProperty.get());
        for (int i=0; i< clientNumberProperty.get(); i++){
            Thread client = new Client(host, port, resources, logger, 2000);
            clientThreads.add(client);
        }
        clientThreads.forEach(client -> client.start());
    }

    private void stopClientThreads() {
        clientThreads.forEach(client -> client.interrupt());
        clientThreads.clear();
    }

    @Override
    public void stop() throws Exception {
        stopClientThreads();
    }
}
