package HomeWork5;

// Разница во времени расчета по формуле из задания при разной длине массивов
public class Main {
    public static void main(String[] args) {
        float x;
        long a = System.currentTimeMillis();
        for (int i = 0; i < 5000000; i++) {
            x = (float) (1.0 * Math.sin(0.2f + i / 5.0) * Math.cos(0.2f + i / 5.0) * Math.cos(0.4f + i / 2.0));
        }
        System.out.println("Время расчета при i от 1 до 5000000: " + (System.currentTimeMillis() - a));

        a = System.currentTimeMillis();
        for (int i = 5000000; i < 10000000; i++) {
            x = (float) (1.0 * Math.sin(0.2f + i / 5.0) * Math.cos(0.2f + i / 5.0) * Math.cos(0.4f + i / 2.0));
        }
        System.out.println("Время расчета при i от 5000000 до 10000000: " + (System.currentTimeMillis() - a));

    }
}

//    Вывод в консоль:
//    Время расчета при i от 1 до 5000000: 1209
//    Время расчета при i от 5000000 до 10000000: 3358