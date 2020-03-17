package HomeWork6;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static final String END_COMMAND = "/end";
    public static Socket clientSocket = null;
    public static ServerSocket serverSocket = null;
    private final ArrayList<Socket> connections = new ArrayList<>();
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        new Server().startServer();
    }

    public void startServer() throws IOException {
        try {
            serverSocket = new ServerSocket(8189);
            System.out.println("Сервер запущен, ожидаем подключения...");
            while (true) {
                connections.add(serverSocket.accept());
                System.out.println("Клиент подключился");
                runInputThread();
                runSendMessage();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) serverSocket.close();
            if (clientSocket != null) clientSocket.close();
        }
    }

    public void runSendMessage() {
        new Thread(() -> {
            while (true) {
                try {
                    String messageToClient = reader.readLine();
                    for (Socket connection : connections) {
                        new DataOutputStream(connection.getOutputStream()).writeUTF(messageToClient);
                    }
                } catch (Exception e) {
                    System.out.println("Ошибка отправки сообщения");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void runInputThread() {
        for (Socket connection : connections) {
            new Thread(() -> {
                while (true) {
                    try {
                        String messageFromClient = new DataInputStream(connection.getInputStream()).readUTF();
                        if (messageFromClient.equals(END_COMMAND)) {
                            System.out.println("Клиент отключился");
                            connection.close();
                            break;
                        }
                        System.out.println(messageFromClient);
                    } catch (Exception e) {
                        System.out.println("Соединение завершено");
                        break;
                    }
                }
            }).start();
        }
    }
}
