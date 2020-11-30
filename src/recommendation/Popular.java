package recommendation;

import main.User;
import main.Video;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class Popular {

    private Popular() {

    }

    /**
     * metoda calculeaza numarul de aparitii al tuturor genurilor in toate videourile
     * se parcurg toate videoclipurile si se calculeaza pentru fiecare numarul de aparitii,
     * daca genul nu a fost deja adaugat in mapul de genuri populare, se adauga
     * @param videos lista cu toate videourile
     * @return un map cu numele tuturor genurilor si numarul de aparitii al acestora in toate
     * filmele
     */
    public static Map contorPopularGenres(final ArrayList<Video> videos) {
        Map<String, Long> popularGenres = new HashMap<>();
        for (int i = 0; i < videos.size(); i++) {
            for (int j = 0; j < videos.get(i).getGenres().size(); j++) {
                long contorGenre = genresNumber(videos, videos.get(i).getGenres().get(j));
                if (!popularGenres.containsKey(videos.get(i).getGenres().get(j))) {
                    popularGenres.put(videos.get(i).getGenres().get(j), contorGenre);
                }
            }
        }
        return popularGenres;
    }

    /**
     * metoda numara de cate ori se gaseste un anumit gen in toate videoclipurile
     * @param videos lista cu toate videourile
     * @param genre numele genului pe care il caut
     * @return numarul de aparitii al genului primit ca parametru
     */
    protected static long genresNumber(final ArrayList<Video> videos, final String genre) {
        long contor = videos.stream().filter(video1 -> video1.getGenres().contains(genre)).count();
        return contor;
    }

    /**
     * metoda sorteaza mapul cu genurile populare dupa numarul de aparitii, descrescator, iar
     * apoi dupa titlu, ascendent
     * astfel vor fi afisate cele mai populare genuri, spre cel mai putin populare
     * @param popularGenres primeste mapul cu numele genurilor si numarul fiecaruia de aparitii
     *                      in toate videourile
     * @return o lista sortata dupa cum am mentionat, cu numele genurilor
     */
    public static List sortPopularGenres(final Map<String, Long> popularGenres) {
        List<String> favVideosSorted = popularGenres.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey())).map(Map.Entry::getKey)
                .collect(Collectors.toList());
        return favVideosSorted;
    }
    /**
     * metoda intoarce primul video din baza de date cu cel mai popular gen
     * se parcurge lista cu genurile populare, sortate deja si se parcurge pentru fiecare
     * gen popular lista cu toate videourile din baza de date
     * daca videoul din baza de date contine genul popular si daca acest video nu se gaseste deja
     * in istoricul userului, am gasit primul video cu cel mai popular gen si opresc cautarea
     * @param sortedPopularGenres lista cu genurile sortate dupa popularite
     * @param videos lista cu toate videourile
     * @param user userul pentru care caut cel primul video cu cel mai popular gen
     * @return un string reprezentand numele primului video cu cel mai popular gen pentru userul
     * dat ca parametru
     */
    public static String popularGenre(final List<String> sortedPopularGenres,
                                      final ArrayList<Video> videos, final User user) {
        String popularGenre = null;
        for (int i = 0; i < sortedPopularGenres.size(); i++) {
            for (int j = 0; j < videos.size(); j++) {
                if (videos.get(j).getGenres().contains(sortedPopularGenres.get(i))) {
                    if (!user.getHistory().containsKey(videos.get(j).getTitle())) {
                        popularGenre = videos.get(j).getTitle();
                        break;
                    }
                }
            }
            if (popularGenre != null) {
                break;
            }
        }
        return popularGenre;
    }

}
