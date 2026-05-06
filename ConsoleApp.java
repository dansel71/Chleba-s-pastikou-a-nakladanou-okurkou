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
            System.out.println("SQL data načtena.");
        } catch (Exception e) {
            System.out.println("SQL není dostupné nebo se nedaří načíst: " + e.getMessage());
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
                    default -> System.out.println("Neplatná volba.");
                }
            } catch (Exception e) {
                System.out.println("Chyba: " + e.getMessage());
            }
        }

        try {
            sql.ulozVse(databaze);
            System.out.println("Data uložena do SQL zálohy.");
        } catch (SQLException e) {
            System.out.println("SQL uložení selhalo: " + e.getMessage());
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1) Přidat zaměstnance");
        System.out.println("2) Přidat spolupráci");
        System.out.println("3) Odebrat zaměstnance");
        System.out.println("4) Vyhledat zaměstnance dle ID");
        System.out.println("5) Spustit dovednost zaměstnance");
        System.out.println("6) Abecední výpis ve skupinách");
        System.out.println("7) Statistiky");
        System.out.println("8) Počet zaměstnanců ve skupinách");
        System.out.println("9) Uložit zaměstnance do souboru");
        System.out.println("10) Načíst zaměstnance ze souboru");
        System.out.println("0) Konec");
    }

    private void pridejZamestnance() {
        SkupinyZamestnancu skupina = readSkupina();
        String jmeno = readText("Jméno: ");
        String prijmeni = readText("Příjmení: ");
        int rokNarozeni = readInt("Rok narození: ");
        Zamestnanec z = databaze.pridejZamestnance(skupina, jmeno, prijmeni, rokNarozeni);
        System.out.println("Přidán zaměstnanec: " + z.getZakladniInfo());
    }

    private void pridejSpolupraci() {
        int id = readInt("ID zaměstnance: ");
        int kolegaId = readInt("ID kolegy: ");
        System.out.println("1) špatná, 2) průměrná, 3) dobrá");
        UrovenSpoluprace uroven = UrovenSpoluprace.fromMenu(readInt("Úroveň spolupráce: "));
        databaze.pridejSpolupraci(id, kolegaId, uroven);
        System.out.println("Spolupráce přidána.");
    }

    private void odeberZamestnance() {
        int id = readInt("ID zaměstnance k odebrání: ");
        Zamestnanec z = databaze.odeberZamestnance(id);
        System.out.println("Odebrán: " + z.getZakladniInfo());
    }

    private void najdiZamestnance() {
        Zamestnanec z = databaze.getOrThrow(readInt("ID zaměstnance: "));
        System.out.println(z.getZakladniInfo());
        System.out.println(z.getStatistikySpoluprace());
        z.getSpolupracovnici().forEach((kolegaId, uroven) ->
                System.out.println("  kolega ID " + kolegaId + " | " + uroven.getPopis()));
    }

    private void spustDovednost() {
        int id = readInt("ID zaměstnance: ");
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
        System.out.println("Převažující kvalita spolupráce: " + databaze.prevazujiciKvalitaSpoluprace());
        System.out.println("Zaměstnanec s nejvíce vazbami: " + databaze.zamestnanecSNajviceVazbami()
                .map(z -> z.getZakladniInfo() + " | " + z.getStatistikySpoluprace())
                .orElse("žádný zaměstnanec"));
    }

    private void vypisPocetVeSkupinach() {
        Map<SkupinyZamestnancu, Long> counts = databaze.pocetVeSkupinach();
        for (SkupinyZamestnancu skupina : SkupinyZamestnancu.values()) {
            System.out.println(skupina.getPopis() + ": " + counts.getOrDefault(skupina, 0L));
        }
    }

    private void ulozZamestnanceDoSouboru() throws Exception {
        Zamestnanec z = databaze.getOrThrow(readInt("ID zaměstnance: "));
        ukladani.uloz(z, Path.of(readText("Cesta ke souboru: ")));
        System.out.println("Zaměstnanec uložen do souboru.");
    }

    private void nactiZamestnanceZeSouboru() throws Exception {
        Zamestnanec z = ukladani.nacti(Path.of(readText("Cesta k souboru: ")));
        databaze.pridejNactenehoZamestnance(z);
        System.out.println("Načteno: " + z.getZakladniInfo());
    }

    private SkupinyZamestnancu readSkupina() {
        System.out.println("1) Datový analytik");
        System.out.println("2) Bezpečnostní specialista");
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
