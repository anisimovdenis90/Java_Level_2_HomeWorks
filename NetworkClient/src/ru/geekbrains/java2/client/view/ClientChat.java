package ru.geekbrains.java2.client.view;

import ru.geekbrains.java2.client.controller.ClientController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class ClientChat extends JFrame {

    private JPanel mainPanel;
    private JList<String> usersList;
    private JTextField messageTextField;
    private JButton sendButton;
    private JTextArea chatText;
    // Ссылка на контроллер
    private ClientController controller;

    public ClientChat(ClientController controller) {
        this.controller = controller;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(640, 480);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);
        addListeners();
        // При закрытии окна отключаем клиента от сервера
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                controller.sendEndMessage();
                controller.shutdown();
            }
        });
    }

    /**
     * Добавление действий на кнопку и поле ввода текста
     */
    private void addListeners() {
        sendButton.addActionListener(e -> ClientChat.this.sendMessage());
        messageTextField.addActionListener(e -> sendMessage());
    }

    /**
     * Метод отправки сообщения из окна чата
     */
    private void sendMessage() {
        String message = messageTextField.getText().trim();
        if (message.isEmpty()) {
            return;
        }
        // Отображаем набранное сообщение в окне чата
        appendOwnMessage(message);

        // Отправка сообщения всем
        if (usersList.getSelectedIndex() < 1) {
            controller.sendMessageToAllUsers(message);
        }
        // Отправка сообщения выбранному контакту
        else {
            String username = usersList.getSelectedValue();
            controller.sendPrivateMessage(username, message);
        }
        // Очищаем окно ввода текста после отправки сообщения
        messageTextField.setText(null);
    }

    /**
     * Метод обновления окна чата в отдельном потоке
     *
     * @param message - текст полученного сообщения
     */
    public void appendMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            chatText.append(message);
            chatText.append(System.lineSeparator());
        });
    }

    private void appendOwnMessage(String message) {
        appendMessage("Я: " + message);
    }

    /**
     * Отображает предупреждающее окно
     *
     * @param message - описание ошибки
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    /**
     * Обновление списка контактов
     *
     * @param users - на вход коллекция контактов
     */
    public void updateUsers(List<String> users) {
        SwingUtilities.invokeLater(() -> {
            DefaultListModel<String> model = new DefaultListModel<>();
            model.addAll(users);
            usersList.setModel(model);
        });
    }
}
