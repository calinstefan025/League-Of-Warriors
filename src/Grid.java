import java.awt.desktop.SystemEventListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Grid extends ArrayList<ArrayList<Cell>> {
    public int rows;
    public int cols;
    public Character playerCharacter;
    public Cell currentCell;
    public boolean dead = false;

    // colors
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String CYAN = "\u001B[36m";
    public static final String YELLOW = "\u001B[33m";

    private Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public void setPlayerCharacter(Character playerCharacter) {
        this.playerCharacter = playerCharacter;
    }

    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }

    public static Grid generateGrid(int rows , int cols , boolean hardCoded){
        if (rows > 10 || cols > 10){
            throw new IllegalArgumentException("Tabla nu poate depasi 10 linii sau 10 coloane");
        }

        Grid table = new Grid(rows, cols);

        // generare tabla goala
        for (int i = 0; i < rows; i++) {
            ArrayList<Cell> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                Cell cell = new Cell(i, j, CellEntityType.VOID);
                row.add(cell);
            }
            table.add(row);
        }

        // adaugare entitati
        if (!hardCoded){
            addEntities(table);
        } else {
            generateSpecialMap(table);
        }

        return table;
    }

    public static void addEntities(Grid table){
        // generare player
        Random rand = new Random();
        int playerRow = rand.nextInt(table.rows);
        int playerCol = rand.nextInt(table.cols);
        Cell playerCell = table.get(playerRow).get(playerCol);
        playerCell.setType(CellEntityType.PLAYER);
        table.setCurrentCell(playerCell);

        // generare minim 2 sanctuare
        int numSanctuaries = rand.nextInt(3) + 2; // 2-4 sanctuare
        for (int i = 0 ; i < numSanctuaries; i++){
            placeEntity(table, CellEntityType.SANCTUARY);
        }

        // generare minim 4 inamici
        int numEnemies = rand.nextInt(3) + 4; // 4-6 inamici
        for (int i = 0 ; i < numEnemies; i++){
            placeEntity(table, CellEntityType.ENEMY);
        }

        // generare 1 portal
        placeEntity(table, CellEntityType.PORTAL);
    }

    public static void placeEntity(Grid table, CellEntityType entityType){
        Random rand = new Random();
        int row = rand.nextInt(table.rows);
        int col = rand.nextInt(table.cols);
        Cell cell = table.get(row).get(col);
        if (cell.getType() == CellEntityType.VOID){
            cell.setType(entityType);
        } else {
            placeEntity(table, entityType);
        }
    }

    public static void generateSpecialMap(Grid table){
        // generare player
        Cell playerCell = table.get(0).get(0);
        playerCell.setType(CellEntityType.PLAYER);
        table.setCurrentCell(playerCell);

        // generare sanctuare
        table.get(0).get(3).setType(CellEntityType.SANCTUARY);
        table.get(2).get(0).setType(CellEntityType.SANCTUARY);
        table.get(1).get(3).setType(CellEntityType.SANCTUARY);
        table.get(4).get(3).setType(CellEntityType.SANCTUARY);

        // generare inamici
        table.get(3).get(4).setType(CellEntityType.ENEMY);

        // generare 1 portal
        table.get(4).get(4).setType(CellEntityType.PORTAL);
    }

    public void move(int i, int j) {
        if (currentCell != null) {
            currentCell.setType(CellEntityType.VOID);
            currentCell.setVisitedCell(true);
        }

        currentCell = this.get(i).get(j);
    }

    public void goNorth(boolean ui) throws ImpossibleMove {
        if (currentCell.getX() == 0) {
            throw new ImpossibleMove("Nu poti merge la Nord");
        }
        currentCell.setVisitedCell(true);
        move(currentCell.getX() - 1, currentCell.getY());

        checkCell(ui);
    }

    public void goSouth(boolean ui) throws ImpossibleMove{
        if (currentCell.getX() == rows - 1) {
            throw new ImpossibleMove("Nu poti merge la Sud");
        }
        currentCell.setVisitedCell(true);
        move(currentCell.getX() + 1, currentCell.getY());

        checkCell(ui);
    }

    public void goWest(boolean ui) throws ImpossibleMove {
        if (currentCell.getY() == 0) {
            throw new ImpossibleMove("Nu poti merge la Vest");
        }
        currentCell.setVisitedCell(true);
        move(currentCell.getX(), currentCell.getY() - 1);

        checkCell(ui);
    }

    public void goEast(boolean ui) throws ImpossibleMove {
        if (currentCell.getY() == cols - 1) {
            throw new ImpossibleMove("Nu poti merge la Est");
        }
        currentCell.setVisitedCell(true);
        move(currentCell.getX(), currentCell.getY() + 1);

        checkCell(ui);
    }

    // in functie de tipul celulei:
    // - daca e inamic, generam inamicul cu valori random si ne batem cu el
    // - daca e sanctuar, ne regeneram mana si viata
    // - daca e portal, generam harta noua si dam levelup
    public void checkCell(boolean ui){
        if (ui) {
            if (currentCell.getType() == CellEntityType.ENEMY && !currentCell.isVisited()) {
                System.out.println("\nAi intalnit un inamic!\n");
            }
            if (currentCell.getType() == CellEntityType.SANCTUARY && !currentCell.isVisited()) {
                System.out.println("\nAi gasit un sanctuar!\n");
            }
            if (currentCell.getType() == CellEntityType.PORTAL) {
                System.out.println("\nAi gasit un portal!\n");
            }
            return;
        }
        if (currentCell.getType() == CellEntityType.ENEMY && !currentCell.isVisited()){
            System.out.println("\nAi intalnit un inamic!\n");
            System.out.println(YELLOW + " ********************** STATISTICI INITIALE: **********************\n" + RESET);
            Enemy enemy = generateEnemy();

            playerCharacter.setAbilities(playerCharacter.generateAbilities());
            ArrayList<Spell> abilitati = playerCharacter.getAbilities();
            System.out.println(GREEN + "\nCARACTER: " + RESET + playerCharacter.getCharacterName());
            System.out.println("Viata ta: " + playerCharacter.getHealth() + " / " + playerCharacter.getMaxHealth());
            System.out.println("Mana ta: " + playerCharacter.getMana() + " / " + playerCharacter.getMaxMana());
            for (int i = 0 ; i < abilitati.size() ; i++) {
                System.out.println(i + ": " + abilitati.get(i));
            }
            System.out.println(playerCharacter.getImunities());

            System.out.println(YELLOW + "\n ********************** INCEPE LUPTA: **********************" + RESET);

            // lupta
            battle(playerCharacter, enemy);

            // daca playerul castiga isi regenereaza viata si mana si castiga experienta
            if (!dead) {
                playerCharacter.regenerateHealth(50);
                playerCharacter.regenerateMana(50);
                playerCharacter.gainExperience(30);
            }

        } else if (currentCell.getType() == CellEntityType.SANCTUARY && !currentCell.isVisited()){
            Random rand = new Random();
            System.out.println("\nAi gasit un sanctuar!\n");

            System.out.println("Viata curenta: " + playerCharacter.getHealth() + " / " + playerCharacter.getMaxHealth());
            System.out.println("Mana curenta: " + playerCharacter.getMana() + " / " + playerCharacter.getMaxMana());

            int regenerareMana = rand.nextInt(150); // 0-150 regenerare mana
            int regenerareViata = rand.nextInt(100); // 0-100 regenerare viata
            playerCharacter.regenerateMana(regenerareMana);
            playerCharacter.regenerateHealth(regenerareViata);

            System.out.println("Viata regenerata: " + playerCharacter.getHealth() + " / " + playerCharacter.getMaxHealth());
            System.out.println("Mana regenerata: " + playerCharacter.getMana() + " / " + playerCharacter.getMaxMana());

        } else if (currentCell.getType() == CellEntityType.PORTAL){
            playerCharacter.levelUp();
            System.out.println("\nNivelul tau a crescut: " + playerCharacter.getLevel());
            System.out.println("Experienta ta: " + playerCharacter.getExperience());
            throw new PortalReachedException();
        }
    }

    public Enemy generateEnemy(){
        Random rand = new Random();
        int baseDamage = rand.nextInt(40) + 10; // 10 - 50 damage

        Enemy enemy = new Enemy(baseDamage);
        enemy.setAbilities(enemy.generateAbilities());

        System.out.println(RED + "INAMIC:" + RESET);
        System.out.println("Viata inamic: " + enemy.getHealth() + " / " + enemy.getMaxHealth());
        System.out.println("Mana inamic: " + enemy.getMana() + " / " + enemy.getMaxMana());
        System.out.println("Damage normal dat de inamic: " + enemy.getBaseDamage());
        ArrayList<Spell> abilitati = enemy.getAbilities();
        for (int i = 0 ; i < abilitati.size() ; i++) {
            System.out.println(i + ": " + abilitati.get(i));
        }
        System.out.println(enemy.getImunities());

        return enemy;
    }

    // bataia are loc pe ture
    // jucatorul alege o abilitate si o foloseste
    // inamicul alege o abilitate si o foloseste
    public void battle(Character player, Enemy enemy) {
        Scanner scanner = new Scanner(System.in);

        while (player.getHealth() > 0 && enemy.getHealth() > 0) {
            System.out.println("\nAlege modul de atac:");
            System.out.println("1. Atac normal");
            System.out.println("2. Folosire abilitate");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.println("\nAi ales atac normal.");
                player.useAbility(null, enemy);
            } else if (choice.equals("2")) {
                System.out.println("\nAlege o abilitate (introdu numarul):");
                for (int i = 0; i < player.getAbilities().size(); i++) {
                    System.out.println(i + ": " + player.getAbilities().get(i));
                }
                String input = scanner.nextLine();
                try {
                    int abilityIndex = Integer.parseInt(input);
                    if (abilityIndex >= 0 && abilityIndex < player.getAbilities().size()) {
                        Spell selectedAbility = player.getAbilities().get(abilityIndex);
                        System.out.println("Ai ales: " + selectedAbility);
                        player.useAbility(selectedAbility, enemy);
                    } else {
                        System.out.println("Abilitate invalida. Atac normal.");
                        player.useAbility(null, enemy);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Input invalid. Introdu un numar.");
                }
            } else {
                System.out.println("Optiune invalida. Incearca din nou.");
                continue;
            }

            System.out.println(GREEN + "\nViata ta: " + RESET + player.getHealth() + " / " + player.getMaxHealth());
            System.out.println(GREEN + "Mana ta: " + RESET + player.getMana() + " / " + player.getMaxMana());
            System.out.println(RED + "Viata inamic: " + RESET + enemy.getHealth() + " / " + enemy.getMaxHealth());
            System.out.println(RED + "Mana inamic: " + RESET + enemy.getMana() + " / " + enemy.getMaxMana());

            if (enemy.getHealth() <= 0) {
                System.out.println("\nInamicul a fost invins!");
                break;
            }

            System.out.println("\nInamicul ataca!");
            Spell enemyAbility = enemy.selectAbility(false);
            if (enemyAbility != null) {
                System.out.println("Inamicul foloseste: " + enemyAbility);
                enemy.useAbility(enemyAbility, player);
            } else {
                int damage = enemy.getDamage();
                System.out.println("Total damage aplicat de inamic: " + damage);
                player.receiveDamage(damage);
            }

            System.out.println(GREEN + "\nViata ta: " + RESET + player.getHealth() + " / " + player.getMaxHealth());
            System.out.println(GREEN + "Mana ta: " + RESET + player.getMana() + " / " + player.getMaxMana());
            System.out.println(RED + "Viata inamic: " + RESET + enemy.getHealth() + " / " + enemy.getMaxHealth());
            System.out.println(RED + "Mana inamic: " + RESET + enemy.getMana() + " / " + enemy.getMaxMana());

            if (player.getHealth() <= 0) {
                System.out.println("\nAi fost invins!");
                dead = true;
            }
        }
    }

    public void showGrid() {
        System.out.println();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell cell = this.get(i).get(j);
                // incadram celula curenta intre | |
                if (cell == currentCell) {
                    switch (cell.getType()) {
                        case PLAYER:
                            System.out.print("|P|");
                            break;
                        case ENEMY:
                            System.out.print("|E|");
                            break;
                        case SANCTUARY:
                            System.out.print("|S|");
                            break;
                        case PORTAL:
                            System.out.print("|F|");
                            break;
                        case VOID:
                            System.out.print("|V|");
                            break;
                    }
                } else if (cell.isVisited()) {
                    switch (cell.getType()) {
                        case PLAYER:
                            System.out.print(" P ");
                            break;
                        case ENEMY:
                            System.out.print(" E ");
                            break;
                        case SANCTUARY:
                            System.out.print(" S ");
                            break;
                        case PORTAL:
                            System.out.print(" F ");
                            break;
                        case VOID:
                            System.out.print(" V ");
                            break;
                    }
                } else {
                    System.out.print(" N ");
                }
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("======================================================");
        System.out.println();
    }
}

