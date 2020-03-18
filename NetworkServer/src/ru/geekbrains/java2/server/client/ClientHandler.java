package ru.geekbrains.java2.server.client;

import ru.geekbrains.java2.server.NetworkServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {

    private final NetworkServer networkServer;
    private final Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;

    private String nickname;

    public ClientHandler(NetworkServer networkServer, Socket socket) {
        this.networkServer = networkServer;
        this.clientSocket = socket;
    }

    /**
     * Получение никнейма подключенного контакта
     * @return - String никнейм контакта
     */
    public String getNickname() {
        return nickname;
    }

    public void run() {
        startHandler();
    }

    /**
     * Метод организации подключения клиента к серверу
     */
    private void startHandler() {
        try {
            // Создаем потоки обмена информацией
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            // Запускаем в отдельном потоке чтение сообщений от клиента
            new Thread(() -> {
                try {
                    authentication();
                    readMessage();
                } catch (IOException e) {
                    System.out.printf("Соединение с клиентом %s закрыто", nickname);
                    System.out.println();
                } finally {
                    closeConnection();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Закрытие соединения с сервером
     */
    private void closeConnection() {
        // Передаем экземпляр подключения конкретного клиента
        networkServer.unsubscribe(this);
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Чтение сообщения от клиента
     * @throws IOException - пробрасываем исключение метода readUTF
     */
    private void readMessage() throws IOException {
        while (true) {
            String message = in.readUTF();
            // Завершаем подключение, если введена соответствующая команда
            if ("/end".equals(message)) {
                return;
            } else if (message.startsWith("/w")) {
                // Отправка сообщения указанному контакту
                // Отделяем имя контакта от сообщения
                String contactToSendMessage = getStrings(message)[1];
                String messageToContact = getStrings(message)[2];
                if (networkServer.getAuthService().checkContact(contactToSendMessage)) {
                    System.out.printf("From %s To %s: %s", nickname, contactToSendMessage, messageToContact + System.lineSeparator());
                    networkServer.sendMessageToContact(messageToContact, contactToSendMessage, nickname);
                } else {
                    sendMessage(String.format("Контакта с именем %s не существует!", contactToSendMessage));
                }
            } else {
                // Отправка сообщения остальным контактам
                System.out.printf("От %s: %s", nickname, message + System.lineSeparator());
                networkServer.broadcastMessage(nickname + ": " + message, this);
            }
        }
    }


    /**
     * Авторизация контакта на сервере
     * @throws IOException - пробрасываем исключение метода readUTF
     */
    private void authentication() throws IOException {
        while (true) {
            String message = in.readUTF();
            // "/auth login password"
            if (message.startsWith("/auth")) {
                // Получаем из сообщения логин и пароль
                String login = getStrings(message)[1];
                String password = getStrings(message)[2];
                // Получаем никнейм авторизованного пользователя, если данные верны
                String username = networkServer.getAuthService().getUsernameByLoginAndPassword(login, password);
                if (username == null) {
                    sendMessage("Отсутствует учетная запись по данному логину и паролю!");
                } else {
                    nickname = username;
                    networkServer.broadcastMessage(nickname + " зашел в чат!", this);
                    // Отправляем отклик авторизации клиенту
                    sendMessage("/auth " + nickname);
                    networkServer.subscribe(this);
                    break;
                }
            }
        }
    }

    /**
     * Получение массива строк из сообщения
     * @param message - сообщения от контакта
     * @return - массив строк
     */
    private String[] getStrings(String message) {
        return message.split("\\s+", 3);
    }

    /**
     * Отправка сообщения от текущего контакта
     * @param message - текст сообщения
     * @throws IOException - пробрасываем исключение
     */
    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
    }
}
