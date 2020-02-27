package HomeWork1;

public class Main {


    public static void main(String[] args) {
        Player[] players = new Player[9];
        players[0] = new Human("Джон Коннор", 300, 3.5);
        players[1] = new Human("Чак Норрис", 500, 5);
        players[2] = new Human("Дядя Вова", 100, 1);
        players[3] = new Cat("Барсик", 200, 2.5);
        players[4] = new Cat("Рыжик", 150, 3);
        players[5] = new Cat("Том", 300, 2.5);
        players[6] = new Robot("Бендер", 100, 0.5);
        players[7] = new Robot("Амиго", 180, 1);
        players[8] = new Robot("Т-800", 490, 1.5);

        ObstacleCourse[] courses = new ObstacleCourse[6];
        courses[0] = new RunningTrack();
        courses[1] = new Wall();
        courses[2] = new RunningTrack(300);
        courses[3] = new Wall(3);
        courses[4] = new RunningTrack(500);
        courses[5] = new Wall(5);

        for (Player player : players) {
            for (ObstacleCourse course : courses) {
                System.out.println(player.getName() + " VS " + course.getInfo());
                if (course.overpass(player)) {
                    System.out.println(player.getName() + " идет дальше.");
                } else {
                    System.out.println(player.getName() + " выбывает..." + System.lineSeparator());
                    break;
                }
            }
        }
    }
}
