package sample;



import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Controller  {

    public int i = 0;
    @FXML
    VBox vBox;
    @FXML
    ScrollPane scroll;
    @FXML
    TextField textField;
    @FXML
    TextField loginField;
    @FXML
    PasswordField passField;
    @FXML
    VBox upperPanel;
    @FXML
    HBox buttonPanel;
    @FXML
    Button btn;
    @FXML
    Label isAuth;

    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    private boolean isAuthorized;
    private String name;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
    Date date = new Date();
    final String IP_ADRESS = "localhost";
    final int PORT = 8189;

    public void setAuthorized(boolean isAuthorized){
        this.isAuthorized = isAuthorized;
        if(!isAuthorized){
            upperPanel.setVisible(true);
            upperPanel.setManaged(true);
            buttonPanel.setVisible(false);
            buttonPanel.setManaged(false);
        }else{
            upperPanel.setVisible(false);
            upperPanel.setManaged(false);
            buttonPanel.setVisible(true);
            buttonPanel.setManaged(true);
        }
    }


    public void connect() {

        try {
            socket = new Socket(IP_ADRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        while (true) {
                            String str = in.readUTF();
                            if (str.startsWith("/authok")) {
                                setAuthorized(true);
                                break;
                            } else {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        isAuth.setText(str);
                                    }
                                });


                            }
                        }
                        while (true) {
                            String str = in.readUTF();
                            if (str.equals("/serverClosed")) break;
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
                        setAuthorized(false);
                    }
                }
            }).start();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMsg(ActionEvent actionEvent) {
        try {
            String str = textField.getText();

            out.writeUTF(textField.getText());
            out.flush();
            Label label = new Label();
            VBox vBoxLabel = new VBox();
            if(str.startsWith("/w")) {
                String[] tokens = str.split(" ", 3);

                label.setText(dateFormat.format(date) + "\n" + "Личное сообщение для " + tokens[1] + ": " + tokens[2]);
            }else {
                label.setText(dateFormat.format(date) + "\n" + str);
            }
            vBoxLabel.setAlignment(Pos.TOP_RIGHT);
            vBoxLabel.getChildren().add(label);
            vBox.getChildren().add(vBoxLabel);



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
        label.setText(dateFormat.format(date) + "\n"  + str);
        vBoxLabel.getChildren().add(label);
        vBox.getChildren().add(vBoxLabel);
    }

    public void tryToAuth(){
        if(socket == null || socket.isClosed()){
            connect();
        }
        try {
            out.writeUTF("/auth " + loginField.getText() + " " + passField.getText());
            loginField.clear();
            passField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}