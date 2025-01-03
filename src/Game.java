import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class Game {
    private Account account;
    private Grid grid;
    private Character playerCharacter;
    private boolean isRunning;
    private ArrayList<Account> accounts;

    //colors
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";

    // singleton pattern (lazy initialization)
    private static Game instance = null;

    private Game() {
        this.isRunning = true;
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public void run() {
        System.out.println(RED + "******************************************************" + RESET);
        System.out.println(RED + "*************    League of Warriors    ***************" + RESET);
        System.out.println(RED + "******************************************************\n" + RESET);

        this.accounts = JsonInput.deserializeAccounts();

        while (isRunning) {
            login();
            selectCharacter();

            menu();

            // prima mapa va fi cea hardcodata pt testare
            // ulterior se va genera random
            generateHardCodedMap();

            while (isRunning) {
                doMove();
            }

            // programul nu se termina, selectam un cont
            isRunning = true;
        }
        System.out.println("Game over!");
    }

    private void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please log in:");

        while (true) {
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Parola: ");
            String password = scanner.nextLine();

            for (Account acc : accounts) {
                Account.Information playerInfo = acc.getPlayerInfo();
                Credentials credentials = playerInfo.getPlayerCredentials();
                if (email.equals(credentials.getEmail()) && password.equals(credentials.getPassword())) {
                    this.account = acc;
                    System.out.println("SUCCES! BINE AI VENIT, " + playerInfo.getPlayerName() + "\n");
                    // afisam jocurile preferate in ordine alfabetica
                    System.out.println("Jocurile tale preferate sunt: " + playerInfo.getFavoriteGames());
                    // afisam numarul de jocuri jucate
                    System.out.println("Ai jucat " + acc.getGamesPlayed() + " jocuri.\n");
                    return;
                }
            }
            System.out.println("Logare invalida. Incearca din nou!\n");
        }
    }

    private void selectCharacter() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Alege caracterul:");

        ArrayList<Character> characters = account.getPlayerCharacters();
        for (int i = 0; i < characters.size(); i++) {
            System.out.println(i + ": " + characters.get(i).getCharacterName());
        }

        int choice = -1;
        String input;
        while (choice < 0 || choice >= characters.size()) {
            System.out.print("Introduce un numar din cele afisate pentru a alege: ");
            input = scanner.nextLine();
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Introdu un numar valid!");
            }
        }

        this.playerCharacter = characters.get(choice);

        System.out.println(YELLOW + "\nAi ales: " + RESET + playerCharacter.getCharacterName());
        System.out.println(GREEN + "Viata: " + RESET + playerCharacter.getHealth());
        System.out.println(GREEN + "Mana: " + RESET + playerCharacter.getMana());
        System.out.println(playerCharacter.getImunities());
        System.out.println("Nivel: " + playerCharacter.getLevel());
        System.out.println("Experienta: " + playerCharacter.getExperience());
    }

    private void generateMap() {
        Random random = new Random();
        int rows = random.nextInt(6) + 5;
        int cols = random.nextInt(6) + 5;
        this.grid = Grid.generateGrid(rows, cols, false);
        grid.setPlayerCharacter(playerCharacter);
        grid.showGrid();
    }

    public void generateHardCodedMap() {
        this.grid = Grid.generateGrid(5, 5, true);
        grid.setPlayerCharacter(playerCharacter);
        grid.showGrid();
    }

    private void doMove() {
        Scanner scanner = new Scanner(System.in);

        String choice = scanner.nextLine();

        try {
            switch (choice) {
                case "w":
                    grid.goNorth();
                    break;
                case "s":
                    grid.goSouth();
                    break;
                case "d":
                    grid.goEast();
                    break;
                case "a":
                    grid.goWest();
                    break;
                case "q":
                    isRunning = false;
                    break;
                default:
                    throw new InvalidComandException();
            }

            if (grid.dead) {
                isRunning = false;
            } else {
                grid.showGrid();
            }

        } catch (ImpossibleMove e) {
            System.out.println(e.getMessage());
        } catch (PortalReachedException e) {
            System.out.println(e.getMessage());
            account.setGamesPlayed(account.getGamesPlayed() + 1);
            System.out.println("Jocuri jucate: " + account.getGamesPlayed());
            generateMap();
        } catch (InvalidComandException e) {
            System.out.println(e.getMessage());
        }
    }

    private void menu() {
        System.out.println(CYAN + "\n======================================================" + RESET);
        System.out.println(CYAN + "|                        Meniu                       |" + RESET);
        System.out.println(CYAN + "======================================================" + RESET);
        System.out.println(CYAN + "|                      (w) Nord                      |" + RESET);
        System.out.println(CYAN + "|                      (s) Sud                       |" + RESET);
        System.out.println(CYAN + "|                      (d) Est                       |" + RESET);
        System.out.println(CYAN + "|                      (a) Vest                      |" + RESET);
        System.out.println(CYAN + "|                      (q) Iesire                    |" + RESET);
        System.out.println(CYAN + "======================================================" + RESET);
    }
}
