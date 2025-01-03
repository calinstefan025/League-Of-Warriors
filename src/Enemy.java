import java.util.Random;

public class Enemy extends Entity {
    private int damage;

    // de lucrat la enemy
    public Enemy(int damage) {
        super(
                randomImunity(),
                randomImunity(),
                randomImunity()
        );
        this.damage = damage;
    }

    private static int randomInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public static boolean randomImunity() {
        Random random = new Random();
        return random.nextBoolean();
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void receiveDamage(int damage) {
        Random rand = new Random();

        if (rand.nextBoolean()) {
            System.out.println("Inamicul a evitat atacul.");
            return;
        }

        int currentHealth = this.getHealth();
        currentHealth -= damage;
        if (currentHealth < 0) {
            currentHealth = 0;
        }

        this.setHealth(currentHealth);
    }

    public int getBaseDamage() {
        return this.damage;
    }

    public int getDamage() {
        Random rand = new Random();
        int baseDamage = this.getBaseDamage();
        if (rand.nextBoolean()) {
            baseDamage *= 2;
            System.out.println("Enemy a dat critical hit!");
        }

        return baseDamage;
    }
}
