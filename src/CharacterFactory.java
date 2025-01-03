// Factory pattern

public class CharacterFactory {
    public static Character createCharacter(String profession, String name, int experience, int level) {
        switch (profession) {
            case "Warrior":
                return new Warrior(name, experience, level);
            case "Rogue":
                return new Rogue(name, experience, level);
            case "Mage":
                return new Mage(name, experience, level);
            default:
                throw new IllegalArgumentException("Caracterul nu exista");
        }
    }
}
