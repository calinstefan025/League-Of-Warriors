import javax.swing.*;
import java.awt.*;

public class FinalPage extends JPanel {
    private GameController controller;

    public FinalPage(GameController controller) {
        this.controller = controller;

        setLayout(new GridLayout(2, 1, 10, 10));

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));
        add(quitButton);

        JButton loginButton = new JButton("Play Again");
        loginButton.addActionListener(e -> controller.showAuthPage());
        add(loginButton);
    }
}
