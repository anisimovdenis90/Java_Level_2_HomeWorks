package ru.geekbrains.java2.server;

import ru.geekbrains.lava2.client.Command;
import ru.geekbrains.java2.server.auth.AuthService;
import ru.geekbrains.java2.server.auth.BaseAuthService;
import ru.geekbrains.java2.server.client.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
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
                //  Создаем обработчик клиента
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
     *
     * @return - AuthService - сервис авторизации
     */
    public AuthService getAuthService() {
        return authService;
    }

    /**
     * Отправка сообщения всем подключенным клиентам
     *
     * @param message - String сообщения для отправки
     * @throws IOException - пробрасываем исключение
     */
    public synchronized void broadcastMessage(Command message, ClientHandler owner) throws IOException {
        for (ClientHandler client : clients) {
            if (client != owner) {
                client.sendMessage(message);
            }
        }
    }

    /**
     * Метод отправки сообщения конкретному пользователю
     *
     * @param receiver       - никнейм контакта, кому отправляется сообщение
     * @param commandMessage - объект с сообщением и ником отправителя
     * @throws IOException - пробрасываем исключение
     */
    public synchronized void sendPrivateMessage(String receiver, Command commandMessage) throws IOException {
        for (ClientHandler client : clients) {
            if (client.getNickname().equals(receiver)) {
                client.sendMessage(commandMessage);
                break;
            }
        }
    }

    /**
     * Добавление подключения в список
     *
     * @param clientHandler - на вход подключение клиента
     */
    public synchronized void subscribe(ClientHandler clientHandler) throws IOException {
        clients.add(clientHandler);
        List<String> users = getAllUsernames();
        broadcastMessage(Command.updateUsersListCommand(users), null);
    }

    /**
     * Удаление подключения из списка
     *
     * @param clientHandler - подключение клиента
     */
    public synchronized void unsubscribe(ClientHandler clientHandler) throws IOException {
        clients.remove(clientHandler);
        List<String> users = getAllUsernames();
        broadcastMessage(Command.updateUsersListCommand(users), null);
    }

    /**
     * Метод возвращает коллекцию подключенных к серверу пользователей
     *
     * @return - List пользователей
     */
    private List<String> getAllUsernames() {
//        return clients.stream()
//                .map(client -> client.getNickname())
//                .collect(Collectors.toList());
        List<String> usernames = new LinkedList<>();
        for (ClientHandler client : clients) {
            usernames.add(client.getNickname());
        }
        return usernames;
    }

    /**
     * Проверка на использование никнейма
     *
     * @param username - проверяемый никнейма
     * @return - boolean
     */
    public boolean isNicknameBusy(String username) {
        for (ClientHandler client : clients) {
            if (client.getNickname().equals(username)) {
                return true;
            }
        }
        return false;
    }
}
