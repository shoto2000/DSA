package Practice.looseCoupling.iterartion2;

public class SuperContra implements GamingConsole{
    @Override
    public void up() {
        System.out.println("jump");
    }

    @Override
    public void down() {
        System.out.println("sit");
    }

    @Override
    public void left() {
        System.out.println("go back");
    }

    @Override
    public void right() {
        System.out.println("go forward");
    }
}
