package query;

import main.Movie;
import main.Serial;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class LongestVideos {

    private LongestVideos() {

    }

    /**
     * metoda sorteaza filmele (care au fost deja sortate in functie de filtrele primite) dupa
     * durata acestora
     * filmele se compara prima data dupa durata, iar al doilea criteriu este titlul
     * @param moviesFiltered primeste lista de filme filtrate
     * @param sortType tipul sortarii, ascendent sau descendent
     * @return o lista de filme sortate dupa cum am precizat deja
     */
    public static List sortDuration(final List<Movie> moviesFiltered, final String sortType) {
        if (sortType.equals("desc")) {
            Comparator<Movie> compareDuration = Comparator.comparing(Movie::getDuration,
                    Comparator.reverseOrder());
            Comparator<Movie> compareName = Comparator.comparing(Movie::getTitle);
            Comparator<Movie> compareFull = compareDuration.thenComparing(compareName);
            List<Movie> moviesDuration = moviesFiltered.stream().sorted(compareFull)
                    .collect(Collectors.toList());
            return moviesDuration;
        } else {
            Comparator<Movie> compareDuration = Comparator.comparing(Movie::getDuration);
            Comparator<Movie> compareName = Comparator.comparing(Movie::getTitle);
            Comparator<Movie> compareFull = compareDuration.thenComparing(compareName);
            List<Movie> moviesDuration = moviesFiltered.stream().sorted(compareFull)
                    .collect(Collectors.toList());
            return moviesDuration;
        }
    }

    /**
     * metoda sorteaza serialele (care au fost deja sortate in functie de filtrele primite) dupa
     * durata acestora
     * serialele se compara prima data dupa durata, iar al doilea criteriu este titlul
     * @param serialsFiltered primeste lista de seriale filtrate
     * @param sortType tipul sortarii, ascendent sau descendent
     * @return o lista de seriale sortate dupa cum am precizat deja
     */
    public static List sortDurationSerials(final List<Serial> serialsFiltered,
                                           final String sortType) {
        if (sortType.equals("desc")) {
            Comparator<Serial> compareDuration = Comparator
                    .comparing(Serial::getTotalDuration,
                            Comparator.reverseOrder());
            List<Serial> serialsDuration = serialsFiltered.stream().sorted(compareDuration)
                    .collect(Collectors.toList());
            return serialsDuration;
        } else {
            Comparator<Serial> compareDuration = Comparator
                    .comparing(Serial::getTotalDuration);
            List<Serial> serialsDuration = serialsFiltered.stream().sorted(compareDuration)
                    .collect(Collectors.toList());
            return serialsDuration;
        }
    }

}
