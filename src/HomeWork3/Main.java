package HomeWork3;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        String[] array = {"Антонов", "Ефимов", "Антонов", "Меньшиков", "Антонов", "Глызин", "Ефимов", "Кузовлев", "Глызин", "Деньков", "Стратичук"};

        printUniqString(array);

    }

    /**
     * Метод вывода уникальных значений переданного массива
     * @param arr - массив строк
     */
    public static void printUniqString(String[] arr) {
        System.out.println("Список уникальных значений:");
        int count;
        Set<String> set = new HashSet<>(Arrays.asList(arr));
        for (String string : set) {
            count = 0;
            for (String s : arr) {
                if (s.equals(string))
                    count++;
            }
            System.out.println(string + " встречается " + count + ((count > 1 && count < 5) ? " раза" : " раз"));
        }
    }
}
