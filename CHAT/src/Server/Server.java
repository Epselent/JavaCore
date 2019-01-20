package Server;

import sample.ClientHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;

public class Server {private ClientHandler clients;

    public Server(){

        ServerSocket server = null;
        Socket socket = null;

        DataInputStream in;
        try {
            server = new ServerSocket(8189);

            System.out.println("Сервер запущен!");

//            while (true) {
                socket = server.accept();
                System.out.println("Клиент подключился");
                in = new DataInputStream(socket.getInputStream());
                clients = new ClientHandler(this,socket);
            //}
            while (true){
                String st = in.readUTF();

                System.out.println("Client message: " + st);
            }
        }catch (SocketException e){
            System.out.println("Клиент отключился");
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMsg(String msg) {

            clients.sendMsg(msg);

    }
}


