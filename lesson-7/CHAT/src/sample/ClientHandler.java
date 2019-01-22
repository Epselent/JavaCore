package sample;

import Server.AuthService;
import Server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private Server server;
    private String nick;


    public ClientHandler(Server server, Socket socket) {
        try {
            this.socket = socket;
            this.server = server;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.nick="";
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        while (true) {
                            String str = in.readUTF();
                            if (str.startsWith("/auth")) {
                                String[] tokens = str.split(" ");
                                String newNick = AuthService.getNickLoginAndPass(tokens[1], tokens[2]);
                                if (newNick != null) {
                                    if(server.isNickfree(newNick)) {
                                        sendMsg("/authok");
                                        nick = newNick;
                                        server.subscribe(ClientHandler.this);
                                        break;
                                    }else {
                                        sendMsg("Логин уже авторизирован");
                                    }
                                } else {
                                    sendMsg("Неверный логин/пароль");
                                }
                            }
                        }
                        while (true) {
                            String str = in.readUTF();
                            if(str.startsWith("/w")){
                                String[] tokens = str.split(" ", 3);
                                if(!server.isNickfree(tokens[1])) {
                                    server.sendMsgUser(tokens[1], tokens[2], nick);
                                }else {
                                    server.sendMsgUser(nick,"Клиент с ником " + tokens[1] + " в сети не зарегистрирован", "Server:");
                                }
                            }else {
                                if (str.equals("/end")) {
                                    out.writeUTF("/serverClosed");
                                    break;
                                }
                                server.broadcastMsg("от " + nick + ": " + str, nick);
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        server.unSubscribe(ClientHandler.this);
                    }
                }

            }).start();


        }catch (IOException e) {
            e.printStackTrace();
        }
        }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNick() {
        return nick;
    }
}
