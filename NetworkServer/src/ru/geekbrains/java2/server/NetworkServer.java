package ru.geekbrains.java2.server;

import ru.geekbrains.java2.server.auth.AuthService;
import ru.geekbrains.java2.server.auth.BaseAuthService;
import ru.geekbrains.java2.server.client.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class NetworkServer {

    private int port;
    private final List<ClientHandler> clients = new ArrayList<>();
    private final AuthService authService;

    public NetworkServer(int port) {
        this.port = port;
        this.authService = new BaseAuthService();
    }

    /**
     * Метод запуска сетевого сервера
     */
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер был успешно запущен на порту " + port);
            //  Запускаем сервис авторизации
            authService.start();
            //  Создаем список подключений
            while (true) {
                System.out.println("Ожидание подключения клиента...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Клиент подключился");
                //  Добавляем подключение в список
                createClientHandler(clientSocket);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при работе сервера");
            e.printStackTrace();
        } finally {
            // Отдельно останавливаем сервис авторизации
            authService.stop();
        }
    }

    private void createClientHandler(Socket clientSocket) {
        ClientHandler clientHandler = new ClientHandler(this, clientSocket);
        clientHandler.run();
    }

    /**
     * Возвращает сервис авторизации из конструктора сетевого сервера
     * @return - AuthService - сервис авторизации
     */
    public AuthService getAuthService() {
        return authService;
    }

    /**
     * Отправка сообщения всем подключенным клиентам
     * @param message - String сообщения для отправки
     * @throws IOException - пробрасываем исключение
     */
    public synchronized void broadcastMessage(String message, ClientHandler owner) throws IOException {
        for (ClientHandler client : clients) {
            if (client != owner) {
                client.sendMessage(message);
            }
        }
    }

    /**
     * Метод отправки сообщения конкретному пользователю
     * @param message - текст сообщения
     * @param contactToSendMessage - никнейм контакта, кому отправляется сообщение
     * @param fromContact - от кого
     * @throws IOException - пробрасываю исключение
     */
    public synchronized void sendMessageToContact(String message, String contactToSendMessage, String fromContact) throws IOException {
        for (ClientHandler client : clients) {
            if (client.getNickname().equals(contactToSendMessage)) {
                client.sendMessage(String.format("От %s: %s", fromContact, message));
            }
        }
    }

    /**
     * Добавление подключения в список
     * @param clientHandler - на вход подключение клиента
     */
    public synchronized void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
    }

    /**
     * Удаление подключения из списка
     * @param clientHandler - подключение клиента
     */
    public synchronized void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }
}
