package recommendation;

import main.User;
import main.Video;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class Favorite {

    private Favorite() {

    }

    /**
     * metoda numara pentru fiecare video de cate ori apare in istoricele userilor
     * @param users primeste lista cu toti userii
     * @param videos primeste lista cu toate videourile
     * @return un map cu numele fiecarui video si numarul de aparitii al acestuia
     * in toate istoricele
     */
    public static Map contorfavoriteVideos(final ArrayList<User> users,
                                           final ArrayList<Video> videos) {
        Map<String, Long> favoriteVideo = new HashMap<>();
        for (int i = 0; i < videos.size(); i++) {
            long contorFavoriteVideo = favoriteNumber(users, videos.get(i).getTitle());
            if (contorFavoriteVideo != 0) {
                favoriteVideo.put(videos.get(i).getTitle(), contorFavoriteVideo);
            }
        }
        return favoriteVideo;
    }

    /**
     * metoda numara de cate ori apare un anumit video in toate istoricele userilor
     * @param users primeste lista cu toti userii
     * @param video primeste numele videoului care se cauta
     * @return numarul de aparitii al videoului in toate istoricele
     */
    protected static long favoriteNumber(final ArrayList<User> users, final String video) {
        long sum = users.stream().filter(user1 -> user1.getHistory().containsKey(video)).count();
        return sum;
    }

    /**
     * metoda sorteaza ascendent videourile nevazute mai intai dupa numarul de aparitii al acestora
     * in toate istoricele, iar apoi dupa titlu
     * @param unseenVideos primeste mapul cu numele videourilor nevazute si numarul lor de
     *                     vizualizari
     * @return o lista cu numele videourilor sortate dupa cum am precizat
     */
    public static List sortFavVideos(final Map<String, Long> unseenVideos) {
        List<String> favVideosSorted = unseenVideos.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue()
                        .thenComparing(Map.Entry.comparingByKey()))
                .map(Map.Entry::getKey).collect(Collectors.toList());
        return favVideosSorted;
    }

    /**
     * metoda verifica daca videourile favorite (in functie de numarul total de vizualizari) care
     * sunt deja sortate se gasesc in istoricul unui anumite user
     * se parcurge lista de favorite videos si primul video care nu este vizualizat de user,
     * se intoarce drept videoclip favorit
     * daca toate videourile primite ca parametru sunt deja vizualizate de user, se intoarce drept
     * video favorit primul video din baza de date care nu se afla in istoricul userului
     * @param sortedFavVideos lista cu videouri favorite si sortate
     * @param user userul pentru care se cauta videoul favorit
     * @param videos lista cu toate videourile
     * @return un string reprezentand videoul favorit pentru userul dat ca parametru, care nu este
     * vizualizat de acesta
     */
    public static String verifyHistory(final List<String> sortedFavVideos, final User user,
                                       final ArrayList<Video> videos) {
        String favVideo = null;
        for (int i = 0; i < sortedFavVideos.size(); i++) {
            if (!user.getHistory().containsKey(sortedFavVideos.get(i))) {
                favVideo = sortedFavVideos.get(i);
                break;
            }
        }
        if (favVideo == null) {
            for (int j = 0; j < videos.size(); j++) {
                if (!user.getHistory().containsKey(videos.get(j).getTitle())) {
                    favVideo = videos.get(j).getTitle();
                    break;
                }
            }
        }
        return favVideo;
    }

}
