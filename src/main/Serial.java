package main;

import entertainment.Season;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class Serial extends Video {
    private final int numberOfSeasons;
    private final ArrayList<Season> seasons;
    private final Map<Integer, Double> seasonRatings; //ratingurile la un sezon
    private int totalDuration;

    protected Serial(final String title, final ArrayList<String> cast,
                  final ArrayList<String> genres,
                  final int numberOfSeasons, final ArrayList<Season> seasons,
                  final int year) {
        super(title, year, cast, genres);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
        this.seasonRatings = new HashMap<>();
        this.totalDuration = 0;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public void setTotalDuration(final int totalDuration) {
        this.totalDuration = totalDuration;
    }

    public Map<Integer, Double> getSeasonRatings() {
        return seasonRatings;
    }

    public int getTotalDuration() {
        return totalDuration;
    }
}
