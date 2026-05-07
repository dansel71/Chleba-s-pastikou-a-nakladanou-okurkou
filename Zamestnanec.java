import java.time.Year;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class Zamestnanec {

    private static int pocitadloID = 1;

    private final int id;
    private final String jmeno;
    private final String prijmeni;
    private final int rokNarozeni;
    private final SkupinyZamestnancu skupina;
    private final HashMap<Integer, UrovenSpoluprace> spolupracovnici = new HashMap<>();

    protected Zamestnanec(String jmeno, String prijmeni, int rokNarozeni, SkupinyZamestnancu skupina) {
        this.id = pocitadloID++;
        this.jmeno = validateText(jmeno, "jmeno");
        this.prijmeni = validateText(prijmeni, "prijmeni");
        this.rokNarozeni = validateRok(rokNarozeni);
        this.skupina = Objects.requireNonNull(skupina, "Skupina nesmi byt null.");
    }

    protected Zamestnanec(int id, String jmeno, String prijmeni, int rokNarozeni, SkupinyZamestnancu skupina) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID musi byt kladne.");
        }
        this.id = id;
        pocitadloID = Math.max(pocitadloID, id + 1);
        this.jmeno = validateText(jmeno, "jmeno");
        this.prijmeni = validateText(prijmeni, "prijmeni");
        this.rokNarozeni = validateRok(rokNarozeni);
        this.skupina = Objects.requireNonNull(skupina, "Skupina nesmi byt null.");
    }

    private static String validateText(String text, String fieldName) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(fieldName + " nesmi byt prazdne.");
        }
        return text.trim();
    }

    private static int validateRok(int rok) {
        int currentYear = Year.now().getValue();
        if (rok < 1900 || rok > currentYear) {
            throw new IllegalArgumentException("Rok narozeni musi byt mezi 1900 a " + currentYear + ".");
        }
        return rok;
    }

    public abstract String spustDovednost(Map<Integer, Zamestnanec> vsichni);

    public void pridejSpolupracovnika(int kolegaId, UrovenSpoluprace uroven) {
        if (kolegaId == id) {
            throw new IllegalArgumentException("Zamestnanec nemuze spolupracovat sam se sebou.");
        }
        spolupracovnici.put(kolegaId, Objects.requireNonNull(uroven, "Uroven spoluprace nesmi byt null."));
    }

    public void odeberSpolupracovnika(int kolegaId) {
        spolupracovnici.remove(kolegaId);
    }

    public void vypisInfo() {
        System.out.println(getZakladniInfo() + " | pocet spolupracovniku: " + spolupracovnici.size());
    }

    public String getZakladniInfo() {
        return "ID: " + id + " | " + jmeno + " " + prijmeni + " | rok narozeni: " + rokNarozeni + " | skupina: " + skupina.getPopis();
    }

    public String getStatistikySpoluprace() {
        return "pocet vazeb: " + spolupracovnici.size() + " | prumer kvality: " + String.format("%.2f", getPrumernaKvalita());
    }

    public double getPrumernaKvalita() {
        return spolupracovnici.values().stream().mapToInt(UrovenSpoluprace::getHodnota).average().orElse(0.0);
    }

    public int getId() {
        return id;
    }

    public String getJmeno() {
        return jmeno;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public int getRokNarozeni() {
        return rokNarozeni;
    }

    public SkupinyZamestnancu getSkupina() {
        return skupina;
    }

    public Map<Integer, UrovenSpoluprace> getSpolupracovnici() {
        return Collections.unmodifiableMap(spolupracovnici);
    }

    public static void setPocitadloID(int id) {
        pocitadloID = id;
    }

    public static int getPocitadloID() {
        return pocitadloID;
    }
}
