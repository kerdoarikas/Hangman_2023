package models;

import models.datastructures.DataScores;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for interacting with and querying a database.
 */
public class Database {
    private Connection connection = null;
    private final String databaseUrl; // DB nimi on modelis
    private final Model model;

    /**
     * A database constructor that is always invoked when an object is created.
     *
     * @param model Model
     */
    public Database(Model model) {
        this.model = model;
        this.databaseUrl = "jdbc:sqlite:" + model.getDatabaseFile(); // take DB name
        this.selectUniqueCategories(); // ComboBox needs categories from the table
    }

    /**
     * Database connection
     *
     * @return Connection
     * @throws SQLException throws error on console.
     */
    private Connection dbConnection() throws SQLException {
        // https://stackoverflow.com/questions/13891006/
        if (connection != null) { // kontrollib kas on varasem ühendus aktiivne
            this.connection.close(); // kui jah siis sulgeb ühenduse
        }
        connection = DriverManager.getConnection(databaseUrl); // loob ühenduse, DriverMan valib õige draiveri
        return connection;
    }

    /**
     * The method reads unique category names from the database and writes them to the cmbNames variable of the model.
     */
    public void selectUniqueCategories() {
        // sql päring muutujasse
        String sql = "SELECT DISTINCT(category) as category FROM words ORDER BY category"; // päring võtab unikaalsed read
        // list muutuja katekoorijatele
        List<String> categories = new ArrayList<>();
        try {
            Connection conn = this.dbConnection(); // kutsub meetodi, tagastub ühendus
            Statement stmt = conn.createStatement(); // loob Statement obj, saadab sql lause DB
            ResultSet rs = stmt.executeQuery(sql); // viib täide sql lause, tagastab tulemuse objektina- andmetabeli

            while (rs.next()) { // käib läbi tabeli
                String category = rs.getString("category"); // võtab rea
                categories.add(category); // kirjutab stringi massiivi
            }
            model.setCorrectCmbNames(categories); // writes unique categories to the cmbNames variable of the model
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                this.connection.close();
                this.connection = null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * This method reads the entire leaderboard content from the database and writes it to the model's dataScores
     * variable
     */
    public void selectScores() {
        String sql = "SELECT * FROM scores ORDER BY gametime, playertime DESC, playername";
        List<DataScores> data = new ArrayList<>();
        try {
            Connection conn = this.dbConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            model.getDataScores().clear();
            while (rs.next()) {
                String datetime = rs.getString("playertime");
                LocalDateTime playerTime = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String playerName = rs.getString("playername");
                String guessWord = rs.getString("guessword");
                String wrongChar = rs.getString("wrongcharacters");
                int timeSeconds = rs.getInt("gametime");
                data.add(new DataScores(playerTime, playerName, guessWord, wrongChar, timeSeconds));
            }
            model.setDataScores(data); // Write dataScore in the model variable

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                this.connection.close();
                this.connection = null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Meetod loeb DB-st juhusliku sõna, mida kasutaja hakkab arvama, vastavalt kasutaja valitud kategooriale
     * sisendiks kasutaja valitud kategooria.
     * väljund on String mis kirjutatakse modeli muutujasse
     */
    public void selectRandomWord(String choosedCatecory) {
        // Päring: üks juhuslik sõna vastavalt kategooriale
        String sql = "SELECT word FROM words WHERE category LIKE ? ORDER BY random() limit 1";
        try {
            Connection conn = this.dbConnection(); // Loob DB ühenduse
            PreparedStatement getWord = conn.prepareStatement(sql); // Prepeared päring, päring sisaldab muutuvat osa ?
            // Päringu muutuv osa, kategooria saab väärtuse
            // KONTROLL KAS KATEGOORIA ON VALITUD https://intellipaat.com/community/38986/using-like-wildcard-in-prepared-statement
                                                // https://alvinalexander.com/blog/post/jdbc/jdbc-preparedstatement-select-like/
            if(choosedCatecory == "All categories"){ // Kui ei ole siis
                getWord.setString(1,"%"); // valib juhusliku sõna kõigi kategooriate hulgast
            } else { // kui on valitud
                getWord.setString(1,"%" + choosedCatecory + "%"); // valib kindla kategooria hulgast
            }
            ResultSet rs = getWord.executeQuery(); // ResultSet on nö tabel mis sisaldab päringu tulemust
            String rWord = rs.getString(1); // Päringu tulemuseks on 1 sõna, split vormindab []-ks
            model.setRandomWord(rWord); // Kirjutab saadud juhusliku sõna modeli muutujasse

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                this.connection.close();
                this.connection = null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Meetod kirjutab mängu tulemuse edetabelisse
     */
    // LocalDateTime gameTime, String playerName, String word, String missedLetters, int timeSeconds)
    public void insertScores() {
        String sql = "INSERT INTO scores (playertime, playername, guessword, wrongcharacters, gametime) VALUES (?,?,?,?,?)";
        try {
            Connection conn = this.dbConnection(); // Loob DB ühenduse
            PreparedStatement stmt = conn.prepareStatement(sql); // Prepeared päring, päring sisaldab muutuvat osa ?
            stmt.setString(1, model.getGameTime()); // Päringu muutuv osa saab väärtuseks valitud kategooria
            stmt.setString(2, model.getPlayerName());
            stmt.setString(3, model.getRandWord());
            stmt.setString(4, model.getMissedLetters());
            stmt.setInt(5, model.getTimeSeconds());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                this.connection.close();
                this.connection = null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
