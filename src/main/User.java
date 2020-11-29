package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class User {
    private final String username;
    private final String subscriptionType;
    private Map<String, Integer> history;
    private ArrayList<String> favoriteMovies;
    private Map<String, Double> movieRatings;
    //rating la sezonul unui serial, string e numele serial, integer nr sez
    private Map<String, Integer> seasonRatings;


    protected User(final String username, final String subscriptionType,
                final Map<String, Integer> history,
                final ArrayList<String> favoriteMovies) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.favoriteMovies = favoriteMovies;
        this.history = history;
        this.movieRatings = new HashMap<>();
        this.seasonRatings = new HashMap<>();
    }

    /**
     * metoda adauga la lista de favorite a userului un video
     * verifica daca exista videoul in istoric si daca nu este deja la facorite si il adauga
     * @param user primeste userul pentru care se adauga videoul la favorite
     * @param video primeste numele videoului
     * @return returneaza 1 daca s-a adaugat, 0 daca este deja in lista de favorite si -1
     * daca videoul nu este vizionat
     */
    public int addToFav(final User user, final String video) {
        if ((user.history.containsKey(video)) && (!user.favoriteMovies.contains(video))) {
            user.favoriteMovies.add(video);
            return 1;
        }
        if (user.favoriteMovies.contains(video)) {
            return 0; //este in favlist, afisez msj respevtic
        }
        return -1; //is not seen
    }

    /**
     * metoda marcheaza un video ca citit, verifica daca nu se gaseste in istoric, il adauga
     * in istoric, iar daca se gaseste deja, se incrementeaza numarul de vizionari pentru
     * videoul respectiv in istoric
     * @param user primeste userul pentru care se marcheaza ca vazut videoul
     * @param video primeste numele videoului ce trebuie marcat ca vazut
     */
    public void markSeen(final User user, final String video) {
        if (!(user.history.containsKey(video))) {   //daca nu a vizualizat filmul
            user.history.put(video, 1);
        } else {
            user.history.put(video, (user.history.get(video) + 1)); //daca e deja vizualizat
        }
    }

    /**
     * metoda adauga rating unui sezon dintr-un serial
     * daca sezonul nu este rate-uit, dar este in history dau daca este rate-uit un alt sezon din
     * serial, adaug in map-ul seasonRatings (din user) numele serialului si numarul sezonului pe
     * care l-am rate-uit pentru a tine evidenta sezoanelor rate-uite dintr-un serial
     * pe urma, adaug la map-ul seasonRatings (din serial) din numarul sezonului si ratingul
     * aferent, iar in final adaug ratingul si in lista de ratinguri a sezonului
     * @param user primeste userul care rate-uieste sezonul
     * @param nrSeason primeste numarul sezonului ce trebuie rate-uit
     * @param video primeste numele videoului pentru care se rate-uiteste un sezon
     * @param rating primeste ratingul ce trebuie dat sezonului
     * @param serials primeste lista de seriale
     * @return returneaza 1 daca am dat rating unui sezon care nu avea
     * 0, daca avea rating deja
     * -1 inseamna ca serialul nu a fost vazut
     */
    public int ratingSeason(final User user, final int nrSeason, final String video,
                            final double rating, final ArrayList<Serial> serials) {
        //daca nu e rateuit sezonul meu si este in history
        if ((!user.seasonRatings.containsKey(video) && user.history.containsKey(video))
                || ((user.seasonRatings.containsKey(video))
                && (user.seasonRatings.get(video) != nrSeason)
                && user.history.containsKey(video))) {
            user.seasonRatings.put(video, nrSeason);
            for (int i = 0; i < serials.size(); i++) {
                if (serials.get(i).getTitle().equals(video)) {
                    serials.get(i).getSeasonRatings().put(nrSeason, rating);
                    if (serials.get(i).getSeasons().size() >= nrSeason) {
                        serials.get(i).getSeasons().get(nrSeason - 1).getRatings().add(rating);
                    }
                }
            }
            return 1; // am dat rating la un sezon care nu avea rating
        } else if (user.seasonRatings.containsKey(video)
                && (user.seasonRatings.get(video) == nrSeason)) {
            return 0;
        } // sezonul are rating deja
        return -1; //asa sa iasa + inseamna ca nu a vazut serialul
    }

    /**
     * metoda rate-uiteste un film, daca acesta se afla in history si nu este rateuit deja
     * @param user primeste userul care rate-uiteste filmul
     * @param video primeste numele filmului
     * @param rating primeste ratingul ce trebuie dat
     * @param movies primeste lista cu toate filmele
     * @return intorace 1 daca a rate-uit filmul
     * 0 daca ii daduse deja rating
     * -1 daca userul nu a vizualizat filmul
     */
    public int ratingMovies(final User user, final String video, final double rating,
                            final ArrayList<Movie> movies) {
        if (user.history.containsKey(video) && (!(user.movieRatings.containsKey(video)))) {
            user.movieRatings.put(video, rating);
            for (int i = 0; i < movies.size(); i++) {
                if (movies.get(i).getTitle().equals(video)) {
                    movies.get(i).getRatings().add(rating);
                }
            }
            return 1;
        } else if (user.history.containsKey(video) && (user.movieRatings.containsKey(video))) {
            return 0;
        } //i a dat deja rating
        return -1; //daca nu l a vazut
    }

    public String getUsername() {
        return username;
    }


    public String getSubscriptionType() {
        return subscriptionType;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public void setHistory(final Map<String, Integer> history) {
        this.history = history;
    }

    public ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }

    public void setFavoriteMovies(final ArrayList<String> favoriteMovies) {
        this.favoriteMovies = favoriteMovies;
    }

    public Map<String, Double> getMovieRatings() {
        return movieRatings;
    }

    public void setMovieRatings(final Map<String, Double> movieRatings) {
        this.movieRatings = movieRatings;
    }

    public Map<String, Integer> getSeasonRatings() {
        return seasonRatings;
    }

    public void setSeasonRatings(final Map<String, Integer> seasonRatings) {
        this.seasonRatings = seasonRatings;
    }
}