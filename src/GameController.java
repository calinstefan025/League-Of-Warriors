import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        // Caracterul selectat si avansam la gamepage cu el
        showGamePage(character);
    }


    public void showGamePage(Character character) {
        mainFrame.setSize(1000, 700);
        mainFrame.getContentPane().removeAll();
        GamePage gamePage = new GamePage(this, character);

        mainFrame.add(gamePage);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public void showFinalPage() {
        mainFrame.setSize(400, 200);
        mainFrame.getContentPane().removeAll();
        FinalPage finalPage = new FinalPage(this);

        mainFrame.add(finalPage);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public void showBattlePage(Character player, Enemy enemy) {
        if (player == null || enemy == null) {
            throw new IllegalArgumentException("Player or enemy cannot be null.");
        }

        mainFrame.setSize(1000, 700);
        mainFrame.getContentPane().removeAll();
        BattlePage battlePage = new BattlePage(this , player, enemy);

        mainFrame.add(battlePage);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public static void main(String[] args) {
        GameController game2 = new GameController();
        game2.showAuthPage();
    }
}


