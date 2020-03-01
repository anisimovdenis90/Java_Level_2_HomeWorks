package HomeWork3;

import java.util.*;

public class TelephoneBook {
    private HashMap<String, String> phoneBook;

    public TelephoneBook(String telephoneNumber, String name) {
        phoneBook = new HashMap<>();
        this.add(telephoneNumber, name);
    }

    /**
     * Метод добавления нового абонента в телефонную книгу
     * @param telephoneNumber - номер телефона
     * @param name - имя
     */
    public void add(String telephoneNumber, String name) {
        this.phoneBook.put(telephoneNumber, name);
    }

    /**
     * Метод выводящий номера телефонов для заданнго абонента
     * @param name - на вход принимает имя абонента
     */
    public void get(String name) {
        if (phoneBook.containsValue(name)) {
            StringBuilder message = new StringBuilder("Номера телефонов абонента " + name + ": ");
            for (String key : this.phoneBook.keySet()) {
                message.append((this.phoneBook.get(key).equals(name.trim())) ? key + " " : "");
            }
            System.out.println(message);
        } else {
            System.out.println("Отсутствует запись с таким именем в телефонной книге.");
        }
    }

    /**
     * Вывод всех записией в телефонной книге
     */
    public void print() {
        System.out.println("Записи в телефонной книге:");
//        for (String key : this.phoneBook.keySet()) {
//            System.out.println(this.phoneBook.get(key) + " - " + key);
//        }

        // Сортировка по значению мапы
        List<Map.Entry<String, String>> phoneList = new ArrayList<>(this.phoneBook.entrySet());
        Collections.sort(phoneList, Comparator.comparing(Map.Entry::getValue));

        for (Map.Entry<String, String> pair : phoneList) {
            System.out.println(pair.getValue() + " - " + pair.getKey());
        }
        System.out.println();
    }

}
