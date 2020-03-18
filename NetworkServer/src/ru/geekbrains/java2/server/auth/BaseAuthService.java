package ru.geekbrains.java2.server.auth;

import java.util.List;

/**
 * Простой сервис авторизации
 */
public class BaseAuthService implements AuthService {

    private static class UserData {
        private String login;
        private String password;
        private String userName;

        public UserData(String login, String password, String userName) {
            this.login = login;
            this.password = password;
            this.userName = userName;
        }
    }

    private static final List<UserData> USER_DATA = List.of(
            new UserData("login1", "pass1", "username1"),
            new UserData("login2", "pass2", "username2"),
            new UserData("login3", "pass3", "username3")
    );

    /**
     * Метод получения никнейма контакта по логину и паролю
     * @param login - на вход логин контакта
     * @param password - на вход пароль контакта
     * @return - String никнейм контакта
     */
    @Override
    public String getUsernameByLoginAndPassword(String login, String password) {
        for (UserData userDatum : USER_DATA) {
            if (userDatum.login.equals(login) && userDatum.password.equals(password)) {
                return userDatum.userName;
            }
        }
        return null;
    }

    /**
     * Метод поиска никнейма среди авторизованных контактов
     * @param nicknameToCheck - на вход интересующий никнейм
     * @return - boolean
     */
    @Override
    public boolean checkContact(String nicknameToCheck) {
        for (UserData userDatum : USER_DATA) {
            if (userDatum.userName.equals(nicknameToCheck)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Запуск сервиса авторизации
     */
    @Override
    public void start() {
        System.out.println("Сервис аутентификации запущен");
    }

    /**
     * Остановка сервиса авторизации
     */
    @Override
    public void stop() {
        System.out.println("Сервис аутентификации остановлен");

    }
}
