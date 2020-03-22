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
    private boolean isOutputThreadStart = false;

    public static void main(String[] args) throws IOException {
        new Server().startServer();
    }

    public void startServer() throws IOException {
        try {
            serverSocket = new ServerSocket(8189);
            System.out.println("Сервер запущен, ожидаем подключения...");
            while (true) {
                clientSocket = serverSocket.accept();
                connections.add(clientSocket);
                System.out.println("Клиент подключился");
                runInputThread(clientSocket);
                if (!isOutputThreadStart) {
                    runOutputThread();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) serverSocket.close();
            if (clientSocket != null) clientSocket.close();
        }
    }

    public void runInputThread(Socket clientSocket) {
        new Thread(() -> {
            while (true) {
                try {
                    DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                    String messageFromClient = in.readUTF();
                    if (messageFromClient.equals(END_COMMAND)) {
                        System.out.println("Клиент отключился");
                        clientSocket.close();
                        connections.remove(clientSocket);
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

    public void runOutputThread() {
        isOutputThreadStart = true;
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
}
