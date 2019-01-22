package client;

import javafx.fxml.Initializable;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class client {
    public static void main(String[] args)  {
        System.out.println("Вас приветствует консольный чат с сервером!"+"\n"+"для выхода введите end");

        Socket socket = null;
        DataInputStream in;
        DataOutputStream out;
        final String IP_ADRESS = "localhost";
        final int PORT = 8089;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            try {
                socket = new Socket(IP_ADRESS, PORT);
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                Thread InThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (true) {
                                String strIn = in.readUTF();
                                System.out.println("Server message: " + strIn);
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                });
                Thread OutThread =new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (true){
                                String str = br.readLine();
                                if(str.equals("end")){
                                    System.out.println("Чат закрыт");
                                    break;
                                }
                                out.writeUTF(str);
                                out.flush();
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                });
                InThread.setDaemon(true);
                InThread.start();
                OutThread.start();


                try {
                    OutThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("конец");
            } catch (IOException e) {
                e.printStackTrace();
            }


    }

}
