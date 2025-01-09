import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Random;

public class GamePage extends JPanel {
    private GameController controller;
    private JLabel healthLabel;
    private JLabel levelLabel;
    private JLabel experienceLabel;
    private JLabel manaLabel;
    private JPanel gridPanel;
    public Grid grid;
    public Game game;
    public Character playerCharacter;

    private boolean isInitialCell = true;

    public GamePage(GameController controller, Character playerCharacter) {
        this.controller = controller;
        this.game = Game.getInstance();
        this.playerCharacter = playerCharacter;
        this.grid = game.getGrid();
        grid.setPlayerCharacter(playerCharacter);

        setLayout(new BorderLayout(10, 10));

        gridPanel = new JPanel();
        add(gridPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout( 5, 5));

        JPanel detailsPanel = new JPanel(new GridLayout(4, 1));
        healthLabel = new JLabel("Health: ");
        manaLabel = new JLabel("Mana: ");
        levelLabel = new JLabel("Level: ");
        experienceLabel = new JLabel("Experience: ");

        detailsPanel.add(levelLabel);
        detailsPanel.add(experienceLabel);
        detailsPanel.add(healthLabel);
        detailsPanel.add(manaLabel);

        rightPanel.add(detailsPanel, BorderLayout.CENTER);

        JPanel directionPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        directionPanel.add(new JLabel());

        JButton upButton = new JButton("Up");
        upButton.addActionListener(e -> moveCharacter("north"));
        upButton.setBackground(Color.BLACK);
        upButton.setForeground(Color.WHITE);
        upButton.setFocusPainted(false);
        directionPanel.add(upButton);
        directionPanel.add(new JLabel());

        JButton leftButton = new JButton("Left");
        leftButton.addActionListener(e -> moveCharacter("west"));
        leftButton.setBackground(Color.BLACK);
        leftButton.setForeground(Color.WHITE);
        leftButton.setFocusPainted(false);
        directionPanel.add(leftButton);

        JButton downButton = new JButton("Down");
        downButton.addActionListener(e -> moveCharacter("south"));
        downButton.setBackground(Color.BLACK);
        downButton.setForeground(Color.WHITE);
        downButton.setFocusPainted(false);
        directionPanel.add(downButton);

        JButton rightButton = new JButton("Right");
        rightButton.addActionListener(e -> moveCharacter("east"));
        rightButton.setBackground(Color.BLACK);
        rightButton.setForeground(Color.WHITE);
        rightButton.setFocusPainted(false);
        directionPanel.add(rightButton);

        rightPanel.add(directionPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.EAST);

        initializeGrid();
        updateCharacterDetails();
    }

    private void initializeGrid() {
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(grid.rows, grid.cols, 2, 2));

        for (int i = 0; i < grid.rows; i++) {
            for (int j = 0; j < grid.cols; j++) {
                JButton cellButton = new JButton("?");
                cellButton.setBackground(Color.GRAY);
                cellButton.setEnabled(false);
                gridPanel.add(cellButton);

                if (grid.get(i).get(j).getType() == CellEntityType.PLAYER) {
                    isInitialCell = false;
                    cellButton.setText("F");
                    cellButton.setBackground(Color.BLUE);
                } else if (grid.get(i).get(j).isVisited()) {
                    cellButton.setText("V");
                    cellButton.setBackground(Color.WHITE);
                }
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void updateGrid() {
        gridPanel.removeAll();

        for (int i = 0; i < grid.rows; i++) {
            for (int j = 0; j < grid.cols; j++) {
                Cell cell = grid.get(i).get(j);
                JButton cellButton = new JButton();

                if (cell == grid.currentCell) {
                    if (cell.getType() == CellEntityType.PORTAL) {
                        regenerateMap();
                        return;
                    } else if (cell.getType() == CellEntityType.SANCTUARY) {
                        regenerateAtSanctuary();
                    } else if (cell.getType() == CellEntityType.ENEMY) {
                        Enemy enemy = grid.generateEnemy();
                        controller.showBattlePage(playerCharacter, enemy);
                    }
                    cellButton.setText(getCellDisplayText(cell));
                    cellButton.setBackground(Color.ORANGE);
                } else if (cell.isVisited()) {
                    cellButton.setText(getCellDisplayText(cell));
                    cellButton.setBackground(Color.WHITE);
                } else {
                    cellButton.setText("?");
                    cellButton.setBackground(Color.GRAY);
                }

                cellButton.setEnabled(false);
                gridPanel.add(cellButton);
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void regenerateAtSanctuary() {
        Random rand = new Random();
        int regenerareMana = rand.nextInt(150);
        int regenerareViata = rand.nextInt(100);
        System.out.println("Regenerare viata: " + regenerareViata);
        System.out.println("Regenerare mana: " + regenerareMana);
        playerCharacter.regenerateMana(regenerareMana);
        playerCharacter.regenerateHealth(regenerareViata);

        updateCharacterDetails();
    }

    private void regenerateMap() {
        Random random = new Random();
        int rows = random.nextInt(6) + 5;
        int cols = random.nextInt(6) + 5;
        grid = Grid.generateGrid(rows, cols, false);
        grid.setPlayerCharacter(playerCharacter);

        JOptionPane.showMessageDialog(this, "You reached a portal! The map has been regenerated!", "Portal Reached", JOptionPane.INFORMATION_MESSAGE);

        initializeGrid();
        updateCharacterDetails();
    }

    private void moveCharacter(String direction) {
        try {
            switch (direction) {
                case "north":
                    grid.goNorth(true);
                    break;
                case "south":
                    grid.goSouth(true);
                    break;
                case "west":
                    grid.goWest(true);
                    break;
                case "east":
                    grid.goEast(true);
                    break;
            }

            updateGrid();
            updateCharacterDetails();

        } catch (ImpossibleMove e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Invalid Move", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateCharacterDetails() {
        levelLabel.setText("Level: " + playerCharacter.getLevel());
        experienceLabel.setText("Experience: " + playerCharacter.getExperience());
        healthLabel.setText("Health: " + playerCharacter.getHealth() + " / " + playerCharacter.getMaxHealth());
        manaLabel.setText("Mana: " + playerCharacter.getMana() + " / " + playerCharacter.getMaxMana());
    }

    private String getCellDisplayText(Cell cell) {
        switch (cell.getType()) {
            case PLAYER:
                return "F";
            case ENEMY:
                return "E";
            case SANCTUARY:
                return "S";
            case PORTAL:
                return "P";
            case VOID:
            default:
                return "V";
        }
    }

}
