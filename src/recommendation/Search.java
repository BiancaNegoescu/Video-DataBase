package recommendation;

import main.Movie;
import main.Serial;
import main.User;
import main.Video;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class Search {

    private Search() {

    }
    /**
     * este aceeasi metoda ca si in clasa querry, dar am scris-o si aici ca sa o pot apela
     * din clasa Recommendation pentru cerintele din categoria de recomandari
     * metoda calculeaza media rantingurilor date pentru fiecare film
     * daca filmul a primit ratinguri se face media acestora, impartind la numarul total de
     * ratinguri care a fost dat filmului respectiv
     * @param users primeste lista cu toti userr
     * @return un map cu numele filmelor si un double reprezentand media fiecaruia
     */
    public static Map calculateMovieRatings(final List<Movie> movies, final ArrayList<User> users) {
        Map<String, Double> mostRatedMovies = new HashMap<>();
        for (int i = 0; i < movies.size(); i++) {
            double movieRatingsSum = ratingsSum(users, movies.get(i).getTitle());
            long count = ratingsNumber(users, movies.get(i).getTitle());
            if (movieRatingsSum > 0) {
                mostRatedMovies.put(movies.get(i).getTitle(), movieRatingsSum / count);
            }
        }
        return mostRatedMovies;
    }

    /**
     * metoda calculeaza suma tuturor rantingurilor primite de un anumit video
     * se cauta in mapul de ratinguri al fiecarui user videoului si aduna la suma ratingul aferent
     * @param users primeste lista cu toti userii
     * @param video primeste numele videoului pentru care se calculeaza suma ratingurilor
     * @return un double reprezentand suma tuturor rantingurilor pentru videoul primit
     */
    protected static double ratingsSum(final ArrayList<User> users, final String video) {
        double sum = users.stream().filter(user1 -> user1.getMovieRatings().get(video) != null)
                .mapToDouble(user1 -> user1.getMovieRatings().get(video)).sum();
        return sum;
    }

    /**
     * metoda numara cate ratinguri au fost date pentru un video
     * se cauta in mapul de ratinguri al fiecarui user videoul si daca se gaseste se numara,
     * deoarece inseamna ca i s-a dat rating
     * @param users primeste lista cu toti userii
     * @param video primeste numele videoului pentru care calculez numarul total de ratinguri
     * @return un long, reprezentand numarul total de ratinguri pentru videoul primit
     */
    protected static long ratingsNumber(final ArrayList<User> users, final String video) {
        long number = users.stream()
                .filter(user1 -> user1.getMovieRatings().get(video) != null).count();
        return number;
    }

    /**
     * metoda intoarce toate videoclipurile nevazute de user dintr-un anumit gen, dat ca filtru in
     * input
     * se calculeaza ratingurile pentru toate filmele si serialele navazute si se adauga toate fie
     * intr-un map (daca au ratinguri), fie intr-un list (daca nu au)
     * daca mapul cu toate filmele si serialele rate-uite nu este gol, adica daca am filme si
     * seriale cu rating, se sorteaza mapul, dupa rating mai intai si apoi dupa titlul videoului
     * daca mapul e gol, atunci se sorteaza lista ce contine numele tuturor videoruilor, in ordine
     * ascendenta
     * @param movies primeste lista cu toate filmele
     * @param serials primeste lista cu toate serialele
     * @param user primeste userul pentru care se cauta videoclipurile navazute
     * @param users primeste lista cu toti userii
     * @param gen primeste genul dupa care se cauta videourile
     * @return o lista de stringuri cu numele videourilor vazute, in ordine ascendeta
     */
    public static List unseenVideos(final ArrayList<Movie> movies, final ArrayList<Serial> serials,
                                    final User user, final ArrayList<User> users,
                                    final String gen) {
        List<Movie> unseenMovies = new ArrayList<>();
        List<Serial> unseenSerials = new ArrayList<>();
        Map<String, Double> unseenMoviesRated = new HashMap<>();
        Map<String, Double> unseenSerialsRated = new HashMap<>();
        Map<String, Double> unseenVideosRated = new HashMap<>();
        List<String> unseenVideos = new ArrayList<>();
        for (int i = 0; i < movies.size(); i++) {
            if ((!user.getHistory().containsKey(movies.get(i).getTitle()))
                    && movies.get(i).getGenres().contains(gen)) {
                unseenMovies.add(movies.get(i));
            }
        }
        for (int i = 0; i < serials.size(); i++) {
            if ((!user.getHistory().containsKey(serials.get(i).getTitle()))
                    && serials.get(i).getGenres().contains(gen)) {
                unseenSerials.add(serials.get(i));
            }
        }

        if (!unseenMovies.isEmpty()) {
            unseenMoviesRated = calculateMovieRatings(unseenMovies, users);
            if (!unseenMoviesRated.isEmpty()) {
                unseenVideosRated.putAll(unseenMoviesRated);
            } else {
                List<String> unseenmovie1 = unseenSerials.stream()
                        .map(Video::getTitle).collect(Collectors.toList());
                unseenVideos.addAll(unseenmovie1);
            }
        }
        if (!unseenSerials.isEmpty()) {
            unseenSerialsRated = BestUnseen.calculateShowRatings(unseenSerials, users);
            if (!unseenSerialsRated.isEmpty()) {
                unseenVideosRated.putAll(unseenSerialsRated);
            } else {
                List<String> serial1 = unseenSerials.stream()
                        .map(Video::getTitle).collect(Collectors.toList());
                unseenVideos.addAll(serial1);
            }
        }
        if (!unseenVideosRated.isEmpty()) {
            unseenVideos = sortUnseenVideos(unseenVideosRated);
        } else {
            unseenVideos.sort(Comparator.naturalOrder());
        }
        return unseenVideos;
    }

    /**
     * metoda sorteaza videourile dupa rating si apoi dupa titlu
     * @param unseenVideos primeste un map cu titlul videoului si ratingul lui
     * @return o lista cu numele videourilor sortate dupa rating si dupa titlu
     */
    protected static List sortUnseenVideos(final Map<String, Double> unseenVideos) {
        List<String> unseenVideosSorted = unseenVideos.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue()
                        .thenComparing(Map.Entry.comparingByKey()))
                .map(Map.Entry::getKey).collect(Collectors.toList());
        return unseenVideosSorted;
    }

}
