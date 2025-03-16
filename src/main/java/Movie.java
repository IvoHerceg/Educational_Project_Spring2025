import java.util.List;

public class Movie {
    private String title;
    private int year;
    private int runtime;
    private String director;
    private List<String> actors;
    private double rating;

    public Movie(String title, int year, int runtime, String director, List<String> actors, double rating) {
        this.title = title;
        this.year = year;
        this.runtime = runtime;
        this.director = director;
        this.actors = actors;
        this.rating = rating;
    }

    public int getYear() {
        return year;
    }
    public int getRuntime() {
        return runtime;
    }
    public String getDirector() {
        return director;
    }
    public List<String> getActors() {
        return actors;
    }
    public double getRating() {
        return rating;
    }
}
