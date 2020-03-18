package ru.geekbrains.java2.client.view;

import ru.geekbrains.java2.client.controller.ClientController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientChat extends JFrame {

    private JPanel mainPanel;
    private JList<String> userList;
    private JTextField messageTextField;
    private JButton sendButton;
    private JTextArea chatText;

    private ClientController controller;

    public ClientChat(ClientController controller) {
        this.controller = controller;
        setTitle(controller.getUsername());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(640, 480);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);
        addListeners();
        // При закрытии окна отключаем клиента от сервера
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
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
        } else if (message.startsWith("/w")) {
            String[] msg = message.split("\\s+", 3);
            String messageToContact = msg[2] + " -> Пользователю: " + msg[1];
            appendOwnMessage(messageToContact);
        } else {
            appendOwnMessage(message);
        }
            controller.sendMessage(message);
            messageTextField.setText(null);
    }

    /**
     * Метод обновления окна чата в отдельном потоке
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
}
