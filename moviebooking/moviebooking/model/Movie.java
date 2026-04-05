package moviebooking.model;

/**
 * Immutable value object representing a movie.
 */
public final class Movie {

    private final String movieId;
    private final String name;
    private final int durationMinutes;

    public Movie(String movieId, String name, int durationMinutes) {
        if (movieId == null || movieId.isBlank()) throw new IllegalArgumentException("movieId required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name required");
        if (durationMinutes <= 0) throw new IllegalArgumentException("duration must be positive");
        this.movieId = movieId;
        this.name = name;
        this.durationMinutes = durationMinutes;
    }

    public String getMovieId() { return movieId; }
    public String getName() { return name; }
    public int getDurationMinutes() { return durationMinutes; }

    @Override
    public String toString() {
        return "Movie{" + name + ", " + durationMinutes + "min}";
    }
}
