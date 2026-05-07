import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;

public class ConsoleApp {

    private final Scanner scanner = new Scanner(System.in);
    private final Databaze databaze = new Databaze();
    private final Ukladani ukladani = new Ukladani();
    private final SQL sql = new SQL("zaloha.db");

    public static void main(String[] args) {
        new ConsoleApp().run();
    }

    private void run() {
        try {
            sql.nactiVse(databaze);
            System.out.println("SQL data nactena.");
        } catch (Exception e) {
            System.out.println("SQL neni dostupne nebo se nedari nacist: " + e.getMessage());
        }

        boolean running = true;
        while (running) {
            printMenu();
            try {
                switch (readInt("Volba: ")) {
                    case 1 -> pridejZamestnance();
                    case 2 -> pridejSpolupraci();
                    case 3 -> odeberZamestnance();
                    case 4 -> najdiZamestnance();
                    case 5 -> spustDovednost();
                    case 6 -> vypisAbecedne();
                    case 7 -> vypisStatistiky();
                    case 8 -> vypisPocetVeSkupinach();
                    case 9 -> ulozZamestnanceDoSouboru();
                    case 10 -> nactiZamestnanceZeSouboru();
                    case 0 -> running = false;
                    default -> System.out.println("Neplatna volba.");
                }
            } catch (Exception e) {
                System.out.println("Chyba: " + e.getMessage());
            }
        }

        try {
            sql.ulozVse(databaze);
            System.out.println("Data ulozena do SQL zalohy.");
        } catch (SQLException e) {
            System.out.println("SQL ulozeni selhalo: " + e.getMessage());
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1) Pridat zamestnance");
        System.out.println("2) Pridat spolupraci");
        System.out.println("3) Odebrat zamestnance");
        System.out.println("4) Vyhledat zamestnance dle ID");
        System.out.println("5) Spustit dovednost zamestnance");
        System.out.println("6) Abecedni vypis ve skupinach");
        System.out.println("7) Statistiky");
        System.out.println("8) Pocet zamestnancu ve skupinach");
        System.out.println("9) Ulozit zamestnance do souboru");
        System.out.println("10) Nacist zamestnance ze souboru");
        System.out.println("0) Konec");
    }

    private void pridejZamestnance() {
        SkupinyZamestnancu skupina = readSkupina();
        String jmeno = readText("Jmeno: ");
        String prijmeni = readText("Prijmeni: ");
        int rokNarozeni = readInt("Rok narozeni: ");
        Zamestnanec z = databaze.pridejZamestnance(skupina, jmeno, prijmeni, rokNarozeni);
        System.out.println("Pridan zamestnanec: " + z.getZakladniInfo());
    }

    private void pridejSpolupraci() {
        int id = readInt("ID zamestnance: ");
        int kolegaId = readInt("ID kolegy: ");
        System.out.println("1) spatna, 2) prumerna, 3) dobra");
        UrovenSpoluprace uroven = UrovenSpoluprace.fromMenu(readInt("Uroven spoluprace: "));
        databaze.pridejSpolupraci(id, kolegaId, uroven);
        System.out.println("Spoluprace pridana.");
    }

    private void odeberZamestnance() {
        int id = readInt("ID zamestnance k odebrani: ");
        Zamestnanec z = databaze.odeberZamestnance(id);
        System.out.println("Odebran: " + z.getZakladniInfo());
    }

    private void najdiZamestnance() {
        Zamestnanec z = databaze.getOrThrow(readInt("ID zamestnance: "));
        System.out.println(z.getZakladniInfo());
        System.out.println(z.getStatistikySpoluprace());
        z.getSpolupracovnici().forEach((kolegaId, uroven) ->
                System.out.println("  kolega ID " + kolegaId + " | " + uroven.getPopis()));
    }

    private void spustDovednost() {
        int id = readInt("ID zamestnance: ");
        System.out.println(databaze.vykonejDovednost(id));
    }

    private void vypisAbecedne() {
        for (SkupinyZamestnancu skupina : SkupinyZamestnancu.values()) {
            System.out.println();
            System.out.println(skupina.getPopis());
            databaze.abecedniVypisPodleSkupiny(skupina)
                    .forEach(z -> System.out.println("  " + z.getZakladniInfo()));
        }
    }

    private void vypisStatistiky() {
        System.out.println("Prevazujici kvalita spoluprace: " + databaze.prevazujiciKvalitaSpoluprace());
        System.out.println("Zamestnanec s nejvice vazbami: " + databaze.zamestnanecSNajviceVazbami()
                .map(z -> z.getZakladniInfo() + " | " + z.getStatistikySpoluprace())
                .orElse("zadny zamestnanec"));
    }

    private void vypisPocetVeSkupinach() {
        Map<SkupinyZamestnancu, Long> counts = databaze.pocetVeSkupinach();
        for (SkupinyZamestnancu skupina : SkupinyZamestnancu.values()) {
            System.out.println(skupina.getPopis() + ": " + counts.getOrDefault(skupina, 0L));
        }
    }

    private void ulozZamestnanceDoSouboru() throws Exception {
        Zamestnanec z = databaze.getOrThrow(readInt("ID zamestnance: "));
        ukladani.uloz(z, Path.of(readText("Cesta ke souboru: ")));
        System.out.println("Zamestnanec ulozen do souboru.");
    }

    private void nactiZamestnanceZeSouboru() throws Exception {
        Zamestnanec z = ukladani.nacti(Path.of(readText("Cesta k souboru: ")));
        databaze.pridejNactenehoZamestnance(z);
        System.out.println("Nacteno: " + z.getZakladniInfo());
    }

    private SkupinyZamestnancu readSkupina() {
        System.out.println("1) Datovy analytik");
        System.out.println("2) Bezpecnostni specialista");
        return SkupinyZamestnancu.fromMenu(readInt("Skupina: "));
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private String readText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
