package main.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.socket.Client;
import java.io.File;
import java.io.IOException;

public class ClientScene {
    private File file;
    public Scene getScene(Stage stage) {
        int width = 516;
        Scene client;

        ProgressBar pb = new ProgressBar(0);
        ProgressIndicator pi = new ProgressIndicator(0.0);
        pi.setLayoutY(100);
        Text fileName = new Text(10, 50, "file name...");

        Button sendFile = new Button("Send File");
        sendFile.setDisable(true);

        Text title = new Text(width/2.5,12, "Send File");
        title.setTextOrigin(VPos.TOP);
        title.setFill(Color.WHITE);
        title.setFont(Font.font("SansSerif", FontWeight.BOLD, 20));

        Rectangle rectangle = new Rectangle(0, 0, width, 45);
        Color col = Color.rgb(57, 140, 178);
        rectangle.setFill(col);
        Group root = new Group(rectangle,title);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.BOTTOM_RIGHT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label userName = new Label("Ip Address:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);
        Button selectFile = new Button("Select File");
        FileChooser fileChooser = new FileChooser();
        file = new File("null");
        selectFile.setOnAction(e -> {
            file = fileChooser.showOpenDialog(stage);
            fileName.setText(file.getName());
            sendFile.setDisable(false);
        });

        sendFile.setOnAction(e -> {

                    new Thread(() -> {
                        sendFile.setDisable(true);
                        Client c = new Client();
                        try {
                            c.run((new File(file.getAbsolutePath())), userTextField.getText(),pb,pi);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        sendFile.setDisable(false);
                    }).start();


                }
        );

        Button selectFolder = new Button("Select Folder");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folder");
        fileChooser.setTitle("Select File");
        selectFolder.setOnAction(e -> {
            file = directoryChooser.showDialog(stage);
            fileName.setText(file.getName());
            sendFile.setDisable(false);
        });

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(selectFile,selectFolder);
        HBox hbBtn2 = new HBox(10);
        hbBtn2.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn2.getChildren().addAll(pb,pi,sendFile);


        grid.add(hbBtn, 1,2);
        grid.add(fileName, 0, 2);
        grid.add(hbBtn2, 1,3);

        grid.setLayoutX(65);
        grid.setLayoutY(100);
        Group group2 = new Group(root,grid);

        client = new Scene(group2, width, 387,Color.rgb(134, 189, 216));
        client.getStylesheets().add("main/gui/style.css");
        return client;

    }
}
