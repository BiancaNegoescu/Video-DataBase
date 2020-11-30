package recommendation;

import main.User;
import main.Video;

import java.util.ArrayList;

public final class Standard {

    private Standard() {

    }

    /**
     * metoda cauta primul video nevizualizat de un anumit user
     * cauta in toate videourile, iar daca in istoricul userului nu se gaseste videoul respectiv,
     * se opreste cautarea deoarece a fost gasit primul video nevizualizat
     * @param user primeste userul pentru care se cauta primul video nevazut
     * @param videos primeste lista cu toate videourile
     * @return un string reprezentand primul video nevizualizat de userul dat ca parametru
     */
    public static String firstUnseenVideo(final User user, final ArrayList<Video> videos) {
        String firstUnseen = null;
        for (int i = 0; i < videos.size(); i++) {
            if (!(user.getHistory().containsKey(videos.get(i).getTitle()))) {
                firstUnseen = videos.get(i).getTitle();
                break;
            }
        }
        return firstUnseen;
    }
}
