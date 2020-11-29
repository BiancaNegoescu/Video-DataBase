package query;

import main.Actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ActorsAwards {

    private ActorsAwards() {

    }

    /**
     * metoda sorteaza actorii ce contin premiile precizate in input
     * @param filter primeste lista cu numele premiilor in functie de care se filtreaza actorii
     * @return o lista cu contine actorii care au toate premiile precizare
     */
    public static List sortActorsWithAwards(final ArrayList<Actor> actors,
                                            final List<String> filter) {
        List<Actor> actorsAwards = new ArrayList<>();
        for (int j = 0; j < actors.size(); j++) {
            int ok = 1;
            for (int k = 0; k < filter.size(); k++) {
                if (!actors.get(j).getAwards().containsKey(actor.ActorsAwards
                        .valueOf(filter.get(k)))) {
                    ok = 0;
                    break;
                }
            }
            if (Integer.compare(ok, 1) == 0) {
                actorsAwards.add(actors.get(j));
            }
        }
        return actorsAwards;
    }

    /**
     * metoda calculeaza pentru fiecare actor numarul total de premii
     * @param actorsAwards primeste o lista cu actorii ce au primit premiile precizate in input
     * @return un map cu numele actorilor si numarul total de premii al fiecaruia
     */
    public static Map awards(final List<Actor> actorsAwards) {
        Map<String, Integer> actorsNrAwards = new HashMap<>();
        for (int i = 0; i < actorsAwards.size(); i++) {
            int sum = 0;
            if (actorsAwards.get(i).getAwards().containsKey(actor.ActorsAwards.BEST_DIRECTOR)) {
                sum += actorsAwards.get(i).getAwards().get(actor.ActorsAwards.BEST_DIRECTOR);
            }
            if (actorsAwards.get(i).getAwards().containsKey(actor.ActorsAwards.BEST_PERFORMANCE)) {
                sum += actorsAwards.get(i).getAwards().get(actor.ActorsAwards.BEST_PERFORMANCE);
            }
            if (actorsAwards.get(i).getAwards().containsKey(actor.ActorsAwards.BEST_SCREENPLAY)) {
                sum += actorsAwards.get(i).getAwards().get(actor.ActorsAwards.BEST_SCREENPLAY);
            }
            if (actorsAwards.get(i).getAwards().containsKey(actor.ActorsAwards
                    .BEST_SUPPORTING_ACTOR)) {
                sum += actorsAwards.get(i).getAwards().get(actor.ActorsAwards
                        .BEST_SUPPORTING_ACTOR);
            }
            if (actorsAwards.get(i).getAwards().containsKey(actor.ActorsAwards
                    .PEOPLE_CHOICE_AWARD)) {
                sum += actorsAwards.get(i).getAwards().get(actor.ActorsAwards.PEOPLE_CHOICE_AWARD);
            }
            actorsNrAwards.put(actorsAwards.get(i).getName(), sum);
        }
        return actorsNrAwards;
    }
}
