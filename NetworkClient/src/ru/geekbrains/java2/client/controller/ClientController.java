package ru.geekbrains.java2.client.controller;

import ru.geekbrains.java2.client.view.AuthDialog;
import ru.geekbrains.java2.client.view.ClientChat;
import ru.geekbrains.java2.client.model.NetworkService;

import javax.swing.*;
import java.io.IOException;

public class ClientController {

    private final NetworkService networkService;
    private final AuthDialog authDialog;
    private final ClientChat clientChat;
    private String nickname;

    public ClientController(String serverHost, int serverPort) {
        this.networkService = new NetworkService(serverHost, serverPort);
        this.authDialog = new AuthDialog(this);
        this.clientChat = new ClientChat(this);
    }

    public void runApplication() throws IOException {
        connectToServer();
        runAuthProcess();
    }

    /**
     * Запуск подключения клиенту к серверу
     * @throws IOException - ошибка подключения
     */
    private void connectToServer() throws IOException {
        try {
            networkService.connect();
        } catch (IOException e) {
            System.err.println("Невозможно подключиться к серверу");
            throw e;
        }
    }

    private void runAuthProcess() {
        networkService.setSuccessfulAuthEvent(new AuthEvent() {
            @Override
            public void authIsSuccessful(String nickname) {
                ClientController.this.setUserName(nickname);
                ClientController.this.openChat();
            }
        });
        authDialog.setVisible(true);
    }

    /**
     * Отображение окна чата
     */
    private void openChat() {
        // Убираем окно авторизации
        authDialog.dispose();
        networkService.setMessageHandler(clientChat::appendMessage);
        clientChat.setVisible(true);
    }

    private void setUserName(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Отправка сообщения авторизации, вызывается из окна авторизации (AuthDialog)
     * @param login - String логин
     * @param pass - String пароль
     * @throws IOException - Exception при отправке сообщения
     */
    public void sendAuthMessage(String login, String pass) throws IOException {
        networkService.sendAuthMessage(login, pass);
    }

    /**
     * Метод отправки сообщения, вызывается из окна чата
     * @param message - текст сообщения
     */
    public void sendMessage(String message) {
        try {
            networkService.sendMessage(message);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Ошибка отправки сообщения!");
            e.printStackTrace();
        }
    }

    /**
     * Закрываем соединение при отключении клиента
     */
    public void shutdown() {
        networkService.close();
    }

    public String getUsername() {
        return nickname;
    }
}
