public abstract class Character extends Entity{
    private String characterName;
    private int experience;
    private int level;
    private int strength , dexterity , charisma;

    public int enemiesKilled = 0;

    public Character(String name, boolean immuneToFire, boolean immuneToIce, boolean immuneToEarth) {
        super(immuneToFire, immuneToIce, immuneToEarth);
        this.characterName = name;
    }

    public String getCharacterName() {
        return characterName;
    }

    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    public int getStrength() {
        return strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getCharisma() {
        return charisma;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void gainExperience(int experience) {
        this.experience += experience;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void levelUp() {
        this.level++;
        this.setHealth(200);
        this.setMana(200);
        gainExperience(this.level * 5);
        this.setStrength(this.getStrength() + 5);
        this.setDexterity(this.getDexterity() + 5);
        this.setCharisma(this.getCharisma() + 5);
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }
}
