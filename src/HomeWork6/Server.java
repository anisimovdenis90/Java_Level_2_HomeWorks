package HomeWork6;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final String END_COMMAND = "/end";

    public static void main(String[] args) throws IOException {
        Socket clientSocket = null;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8189);
            System.out.println("Сервер запущен, ожидаем подключения...");
            clientSocket = serverSocket.accept();
            System.out.println("Клиент подключился");
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            new Thread(() -> {
                try {
                    while (true) {
                        String messageFromClient = in.readUTF();
                        System.out.println("From client: " + messageFromClient);
                        if (messageFromClient.equals(END_COMMAND)) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            new Thread(() -> {
                try {
                    while (true) {

                            String messageToClient = reader.readLine();
                        while (reader.ready()) {
                            if (messageToClient.equals(END_COMMAND)) {
                                out.writeUTF(messageToClient);
                                break;
                            } else if (!messageToClient.trim().isEmpty()) {
                                out.writeUTF("From server: " + messageToClient);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) serverSocket.close();
            if (clientSocket != null) clientSocket.close();
        }
    }
}



