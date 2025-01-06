public abstract class Spell implements Visitor<Entity> {
    private int damage;
    private int manaCost;
    private String type;

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String CYAN = "\u001B[36m";
    public static final String YELLOW = "\u001B[33m";

    public Spell(int damage, int manaCost, String type) {
        this.damage = damage;
        this.manaCost = manaCost;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public int getDamageDone() {
        return damage;
    }

    public int getManaCost() {
        return manaCost;
    }

    @Override
    public void visit(Entity entity) {
        if (entity.isImmuneToFire() && this instanceof Fire) {
            System.out.println("Imun la Fire!");
            this.damage = 0;
        }
        if (entity.isImmuneToIce() && this instanceof Ice) {
            System.out.println("Imun la Ice!");
            this.damage = 0;
        }
        if (entity.isImmuneToEarth() && this instanceof Earth) {
            System.out.println("Imun la Earth!");
            this.damage = 0;
        }
    }

    @Override
    public String toString() {
        if (type.equals("Fire")) {
            return RED + "[ " +
                    "'" + type + '\'' +
                    ", damage= " + damage +
                    ", manaCost= " + manaCost +
                    " ]" + RESET;
        } else if (type.equals("Ice")) {
            return CYAN + "[ " +
                    "'" + type + '\'' +
                    ", damage= " + damage +
                    ", manaCost= " + manaCost +
                    " ]" + RESET;
        }
        return YELLOW + "[ " +
                "'" + type + '\'' +
                ", damage= " + damage +
                ", manaCost= " + manaCost +
                " ]" + RESET;
    }

}
