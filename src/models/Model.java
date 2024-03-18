package models;

import models.datastructures.DataScores;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A model where the entire logic of the game must take place
 */
public class Model {
    /*
    hangman_words_ee.db - Estonian words, the leaderboard table is empty
    hangman_words_en.db - English words, the leaderboard table is empty
    hangman_words_ee_test.db - Estonian words, the leaderboard table is NOT empty
     */
    private final String databaseFile = "hangman_words_ee.db"; // "hangman_words_ee_test.db"; // Default database
    private final String imagesFolder = "images"; // Hangman game images location
    private List<String> imageFiles = new ArrayList<>(); // All images with full folder path
    private String[] cmbNames; // Array muutuja ComboBox categories names (contents)
    private final String chooseCategory = "All categories"; // Default first ComboBox choice
    private DefaultTableModel dtmScores; // Leaderboard DefaultTableModel
    private List<DataScores> dataScores = new ArrayList<>(); // The contents of the entire database table scores
    private int imageId = 0; // Current image id (0..11)
    private String selectedCategory = chooseCategory; // Default all categories as "All categories"
    private String[] randomWordArr; // Juhuslik sõna teisendatud array
    private String[] userWord; // Sõna mida kasutaja arvab, hoiab meeles arvatud ja arvamata tähed [m_ja]
    private ArrayList<String> quessedCorrectChars = new ArrayList<String>(); // Arvatud õiged tähed
    private ArrayList<String> quessedWrongChars = new ArrayList<String>(); // Arvatud tähed aga valed
    private boolean quessPassed = false; // kas valitud täht oli õige või vale
    private boolean wordQuessed = false; // kas sõna on ära arvatud
    private String playerName = "Tühi"; // Küsitud mängija nimi scores jaoks
    private String randWord = "Puudub"; // Juhuslik sõna DB-st, kasutab ainult scores jaoks- vajalik Stringina
    private String gameTime = "2023-08-15 00:00:00"; // Ajatempel scores jaoks
    private String missedLetters = "p,u,u,d,u,b"; // eksitud tähed scores jaoks Stringina
    private int timeSeconds = 46 ; // mängu kestvus scores jaoks




    // LocalDateTime gameTime, String playerName, String word, String missedLetters, int timeSeconds)


    /**
     * During the creation of the model, the names of the categories to be shown in the combobox are known
     */
    public Model() {
        new Database(this);
    }

    /**
     * Sets the content to match the ComboBox. Adds "All categories" and unique categories obtained from the database.
     * @param unique all unique categories from database
     * meetod kutsutakse välja Database classist, sisendiks DB päringust saadud unikaalsed kategooriad.
     * meetod kirjutab unikaalsed kategooriate nimetused array
     */
    public void setCorrectCmbNames(List<String> unique) {
        cmbNames = new String[unique.size()+1]; // cmbNames on array, new loob array String type ja kindla suurusega
        cmbNames[0] = chooseCategory; // array esimene rida on "All categories"
        int x = 1;
        for(String category : unique) {
            cmbNames[x] = category; // kirjutab kategooria nimetused massiivi
            x++;
        }
    }

    /**
     * All ComboBox contents
     * @return ComboBox contents
     */
    public String[] getCmbNames() {
        return cmbNames;
    }

    /**
     * Sets a new DefaultTableModel
     * @param dtmScores DefaultTableModel
     */
    public void setDtmScores(DefaultTableModel dtmScores) {
        this.dtmScores = dtmScores;
    }

    /**
     * ALl leaderbaord content
     * @return List<DataScores>
     */
    public List<DataScores> getDataScores() {
        return dataScores;
    }

    /**
     * Sets the new content of the leaderboard
     * @param dataScores List<DataScores>
     */
    public void setDataScores(List<DataScores> dataScores) {
        this.dataScores = dataScores;
    }

    /**
     * Returns the configured database file
     * @return databaseFile
     */
    public String getDatabaseFile() {
        return databaseFile;
    }

    /**
     * Returns the default table model (DefaultTableModel)
     * @return DefaultTableModel
     */
    public DefaultTableModel getDtmScores() {
        return dtmScores;
    }

    /**
     * Returns the images folder
     * @return String
     */
    public String getImagesFolder() {
        return imagesFolder;
    }

    /**
     * Sets up an array of new images
     * @param imageFiles List<String>
     */
    public void setImageFiles(List<String> imageFiles) {
        this.imageFiles = imageFiles;
    }

    /**
     * An array of images
     * @return List<String>
     */
    public List<String> getImageFiles() {
        return imageFiles;
    }

    /**
     * The id of the current image
     * @return id
     */
    public int getImageId() {
        return imageId;
    }

    /**
     * Sets the new image id
     * @param imageId id
     */
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    /**
     * Returns the selected category
     * @return selected category
     */
    public String getSelectedCategory() {
        return selectedCategory;
    }

    /**
     * Sets a new selected category
     * @param selectedCategory new category
     */
    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    /**
     * Tagastab koopia juhusliku sõna massiivist
     * @return
     */
    public String[] getRandomWordArr() {
        return randomWordArr.clone(); // loob arrayst uue obj, et algne jääks muutmata ()
        //https://stackoverflow.com/questions/55428172/how-to-prevent-changing-the-value-of-array-or-object
    }

    /**
     * Meetod salvestab mängija nime
     * @param playerName
     */
    public void setPlayerName(String playerName){
        this.playerName = playerName;
    }

    /**
     * Meetod tagastab mängija nime
     * @return
     */
    public String getPlayerName(){
        return this.playerName;
    }
    /**
     * Meetod salvestab model muutujasse juhusliku sõna mida kasutaja hakkab arvama
     * @param randomWord
     */
    public void setRandomWord(String randomWord) {
        String randW = randomWord.toUpperCase(); // Muudab trükitähtedeks
        this.randomWordArr = randW.split(""); // Salvestab väärtuse [] kujul, teisendab sõna String[] {"a","b","c"}
        String temp = randomWord.replaceAll(".","_"); // Asendab tähed _-ga, abi rida
        this.userWord = temp.split(""); // Salvestab väärtuse [] kujul
        this.randWord = randomWord; // Salvestab juhusliku sõna stringina scores jaoks
    }

    /**
     * Meetod tagastab true/false, kas sõna on arvatud või mitte
     * @return
     */
    public boolean isWordQuessed() {
        return wordQuessed;
    }

    /**
     * Meetod salvestab tunnuse, kas sõna on arvatud või mitte
     * @param state
     */
    public void setWordQuessed(boolean state) {
        this.wordQuessed = state;
    }

    /**
     * Meetod tagastab true/false, kas arvati õigesti või valesti
     * @return
     */
    public boolean isQuessPassed() {
        return quessPassed;
    }

    /**
     * Meetod salvestab tunnuse, kas arvati õigesti või valesti
     * @param state
     */
    public void setQuessPassed(boolean state) {
        this.quessPassed = state;
    }

    /**
     * Meetod analüüsib, kas sisestatud täht on õige või vale
     * @param userChar
     * @return Tagastab sõna milles õigesti arvatud tähed on näha
     */
    public String[] getWordToShow(String userChar) {
       // TAGAUKS KIIRELT ÕIGE SÕNAGA LÕPETAMISEKS
        if(userChar.equals("X")){
            this.wordQuessed = true;
            return this.userWord;
        }
        // KONTROLL KAS TÄHT ON SÕNAS
        if (isChar(userChar)){ // Kas täht on sõnas

            // KONTROLL KAS ON JUBA ARVATUD ÕIGE
            if (isInUserWord(userChar)){ // Kas täht on juba arvatud õige siis
                this.quessedCorrectChars.add(userChar);
                this.quessPassed = true; // Arvati valesti, kontroller true
                return this.userWord;
            } else { // KUI TÄHT ON ÕIGE SIIS
                // Muuda userWord, asenda _ tähega
                updateUserWord(userChar);
                return this.userWord;
            }
        } else { // KUI TÄHT ON VALE SIIS
            // Lisa vale täht quessedWrongChars
            this.quessedWrongChars.add(userChar); // salvestab arvatud vale tähe
            this.quessPassed = true; // Kontroller true
            return this.userWord;
        }
    }
    /**
     * Meetod kontrollib kas täht on sõnas.
     * @param userChar On kasutaja sisestatud täht
     * @return Tagastab bbblean kas täht oli või mitte
     */
    // https://stackoverflow.com/questions/8777257/equals-vs-arrays-equals-in-java
    // https://www.baeldung.com/java-array-contains-value
    private boolean isChar(String userChar) {
        boolean isCh = false;
        for (String s : this.randomWordArr) { // Loop s on üksikud tähed, võetakse radomW... massiivist
            if(userChar.equals(s)) { // Kui kasutaja sisestatud täht võrdub sõnas oleva tähega siis
                isCh = true; // Täht oli sõnas olemas
                return isCh;
            }
        }
        return isCh;
    }

    /**
     * Meetod kontrollib kas tähte on juba arvatud õige.
     * @param userChar
     * @return
     */
    private boolean isInUserWord(String userChar) {
        boolean isIn = false; // Kontrolleri väärtus
        for (String s : this.userWord){ // Loop
            if(s.equals(userChar)) { // Kui sõnas olev täht võrdub kasutaja tähega siis
                isIn = true; // Kontroller true
                return isIn;
            }
        }
        return isIn;
    }

    /**
     * Meetod kontrollib kas täht on sõnas, õige tähe korral asendab _ õige tähega.
     * Kontrollib kas sõna on lõpuni arvatud, kui jah siis muudab kontrolleri staatust true
     * @param userChar on kasutaja täht
     */
    private void updateUserWord(String userChar) {
        // Loop kuni on tähti sõnas ja sõna ei ole ära arvatud (võrdleb kahte massiivi)
        for (int x=0; x<this.randomWordArr.length && ! Arrays.equals(this.userWord, this.randomWordArr); x++) {
            String s = this.randomWordArr[x]; // Võtab sõnast tähe
            if (s.equals(userChar)) { // Võrdle kasutaja tähte sõnas oleva tähega, kui täht on sõnas siis
                this.userWord[x] = userChar; // Asenda _ tähega
                // Kontroll kas sõna on arvatud, kui array'd on võrdsed siis on arvatud
                if (Arrays.equals(this.userWord, this.randomWordArr)) {
                    this.wordQuessed = true; // Väärtusta kontroller
                }
            }
        }
    }

    /**
     * Meetod vormistab valesti arvatud tähed esitatavaks stringiks
     * @return
     */
    public String getQuessedWrongChars() {
        // https://www.geeksforgeeks.org/convert-an-arraylist-of-string-to-a-string-array-in-java/
        // Vormindab Arraylist String arrayks
        String[] temp = this.quessedWrongChars.toArray(new String[this.quessedWrongChars.size()]);
        String tempj = String.join(",",temp); // ühendab tähed koos komadega Stringiks
        // Vormindab esitatava Stringi
        String wrongChars = "Wrong " + (this.quessedWrongChars.size()+this.quessedCorrectChars.size()) + " letter(s): " + tempj;
        this.missedLetters = tempj; // Salvestab väärtuse scores tabeli jaoks
        return wrongChars;
    }

    /**
     * Meetod tagastab DB-st saadud juhusliku sõna
     * @return
     */
    public String getRandWord() {
        return randWord;
    }

    /**
     * Meetod tagastab ajatempli millal mängiti, scores jaoks
     * @return
     */
    public String getGameTime() {
        return gameTime;
    }

    /**
     * Meetod salvestab ajatempli millal mängiti, scores jaoks
     * @param gameTime
     */
    public void setGameTime(String gameTime) {
        this.gameTime = gameTime;
    }

    /**
     * Meetod tagastab valesti arvatud tähed
     * @return
     */
    public String getMissedLetters() {
        return missedLetters;
    }

    /**
     * Meetod tagastab aja kui kaua mängiti / arvati
     * @return
     */
    public int getTimeSeconds() {
        return timeSeconds;
    }

    /**
     * Meetod salvestab aja kui kaua mängiti / arvati
     * @param timeSeconds
     */
    public void setTimeSeconds(int timeSeconds) {
//        String sec = String.valueOf(timeSeconds);
        this.timeSeconds = timeSeconds;
    }

    /**
     * Meetod algväärtustab muutujad uue mängu jaoks
     */
    public void resetGame() {
        this.missedLetters = ""; // Kustuta valed tähed
        this.quessedWrongChars.clear(); // Kustuta valed tähed
        this.quessedCorrectChars.clear(); // Kustuta õiged valed tähed
    }


}
/*
https://www.javatpoint.com/string-array-in-java
public class StringArrayExample {
        public static void main(String[] args) {
            String[] strArray = { "Ani", "Sam", "Joe" };
            boolean x = false; //initializing x to false
            int in = 0; //declaration of index variable
            String s = "Sam";  // String to be searched
            // Iteration of the String Array
            for (int i = 0; i < strArray.length; i++) {
                if(s.equals(strArray[i])) {
                    in = i; x = true; break;
                }
            }
            if(x)
                System.out.println(s +" String is found at index "+in);
            else
                System.out.println(s +" String is not found in the array");
        }
}

https://www.tutorialspoint.com/fill-elements-in-a-java-char-array-in-a-specified-range
erinevad näited stringi ja char[]

public class Test {
    public static void main(String[] args) {

        String alg = ("sel");
        String[] algArr = alg.split("");

        String[] teineArr = {"s", "o", "l"};
        boolean c = Arrays.equals(algArr, teineArr);
        System.out.println(c);

        List<String> kolmas = Arrays.asList(teineArr);
        Collections.replaceAll(kolmas,?,"_");

    }
}
 */