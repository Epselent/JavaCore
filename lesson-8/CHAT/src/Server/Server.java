package Server;

import sample.ClientHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;

public class Server {private Vector<ClientHandler> clients;

    public Server(){
        clients = new Vector<>();
        ServerSocket server = null;
        Socket socket = null;


        try {
            AuthService.connect();

            server = new ServerSocket(8189);

            System.out.println("Сервер запущен!");

            while (true) {
                socket = server.accept();
                System.out.println("Клиент подключился");
                new ClientHandler(this,socket);

            }

        }catch (IOException e) {
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
            AuthService.disconnect();
        }
    }
    public void subscribe (ClientHandler client){
        clients.add(client);
    }
    public void unSubscribe (ClientHandler client){
        clients.remove(client);
    }
    public void broadcastMsg(ClientHandler from, String msg) {
        for (ClientHandler o: clients) {
            if(!o.chekBlackList(from.getNick())) {
                if(!o.getNick().equals(from.getNick())) {
                    o.sendMsg(msg);
                }
            }
        }
    }
    public void sendMsgUser(String nick1, String msg, ClientHandler sender){
        for (ClientHandler o: clients) {
            if(o.getNick().equals(nick1)) {
                o.sendMsg("Личное сообщение от " + sender.getNick() + ":  " + msg);

                return;
            }
        }
        sender.sendMsg("Server: Клиент с ником " + nick1 + " в сети не зарегистрирован");
    }
    public boolean isNickfree (String nick){

        for (ClientHandler o: clients) {
           if(o.getNick().equals(nick)){
               return false;
           }
        }
        return true;
    }
}


