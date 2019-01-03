package main.gui;

import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StartingScene {

    public Scene getScene(Stage stage, Scene client, Scene server) {
        int width = 516;
        int size = 150;
        Text selectionTitle = new Text(width/2.5,12, "Selection Page");
        selectionTitle.setTextOrigin(VPos.TOP);
        selectionTitle.setFill(Color.WHITE);
        selectionTitle.setFont(Font.font("SansSerif", FontWeight.BOLD, 20));

        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER);

        Button clientBtn = new Button("Send File");


        clientBtn.setMinSize(size,size);
        clientBtn.setOnAction(e -> {
            stage.setScene(client);
            stage.setTitle("Send File");
            stage.setResizable(true);
        });

        Rectangle selectionRectangle = new Rectangle(0, 0, width, 45);
        Color col = Color.rgb(57, 140, 178);
        selectionRectangle.setFill(col);

        Button serverBtn = new Button("Receive File");
        serverBtn.setOnAction(e -> {
            stage.setScene(server);
            stage.setTitle("Receive File");
        });

        serverBtn.setMinSize(size,size);
        hbox.getChildren().addAll(clientBtn,serverBtn);
        hbox.setLayoutY(100);
        hbox.setLayoutX(105);
        Group selectRoot = new Group(selectionRectangle,selectionTitle, hbox);

        Scene startingScene = new Scene(selectRoot, width, 387, Color.rgb(134, 189, 216));
        startingScene.getStylesheets().add("main/gui/style.css");
//        startingScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        return startingScene;


    }
}
