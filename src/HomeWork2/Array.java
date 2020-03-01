package HomeWork2;

import java.util.Arrays;

public class Array {

    public static final int RIGHT_LENGTH = 4;

    /**
     * Метод определения суммы элементов двумерного массива
     * @param array - на вход получает двумерный массив
     * @return sum - сумма элементов массива
     * @throws MyArraySizeException - исключение
     * @throws MyArrayDataException - исключение
     */
    public static int sumOfArrayElements(String[][] array) throws MyArraySizeException, MyArrayDataException {
        int sum = 0;
        int element;

        if (array.length != RIGHT_LENGTH) {
            throw new MyArraySizeException(RIGHT_LENGTH);
        }

        for (String[] arrayElement : array) {
            if (arrayElement.length != RIGHT_LENGTH) {
                throw new MyArraySizeException(RIGHT_LENGTH);
            }
        }

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                try {
                    element = Integer.parseInt(array[i][j].trim());
                } catch (NumberFormatException e) {
                    throw new MyArrayDataException(i, j);
                }
                sum += element;
            }
        }

        return sum;
    }

    /**
     * Метод создания двумерного массива
     * @param oneLength - размер массива
     * @param twoLength - размер массива
     * @return array - двумерный массив
     */
    public static String[][] createTwoDimArray (int oneLength, int twoLength) {
        int count = 1;
        String[][] array = new String[oneLength][twoLength];
        for (int i = 0; i < oneLength; i++) {
            for (int j = 0; j < twoLength; j++) {
                array[i][j] = "" + count;
                count++;
            }
        }

        return array;
    }

    public static void startSum(String[][] array) {
        System.out.println("Расчет суммы элементов массива: " + Arrays.deepToString(array));
        try {
            System.out.println("Сумма равна: " + sumOfArrayElements(array) + System.lineSeparator());
        } catch (MyArraySizeException | MyArrayDataException e) {
            System.err.println("Невозможно выполнить расчет!");
            e.printStackTrace();
        }
    }

}
