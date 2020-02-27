package HomeWork1;

public class RunningTrack implements ObstacleCourse {

    private int id;
    private int distance;
    private static int count = 1;
    private static final int DEFAULT_DISTANCE = 150;

    public RunningTrack(int distance) {
        this.id = count;
        count++;
        this.distance = distance;
    }

    public RunningTrack() {
        this(DEFAULT_DISTANCE);
    }

    public int getId() {
        return id;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String getInfo() {
        return "Беговая дорожка №" + this.getId() + ". Дистанция " + this.getDistance() + " метров.";
    }

    @Override
    public boolean overpass(Player player) {
        boolean result;

        player.run();
        if (player.getRunLimit() >= this.getDistance()) {
            System.out.println(player.getName() + " успешно преодолел препятсвие.");
            result = true;
        } else {
            System.out.println(player.getName() + " не смог пробежать требуемую дистанцию.");
            result = false;
        }

        return result;
    }

}
