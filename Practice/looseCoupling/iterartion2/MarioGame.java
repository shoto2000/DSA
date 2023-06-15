package Practice.looseCoupling.iterartion2;

public class MarioGame implements GamingConsole{
    @Override
    public void up() {
        System.out.println("jump");
    }

    @Override
    public void down() {
        System.out.println("Go into hole");
    }

    @Override
    public void left() {
        System.out.println("break");
    }

    @Override
    public void right() {
        System.out.println("Accelerate");
    }
}
