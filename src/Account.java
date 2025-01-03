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

        private Information(Builder builder) {
            this.playerCredentials = builder.playerCredentials;
            this.favoriteGames = builder.favoriteGames;
            this.playerName = builder.playerName;
            this.playerCountry = builder.playerCountry;
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

        public static class Builder {
            private Credentials playerCredentials;
            private SortedSet<String> favoriteGames;
            private String playerName;
            private String playerCountry;

            public Builder setCredentials(Credentials playerCredentials) {
                this.playerCredentials = playerCredentials;
                return this;
            }

            public Builder setFavoriteGames(SortedSet<String> favoriteGames) {
                this.favoriteGames = favoriteGames;
                return this;
            }

            public Builder setPlayerName(String playerName) {
                this.playerName = playerName;
                return this;
            }

            public Builder setPlayerCountry(String playerCountry) {
                this.playerCountry = playerCountry;
                return this;
            }

            public Information build() {
                return new Information(this);
            }
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
