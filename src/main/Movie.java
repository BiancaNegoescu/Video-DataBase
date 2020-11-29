package main;

import java.util.ArrayList;
import java.util.List;

public final class Movie extends Video {

    private final int duration;
    private final List<Double> ratings;

    protected Movie(final String title, final ArrayList<String> cast,
                 final ArrayList<String> genres, final int year,
                 final int duration) {
        super(title, year, cast, genres);
        this.duration = duration;
        this.ratings = new ArrayList<>();
    }

    public int getDuration() {
        return duration;
    }

    public List<Double> getRatings() {
        return ratings;
    }
}
