package HomeWork2;

public class MyArrayDataException extends NumberFormatException {

    // Величины размерности неправильного массива
    private int wrongXElementArray;
    private int wrongYElementArray;


    // Конструктор класса
    public MyArrayDataException(int wrongXElementArray, int wrongYElementArray) {
        this.wrongXElementArray = wrongXElementArray;
        this.wrongYElementArray = wrongYElementArray;
    }

    @Override
    public String getMessage() {
        return "Нет числовых данных в ячейке [" + this.wrongXElementArray + "][" + this.wrongYElementArray + "].";
    }
}
