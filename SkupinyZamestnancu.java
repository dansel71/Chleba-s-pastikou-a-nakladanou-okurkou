public enum SkupinyZamestnancu {
    DATOVY_ANALYTIK("Datovy analytik"),
    BEZPECNOSTNI_SPECIALISTA("Bezpecnostni specialista");

    private final String popis;

    SkupinyZamestnancu(String popis) {
        this.popis = popis;
    }

    public String getPopis() {
        return popis;
    }

    public String toString() {
        return popis;
    }

    public static SkupinyZamestnancu fromMenu(int volba) {
        return switch (volba) {
            case 1 -> DATOVY_ANALYTIK;
            case 2 -> BEZPECNOSTNI_SPECIALISTA;
            default -> throw new IllegalArgumentException("Neplatna skupina zamestnancu.");
        };
    }
}
