package HomeWork5;

public class TestMultiThreading {

    private static final int SIZE = 10_000_000;
    private static final int HALF = SIZE / 2;

    public static void main(String[] args) {
        System.out.println("Начало обработки...");
        singleThreadProcessing();

        try {
            doubleTreadProcessing();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Обработка завершена.");
    }

    /**
     * Обработка массива одним потоком
     */
    public static void singleThreadProcessing() {
        // Создается массив
        float[] singleThreadArr = createArray(SIZE);
        // Замеряется время и запускается обработка массива
        long a = System.currentTimeMillis();
        arrayProcessing(singleThreadArr, 0);
        System.out.printf("Время обработки массива одним потоком: %d милисекунд.\n", (System.currentTimeMillis() - a));
    }

    /**
     * Обработка массива двумя потоками
     * @throws InterruptedException - пробрасываю исключение
     */
    public static void doubleTreadProcessing() throws InterruptedException {
        // Создается массив
        float[] doubleThreadArr = createArray(SIZE);
        // Запуск таймера
        long a = System.currentTimeMillis();
        // Разделения массива на два
        float[] arr1 = new float[HALF];
        float[] arr2 = new float[HALF];
        System.arraycopy(doubleThreadArr, 0, arr1, 0, HALF);
        System.arraycopy(doubleThreadArr, HALF, arr2, 0, HALF);
        // Создание потоков
        Thread thread1 = new Thread(() -> arrayProcessing(arr1,0));
        Thread thread2 = new Thread(() -> arrayProcessing(arr2, HALF));
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        // Объединение массивов в один
        System.arraycopy(arr1, 0, doubleThreadArr, 0, HALF);
        System.arraycopy(arr2, 0, doubleThreadArr, HALF, HALF);
        // Вывод результата
        System.out.printf("Время обработки массива двумя потоками: %d милисекунд.\n", (System.currentTimeMillis() - a));
    }

    /**
     * Метод, создающий массив, заполненный единицами
     * @param size - размер массива
     * @return - возвращает массив
     */
    public static float[] createArray(int size) {
        float[] arr = new float[size];
//        Arrays.fill(arr, 1.0f);
        for (int i = 0; i < size; i++) {
            arr[i] = 1.0f;
        }
        return arr;
    }

    /**
     * Метод обработки элементов массива
     * @param arr - на вход обрабатываемый массив
     * @param add - добавочная величина индекса для двухпоточной обработки
     */
    public static void arrayProcessing(float[] arr, int add) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (float) (arr[i] * Math.sin(0.2f + (i + add) / 5.0) * Math.cos(0.2f + (i + add) / 5.0) * Math.cos(0.4f + (i + add) / 2.0));
        }
    }
}
