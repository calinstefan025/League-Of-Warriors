import java.util.Random;

public class Rogue extends Character {
    public Rogue(String name, int experience, int level) {
        super(name,false, false, true);
        Random rand = new Random();
        int randStrength = rand.nextInt(10) + 20; // 20 - 30
        int randDexterity = rand.nextInt(10) + 40; // 40 - 50
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
        if (this.getCharisma() + this.getStrength() > 32) {
            if (rand.nextBoolean()) {
                damage /= 2;
            }
        }

        int currentHealth = this.getHealth();
        currentHealth -= damage;
        if (currentHealth < 0) {
            currentHealth = 0;
        }
        this.setHealth(currentHealth);
    }

    @Override
    public int getDamage() {
        Random rand = new Random();

        int baseDamage = this.getDexterity();
        if (baseDamage > 45) {
            if (rand.nextBoolean()) {
                baseDamage *= 2;
                System.out.println("Critical hit!");
            }
        }

        return baseDamage;
    }
}
