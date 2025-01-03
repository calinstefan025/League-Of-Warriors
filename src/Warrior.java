import java.util.Random;

public class Warrior extends Character {
    public Warrior(String name, int experience, int level) {
        super(name,true, false, false);
        // atribut principal strength
        Random rand = new Random();
        int randStrength = rand.nextInt(10) + 40; // 40 - 50
        int randDexterity = rand.nextInt(10) + 20; // 20 - 30
        int randCharisma = rand.nextInt(10) + 10; // 10 - 20
        this.setStrength(randStrength);
        this.setDexterity(randDexterity);
        this.setCharisma(randCharisma);

        this.setExperience(experience);
        this.setLevel(level);
    }

    @Override
    public void receiveDamage(int damage) {
        Random rand = new Random();

        // in fct de atributele secundare exista sansa de 50% sa se injum damageul
        if (this.getDexterity() + this.getCharisma() > 32) {
            if (rand.nextBoolean()) {
                damage /= 2;
            }
        }

        // se scade damageul primit din viata
        int currentHealth = this.getHealth();
        currentHealth -= damage;

        // daca viata scade sub 0, se seteaza la 0
        if (currentHealth < 0) {
            currentHealth = 0;
        }

        this.setHealth(currentHealth);

        System.out.println(this.getCharacterName() + " a primit: " + damage + " damage.");
    }

    @Override
    public int getDamage() {
        Random rand = new Random();

        int baseDamage = this.getStrength();
        if (baseDamage > 45) {
            if (rand.nextBoolean()) {
                baseDamage *= 2;
                System.out.println("Critical hit!");
            }
        }

        System.out.println(this.getCharacterName() + " a dat: " + baseDamage + " damage.");
        return baseDamage;
    }
}
