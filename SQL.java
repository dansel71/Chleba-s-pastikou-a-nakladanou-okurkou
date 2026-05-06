import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL {

    private final String jdbcUrl;

    public SQL(String soubor) {
        this.jdbcUrl = "jdbc:sqlite:" + soubor;
    }

    public void ulozVse(Databaze databaze) throws SQLException {
        try (Connection c = DriverManager.getConnection(jdbcUrl)) {
            vytvorSchema(c);
            c.setAutoCommit(false);
            try (Statement st = c.createStatement()) {
                st.executeUpdate("DELETE FROM spolupracovnici");
                st.executeUpdate("DELETE FROM zamestnanci");
            }
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO zamestnanci(id, jmeno, prijmeni, rok_narozeni, skupina) VALUES(?,?,?,?,?)")) {
                for (Zamestnanec z : databaze.vsechny()) {
                    ps.setInt(1, z.getId());
                    ps.setString(2, z.getJmeno());
                    ps.setString(3, z.getPrijmeni());
                    ps.setInt(4, z.getRokNarozeni());
                    ps.setString(5, z.getSkupina().name());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO spolupracovnici(zamestnanec_id, kolega_id, uroven) VALUES(?,?,?)")) {
                for (Zamestnanec z : databaze.vsechny()) {
                    for (var entry : z.getSpolupracovnici().entrySet()) {
                        ps.setInt(1, z.getId());
                        ps.setInt(2, entry.getKey());
                        ps.setString(3, entry.getValue().name());
                        ps.addBatch();
                    }
                }
                ps.executeBatch();
            }
            c.commit();
        }
    }

    public void nactiVse(Databaze databaze) throws SQLException {
        try (Connection c = DriverManager.getConnection(jdbcUrl)) {
            vytvorSchema(c);
            databaze.clear();
            try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM zamestnanci ORDER BY id")) {
                while (rs.next()) {
                    databaze.pridejNactenehoZamestnance(TvorbaZamestnance.create(
                            rs.getInt("id"),
                            SkupinyZamestnancu.valueOf(rs.getString("skupina")),
                            rs.getString("jmeno"),
                            rs.getString("prijmeni"),
                            rs.getInt("rok_narozeni")));
                }
            }
            try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM spolupracovnici")) {
                while (rs.next()) {
                    Zamestnanec z = databaze.najdi(rs.getInt("zamestnanec_id"));
                    if (z != null && databaze.najdi(rs.getInt("kolega_id")) != null) {
                        z.pridejSpolupracovnika(rs.getInt("kolega_id"),
                                UrovenSpoluprace.valueOf(rs.getString("uroven")));
                    }
                }
            }
        }
    }

    private void vytvorSchema(Connection c) throws SQLException {
        try (Statement st = c.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS zamestnanci (id INTEGER PRIMARY KEY, jmeno TEXT NOT NULL, prijmeni TEXT NOT NULL, rok_narozeni INTEGER NOT NULL, skupina TEXT NOT NULL)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS spolupracovnici (zamestnanec_id INTEGER NOT NULL, kolega_id INTEGER NOT NULL, uroven TEXT NOT NULL, PRIMARY KEY(zamestnanec_id, kolega_id))");
        }
    }
}
