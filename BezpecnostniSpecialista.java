import java.util.Map;

public class BezpecnostniSpecialista extends Zamestnanec {

    public BezpecnostniSpecialista(String jmeno, String prijmeni, int rokNarozeni) {
        super(jmeno, prijmeni, rokNarozeni, SkupinyZamestnancu.BEZPECNOSTNI_SPECIALISTA);
    }

    public BezpecnostniSpecialista(int id, String jmeno, String prijmeni, int rokNarozeni) {
        super(id, jmeno, prijmeni, rokNarozeni, SkupinyZamestnancu.BEZPECNOSTNI_SPECIALISTA);
    }

    @Override
    public String spustDovednost(Map<Integer, Zamestnanec> vsichni) {
        if (getSpolupracovnici().isEmpty()) {
            return "Rizikove skore: 0 (zadni spolupracovnici)";
        }

        int pocet = getSpolupracovnici().size();
        double prumernaKvalita = getPrumernaKvalita();
        double sance = 4.0 - prumernaKvalita;
        double skore = pocet * sance * 10.0;
        String riziko = skore < 25 ? "nizke" : skore < 60 ? "stredni" : "vysoke";

        return "Rizikove skore: " + String.format("%.2f", skore) + " | riziko: " + riziko +
                " | pocet spolupracovniku: " + pocet + " | prumerna kvalita: " + String.format("%.2f", prumernaKvalita);
    }

    @Override
    public String toString() {
        return "BezpecnostniSpecialista";
    }
}
