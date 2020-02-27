package HomeWork2;

public class MainWrongElementArray {

    public static void main(String[] args) {
        String[][] wrongElementArray = Array.createTwoDimArray(Array.RIGHT_LENGTH, Array.RIGHT_LENGTH);
        wrongElementArray[3][1] = "wrong";

        Array.startSum(wrongElementArray);

    }
}
