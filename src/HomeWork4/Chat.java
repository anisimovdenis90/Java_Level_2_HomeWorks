package HomeWork4;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Chat extends JFrame {

    // Массив со списком контактов
    private static final String[] CONTACTS = {"Павел Медведев", "Ольга Чернова"};
    // В мапе храню всю переписку с конкретным контактом
    private static final Map<String, ArrayList<String>> CHAT_BY_CONTACT = new TreeMap<>();
    // Выбранный контакт в списке контаков
    private static String choosenContact;

    private JPanel chatPanel;
    private JList<String> contacts;
    private JTextArea chatField;
    private JButton button1;
    private JTextField inputText;


    public Chat(String title) {
        super(title);
        DefaultListModel<String> listModel = new DefaultListModel<>();
        contacts.setModel(listModel);
        getContacts(listModel, CONTACTS);
        createChatByContact(CONTACTS);

        contacts.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                button1.setEnabled(true);
                inputText.setEnabled(true);
                chatField.setText("");
                choosenContact = contacts.getSelectedValue();
                for(String message : CHAT_BY_CONTACT.get(choosenContact)) {
                    printMessage(message);
                }
            }
        });

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveMessageByContact(inputText.getText());
                inputText.setText("");
            }
        });

        inputText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveMessageByContact(inputText.getText());
                inputText.setText("");
            }
        });

        setContentPane(this.chatPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocation(200, 200);
//        setBounds(200, 200, 800, 600);
//        setResizable(false);
        pack();

        setVisible(true);
    }

    /**
     * Метод, заполняющий список контактов
     * @param listModel - listModel
     * @param contacts - массив списка контактов
     */
    private void getContacts(DefaultListModel<String> listModel, String[] contacts) {
        for (String contact : contacts) {
            listModel.addElement(contact);
        }
    }

    /**
     * Для переписки с каждым контактом создается своя коллекция
     * @param contacts - массив контактов
     */
    private void createChatByContact(String[] contacts) {
        for(String contact : contacts) {
            CHAT_BY_CONTACT.put(contact, new ArrayList<>());
        }
    }

    /**
     * Метод для сохранения переписки с конкретным констактом в мапу
     * @param message - сообщение в поле ввода
     */
    private void saveMessageByContact(String message) {
        // Проверка введенного сообщения
        if (message.equals(""))  {
            return;
        }

        String text = "Я: " + message;
        String botText = choosenContact + ": " + botMessage(message);
        CHAT_BY_CONTACT.get(choosenContact).add(text);
        CHAT_BY_CONTACT.get(choosenContact).add(botText);
        printMessage(text);
        printMessage(botText);
    }

    /**
     * Вывод сообщений в чат
     * @param message - String сообщение
     */
    private void printMessage(String message) {
        chatField.append(message + "\n");
//        chatField.setCaretPosition(chatField.getDocument().getLength());        // перенос каретки в конец строки
    }

    // Сообщения бота
    private String botMessage(String message) {
        String botMessage;
        String msg = message.toLowerCase();
        if (msg.contains("прив") || msg.contains("здрав")) {
            botMessage = "Привет!";
        } else if (msg.contains("как") && msg.contains("дела")) {
            botMessage = "Отлично! А как твои?";
        } else if (msg.contains("как") && msg.contains("настроен")) {
            botMessage = "Спасибо, очень позитивное!";
        } else if ((msg.contains("чем") && msg.contains("занима")) || (msg.contains("что") && msg.contains("делаеш"))) {
            botMessage = "Общаюсь в чате...";
        } else {
            botMessage = "[тестовый ответ]";
        }
        return botMessage;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Chat("Мой чат");
            }
        });
    }

}
