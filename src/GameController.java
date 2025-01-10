import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GameController {
    private JFrame mainFrame;
    private Account currentAccount;


    public GameController() {
        mainFrame = new JFrame("League of Warriors");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 200);

        mainFrame.setVisible(true);
    }

    public void setAccount(Account account) {
        this.currentAccount = account;
    }

    public void showAuthPage() {
        mainFrame.getContentPane().removeAll();
        AuthPage authPage = new AuthPage(this);
        mainFrame.add(authPage);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public void showInfoPage() {
        mainFrame.setSize(400, 400);
        mainFrame.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));

        JLabel welcomeLabel = new JLabel("Bun venit, " + currentAccount.getPlayerInfo().getPlayerName());
        panel.add(welcomeLabel);

        JLabel favoriteGamesLabel = new JLabel("Jocuri favorite: " + currentAccount.getPlayerInfo().getFavoriteGames());
        panel.add(favoriteGamesLabel);

        JLabel gamesPlayedLabel = new JLabel("Harti jucate: " + currentAccount.getGamesPlayed());
        panel.add(gamesPlayedLabel);

        JLabel chooseCharacterLabel = new JLabel("Alege un caracter:");
        panel.add(chooseCharacterLabel);

        ButtonGroup characterGroup = new ButtonGroup();
        for (Character character : currentAccount.getPlayerCharacters()) {
            JButton characterButton = new JButton(character.getCharacterName());
            characterGroup.add(characterButton);
            panel.add(characterButton);

            characterButton.addActionListener(e -> advanceToGamePage(character));
        }

        mainFrame.add(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public void advanceToGamePage(Character character) {
        JOptionPane.showMessageDialog(mainFrame,
                "Ai selectat: " + character.getCharacterName() + "\n" +
                        "Viata: " + character.getHealth() + "\n" +
                        "Mana: " + character.getMana() + "\n" +
                        "Nivel: " + character.getLevel() + "\n" +
                        "Experienta: " + character.getExperience() + "\n");

        Grid grid = Game.getInstance().getGrid();
        if (grid == null) {
            grid = Grid.generateGrid(5, 5, true);
            Game.getInstance().setGrid(grid);
        }

        grid.setPlayerCharacter(character);
        showGamePage(character, grid);
    }

    public Grid getCurrentGrid() {
        return Game.getInstance().getGrid();
    }

    public void showGamePage(Character character, Grid grid) {
        mainFrame.setSize(1000, 700);
        mainFrame.getContentPane().removeAll();
        GamePage gamePage = new GamePage(this, character, grid);

        mainFrame.add(gamePage);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public void showFinalPage(Character playerCharacter) {
        mainFrame.setSize(400, 200);
        mainFrame.getContentPane().removeAll();
        FinalPage finalPage = new FinalPage(this, playerCharacter);

        mainFrame.add(finalPage);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public void showBattlePage(Character player, Enemy enemy, Grid grid) {
        if (player == null || enemy == null) {
            throw new IllegalArgumentException("Jucatorul sau inamicul nu pot fi nuli.");
        }

        mainFrame.setSize(1000, 700);
        mainFrame.getContentPane().removeAll();
        BattlePage battlePage = new BattlePage(this , player, enemy, grid);

        mainFrame.add(battlePage);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public void resetGameState() {
        Game.reset();
        this.currentAccount = null;
    }

    public static void main(String[] args) {
        GameController game2 = new GameController();
        game2.showAuthPage();
    }
}


