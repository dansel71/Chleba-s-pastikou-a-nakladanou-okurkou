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
            return "Rizikové skóre: 0 (žádní spolupracovníci)";
        }

        int pocet = getSpolupracovnici().size();
        double prumernaKvalita = getPrumernaKvalita();
        double sance = 4.0 - prumernaKvalita;
        double skore = pocet * sance * 10.0;
        String riziko = skore < 25 ? "nízké" : skore < 60 ? "střední" : "vysoké";

        return "Rizikové skóre: " + String.format("%.2f", skore) + " | riziko: " + riziko +
                " | počet spolupracovníků: " + pocet + " | průměrná kvalita: " + String.format("%.2f", prumernaKvalita);
    }

    @Override
    public String toString() {
        return "BezpecnostniSpecialista";
    }
}
