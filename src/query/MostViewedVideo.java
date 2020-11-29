package query;

import main.Movie;
import main.Serial;
import main.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MostViewedVideo {

    private MostViewedVideo() {

    }

    /**
     * metoda calculeaza pentru fiecare film numarul de vizualizari
     * pentru fiecare film, se cauta in istoricul fiecarui user daca l-a vizualizat, si daca da
     * il adauga in map cu numarul de vizualizari calculat
     * @param moviesFiltered primeste lista de filme filtrate
     * @param users primeste lista cu toti userii
     * @return un map cu numele filmelor si numarul de vizualizari aferent
     */
    public static Map calculateMovieViews(final List<Movie> moviesFiltered,
                                          final ArrayList<User> users) {
        Map<String, Integer> mostViewedMovies = new HashMap<>();
        for (int i = 0; i < moviesFiltered.size(); i++) {
            int movieViews = viewsNumber(users, moviesFiltered.get(i).getTitle());
            if (movieViews > 0) {
                mostViewedMovies.put(moviesFiltered.get(i).getTitle(), movieViews);
            }
        }
        return mostViewedMovies;
    }

    /**
     * metoda calculeaza numarul total de vizualizari al unui video, cautand in istoricul
     * fiecarui user videoul respectiv si adunand de cate ori il gaseste
     * @param users primeste lista cu toti userii
     * @param video primeste numele videoului pentru care calculez numarul de vizualizari
     * @return numarul total de vizualizari al videoului dat ca parametru
     */
    public static int viewsNumber(final ArrayList<User> users, final String video) {
        int sum = users.stream().filter(user1 -> user1.getHistory().get(video) != null)
                .mapToInt(user1 -> user1.getHistory().get(video)).sum();
        return sum;
    }

    /**
     * metoda calculeaza pentru fiecare serial numarul de vizualizari
     * pentru fiecare serial, se cauta in istoricul fiecarui user daca l-a vizualizat, si
     * daca da il adauga in map cu numarul de vizualizari calculat
     * @param serials primeste lista de seriale filtrate
     * @param users primeste lista cu toti userii
     * @return un map cu numele serialelor si numarul de vizualizari aferent
     */
    public static Map calculateShowViews(final ArrayList<Serial> serials,
                                         final ArrayList<User> users) {
        Map<String, Integer> mostViewdSerials = new HashMap<>();
        for (int i = 0; i < serials.size(); i++) {
            int serialViews = viewsNumber(users, serials.get(i).getTitle());
            if (serialViews > 0) {
                mostViewdSerials.put(serials.get(i).getTitle(), serialViews);
            }
        }
        return mostViewdSerials;
    }
}
