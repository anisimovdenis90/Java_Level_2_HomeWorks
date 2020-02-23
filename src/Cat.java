public class Cat implements Player {

    private String name;
    private int runLimit;
    private double jumpLimit;

    public Cat(String name, int runLimit, double jumpLimit) {
        this.name = name;
        this.runLimit = runLimit;
        this.jumpLimit = jumpLimit;
    }

    @Override
    public String getName() {
        return "Кот " + this.name;
    }

    @Override
    public int getRunLimit() {
        return this.runLimit;
    }

    @Override
    public double getJumpLimit() {
        return this.jumpLimit;
    }

    @Override
    public void run() {
        System.out.println("Участник " + this.getName() + " побежал.");
    }

    @Override
    public void jump() {
        System.out.println("Участник " + this.getName() + " разогнался и прыгнул.");
    }

}
