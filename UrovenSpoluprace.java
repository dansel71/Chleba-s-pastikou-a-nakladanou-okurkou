public enum UrovenSpoluprace {
    SPATNA(1, "ĹˇpatnĂˇ"),
    PRUMERNA(2, "prĹŻmÄ›rnĂˇ"),
    DOBRA(3, "dobrĂˇ");

    private final int hodnota;
    private final String popis;

    UrovenSpoluprace(int hodnota, String popis) {
        this.hodnota = hodnota;
        this.popis = popis;
    }

    public int getHodnota() {
        return hodnota;
    }

    public String getPopis() {
        return popis;
    }

    public String toString() {
        return popis;
    }

    public static UrovenSpoluprace fromMenu(int volba) {
        return switch (volba) {
            case 1 -> SPATNA;
            case 2 -> PRUMERNA;
            case 3 -> DOBRA;
            default -> throw new IllegalArgumentException("NeplatnĂˇ ĂşroveĹ spoluprĂˇce.");
        };
    }

    public static UrovenSpoluprace fromString(String text) {
        if (text == null) throw new IllegalArgumentException("ĂšroveĹ spoluprĂˇce nesmĂ­ bĂ˝t null.");
        String normalized = text.trim().toLowerCase();
        return switch (normalized) {
            case "ĹˇpatnĂˇ", "spatna" -> SPATNA;
            case "prĹŻmÄ›rnĂˇ", "prumerna" -> PRUMERNA;
            case "dobrĂˇ", "dobra" -> DOBRA;
            default -> throw new IllegalArgumentException("NeznĂˇmĂˇ ĂşroveĹ spoluprĂˇce: " + text);
        };
    }
}
