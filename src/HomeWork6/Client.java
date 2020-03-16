package HomeWork6;

import java.io.*;
import java.net.Socket;

public class Client {
    private final String SERVER_ADDR = "localhost";
    private final int SERVER_PORT = 8189;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private BufferedReader reader;

    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        try {
            openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void openConnection() throws IOException {
        socket = new Socket(SERVER_ADDR, SERVER_PORT);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        System.out.println("Выходной поток есть");
        reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("reader есть");
        new Thread(() -> {
            try {
                while (true) {
                    String messageFromServer = in.readUTF();
                    if (messageFromServer.equalsIgnoreCase(Server.END_COMMAND)) {
                        break;
                    }
                    System.out.println("From server: " + messageFromServer);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Что-то не так");
                System.out.println("Connection has been closed!");
            }
        }).start();
        new Thread(() -> {
            try {
                while (true) {


                        String messageToServer = reader.readLine();
                    while (reader.ready()) {
                        if (messageToServer.equalsIgnoreCase("/end")) {
                            closeConnection();
                            break;
                        }
                        sendMessage(messageToServer);
                    }
                }
            } catch (Exception e) {
                System.out.println("Connection has been closed!");
            }
        }).start();
    }

    public boolean checkMessage(String message) {
        return !message.equalsIgnoreCase(Server.END_COMMAND);
    }

    public void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        if (!message.trim().isEmpty()) {
            try {
                out.writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Ошибка отправки сообщения");
            }
        }
    }
}