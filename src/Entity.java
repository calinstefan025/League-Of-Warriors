import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public abstract class Entity implements Element<Entity>, Battle {
    private ArrayList<Spell> abilities;
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
        if (abilities.isEmpty()) {
            if (isPlayer) {
                System.out.println("Nu ai abilitati disponibile");
            } else {
                System.out.println("Inamicul nu are abilitati disponibile.");
            }
            return null;
        }

        if (!isPlayer) {
            Random rand = new Random();
            int choice = rand.nextInt(abilities.size() + 2);
            if (choice >= abilities.size()) {
                System.out.println("Inamicul alege atac normal.");
                return null;
            }

            Spell selectedAbility = abilities.get(choice);
            if (selectedAbility.getManaCost() > mana) {
                System.out.println("Inamicul nu are suficienta mana pentru abilitate. \nAtac normal.");
                return null;
            }

            return selectedAbility;
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Alege o abilitate (introdu numarul):");
            for (int i = 0; i < abilities.size(); i++) {
                System.out.println(i + ": " + abilities.get(i));
            }

            String input = scanner.nextLine();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 0 && choice < abilities.size()) {
                    Spell selectedAbility = abilities.get(choice);
                    return selectedAbility;
                } else {
                    System.out.println("Numar invalid. Incearca din nou.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input invalid. Introdu un numar.");
            }
        }
    }


    public void useAbility(Spell ability, Entity target) {
        if (ability == null) {
            int damage = this.getDamage();
            System.out.println("Damage normal: " + damage + " damage!");
            target.receiveDamage(damage);
            return;
        }

        if (ability.getManaCost() > this.getMana()) {
            int damage = this.getDamage();
            System.out.println("NO SUFFICIENT MANA! \nDamage normal: " + damage + " damage!");
            target.receiveDamage(damage);
            return;
        }

        target.accept(ability);

        int spellDamage = ability.getDamageDone();
        int normalDamage = this.getDamage();
        int totalDamage = spellDamage + normalDamage;

        target.receiveDamage(totalDamage);

        this.setMana(this.getMana() - ability.getManaCost());
        if (this.getMana() < 0) {
            this.setMana(0);
        }

        abilities.remove(ability);
        System.out.println("Damage normal: " + normalDamage + " damage!");
        System.out.println("Damage abilitate: " + spellDamage + " damage!");
        System.out.println("Damage total (abilitate + normal): " + totalDamage + " damage!");
    }

    @Override
    public void accept(Visitor<Entity> visitor) {
        visitor.visit(this);
    }
}
