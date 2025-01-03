import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public abstract class Entity implements Battle {
    private ArrayList<Spell> abilities; // decat pentru caractere, nu pentru inamici
    private int health;
    private int maxHealth;
    private int mana;
    private int maxMana;
    private boolean immuneToFire;
    private boolean immuneToIce;
    private boolean immuneToEarth;

    public Entity() {
        this.health = 0;
        this.maxHealth = 200;
        this.mana = 0;
        this.maxMana = 200;
        this.immuneToFire = false;
        this.immuneToIce = false;
        this.immuneToEarth = false;
        this.abilities = new ArrayList<>();
    }

    public Entity(boolean immuneToFire, boolean immuneToIce, boolean immuneToEarth) {
        int randHealth = new Random().nextInt(100) + 100; // 100 - 200
        int randMana = new Random().nextInt(100) + 100; // 100 - 200
        this.health = randHealth;
        this.maxHealth = 200;
        this.mana = randMana;
        this.maxMana = 200;
        this.immuneToFire = immuneToFire;
        this.immuneToIce = immuneToIce;
        this.immuneToEarth = immuneToEarth;
        this.abilities = new ArrayList<>();
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getMana() {
        return mana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public boolean isImmuneToFire() {
        return immuneToFire;
    }

    public boolean isImmuneToIce() {
        return immuneToIce;
    }

    public boolean isImmuneToEarth() {
        return immuneToEarth;
    }

    public ArrayList<Spell> getAbilities() {
        return this.abilities;
    }

    public void setAbilities(ArrayList<Spell> abilities) {
        this.abilities = abilities;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    public void regenerateHealth(int amount) {
        if (this.health + amount > maxHealth) {
            this.health = maxHealth;
        } else {
            this.health += amount;
        }
    }

    public void regenerateMana(int amount) {
        if (this.mana + amount > maxMana) {
            this.mana = maxMana;
        } else {
            this.mana += amount;
        }
    }

    public ArrayList<Spell> generateAbilities() {
        ArrayList<Spell> abilities = new ArrayList<>();
        Random rand = new Random();

        int maxDamage = 60, minDamage = 10;
        int maxManaCost = 40, minManaCost = 5;
        int numAbilities = rand.nextInt(4) + 3; // 3-6 abilities

        // generam minim 3 abilitati
        abilities.add(new Fire(
                rand.nextInt(maxDamage - minDamage + 1) + minDamage,
                rand.nextInt(maxMana - minManaCost + 1) + minManaCost
        ));
        abilities.add(new Ice(
                rand.nextInt(maxDamage - minDamage + 1) + minDamage,
                rand.nextInt(maxMana - minManaCost + 1) + minManaCost
        ));
        abilities.add(new Earth(
                rand.nextInt(maxDamage - minDamage + 1) + minDamage,
                rand.nextInt(maxMana - minManaCost + 1) + minManaCost
        ));

        // generam restul abilitatilor
        for (int i = 3; i < numAbilities; i++) {
            int damage = rand.nextInt(maxDamage - minDamage + 1) + minDamage;
            int manaCost = rand.nextInt(maxMana - minManaCost + 1) + minManaCost;

            int tip = rand.nextInt(3);

            if (tip == 0) {
                abilities.add(new Fire(damage, manaCost));
            } else if (tip == 1) {
                abilities.add(new Ice(damage, manaCost));
            } else {
                abilities.add(new Earth(damage, manaCost));
            }
        }

        return abilities;
    }

    public abstract void receiveDamage(int damage);
    public abstract int getDamage();


    public String getImunities() {
        StringBuilder imunities = new StringBuilder("Imunitati: ");
        if (this.isImmuneToFire()) {
            imunities.append("fire ");
        }
        if (this.isImmuneToIce()) {
            imunities.append("ice ");
        }
        if (this.isImmuneToEarth()) {
            imunities.append("earth ");
        }

        return imunities.toString();
    }

    public Spell selectAbility(boolean isPlayer) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Spell> abilities = this.getAbilities();

        if (!isPlayer) {
            Random rand = new Random();
            // alegem o abilitate random si daca random > abilities.size() atunci alegem sa dam damage normal
            int choice = rand.nextInt(this.abilities.size() + 2);
            if (choice >= this.abilities.size()) {
                System.out.println("Inamicul da damage normal.");
                return null;
            }

            if (abilities.isEmpty()) {
                System.out.println("Inamicul nu are abilitati disponibile.");
                return null;
            }

            Spell selectedAbility = this.abilities.get(choice);
            if (selectedAbility.getManaCost() > this.getMana()) {
                System.out.println("Inamicul nu are suficienta mana pentru abilitate. Da damage normal.");
                return null;
            }

            return selectedAbility;
        }

        if (abilities.isEmpty()) {
            System.out.println("Nu ai abilitati disponibile.");
            return null;
        }

        String input;
        int choice = -1;

        while (choice < 0 || choice >= abilities.size()) {
            input = scanner.nextLine();
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Introdu un numar valid.");
            }
        }

        Spell selectedAbility = abilities.get(choice);
        if (selectedAbility.getManaCost() > this.getMana()) {
            System.out.println("Nu ai suficienta mana. Dai damage normal.");
            return null;
        }

        return selectedAbility;
    }

    public void useAbility(Spell ability, Entity target) {
        if (ability == null) {
            int damage = this.getDamage(); // damage normal
            System.out.println("Total damage aplicat: " + damage);
            target.receiveDamage(damage);
            return;
        }

        int normalDamage = this.getDamage();

        if (ability instanceof Fire && target.isImmuneToFire()) {
            System.out.println("Imun la foc.");
            System.out.println("Total damage aplicat: " + normalDamage);
            target.receiveDamage(normalDamage);
            return;
        }

        if (ability instanceof Ice && target.isImmuneToIce()) {
            System.out.println("Imun la gheață.");
            System.out.println("Total damage aplicat: " + normalDamage);
            target.receiveDamage(normalDamage);
            return;
        }

        if (ability instanceof Earth && target.isImmuneToEarth()) {
            System.out.println("Imun la pământ.");
            System.out.println("Total damage aplicat: " + normalDamage);
            target.receiveDamage(normalDamage);
            return;
        }

        int spellDamage = ability.getDamageDone();
        int totalDamage = normalDamage + spellDamage;

        this.setMana(this.getMana() - ability.getManaCost());
        if (this.getMana() < 0) {
            this.setMana(0);
        }

        this.abilities.remove(ability);

        System.out.println("Total damage aplicat: " + totalDamage);
        target.receiveDamage(totalDamage);
    }
}
