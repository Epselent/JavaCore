import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws InterruptedException{
        ServerSocket server = null;
        Socket socket = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Вас приветствует консольный чат");

        try {
            server = new ServerSocket(8089);
            System.out.println("Сервер запущен");
            socket = server.accept();
            System.out.println("Клиент подключен");
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            Thread InThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String str = in.readUTF();
                            if (str.equals("end")) {
                                System.out.println("Чат закрыт");
                                break;
                            }
                            System.out.println("Client message: " + str);
                        }
                    }catch (SocketException e){
                        System.out.println("Клиент отключился");
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
            Thread OutThread =new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        while (true){
                            String strOut = br.readLine();
                            out.writeUTF(strOut);
                            out.flush();
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
            OutThread.setDaemon(true);
            InThread.start();
            OutThread.start();

            InThread.join();


        }catch (SocketException e){
            System.out.println("Клиент отключился");
        }
        catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                System.out.println("Сервер прекратил свою работу");
                server.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
