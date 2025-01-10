import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

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

        JPanel abilitiesPanel = new JPanel(new GridLayout(0, 6, 5, 5));
        ArrayList<Spell> abilities = playerCharacter.getAbilities();

        for (Spell ability : abilities) {
            String abilityName = ability.getType();
            int abilityDamage = ability.getDamageDone();
            int abilityManaCost = ability.getManaCost();

            JPanel abilityCard = new JPanel(new GridLayout(3, 1));
            abilityCard.add(new JLabel(abilityName));
            abilityCard.add(new JLabel("Damage: " + abilityDamage));
            abilityCard.add(new JLabel("Mana Cost: " + abilityManaCost));

            JButton abilityButton = new JButton();

            abilityButton.add(abilityCard);
            abilityButton.addActionListener(e -> useAbility(ability));
            abilitiesPanel.add(abilityButton);

            if (abilityName.contains("Fire")) {
                abilityButton.setBackground(Color.RED);
                abilityCard.setBackground(Color.RED);
            } else if (abilityName.contains("Ice")) {
                abilityButton.setBackground(Color.CYAN);
                abilityCard.setBackground(Color.CYAN);
            } else if (abilityName.contains("Earth")) {
                abilityButton.setBackground(Color.ORANGE);
                abilityCard.setBackground(Color.ORANGE);
            }
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

            Random rand = new Random();
            int randXp = rand.nextInt(30) + 1; // 1 - 30
            playerCharacter.setHealth(playerCharacter.getHealth() * 2);
            playerCharacter.setMana(playerCharacter.getMaxMana());
            playerCharacter.setExperience(playerCharacter.getExperience() + randXp);
            playerCharacter.enemiesKilled++;

            if (playerCharacter.getHealth() > 200) {
                playerCharacter.setHealth(200);
            }

            controller.showGamePage(playerCharacter, controller.getCurrentGrid());
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

            Random rand = new Random();
            int randXp = rand.nextInt(30) + 1; // 1 - 30
            playerCharacter.setHealth(playerCharacter.getHealth() * 2);
            playerCharacter.setMana(playerCharacter.getMaxMana());
            playerCharacter.setExperience(playerCharacter.getExperience() + randXp);
            playerCharacter.enemiesKilled++;

            if (playerCharacter.getHealth() > 200) {
                playerCharacter.setHealth(200);
            }

            controller.showGamePage(playerCharacter, controller.getCurrentGrid());
            return;
        }

        battlePage.updateCharacterStats();
        if (playerCharacter.getHealth() <= 0) {
            JOptionPane.showMessageDialog(this, "Ai pierdut!");
            controller.showFinalPage(playerCharacter);
            return;
        }

        battlePage.updateAbilities();

        dispose();
    }
}
