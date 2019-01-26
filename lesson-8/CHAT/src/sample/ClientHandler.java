package sample;

import Server.AuthService;
import Server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ClientHandler {

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private Server server;
    private String nick;
    private ArrayList<String > blackList;
    private Timer timer =new Timer();
    private TimerTask timerTask;
    private long timeOut = 20000;
    private boolean closed = false;
    public ClientHandler(Server server, Socket socket) {
        try {
            this.socket = socket;
            this.server = server;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.nick="";
            this.blackList = new ArrayList<>();

            timerTask = new TimerTask() {
                @Override
                public void run() {


                    try {
                        out.writeUTF("/serverClosed");
                        closed = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            };

            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        while (true) {
                            String str = in.readUTF();
                            if(str.startsWith("/singUp")){
                                String[] tokens = str.split(" ");
                                AuthService.addUser(tokens[1],tokens[2]);

                            }
                            if (str.startsWith("/auth")) {
                                String[] tokens = str.split(" ");
                                String newNick = AuthService.getNickLoginAndPass(tokens[1], tokens[2]);
                                if (newNick != null) {
                                    if(server.isNickfree(newNick)) {
                                        sendMsg("/authok");
                                        nick = newNick;
                                        server.subscribe(ClientHandler.this);
                                        AuthService.getBlackList(ClientHandler.this);
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

                            Thread t1 = new Thread(new Runnable() {
                                @Override
                                public void run() {

                                        timer.purge();
                                        timer.schedule(timerTask, timeOut);

                                }
                            });
                            t1.setDaemon(true);
                            t1.start();

                            String str = in.readUTF();
                            if(str.startsWith("/")){
                                if(str.startsWith("/w")){
                                    String[] tokens = str.split(" ", 3);
                                    server.sendMsgUser(tokens[1], tokens[2], ClientHandler.this);
                                    }
                                if (str.equals("/end") || closed) {
                                    out.writeUTF("/serverClosed");
                                    break;
                                }
                                if(str.startsWith("/blacklist")){
                                    String[] tokens = str.split(" ");
                                    AuthService.addBlackList(ClientHandler.this, tokens[1]);
                                    blackList.add(tokens[1]);
                                    sendMsg("Вы добавили пользователя с ником " + tokens[1] + " в черный список");

                                }
                                }else {

                                timer.cancel();
                                server.broadcastMsg(ClientHandler.this, "от " + nick + ": " + str);
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
    public boolean chekBlackList (String nick){
        return blackList.contains(nick);
    }

    public void setBlackList(String nick) {
        this.blackList.add(nick);
    }
    public void stop(){
        this.timer.cancel();
    }
//    public void start(){
//        timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    sendMsg("Вы отключены из-за не активности");
//                    out.writeUTF("/serverClosed");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }finally {
//                    try {
//                        in.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        out.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        socket.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    server.unSubscribe(ClientHandler.this);
//                }
//
//            }
//        };
//    }


}
