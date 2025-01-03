import java.util.ArrayList;
import java.util.SortedSet;

public class Account {
    private Information playerInfo;
    private ArrayList<Character> playerCharacters;
    private int gamesPlayed;

    public Account(ArrayList<Character> playerCharacters, int gamesPlayed, Information playerInfo) {
        this.playerInfo = playerInfo;
        this.playerCharacters = playerCharacters;
        this.gamesPlayed = gamesPlayed;
    }

    // clasa interna Information
    public static class Information {
        private Credentials playerCredentials;
        public SortedSet<String> favoriteGames;
        public String playerName;
        public String playerCountry;

        public Information(Credentials playerCredentials, SortedSet<String> favoriteGames, String playerName, String playerCountry) {
            this.playerCredentials = playerCredentials;
            this.favoriteGames = favoriteGames;
            this.playerName = playerName;
            this.playerCountry = playerCountry;
        }

        public Credentials getPlayerCredentials() {
            return playerCredentials;
        }

        public SortedSet<String> getFavoriteGames() {
            return favoriteGames;
        }

        public String getPlayerName() {
            return playerName;
        }

        public String getPlayerCountry() {
            return playerCountry;
        }
    }

    // getters
    public Information getPlayerInfo() {
        return playerInfo;
    }

    public ArrayList<Character> getPlayerCharacters() {
        return playerCharacters;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    // setters
    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void setPlayerInfo(Information playerInfo) {
        this.playerInfo = playerInfo;
    }

    public void setPlayerCharacters(ArrayList<Character> playerCharacters) {
        this.playerCharacters = playerCharacters;
    }
}
