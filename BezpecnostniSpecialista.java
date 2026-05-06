import java.util.HashMap;

public class BezpecnostniSpecialista extends Zamestnanec {

    public BezpecnostniSpecialista(String jmeno, String prijmeni, int rokNarozeni) {
        super(jmeno, prijmeni, rokNarozeni);
    }

    
    public void spustDovednost(HashMap<Integer, Zamestnanec> vsichni) {
        HashMap<Integer, String> spol = getSpolupracovnici();
        if (spol.size() == 0) {
            System.out.println("Rizzikove skore: 0 (zadni spolupracovnici)");
            return;
        }

        int pocet = spol.size();
        int soucet = 0;
        for (String uroven : spol.values()) {
            if (uroven.equals("spatna")) {
                soucet += 3;
            } else if (uroven.equals("prumerna")) {
                soucet += 2;
            } else {
                soucet += 1;
            }
        }

        double prumernaKvalita = (double) soucet / pocet;
        double skore = pocet * prumernaKvalita * 10;
        System.out.println("Rizikove skore: " + skore + " (pocet: " + pocet + ", prumerna kvalita: " + prumernaKvalita + ")");
    }

    
    public String toString() {
        return "BezpecnostniSpecialista";
    }
}
