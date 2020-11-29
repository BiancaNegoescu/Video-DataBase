package query;

import main.Movie;
import main.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class FavoriteVideos {

    private FavoriteVideos() {

    }

    /**
     * metoda calculeaza pentru fiecare film de cate ori apare in listele de favorite ale
     * tuturor userilor
     * parsez userii si lista lor de favorite, daca videoul este film si nu a fost inca adaugat
     * in map-ul meu de filme favorite, se adauga, daca se gaseste deja, se incrementeaza
     * numarul deoarece inseamna ca a mai fost gasit si in lista altui user de favorite
     * @param users primeste lista cu toti userii
     * @return un map ce retine numele tuturor filmelor si numarul de cate ori apare fiecare in
     * ca favorit
     */
    public static Map favoriteMovies(final ArrayList<Movie> movies, final ArrayList<User> users) {
        Map<String, Integer> favMovies = new HashMap<>();
        for (int i = 0; i < users.size(); i++) {
            if (!users.get(i).getFavoriteMovies().isEmpty()) {
                for (int j = 0; j < users.get(i).getFavoriteMovies().size(); j++) {
                    //daca este film
                    if (verifyMovie(movies, users.get(i).getFavoriteMovies().get(j)) == 1) {
                        //daca nu se gaseste deja in lista de favorite il adauga
                        if (!favMovies.containsKey(users.get(i).getFavoriteMovies().get(j))) {
                            favMovies.put(users.get(i).getFavoriteMovies().get(j), 1);
                        } else { // daca se gaseste deja in lista de favorite
                            favMovies.put(users.get(i).getFavoriteMovies().get(j),
                                    (favMovies.get(users.get(i).getFavoriteMovies().get(j)) + 1));
                        }
                    }
                }
            }
        }
        return favMovies;
    }

    /**
     * metoda verifica daca un video este film sau nu
     * se parcurge lista de filme, daca numele videoului se gaseste inseamna ca e film
     * @param movie primeste lista cu toate filmele
     * @param movieName primeste numele videoului pe care il verific daca e film sau nu
     * @return 0 sau 1, 1 daca videoul e film, 0 caz contrar, adica e serial
     */
    protected static int verifyMovie(final ArrayList<Movie> movie, final String movieName) {
        int ok = 0;
        for (int i = 0; i < movie.size(); i++) {
            if (movie.get(i).getTitle().equals(movieName)) {
                ok = 1; //videoul e film
                break;
            }
        }
        return ok;
    }

    /**
     * metoda calculeaza pentru fiecare serial de cate ori apare in listele de favorite ale
     * tuturor userilor
     * parsez userii si lista lor de favorite, daca videoul este serial si nu a fost inca
     * adaugat in map-ul meu de seriale favorite, se adauga, daca se gaseste deja, se incrementeaza
     * numarul deoarece inseamna ca a mai fost gasit si in lista altui user de favorite
     * @param users primeste toti userii
     * @return un map ce retine numele tuturor serialelor si numarul de cate ori apare fiecare in
     * ca favorit
     */
    public static Map favoriteShows(final ArrayList<Movie> movies, final ArrayList<User> users) {
        Map<String, Integer> favShows = new HashMap<>();
        for (int i = 0; i < users.size(); i++) {
            if (!users.get(i).getFavoriteMovies().isEmpty()) {
                for (int j = 0; j < users.get(i).getFavoriteMovies().size(); j++) {
                    //daca nu este film -> e serial
                    if (verifyMovie(movies, users.get(i).getFavoriteMovies().get(j)) == 0) {
                        //daca nu se gaseste deja in lista de favorite il adauga
                        if (!favShows.containsKey(users.get(i).getFavoriteMovies().get(j))) {
                            favShows.put(users.get(i).getFavoriteMovies().get(j), 1);
                        } else { // daca se gaseste deja in lista de favorite
                            favShows.put(users.get(i).getFavoriteMovies().get(j),
                                    (favShows.get(users.get(i).getFavoriteMovies().get(j)) + 1));
                        }
                    }
                }
            }
        }
        return favShows;
    }

}
