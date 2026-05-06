import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DatovyAnalytik extends Zamestnanec {

    public DatovyAnalytik(String jmeno, String prijmeni, int rokNarozeni) {
        super(jmeno, prijmeni, rokNarozeni, SkupinyZamestnancu.DATOVY_ANALYTIK);
    }

    public DatovyAnalytik(int id, String jmeno, String prijmeni, int rokNarozeni) {
        super(id, jmeno, prijmeni, rokNarozeni, SkupinyZamestnancu.DATOVY_ANALYTIK);
    }

    @Override
    public String spustDovednost(Map<Integer, Zamestnanec> vsichni) {
        Set<Integer> moji = getSpolupracovnici().keySet();
        Zamestnanec best = null;
        int maxSpolecnych = -1;

        for (Integer kolegaId : moji) {
            Zamestnanec kolega = vsichni.get(kolegaId);
            if (kolega == null) {
                continue;
            }
            Set<Integer> common = new HashSet<>(moji);
            common.retainAll(kolega.getSpolupracovnici().keySet());
            common.remove(getId());
            common.remove(kolega.getId());
            if (common.size() > maxSpolecnych) {
                maxSpolecnych = common.size();
                best = kolega;
            }
        }

        if (best == null) {
            return "Analytik nemá žádného existujícího spolupracovníka.";
        }
        return "Nejvíce společných spolupracovníků má s: " + best.getJmeno() + " " + best.getPrijmeni() + " (" + maxSpolecnych + ")";
    }

    @Override
    public String toString() {
        return "DatovyAnalytik";
    }
}
