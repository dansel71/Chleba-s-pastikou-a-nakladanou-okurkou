import java.util.*;
import java.util.stream.Collectors;

public class Databaze {

    private final Map<Integer, Zamestnanec> zamestnanci = new LinkedHashMap<>();
    private int dalsiId = 1;

    public Zamestnanec pridejZamestnance(SkupinyZamestnancu skupina, String jmeno, String prijmeni, int rokNarozeni) {
        Zamestnanec z = TvorbaZamestnance.create(dalsiId++, skupina, jmeno, prijmeni, rokNarozeni);
        zamestnanci.put(z.getId(), z);
        return z;
    }

    public void pridejNactenehoZamestnance(Zamestnanec z) {
        zamestnanci.put(z.getId(), z);
        dalsiId = Math.max(dalsiId, z.getId() + 1);
    }

    public void pridejSpolupraci(int zamestnanecId, int kolegaId, UrovenSpoluprace uroven) {
        Zamestnanec z = getOrThrow(zamestnanecId);
        getOrThrow(kolegaId);
        z.pridejSpolupracovnika(kolegaId, uroven);
    }

    public Zamestnanec odeberZamestnance(int id) {
        Zamestnanec removed = zamestnanci.remove(id);
        if (removed == null) {
            throw new NoSuchElementException("Zaměstnanec s ID " + id + " neexistuje.");
        }
        for (Zamestnanec z : zamestnanci.values()) {
            z.odeberSpolupracovnika(id);
        }
        return removed;
    }

    public Zamestnanec najdi(int id) {
        return zamestnanci.get(id);
    }

    public Zamestnanec getOrThrow(int id) {
        Zamestnanec z = zamestnanci.get(id);
        if (z == null) {
            throw new NoSuchElementException("Zaměstnanec s ID " + id + " neexistuje.");
        }
        return z;
    }

    public Collection<Zamestnanec> vsechny() {
        return Collections.unmodifiableCollection(zamestnanci.values());
    }

    public String vykonejDovednost(int id) {
        return getOrThrow(id).spustDovednost(zamestnanci);
    }

    public List<Zamestnanec> abecedniVypisPodleSkupiny(SkupinyZamestnancu skupina) {
        return zamestnanci.values().stream()
                .filter(z -> z.getSkupina() == skupina)
                .sorted(Comparator.comparing(Zamestnanec::getPrijmeni, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(Zamestnanec::getJmeno, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public String prevazujiciKvalitaSpoluprace() {
        Map<UrovenSpoluprace, Long> counts = zamestnanci.values().stream()
                .flatMap(z -> z.getSpolupracovnici().values().stream())
                .collect(Collectors.groupingBy(x -> x, () -> new EnumMap<>(UrovenSpoluprace.class), Collectors.counting()));
        return counts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> e.getKey().getPopis() + " (" + e.getValue() + ")")
                .orElse("žádná spolupráce");
    }

    public Optional<Zamestnanec> zamestnanecSNajviceVazbami() {
        return zamestnanci.values().stream().max(Comparator.comparingInt(z -> z.getSpolupracovnici().size()));
    }

    public Map<SkupinyZamestnancu, Long> pocetVeSkupinach() {
        return zamestnanci.values().stream()
                .collect(Collectors.groupingBy(Zamestnanec::getSkupina,
                        () -> new EnumMap<>(SkupinyZamestnancu.class), Collectors.counting()));
    }

    public void clear() {
        zamestnanci.clear();
        dalsiId = 1;
    }
}
