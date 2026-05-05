import java.util.HashMap;

public abstract class Zamestnanec {

    private static int pocitadloID = 1;

    private int id;
    private String jmeno;
    private String prijmeni;
    private int rokNarozeni;
    private HashMap<Integer, String> spolupracovnici = new HashMap<Integer, String>();

    public Zamestnanec(String jmeno, String prijmeni, int rokNarozeni) {
        this.id = pocitadloID++;
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.rokNarozeni = rokNarozeni;
    }

    public abstract void spustDovednost(HashMap<Integer, Zamestnanec> vsichni);

    public void pridejSpolupracovnika(int kolegaId, String uroven) {
        spolupracovnici.put(kolegaId, uroven);
    }

    public void odeberSpolupracovnika(int kolegaId) {
        spolupracovnici.remove(kolegaId);
    }

    public void vypisInfo() {
        System.out.println("ID: " + id + " | " + jmeno + " " + prijmeni +
                " | rok narozeni: " + rokNarozeni + " | pocet spolupracovniku: " + spolupracovnici.size());
    }

    public int getId() { return id; }
    public String getJmeno() { return jmeno; }
    public String getPrijmeni() { return prijmeni; }
    public int getRokNarozeni() { return rokNarozeni; }
    public HashMap<Integer, String> getSpolupracovnici() { return spolupracovnici; }

    public static void setPocitadloID(int id) { pocitadloID = id; }
    public static int getPocitadloID() { return pocitadloID; }
}
