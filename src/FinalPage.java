import javax.swing.*;
import java.awt.*;

public class FinalPage extends JPanel {
    private GameController controller;

    public FinalPage(GameController controller, Character playerCharacter) {
        this.controller = controller;

        JPanel finalInfo = new JPanel(new GridLayout(5, 1));

        JLabel finalLabel = new JLabel("Game Over", SwingConstants.CENTER);
        finalInfo.add(finalLabel);

        JLabel characterName = new JLabel("Character Name: " + playerCharacter.getCharacterName(), SwingConstants.CENTER);
        finalInfo.add(characterName);

        JLabel finalLvl = new JLabel("Character Level: " + playerCharacter.getLevel(), SwingConstants.CENTER);
        finalInfo.add(finalLvl);

        JLabel finalXp = new JLabel("Character Experience: " + playerCharacter.getExperience(), SwingConstants.CENTER);
        finalInfo.add(finalXp);

        JLabel enemiesKilled = new JLabel("Enemies Killed: " + playerCharacter.enemiesKilled, SwingConstants.CENTER);
        finalInfo.add(enemiesKilled);

        add(finalInfo);

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));
        add(quitButton);

        JButton loginButton = new JButton("Play Again");
        loginButton.addActionListener(e -> startNewGame());
        add(loginButton);
    }

    private void startNewGame() {
        controller.resetGameState();
        controller.showAuthPage();
    }
}
