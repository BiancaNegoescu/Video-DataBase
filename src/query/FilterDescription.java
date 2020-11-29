package query;

import main.Actor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class FilterDescription {

    private FilterDescription() {

    }

    /**
     * metoda sorteaza actorii in descrierea carora apar cuvintele precizate in input
     * in descriere am inlocuit caracterele care nu sunt litere cu spatiu penntru a nu avea
     * probleme la cautarea cuvintelor-cheie
     * @param words lista ce contine cuvintele-cheie date in input
     * @return o lista cu numele actorilor ce au in descriere toate cuvintele din input
     */
    public static List sortActorsDescription(final ArrayList<Actor> actors,
                                             final List<String> words) {
        List<String> sortedActorsDescription = new ArrayList<>();
        for (int j = 0; j < actors.size(); j++) {
            int ok = 1;
            for (int k = 0; k < words.size(); k++) {
                if (!actors.get(j).getCareerDescription()
                        .replaceAll("[^a-zA-Z]", " ")
                        .toLowerCase().toLowerCase().contains(" " + words.get(k))) {
                    ok = 0;
                    break;
                }
            }
            if (Integer.compare(ok, 1) == 0) {
                sortedActorsDescription.add(actors.get(j).getName());
            }
        }
        return sortedActorsDescription;
    }

    /**
     * metoda sorteaza actorii dupa nume in descrierea carora se gasesc cuvintele-cheie in functie
     * de tipul de sortare precizat
     * @param actorsDescription primeste o lista cu numele actorilor ce au in descriere cuvintele
     *                          din input
     * @param sortType primeste tipul sortarii, ascendent sau descendent
     * @return o lista sortata cu numele actorilor ce au in descriere toate cuvintele-cheie
     */
    public static List descriptionCriteria(final List<String> actorsDescription,
                                           final String sortType) {
        if (sortType.equals("asc")) {
            List<String> actorsDescription1 = actorsDescription.stream().sorted()
                    .collect(Collectors.toList());
            return actorsDescription;
        } else {
            List<String> actorsDescription1 = actorsDescription.stream()
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());
            return actorsDescription1;
        }
    }
}
