import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class BattlePage extends JPanel {
    private GameController controller;
    private Character playerCharacter;
    private Enemy enemy;
    private Grid grid;

    private ArrayList<Spell> playerAbilities;
    private ArrayList<Spell> enemyAbilities;

    private JTextField playerHealthField;
    private JTextField playerManaField;
    private JTextField playerImmunitiesField;
    private JTextField enemyHealthField;
    private JTextField enemyManaField;
    private JTextField enemyImmunitiesField;

    private JPanel abilityPanel = new JPanel();

    public BattlePage(GameController controller, Character playerCharacter, Enemy enemy, Grid grid) {
        this.controller = controller;
        this.playerCharacter = playerCharacter;
        this.enemy = enemy;
        this.grid = grid;


        playerCharacter.setAbilities(playerCharacter.generateAbilities());

        playerAbilities = playerCharacter.getAbilities();
        enemyAbilities = enemy.getAbilities();


        setLayout(new BorderLayout( 5, 5));

        JPanel playerPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        playerHealthField = new JTextField("Health: " + playerCharacter.getHealth() + " / " + playerCharacter.getMaxHealth());
        playerHealthField.setEditable(false);
        playerHealthField.setHorizontalAlignment(JTextField.CENTER);
        playerPanel.add(playerHealthField);

        playerManaField = new JTextField("Mana: " + playerCharacter.getMana() + " / " + playerCharacter.getMaxMana());
        playerManaField.setEditable(false);
        playerManaField.setHorizontalAlignment(JTextField.CENTER);
        playerPanel.add(playerManaField);

        playerImmunitiesField = new JTextField(playerCharacter.getImunities());
        playerImmunitiesField.setEditable(false);
        playerImmunitiesField.setHorizontalAlignment(JTextField.CENTER);
        playerPanel.add(playerImmunitiesField);

        playerPanel.setBorder(BorderFactory.createTitledBorder("Player"));
        playerPanel.setPreferredSize(new Dimension(300, 600));

        JPanel enemyPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        enemyHealthField = new JTextField("Health: " + enemy.getHealth() + " / " + enemy.getMaxHealth());
        enemyHealthField.setEditable(false);
        enemyHealthField.setHorizontalAlignment(JTextField.CENTER);
        enemyPanel.add(enemyHealthField);

        enemyManaField = new JTextField("Mana: " + enemy.getMana() + " / " + enemy.getMaxMana());
        enemyManaField.setEditable(false);
        enemyManaField.setHorizontalAlignment(JTextField.CENTER);
        enemyPanel.add(enemyManaField);

        enemyImmunitiesField = new JTextField(enemy.getImunities());
        enemyImmunitiesField.setEditable(false);
        enemyImmunitiesField.setHorizontalAlignment(JTextField.CENTER);
        enemyPanel.add(enemyImmunitiesField);

        enemyPanel.setBorder(BorderFactory.createTitledBorder("Enemy"));
        enemyPanel.setPreferredSize(new Dimension(300, 600));

        add(playerPanel, BorderLayout.WEST);
        add(enemyPanel, BorderLayout.EAST);

        abilityPanel = new JPanel(new GridLayout(15, 1, 5, 5));
        add(abilityPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        JButton attackButton = new JButton("Attack");
        attackButton.setBackground(Color.BLACK);
        attackButton.setForeground(Color.WHITE);
        attackButton.setFocusPainted(false);

        JButton abilityButton = new JButton("Use Ability");
        abilityButton.setBackground(Color.BLACK);
        abilityButton.setForeground(Color.WHITE);
        abilityButton.setFocusPainted(false);

        buttonPanel.add(attackButton);
        buttonPanel.add(abilityButton);

        updateAbilities();

        add(buttonPanel, BorderLayout.SOUTH);

        attackButton.addActionListener(e -> attackEnemy(playerCharacter, enemy));
        abilityButton.addActionListener(e -> showAbilitiesPage(playerCharacter, enemy));
    }

    public void showAbilitiesPage(Character playerCharacter, Enemy enemy) {
        new AbilitiesPage(controller, playerCharacter, enemy, this);
    }

    public void attackEnemy(Character playerCharacter, Enemy enemy) {
        playerCharacter.useAbility(null, enemy);
        updateEnemyStats();
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

            controller.showGamePage(playerCharacter, grid);
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

        updateEnemyStats();
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

            controller.showGamePage(playerCharacter, grid);
            return;
        }

        updateCharacterStats();
        if (playerCharacter.getHealth() <= 0) {
            JOptionPane.showMessageDialog(this, "Ai pierdut!");
            controller.showFinalPage(playerCharacter);
            return;
        }

        updateAbilities();
    }

    public void updateEnemyStats() {
        enemyHealthField.setText("Health: " + enemy.getHealth() + " / " + enemy.getMaxHealth());
        enemyManaField.setText("Mana: " + enemy.getMana() + " / " + enemy.getMaxMana());
    }

    public void updateCharacterStats() {
        playerHealthField.setText("Health: " + playerCharacter.getHealth() + " / " + playerCharacter.getMaxHealth());
        playerManaField.setText("Mana: " + playerCharacter.getMana() + " / " + playerCharacter.getMaxMana());
    }

    public void updateAbilities() {
        abilityPanel.removeAll();

        playerAbilities = playerCharacter.getAbilities();
        enemyAbilities = enemy.getAbilities();

        JLabel emptyLabel = new JLabel();
        JLabel playerAbilityLabel = new JLabel("Player abilities:" , SwingConstants.CENTER);
        abilityPanel.add(playerAbilityLabel);

        for (Spell ability : playerAbilities) {
            String abilityName = removeColorCodes(ability);
            JLabel abilityLabel1 = new JLabel(abilityName , SwingConstants.CENTER);
            if (abilityName.contains("Fire")) {
                abilityLabel1.setForeground(Color.RED);
            } else if (abilityName.contains("Ice")) {
                abilityLabel1.setForeground(Color.BLUE);
            } else if (abilityName.contains("Earth")) {
                abilityLabel1.setForeground(Color.ORANGE);
            }
            abilityPanel.add(abilityLabel1);
        }

        abilityPanel.add(emptyLabel);

        JLabel enemyAbilityLabel = new JLabel("Enemy abilities:" , SwingConstants.CENTER);
        abilityPanel.add(enemyAbilityLabel);

        for (Spell ability : enemyAbilities) {
            String abilityName = removeColorCodes(ability);
            JLabel abilityLabel2 = new JLabel(abilityName, SwingConstants.CENTER);
            if (abilityName.contains("Fire")) {
                abilityLabel2.setForeground(Color.RED);
            } else if (abilityName.contains("Ice")) {
                abilityLabel2.setForeground(Color.BLUE);
            } else if (abilityName.contains("Earth")) {
                abilityLabel2.setForeground(Color.ORANGE);
            }
            abilityPanel.add(abilityLabel2);
        }

        add(abilityPanel, BorderLayout.CENTER);

        abilityPanel.revalidate();
        abilityPanel.repaint();
    }

    public String removeColorCodes(Spell ability) {
        String abilityName = ability.toString();
        abilityName = abilityName.replaceAll("\u001B\\[[;\\d]*m", "");

        return abilityName;
    }
}
