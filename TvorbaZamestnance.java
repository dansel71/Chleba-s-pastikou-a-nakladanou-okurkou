public final class TvorbaZamestnance {

    private TvorbaZamestnance() {}

    public static Zamestnanec create(SkupinyZamestnancu skupina, String jmeno, String prijmeni, int rokNarozeni) {
        return switch (skupina) {
            case DATOVY_ANALYTIK -> new DatovyAnalytik(jmeno, prijmeni, rokNarozeni);
            case BEZPECNOSTNI_SPECIALISTA -> new BezpecnostniSpecialista(jmeno, prijmeni, rokNarozeni);
        };
    }

    public static Zamestnanec create(int id, SkupinyZamestnancu skupina, String jmeno, String prijmeni, int rokNarozeni) {
        return switch (skupina) {
            case DATOVY_ANALYTIK -> new DatovyAnalytik(id, jmeno, prijmeni, rokNarozeni);
            case BEZPECNOSTNI_SPECIALISTA -> new BezpecnostniSpecialista(id, jmeno, prijmeni, rokNarozeni);
        };
    }
}
