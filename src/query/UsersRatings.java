package query;

import main.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class UsersRatings {

    private UsersRatings() {

    }

    /**
     * metoda calculeaza pentru fiecare user numarul total de ratinguri pe care le-a dat, atat
     * filmelor, cat si serialelor
     * @param users primeste lista cu toti userii
     * @return un map cu numele fiecarui user si numarul total de ratinguri pe care le-a dat
     */
    public static Map userRatings(final ArrayList<User> users) {
        Map<String, Integer> usersRatings = new HashMap<>();
        for (int i = 0; i < users.size(); i++) {
            int nrMovieRatings = users.get(i).getMovieRatings().size();
            int nrSerialRatings = users.get(i).getSeasonRatings().size();
            int nrRatings = nrMovieRatings + nrSerialRatings;
            if (nrRatings != 0) {
                usersRatings.put(users.get(i).getUsername(), nrRatings);
            }
        }
        return usersRatings;
    }
}
