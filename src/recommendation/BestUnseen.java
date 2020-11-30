package recommendation;

import main.Movie;
import main.Serial;
import main.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class BestUnseen {

    private BestUnseen() {

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
     * este aceeasi metoda ca si in clasa querry, dar am scris-o si aici ca sa o pot apela
     * din clasa Recommendation pentru cerintele din categoria de recomandari
     * metoda calculeaza media rantingurilor date pentru fiecare serial
     * daca sezoanele serialul au primit ratinguri, se face mai intai media sezoanelor, care se
     * aduna la media serialului
     * in final media serialului se imparte la numarul de sezoane si se obtine media acestuia
     * @param serials primeste lista cu toate serialele
     * @param users primeste lista cu toti userii
     * @return un map cu numele serialelor si un double reprezentand media fiecaruia
     */
    public static Map calculateShowRatings(final List<Serial> serials,
                                           final ArrayList<User> users) {
        Map<String, Double> mostRatedShows = new HashMap<>();
        for (int i = 0; i < serials.size(); i++) {
            double mediaSerial = 0;
            double sumaRatingSezoane = 0;
            for (int j = 0; j < serials.get(i).getSeasons().size(); j++) {
                if (!serials.get(i).getSeasons().get(j).getRatings().isEmpty()) {
                    double sumaRatingsSezon =
                            seasonRating(serials.get(i).getSeasons().get(j).getRatings());
                    long count = seasonRatingsNumber(serials.get(i).getSeasons()
                            .get(j).getRatings());
                    sumaRatingSezoane += sumaRatingsSezon / count;
                }
            }
            if (sumaRatingSezoane > 0) {
                mediaSerial = sumaRatingSezoane / serials.get(i).getNumberOfSeasons();
                mostRatedShows.put(serials.get(i).getTitle(), mediaSerial);
            }
        }
        return mostRatedShows;
    }

    /**
     * metoda calculeaza suma ratingurilor pentru un sezon
     * @param ratings primeste lista cu toate ratingurile date unui sezon
     * @return un double, reprezentand suma ratingurilor pentru un sezon
     */
    protected static Double seasonRating(final List<Double> ratings) {
        double sum = ratings.stream().filter(Objects::nonNull)
                .mapToDouble(rating1 -> rating1).sum();
        return sum;
    }

    /**
     * metoda calculeaza numarul de ratinguri date unui sezon
     * @param ratings primeste lista cu toate ratingurile date unui sezon
     * @return un long reprezentand numarul de ratinguri date pentru un sezon
     */
    protected static long seasonRatingsNumber(final List<Double> ratings) {
        long number = ratings.stream().filter(Objects::nonNull).count();
        return number;
    }

    /**
     * metoda intoarce cel mai bun video nevazut de catre un user, parcurgand lista de videouri
     * rate-uite si sortate si il intoarce pe primul care nu este in istoricul userului
     * daca le-a vazut pe toate, intoarce primul video nevazut din baza de date
     * @param movies primeste lista de filme
     * @param ratedVideos primeste lista cu numele filmelor rateuite si sortate
     * @param user userul pentru care se cauta best unseen video
     * @return un string reprezentand best unseen video
     */
    public static String bestUnseen(final ArrayList<Movie> movies, final List<String> ratedVideos,
                                    final User user) {
        String bestUnseen = null;
        for (int i = 0; i < ratedVideos.size(); i++) {
            if (!user.getHistory().containsKey(ratedVideos.get(i))) {
                bestUnseen = ratedVideos.get(i);
                break;
            }
        }
        if (bestUnseen == null) { //inseamna ca userul are deja in history ratedVideos
            for (int i = 0; i < movies.size(); i++) {
                if (!user.getHistory().containsKey(movies.get(i).getTitle())) {
                    bestUnseen = movies.get(i).getTitle();
                    break;
                }
            }
        }
        return bestUnseen;
    }
}
