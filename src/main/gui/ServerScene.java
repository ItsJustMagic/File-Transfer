package main.gui;

import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import main.socket.IpAddress;
import main.socket.Server;

import java.io.IOException;

public class ServerScene {

    public Scene getScene () {
        int width = 516;
        Scene server;
        Text receiveTitle = new Text(width/2.5,12, "Receive File ");
        receiveTitle.setTextOrigin(VPos.TOP);
        receiveTitle.setFill(Color.WHITE);
        receiveTitle.setFont(Font.font("SansSerif", FontWeight.BOLD, 20));

        Rectangle receiveRectange = new Rectangle(0, 0, width, 45);
        Color col = Color.rgb(57, 140, 178);
        receiveRectange.setFill(col);
        Text ipAddress = new Text("Local Ip Address: "+ IpAddress.getIpAddress());
        ipAddress.setLayoutX(100);
        ipAddress.setLayoutY(100);
        ipAddress.setFill(Color.BLACK);
        ipAddress.setFont(Font.font("SansSerif", FontWeight.BOLD, 20));
        Button receiveFile = new Button("Receive File");
        receiveFile.setLayoutX(80);
        receiveFile.setLayoutY(125);
        receiveFile.setOnAction(e -> {
            new Thread(() -> {
                receiveFile.setDisable(true);
                Server s = new Server();
                try {
                    s.run();
                } catch (IOException|ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
                receiveFile.setDisable(false);
            }).start();

        });
        receiveFile.setMinSize(350,200);
        Group receiveGroup = new Group(receiveRectange,receiveTitle, ipAddress, receiveFile);
        server = new Scene(receiveGroup,width, 387, Color.rgb(134, 189, 216));
        server.getStylesheets().add("main/gui/style.css");
        return server;
    }
}
