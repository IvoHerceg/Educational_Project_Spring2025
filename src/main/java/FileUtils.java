import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.util.*;

public class FileUtils {
    public static String readPreferencesFile(String filePath, String key) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("=", 2);
                if (parts.length == 2 && parts[0].trim().equals(key)) {
                    return parts[1].trim();
                }
            }
        } catch (IOException e) {
            System.err.println("Unable to find the preferences file: " + e.getMessage());
            System.exit(1);
        }
        System.err.println("The key: " + key + " does not exist in the preferences file.");
        System.exit(1);
        return null;
    }

    public static List<Movie> readMoviesFromCSV(String filePath) {
        List<Movie> movies = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] column_names = csvReader.readNext();
            if (column_names == null) {
                return movies;
            }

            Map<String, Integer> columnIndexMap = new HashMap<>();
            for (int i = 0; i < column_names.length; i++) {
                columnIndexMap.put(column_names[i], i);
            }

            String[] movie;
            while ((movie = csvReader.readNext()) != null) {
                String title = movie[columnIndexMap.get("Series_Title")];
                int year = Integer.parseInt(movie[columnIndexMap.get("Released_Year")]);
                int runtime = Integer.parseInt(movie[columnIndexMap.get("Runtime")].replace(" min", ""));
                double rating = Double.parseDouble(movie[columnIndexMap.get("IMDB_Rating")]);
                String director = movie[columnIndexMap.get("Director")];

                List<String> actors = new ArrayList<>();
                String[] actorColumns = {"Star1", "Star2", "Star3", "Star4"};
                for (String col : actorColumns) {
                    if (columnIndexMap.containsKey(col)) {
                        String actor = movie[columnIndexMap.get(col)];
                        if (actor != null && !actor.trim().isEmpty()) {
                            actors.add(actor.trim());
                        }
                    }
                }
                movies.add(new Movie(title, year, runtime, director, actors, rating));
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("Error, check the path of the CSV file: " + e.getMessage());
            System.exit(1);
        }
        if (movies.isEmpty()) {
            System.err.println("There is no film in the specified csv file.");
        }
        return movies;
    }

    public static void writeStatisticsToCSV(StatisticsCalculator statistics, String filePath) {
        File file = new File(filePath);
        boolean fileAlreadyExists = file.exists();

        try {
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println("Statistic,Value");
                writer.println("Total Movies," + statistics.getTotalMovies());
                writer.println("Average Runtime," + statistics.getAvgRuntime());
                writer.println("Best Director," + statistics.getBestDirector()
                        + " (Avg Rating: " + statistics.getBestDirectorAvg() + ")");
                writer.println("Most Present Actor/Actress," + statistics.getMostPresentActor()
                        + " (Count: " + statistics.getMaxActorCount() + ")");
                writer.println("Most Productive Year," + statistics.getProductiveYear()
                        + " (Count: " + statistics.getMaxYearCount() + ")");
            }

            if (fileAlreadyExists) {
                System.out.println("Statistics overwritten onto " + filePath);
            } else {
                System.out.println("Statistics written to " + filePath);
            }

        } catch (IOException e) {
            System.err.println("Errore nella scrittura del file di output: " + e.getMessage());
        }
    }
}
