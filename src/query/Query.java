package query;

import entertainment.Season;
import main.Actor;
import main.Movie;
import main.Serial;
import main.Sezon;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public final class Query {
    private final ArrayList<Movie> movies;
    private final ArrayList<Actor> actors;
    private final ArrayList<Serial> seriale;
    private final ArrayList<Sezon> sezoane;

    public Query(final ArrayList<Movie> movies,
                 final ArrayList<Actor> actors,
                 final ArrayList<Serial> seriale,
                 final ArrayList<Sezon> sezoane) {
        this.movies = movies;
        this.actors = actors;
        this.seriale = seriale;
        this.sezoane = sezoane;
    }

    /**
     * metoda sorteaza ascendent mapul dupa mediile actorilor
     * @param averageActors primeste mapul cu actorii si mediilor lor pe toate videourile
     * @return o lista cu numele actorilor, in ordinea ascendenta a mediilor
     */
    public List sortAsc(final Map<String, Double> averageActors) {
        List<String> averageActor = averageActors.entrySet().stream()
                .sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey)
                .collect(Collectors.toList());
        return averageActor;
    }

    /**
     * metoda intoarce primii "n" actori
     * @param averageActorByValue primeste lista actorii sortati dupa mediile videourilor
     * @param number primeste numarul de actori pe care trebuie sa ii afisez in final
     * @return o lista cu numele primilor "n" actori
     */
    public List sortLimit(final List<String> averageActorByValue, final int number) {
        List<String> averageActors = averageActorByValue.stream()
                .limit(number)
                .collect(Collectors.toList());
        return averageActors;
    }

    /**
     * metoda sorteaza descendent mapul dupa mediile actorilor
     * @param averageActors primeste mapul cu actorii si mediilor lor pe toate videourile
     * @return o lista cu numele actorilor, in ordinea descendenta a mediilor
     */
    public List sortDesc(final Map<String, Double> averageActors) {
        List<String> averageActor = averageActors.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        return averageActor;
    }

    /**
     * metoda sorteaza videourile dupa valoare, iar apoi dupa nume
     * @param videos primeste mapul cu numele videoului si valoarea asociata (depinde ce reprezinta
     *               aceasta valoare, deoarece am folost aceasta metoda in mai multe locuri pentru
     *               sortare)
     * @param sortType este tipul sortarii, ascendent sau descendent
     * @return o lista cu numele videourilor sortate
     */
    public List sortVideos(final Map<String, Integer> videos, final String sortType) {
        if (sortType.equals("asc")) {
            List<String> sortedVideos = videos.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue()
                            .thenComparing(Map.Entry.comparingByKey())).map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            return sortedVideos;
        } else {
            List<String> sortedVideos = videos.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                            .thenComparing(Map.Entry.comparingByKey(Comparator.reverseOrder())))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            return sortedVideos;
        }
    }

    /**
     * aceasta metoda are aceeasi implementare si acelasi scop ca cea anterioara, dar se aplica
     * atunci cand valoarea asociata videoului este un double
     * @param videos primeste mapul cu numele videourilor si valorile asociate
     * @param sortType tipul sortarii, ascendent sau descendent
     * @return o lista cu numele videourilor sortate
     */
    public List sortVideosDouble(final Map<String, Double> videos, final String sortType) {
        if (sortType.equals("asc")) {
            List<String> sortedVideos = videos.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            return sortedVideos;
        } else {
            List<String> sortedVideos = videos.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            return sortedVideos;
        }
    }

    /**
     * metoda filtreaza filmele in functie de filtrele primite in input
     * se verifica daca sunt primite filtre si pentru an si pentru genuri sau daca e primit doar
     * filtru pentru an sau doar filtru pentru gen
     * daca filmul contine toate filtrele precizate, se adauga in lista de filme filtrate
     * @param filters primeste toate filtrele care se vor aplica filmelor
     * @return o lista cu toate filmele ce indeplinesc conditiile primite
     */
    public List moviesFiltered(final List<List<String>> filters) {
        List<Movie> filteredMovies = new ArrayList<>();
        for (int i = 0; i < movies.size(); i++) {
            int ok = 1;
            int yearIdx = 0;
            int year = 0;
            int genresIdx = 1;
            int genre = 0;
            if (filters.get(yearIdx).get(year) != null) {
                if (filters.get(genresIdx).get(genre) != null) {
                    if (!(movies.get(i).getGenres().contains(filters.get(genresIdx).get(genre)))
                            || !((String.valueOf(movies.get(i).getYear()))
                                    .equals(filters.get(yearIdx).get(year)))) {
                        ok = 0;
                    }
                } else {
                    if (!((String.valueOf(movies.get(i).getYear()))
                            .equals(filters.get(yearIdx).get(year)))) {
                        ok = 0;
                    }
                }
            } else if (filters.get(yearIdx).get(year) == null) {
                if ((filters.get(genresIdx).get(genre) != null)
                        && !(movies.get(i).getGenres().contains(filters.get(genresIdx)
                        .get(genre)))) {
                    ok = 0;
                }
            }
            if (Integer.compare(ok, 1) == 0) {
                filteredMovies.add(movies.get(i));
            }
        }
        return filteredMovies;
    }

    /**
     * metoda filtreaza serialele in functie de filtrele primite in input
     * se verifica daca sunt primite filtre si pentru an si pentru genuri sau daca e primit doar
     * filtru pentru an sau doar filtru pentru gen
     * daca serialul contine toate filtrele precizate, se adauga in lista de seriale filtrate
     * @param filters primeste toate filtrele care se vor aplica serialelor
     * @return o lista cu toate serialele ce indeplinesc conditiile primite
     */
    public List serialsFiltered(final ArrayList<Serial> serials,
                                   final List<List<String>> filters) {
        List<Serial> filteredShows = new ArrayList<>();
        for (int i = 0; i < serials.size(); i++) {
            int ok = 1;
            int yearIdx = 0;
            int year = 0;
            int genresIdx = 1;
            int genre = 0;
            if (filters.get(yearIdx).get(year) != null) {
                if (filters.get(genresIdx).get(genre) != null) {
                    if (!(serials.get(i).getGenres().contains(filters.get(genresIdx).get(genre)))
                            || !((String.valueOf(serials.get(i).getYear()))
                            .equals(filters.get(yearIdx).get(year)))) {
                        ok = 0;
                    }
                } else {
                    if (!((String.valueOf(serials.get(i).getYear()))
                            .equals(filters.get(yearIdx).get(year)))) {
                        ok = 0;
                    }
                }
            } else if (filters.get(yearIdx).get(year) == null) {
                if ((filters.get(genresIdx).get(genre) != null)
                        && !(serials.get(i).getGenres().contains(filters.get(genresIdx)
                        .get(genre)))) {
                    ok = 0;
                }
            }
            if (Integer.compare(ok, 1) == 0) {
                filteredShows.add(serials.get(i));
            }
        }
        return filteredShows;
    }

    /**
     * metoda calculeaza durata fiecarui serial
     * @param serials primeste lista cu toate serialele
     */
    public void calculateSerialsDuration(final ArrayList<Serial> serials) {
        for (int i = 0; i < serials.size(); i++) {
            serials.get(i).setTotalDuration(seasonsSum(serials.get(i).getSeasons()));
        }
    }

    /**
     * metoda calculeaza suma duratelor tuturor sezoanelor, aceasta fiind defapt durata intregului
     * serial
     * @param seasons primeste toate sezoanele unui serial, pentru care se calculeaza durata
     * @return durata serialului
     */
    public int seasonsSum(final ArrayList<Season> seasons) {
        int sum = seasons.stream().mapToInt(Season::getDuration).sum();
        return sum;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public ArrayList<Actor> getActors() {
        return actors;
    }

    public ArrayList<Serial> getSeriale() {
        return seriale;
    }

    public ArrayList<Sezon> getSezoane() {
        return sezoane;
    }
}


