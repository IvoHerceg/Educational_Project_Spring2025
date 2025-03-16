import java.util.*;
import java.util.stream.Collectors;

public class StatisticsCalculator {
    private int totalMovies;
    private double avgRuntime;
    private String bestDirector;
    private double bestDirectorAvg;
    private String mostPresentActor;
    private int maxActorCount;
    private int productiveYear;
    private int maxYearCount;

    public StatisticsCalculator(List<Movie> movies) {
        calculateStatistics(movies);
    }

    private void calculateStatistics(List<Movie> movies) {
        this.totalMovies = movies.size();
        this.avgRuntime = movies.stream().mapToInt(Movie::getRuntime).average().orElse(0);

        // Raggruppa i film per regista e calcola la media dei rating per ciascuno
        Map<String, Double> avgRatings = movies.stream()
                .collect(Collectors.groupingBy(
                        Movie::getDirector,
                        Collectors.averagingDouble(Movie::getRating)
                ));

        // Trova il regista con la migliore media
        String bestDirectorTemp = "";
        double bestDirectorAvgTemp = 0;

        for (Map.Entry<String, Double> entry : avgRatings.entrySet()) {
            double currentAvg = entry.getValue();
            if (currentAvg > bestDirectorAvgTemp) {
                bestDirectorAvgTemp = currentAvg;
                bestDirectorTemp = entry.getKey();
            }
        }
        this.bestDirector = bestDirectorTemp;
        this.bestDirectorAvg = bestDirectorAvgTemp;



        Map<String, Long> actorCountMap = movies.stream()
                .flatMap(movie -> movie.getActors().stream())
                .filter(actor -> !actor.isEmpty())
                .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()));

        String mostActor = "";
        long maxCount = 0;
        for (Map.Entry<String, Long> entry : actorCountMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostActor = entry.getKey();
            }
        }
        this.mostPresentActor = mostActor;
        this.maxActorCount = (int) maxCount;


        // Anno più produttivo
        Map<Integer, Integer> yearCount = new HashMap<>();
        for (Movie movie : movies) {
            int year = movie.getYear();
            yearCount.put(year, yearCount.getOrDefault(year, 0) + 1);
        }
        int prodYear = 0;
        int maxYear = 0;
        for (Map.Entry<Integer, Integer> entry : yearCount.entrySet()) {
            if (entry.getValue() > maxYear) {
                maxYear = entry.getValue();
                prodYear = entry.getKey();
            }
        }
        this.productiveYear = prodYear;
        this.maxYearCount = maxYear;
    }


    public int getTotalMovies() {
        return totalMovies;
    }

    public double getAvgRuntime() {
        return avgRuntime;
    }

    public String getBestDirector() {
        return bestDirector;
    }

    public double getBestDirectorAvg() {
        return bestDirectorAvg;
    }

    public String getMostPresentActor() {
        return mostPresentActor;
    }

    public int getMaxActorCount() {
        return maxActorCount;
    }

    public int getProductiveYear() {
        return productiveYear;
    }

    public int getMaxYearCount() {
        return maxYearCount;
    }
}
