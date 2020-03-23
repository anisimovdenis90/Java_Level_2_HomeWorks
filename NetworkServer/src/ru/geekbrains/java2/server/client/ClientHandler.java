package ru.geekbrains.java2.server.client;

import ru.geekbrains.lava2.client.Command;
import ru.geekbrains.lava2.client.CommandType;
import ru.geekbrains.lava2.client.command.AuthCommand;
import ru.geekbrains.lava2.client.command.BroadcastMessageCommand;
import ru.geekbrains.lava2.client.command.PrivateMessageCommand;
import ru.geekbrains.java2.server.NetworkServer;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientHandler {

    private final NetworkServer networkServer;
    private final Socket clientSocket;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private String nickname;

    public boolean successfulAuth = false;

    public ClientHandler(NetworkServer networkServer, Socket socket) {
        this.networkServer = networkServer;
        this.clientSocket = socket;
    }

    /**
     * Получение никнейма подключенного контакта
     *
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
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
            // Запускаем в отдельном потоке чтение сообщений от клиента
            new Thread(() -> {
                try {
                    authentication();
                    readMessages();
                } catch (IOException e) {
                    System.out.printf("Соединение с клиентом %s закрыто!", nickname);
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
        try {
            // Передаем экземпляр подключения конкретного клиента
            networkServer.unsubscribe(this);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Авторизация контакта на сервере
     *
     * @throws IOException - пробрасываем исключение метода readUTF
     */
    private void authentication() throws IOException {
        runTimeOutAuthThread();
        while (true) {
            Command command = readCommand();
            if (command == null) {
                continue;
            }
            if (command.getType() == CommandType.AUTH) {
                successfulAuth = processAuthCommand(command);
                if (successfulAuth) {
                   return;
                }
            } else {
                System.err.println("Неверный тип команды процесса авторизации: " + command.getType());
            }
        }
    }

    /**
     * Запускает поток лимита времени авторизации клиента
     */
    private void runTimeOutAuthThread() {
        System.out.println("Ожидание авторизации клиента...");
        new Thread(() -> {
            try {
//                Thread.sleep(120_000);
                // Отправляет время в окно авторизации
                for (int i = 120; i > 0; i--) {
                    Command timeoutAuthMessageCommand = Command.timeoutAuthMessageCommand("" + i);
                    sendMessage(timeoutAuthMessageCommand);
                    Thread.sleep(1_000);
                    // Заранее выходим из цикла при успешной авторизации
                    if (successfulAuth)
                        break;
                }
                // Если клиент не авторизовался, закрывается соединение
                if (!successfulAuth) {
                    System.out.println("Истекло время авторизации, клиент отключен");
                    Command timeOutAuthErrorCommand = Command.timeoutAuthErrorCommand("Истекло время авторизации, соединение закрыто!");
                    sendMessage(timeOutAuthErrorCommand);
                    closeConnection();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Авторизация не выполнена, закрыто соединение с клиентом");
            }
        }).start();
    }

    /**
     * Метод обработки команды авторизации
     *
     * @param command - команда авторизации
     * @return - boolean
     * @throws IOException - пробрасывается исключение
     */
    private boolean processAuthCommand(Command command) throws IOException {
        AuthCommand commandData = (AuthCommand) command.getData();
        // Получаем из сообщения логин и пароль
        String login = commandData.getLogin();
        String password = commandData.getPassword();
        // Получаем никнейм авторизованного пользователя
        String username = networkServer.getAuthService().getUsernameByLoginAndPassword(login, password);
        // Если никнейм отсутствует
        if (username == null) {
            Command authErrorCommand = Command.authErrorCommand("Отсутствует учетная запись по данному логину и паролю!");
            sendMessage(authErrorCommand);
            return false;
        }
        // Если никнейм уже используется
        else if (networkServer.isNicknameBusy(username)) {
            Command authErrorCommand = Command.authErrorCommand("Данный пользователь уже авторизован!");
            sendMessage(authErrorCommand);
            return false;
        } else {
            nickname = username;
            System.out.printf("Клиент %s авторизовался" + System.lineSeparator(), nickname);
            String message = nickname + " зашел в чат!";
            networkServer.broadcastMessage(Command.messageCommand(null, message), this);
            // Отправляем отклик авторизации клиенту
            commandData.setUsername(nickname);
            sendMessage(command);
            networkServer.subscribe(this);
            return true;
        }
    }

    /**
     * Чтение сообщения от клиента
     *
     * @throws IOException - пробрасываем исключение метода readUTF
     */
    private void readMessages() throws IOException {
        while (true) {
            Command command = readCommand();
            if (command == null) {
                continue;
            }
            switch (command.getType()) {
                case END: {
                    System.out.println("Получена команда 'END'");
                    String message = nickname + " вышел из чата!";
                    networkServer.broadcastMessage(Command.messageCommand(null, message), this);
                    return;
                }
                case PRIVATE_MESSAGE: {
                    PrivateMessageCommand commandData = (PrivateMessageCommand) command.getData();
                    String receiver = commandData.getReceiver();
                    String message = commandData.getMessage();
                    networkServer.sendPrivateMessage(receiver, Command.messageCommand(nickname, message));
                    break;
                }
                case BROADCAST_MESSAGE: {
                    BroadcastMessageCommand commandData = (BroadcastMessageCommand) command.getData();
                    String message = commandData.getMessage();
                    networkServer.broadcastMessage(Command.messageCommand(nickname, message), this);
                    break;
                }
                default:
                    System.err.println("Неверный тип команды: " + command.getType());
            }
        }
    }

    /**
     * чтение данных от клиента
     *
     * @return - возвращает полученную команду, или null в случае ошибки
     * @throws IOException - пробрасывается исключение
     */
    private Command readCommand() throws IOException {
        try {
            return (Command) in.readObject();
        } catch (ClassNotFoundException e) {
            String errorMessage = "Неизвестный тип объекта от клиента";
            System.err.println(errorMessage);
            e.printStackTrace();
            sendMessage(Command.errorCommand(errorMessage));
            return null;
        }
    }

    public void sendMessage(Command command) throws IOException {
        out.writeObject(command);
    }
}
