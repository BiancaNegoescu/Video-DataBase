package main;

import java.util.ArrayList;
import java.util.List;

public final class Sezon {
    private final int currentSeason;
    private final int duration;
    private List<Double> ratings;


    protected Sezon(final int currentSeason, final int duration) {
        this.currentSeason = currentSeason;
        this.duration = duration;
        this.ratings = new ArrayList<>();
    }

    protected void rateSeason(final double rating) {
        ratings.add(rating);

    }

    public int getCurrentSeason() {
        return currentSeason;
    }

    public int getDuration() {
        return duration;
    }

    public List<Double> getRatings() {
        return ratings;
    }

    public void setRatings(final List<Double> ratings) {
        this.ratings = ratings;
    }
}
