package HomeWork6;

import java.io.*;
import java.net.Socket;

public class Client {
    private final String SERVER_ADDR = "localhost";
    private final int SERVER_PORT = 8189;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String name;

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
        try {
            socket = new Socket(SERVER_ADDR, SERVER_PORT);
            System.out.println("Соединение установлено");
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            System.out.print("Введите свое имя: ");
            name = reader.readLine();
            runInputThread(in);
            runOutputThread(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) socket.close();
        }
    }

    public void runInputThread(DataInputStream in) {
        new Thread(() -> {
            try {
                while (true) {
                    String messageFromServer = in.readUTF();
                    if (messageFromServer.equals(Server.END_COMMAND)) {
                        System.out.println("Соединение завершено");
                        System.exit(0);
                    }
                    System.out.println("Сервер: " + messageFromServer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void runOutputThread(DataOutputStream out) {
        while (true) {
            try {
                String messageToServer = reader.readLine();
                if (messageToServer.equals(Server.END_COMMAND)) {
                    out.writeUTF(messageToServer);
                    System.exit(0);
                }
                out.writeUTF(name + ": " + messageToServer);
            } catch (Exception e) {
                System.out.println("Ошибки отправки сообщения");
                e.printStackTrace();
            }
        }
    }
}
