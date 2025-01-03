public class Test {
    public static void main(String[] args) {
        // folosim getInstance pentru a obtine instanta clasei Game
        Game game = Game.getInstance();
        game.run();
    }
}