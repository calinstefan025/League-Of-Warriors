public abstract class Spell {
    private final int damage;
    private final int manaCost;
    private final String type;

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String CYAN = "\u001B[36m";
    public static final String YELLOW = "\u001B[33m";

    public Spell(int damage, int manaCost, String type) {
        this.damage = damage;
        this.manaCost = manaCost;
        this.type = type;
    }

    public int getDamageDone() {
        return damage;
    }

    public int getManaCost() {
        return manaCost;
    }

    public String getType() {
        return type;
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
