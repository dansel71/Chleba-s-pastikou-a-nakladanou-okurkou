public enum UrovenSpoluprace {
    SPATNA(1, "spatna"),
    PRUMERNA(2, "prumerna"),
    DOBRA(3, "dobra");

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
            default -> throw new IllegalArgumentException("Neplatna uroven spoluprace.");
        };
    }

    public static UrovenSpoluprace fromString(String text) {
        if (text == null) throw new IllegalArgumentException("Uroven spoluprace nesmi byt null.");
        String normalized = text.trim().toLowerCase();
        return switch (normalized) {
            case "spatna" -> SPATNA;
            case "prumerna" -> PRUMERNA;
            case "dobra" -> DOBRA;
            default -> throw new IllegalArgumentException("Neznama uroven spoluprace: " + text);
        };
    }
}
