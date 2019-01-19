package sample;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    public int i = 0;
    @FXML
    VBox vBox;
    @FXML
    ScrollPane scroll;
    @FXML
    TextField textField;

    @FXML
    Button btn;

    Socket socket;
    DataInputStream in;
    DataOutputStream out;


    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
    Date date = new Date();
    final String IP_ADRESS = "localhost";
    final int PORT = 8189;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            socket = new Socket(IP_ADRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            Thread t1 = new Thread(() -> {
                try {
                    while (true) {
                        String str = in.readUTF();
                        if (str.equals("/serverClosed")) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    SendMcdgUser("Сервер отключен");
                                }
                            });
                            break;
                        }
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                SendMcdgUser(str);
                            }
                        });


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            t1.setDaemon(true);
            t1.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMsg(ActionEvent actionEvent) {
        try {
            out.writeUTF(textField.getText());
            out.flush();
            Label label = new Label();
            VBox vBoxLabel = new VBox();
            label.setText(dateFormat.format(date) + "\n" + textField.getText());
//            if ( i %2 == 0) {
            vBoxLabel.setAlignment(Pos.TOP_RIGHT);
//            }else{
//                vBoxLabel.setAlignment(Pos.TOP_LEFT);
//            }
            vBoxLabel.getChildren().add(label);
            vBox.getChildren().add(vBoxLabel);
            i++;


            textField.clear();
            textField.requestFocus();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void SendMcdgUser(String str){
        Label label = new Label();
        VBox vBoxLabel = new VBox();
        vBoxLabel.setAlignment(Pos.TOP_LEFT);
        label.setText(dateFormat.format(date) + "\n" + "Сервер:   " + str);
        vBoxLabel.getChildren().add(label);
        vBox.getChildren().add(vBoxLabel);
    }
}