package HomeWork2;

// Наследовал MyArraySizeException от RuntimeException, так как обработка исключения необязательна
// можно найти сумму массива любой размерности, заданного пользователем

public class MyArraySizeException extends RuntimeException {
    // Текстовое сообщение исключения
    private String message;
    // Величины размерности правильного массива
    private int rightOneLength;
    private int rightTwoLength;

    private static final String DEFAULT_MESSAGE = "Неправильный размер массива.";

    // Конструктор родительского класса
    public MyArraySizeException(String message) {
        super(message);
    }

    // Собственные конструкторы класса:
    public MyArraySizeException(String message, int rightOneLength) {
        this.message = message;
        this.rightOneLength = rightOneLength;
        this.rightTwoLength = rightOneLength;
    }

    public MyArraySizeException(String message, int rightOneLength, int rightTwoLength) {
        this(message, rightOneLength);
        this.rightTwoLength = rightTwoLength;
    }

    public MyArraySizeException(int rightOneLength) {
        this(DEFAULT_MESSAGE, rightOneLength);
    }

    public MyArraySizeException(int rightOneLength, int rightTwoLength) {
        this(DEFAULT_MESSAGE, rightOneLength, rightTwoLength);
    }

    /**
     * Метод, возвращающий описание исключения в зависимости от используемого конструктора
     * @return - описание исключения
     */
    @Override
    public String getMessage() {
        if (this.message != null) {
            return this.message + " Резмер введенного массива не соответствует требуему размеру: " + this.rightOneLength + "x" + this.rightTwoLength + ".";
        } else {
            return super.getMessage();
        }
    }
}
