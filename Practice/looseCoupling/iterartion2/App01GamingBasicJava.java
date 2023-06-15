package Practice.looseCoupling.iterartion2;

public class App01GamingBasicJava {
    public static void main(String[] args) {
//        var game = new MarioGame();
        var game = new SuperContra();
        var gameRunner = new GameRunner(game);
        gameRunner.run();
    }
}
