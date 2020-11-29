package query;

import entertainment.Season;
import main.Actor;
import main.Movie;
import main.Serial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AverageActors {

    private AverageActors() {

    }

    /**
     * metoda calculeaza media tuturor filmelor in care joaca un actor, pentru fiecare actor se
     * parcurg toate filmele si se verifica daca acesta apare in ele, daca apare se calculeaza
     * media ratingurilor filmului respectiv
     * @return un map cu numele actorului si media tuturor filmelor in care joaca
     */
    public static Map averageActorMovie(final ArrayList<Actor> actors,
                                        final ArrayList<Movie> movies) {
        Map<String, Double> actorsMoviesRating = new HashMap<>();
        for (int i = 0; i < actors.size(); i++) {
            double mediaFilme = 0;
            int count = 0;
            for (int j = 0; j < movies.size(); j++) {
                double mediaFilm = 0;
                if (movies.get(j).getCast().contains(actors.get(i).getName())) {
                    if (!movies.get(j).getRatings().isEmpty()) {
                        double sumaFilm = 0;
                        count++;
                        for (int k = 0; k < movies.get(j).getRatings().size(); k++) {
                            sumaFilm += movies.get(j).getRatings().get(k);
                        }
                        mediaFilm = sumaFilm / movies.get(j).getRatings().size();
                        mediaFilme += mediaFilm;
                    }
                }
            }
            if (mediaFilme != 0) {
                mediaFilme = mediaFilme / count;

                if (!actorsMoviesRating.containsKey(actors.get(i).getName())) {
                    actorsMoviesRating.put(actors.get(i).getName(), mediaFilme);
                }
            }
        }
        return actorsMoviesRating;
    }

    /**
     * metoda calculeaza media tuturor serialelor in care joaca un actor
     * imi generez o lista ce contine toate serialele in care joaca actorul, iar pentru fiecare
     * serial, parcurg lista de sezoane aferente si se face media pentru fiecare sezon, adunand
     * ratingurile din lista de ratinguri a sezonului
     * se face media fiecarui sezon, se adauga la suma sezoanelor si se imparte la cate sezoane
     * sunt, aceasta fiind media unui serial
     * media unui serial se adauga la media serialelor, iar la final se imparte la cate numarul
     * de seriale in care apare actorul, obtinand media lui finala
     * @param serials primeste lista cu toate serialele
     * @return un map continand numele actorului si media lui pe toate serialele in care apare
     */
    public static Map averageActorSerial(final ArrayList<Actor> actors,
                                         final ArrayList<Serial> serials) {
        Map<String, Double> actorsSerialsRating = new HashMap<>();
        for (int i = 0; i < actors.size(); i++) {
            double mediaSeriale = 0;
            List<Serial> serialecuActorulX = actors.get(i).serialWithActor(serials, actors.get(i));
            for (int j = 0; j < serialecuActorulX.size(); j++) {
                double sumaSezoane = 0;
                for (int k = 0; k < serialecuActorulX.get(j).getSeasons().size(); k++) {
                    double mediaSezon = medieSezon(serialecuActorulX.get(j).getSeasons().get(k));
                    sumaSezoane += mediaSezon;
                }
                if (sumaSezoane != 0) {
                    double mediaSerial = sumaSezoane / serialecuActorulX.get(j).getSeasons().size();
                    mediaSeriale += mediaSerial;
                }
            }
            if (mediaSeriale != 0) {
                mediaSeriale = mediaSeriale / serialecuActorulX.size();
                actorsSerialsRating.put(actors.get(i).getName(), mediaSeriale);
            }
        }
        return actorsSerialsRating;
    }

    /**
     * metoda calculeaza media pe sezon, adunand toate ratingurile dintr-un sezon si impartind
     * la numarul de ratinguri
     * @param season primeste sezonul pentru care se face media
     * @return un double, media pe sezonul primit ca parametru
     */
    private static double medieSezon(final Season season) {
        double mediaSezon = 0.0;
        if (season.getRatings().isEmpty()) {
            return mediaSezon;
        } else {
            for (int i = 0; i < season.getRatings().size(); i++) {
                mediaSezon += season.getRatings().get(i);
            }
        }
        mediaSezon = mediaSezon / season.getRatings().size();
        return mediaSezon;
    }

    /**
     * calculeaza media unui actor, facand media intre media lui pe filmele si pe serialele in care
     * joaca
     * daca joaca si in filme si in seriale, se aduna cele 2 medii si se face media lor, impartind
     * la 2, daca joaca doar in filme sau doar in seriale, se pune in map doar media respectiva
     * @param movieRatings primeste mapul ce contine numele actorilor si media fiecaruia pe filmele
     *                      in care joaca
     * @param serialRatings primeste mapul cu numele actorilor si media fiecaruia pe serialele
     *                      in care joaca
     * @return un map in care se retine numele fiecarui actor si ratingul total al acestuia, pe
     * toate videoruile in care apare
     */
    public static Map averageActor(final ArrayList<Actor> actors,
                                   final Map<String, Double> movieRatings,
                                   final Map<String, Double> serialRatings) {
        Map<String, Double> averageActor = new HashMap<>();
        for (int i = 0; i < actors.size(); i++) {
            if (movieRatings.containsKey(actors.get(i).getName())) {
                averageActor.put(actors.get(i).getName(),
                        movieRatings.get(actors.get(i).getName()));
                if (serialRatings.containsKey(actors.get(i).getName())) {
                    double x = serialRatings.get(actors.get(i).getName());
                    averageActor.put(actors.get(i).getName(),
                            (movieRatings.get(actors.get(i).getName()) + x) / 2);
                }
            } else if (serialRatings.containsKey(actors.get(i).getName())) {
                averageActor.put(actors.get(i).getName(),
                        serialRatings.get(actors.get(i).getName()));
            }
        }
        return averageActor;
    }

}
