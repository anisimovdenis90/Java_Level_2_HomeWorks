public class Wall implements ObstacleCourse {

    private int id;
    private double height;
    private static int count = 1;
    private static final double DEFAULT_HEIGHT = 1.0;

    public Wall(double height) {
        this.id = count;
        count++;
        this.height = height;
    }

    public Wall() {
        this(DEFAULT_HEIGHT);
    }

    public int getId() {
        return id;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public String getInfo() {
        return "Стена №" + this.getId() + ". Высота " + this.getHeight() + " метра.";
    }

    @Override
    public boolean Overpass(Player player) {
        boolean result;

        player.jump();
        if (player.getJumpLimit() > this.getHeight()) {
            System.out.println(player.getName() + " успешно преодолел препятсвие.");
            result = true;
        } else {
            System.out.println(player.getName() + " не смог прыгнуть на требуемую высоту.");
            result = false;
        }

        return result;
    }

}
