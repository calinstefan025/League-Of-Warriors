import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AbilitiesPage extends JDialog {
    private GameController controller;
    private Character playerCharacter;
    private Enemy enemy;
    private BattlePage battlePage;

    public AbilitiesPage(GameController controller, Character playerCharacter, Enemy enemy, BattlePage battlePage) {
        super(new Frame(), "Select Ability", true);
        this.controller = controller;
        this.playerCharacter = playerCharacter;
        this.enemy = enemy;
        this.battlePage = battlePage;

        setLayout(new BorderLayout(10, 10));
        setSize(1000, 600);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Select an Ability", SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel abilitiesPanel = new JPanel(new GridLayout(0, 6, 10, 10));
        ArrayList<Spell> abilities = playerCharacter.getAbilities();

        for (Spell ability : abilities) {
            JButton abilityButton = new JButton(ability.toString());
            abilityButton.addActionListener(e -> useAbility(ability));
            abilitiesPanel.add(abilityButton);
        }

        add(abilitiesPanel, BorderLayout.CENTER);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void useAbility(Spell ability) {
        System.out.println("Ai ales: " + ability);
        playerCharacter.useAbility(ability, enemy);
        battlePage.updateEnemyStats();
        if (enemy.getHealth() <= 0) {
            JOptionPane.showMessageDialog(this, "Ai invins inamicul!");
            controller.showGamePage(playerCharacter);
            return;
        }

        Spell enemyAbility = enemy.selectAbility(false);
        if (enemyAbility != null) {
            System.out.println("Inamicul foloseste: " + enemyAbility);
            enemy.useAbility(enemyAbility, playerCharacter);
        } else {
            int damage = enemy.getDamage();
            System.out.println("Total damage aplicat de inamic: " + damage);
            playerCharacter.receiveDamage(damage);
        }

        battlePage.updateEnemyStats();
        if (enemy.getHealth() <= 0) {
            JOptionPane.showMessageDialog(this, "Ai invins inamicul!");
            controller.showGamePage(playerCharacter);
            return;
        }

        battlePage.updateCharacterStats();
        if (playerCharacter.getHealth() <= 0) {
            JOptionPane.showMessageDialog(this, "Ai pierdut!");
            controller.showFinalPage();
            return;
        }

        battlePage.updateAbilities();

        dispose();
    }
}
