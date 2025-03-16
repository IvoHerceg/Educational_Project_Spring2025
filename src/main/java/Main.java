import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // Usa direttamente il percorso fornito dall'utente
        String preferencesFilePath = args[0];

        // Legge i percorsi di input e output dal file di preferenze
        String inputFilePath = FileUtils.readPreferencesFile(preferencesFilePath, "inputFile");
        String outputFilePath = FileUtils.readPreferencesFile(preferencesFilePath, "outputFile");

        // Lettura dei film dal CSV
        List<Movie> movies = FileUtils.readMoviesFromCSV(inputFilePath);

        // Calcola statistiche
        StatisticsCalculator statsCalc = new StatisticsCalculator(movies);

        // Scrittura risultati
        FileUtils.writeStatisticsToCSV(statsCalc, outputFilePath);
    }
}
