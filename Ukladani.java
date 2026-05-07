import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Ukladani {

    public void uloz(Zamestnanec zamestnanec, Path cesta) throws IOException {
        String spoluprace = zamestnanec.getSpolupracovnici().entrySet().stream()
                .map(e -> e.getKey() + ":" + e.getValue().name())
                .reduce((a, b) -> a + "," + b)
                .orElse("");
        String radek = String.join(";",
                String.valueOf(zamestnanec.getId()),
                zamestnanec.getSkupina().name(),
                zamestnanec.getJmeno(),
                zamestnanec.getPrijmeni(),
                String.valueOf(zamestnanec.getRokNarozeni()),
                spoluprace);
        Files.writeString(cesta, radek);
    }

    public Zamestnanec nacti(Path cesta) throws IOException {
        String obsah = Files.readString(cesta).trim();
        String[] casti = obsah.split(";", -1);
        if (casti.length < 5) {
            throw new IOException("Soubor nema spravny format.");
        }
        int id = Integer.parseInt(casti[0]);
        SkupinyZamestnancu skupina = SkupinyZamestnancu.valueOf(casti[1]);
        Zamestnanec z = TvorbaZamestnance.create(id, skupina, casti[2], casti[3], Integer.parseInt(casti[4]));
        if (casti.length >= 6 && !casti[5].isBlank()) {
            Arrays.stream(casti[5].split(","))
                    .map(item -> item.split(":"))
                    .forEach(pair -> z.pridejSpolupracovnika(Integer.parseInt(pair[0]), UrovenSpoluprace.valueOf(pair[1])));
        }
        return z;
    }
}
