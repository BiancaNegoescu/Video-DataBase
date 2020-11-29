package main;

import actor.ActorsAwards;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public final class Actor {
    private final String name;
    private final String careerDescription;
    private final ArrayList<String> filmography;
    private final Map<ActorsAwards, Integer> awards;
    private double media;

    public Actor(final String name, final String careerDescription,
                 final ArrayList<String> filmography,
                 final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;
        this.media = 0;
    }

    /**
     * metoda genereaza o lista cu toate serialele in care joaca un anume actor
     * @param serials primeste lista cu toate serialele
     * @param actor primeste actorul pe care il caut in toate serialele
     * @return lista cu serialele in care joaca actorul
     */
    public List serialWithActor(final ArrayList<Serial> serials, final Actor actor) {
        List<Serial> result2 = serials.stream()
                .filter(serial -> serial.getCast().contains(actor.name))
                .collect(Collectors.toList());
        return result2;
    }

    public String getName() {
        return name;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public double getMedia() {
        return media;
    }

    public void setMedia(final double media) {
        this.media = media;
    }
}
