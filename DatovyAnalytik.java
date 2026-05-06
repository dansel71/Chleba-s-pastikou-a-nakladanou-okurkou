import java.util.HashMap;

public class DatovyAnalytik extends Zamestnanec {

    public DatovyAnalytik(String jmeno, String prijmeni, int rokNarozeni) {
        super(jmeno, prijmeni, rokNarozeni);
    }

    
    public void spustDovednost(HashMap<Integer, Zamestnanec> vsichni) {
        int nejlepsiKolegaId = -1;
        int maxSpolecnych = 0;

        for (int kolegaId : getSpolupracovnici().keySet()) {
            Zamestnanec kolega = vsichni.get(kolegaId);
            if (kolega == null) {
                continue;
            }

            int spolecnych = 0;
            for (int kk : kolega.getSpolupracovnici().keySet()) {
                if (getSpolupracovnici().containsKey(kk)) {
                    spolecnych++;
                }
            }
            if (spolecnych > maxSpolecnych) {
                maxSpolecnych = spolecnych;
                nejlepsiKolegaId = kolegaId;
            }
        }

        if (nejlepsiKolegaId == -1) {
            System.out.println("Zadni spolecni spolupracovnici nenalezeni.");
        } else {
            Zamestnanec nej = vsichni.get(nejlepsiKolegaId);
            System.out.println("Nejvice spolecnych spolupracovniku (" + maxSpolecnych +
                    ") s: " + nej.getJmeno() + " " + nej.getPrijmeni());
        }
    }

    
    public String toString() {
        return "DatovyAnalytik";
    }
}
