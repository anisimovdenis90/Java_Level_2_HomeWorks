package ru.geekbrains.java2.server.auth;

public interface AuthService {

    String getUsernameByLoginAndPassword(String login, String password);
    boolean checkContact(String nicknameToCheck);
    void start();
    void stop();

}
