import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class AuthPage extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private GameController controller;


    public AuthPage(GameController controller) {
        this.controller = controller;

        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);

        inputPanel.add(emailLabel);
        inputPanel.add(emailField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        JPanel buttonPanel = new JPanel();
        loginButton = new JButton("Login");
        loginButton.addActionListener(this::handleLogin);
        buttonPanel.add(loginButton);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleLogin(ActionEvent e) {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        ArrayList<Account> accounts = JsonInput.deserializeAccounts();
        for (Account acc : accounts) {
            Account.Information playerInfo = acc.getPlayerInfo();
            Credentials credentials = playerInfo.getPlayerCredentials();

            if (email.equals(credentials.getEmail()) && password.equals(credentials.getPassword())) {
                controller.setAccount(acc);
                controller.showInfoPage();
                return;
            }
        }
    }
}
