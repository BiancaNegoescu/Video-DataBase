package main;

import query.Query;
import query.UsersRatings;
import recommendation.Favorite;
import recommendation.Popular;
import recommendation.Standard;
import recommendation.BestUnseen;
import recommendation.Search;
import query.ActorsAwards;
import query.AverageActors;
import query.FavoriteVideos;
import query.FilterDescription;
import query.LongestVideos;
import query.MostViewedVideo;
import query.RatingVideos;
import checker.Checker;
import checker.Checkstyle;
import common.Constants;
import fileio.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     *
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */

    public static <input> void action(final String filePath1,
                                      final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();
        //  arrayResult.add(fileWriter.writeFile())
        //TODO add here the entry point to your implementation

        ArrayList<Movie> movies = new ArrayList<>();
        ArrayList<User> users = new ArrayList<>();
        ArrayList<Actor> actors = new ArrayList<>();
        ArrayList<Serial> serials = new ArrayList<>();
        ArrayList<Sezon> seasons = new ArrayList<>();
        ArrayList<Video> videos = new ArrayList<>();

        for (MovieInputData movie1 : input.getMovies()) {
            Movie movie = new Movie(movie1.getTitle(), movie1.getCast(), movie1.getGenres(),
                    movie1.getYear(), movie1.getDuration());
            movies.add(movie);
            Video video = new Video(movie1.getTitle(), movie1.getYear(), movie1.getCast(),
                    movie1.getGenres());
            videos.add(video);
        }

        for (UserInputData user1 : input.getUsers()) {
            User user = new User(user1.getUsername(), user1.getSubscriptionType(),
                    user1.getHistory(), user1.getFavoriteMovies());
            users.add(user);
        }

        for (ActorInputData actor1 : input.getActors()) {
            Actor actor = new Actor(actor1.getName(), actor1.getCareerDescription(),
                    actor1.getFilmography(), actor1.getAwards());
            actors.add(actor);
        }

        for (SerialInputData serial1 : input.getSerials()) {
            Serial serial = new Serial(serial1.getTitle(), serial1.getCast(), serial1.getGenres(),
                    serial1.getNumberSeason(), serial1.getSeasons(), serial1.getYear());
            serials.add(serial);
            Video video = new Video(serial1.getTitle(), serial1.getYear(), serial1.getCast(),
                    serial1.getGenres());
            videos.add(video);

            for (int i = 0; i < serial.getNumberOfSeasons(); i++) {
                Sezon season = new Sezon(serial.getNumberOfSeasons(), serial.getSeasons().get(i)
                        .getDuration());
                seasons.add(season);
            }
        }

        for (int i = 0; i < input.getCommands().size(); i++) {
            // ADAUGARE LA FAVORITE
            if (input.getCommands().get(i).getActionType().equals("command")
                    && input.getCommands().get(i).getType().equals("favorite")) {
                for (int j = 0; j < users.size(); j++) {
                    if (users.get(j).getUsername().equals(input.getCommands().get(i)
                            .getUsername())) {
                        if (users.get(j).addToFav(users.get(j), input.getCommands().get(i)
                                .getTitle()) == 1) {
                            JSONObject obj = fileWriter.writeFile(input.getCommands().get(i)
                                    .getActionId(), "success -> " + input.getCommands()
                                    .get(i).getTitle() + " was added as favourite");
                            arrayResult.add(obj);
                        } else if (users.get(j).addToFav(users.get(j), input.getCommands()
                                .get(i).getTitle()) == 0) {
                            JSONObject obj = fileWriter.writeFile(input.getCommands().get(i)
                                    .getActionId(), "error -> " + input.getCommands()
                                    .get(i).getTitle() + " is already in favourite list");
                            arrayResult.add(obj);
                        } else if (users.get(j).addToFav(users.get(j), input.getCommands().get(i)
                                .getTitle()) == -1) {
                            JSONObject obj = fileWriter.writeFile(input.getCommands().get(i)
                                    .getActionId(), "error -> " + input.getCommands()
                                    .get(i).getTitle() + " is not seen");
                            arrayResult.add(obj);
                        }
                    }
                }
            }

            //RATING
            if (input.getCommands().get(i).getActionType().equals("command") && input.getCommands()
                    .get(i).getType().equals("rating")
                    && (input.getCommands().get(i).getSeasonNumber() == 0)) { //pentru filme
                for (int j = 0; j < users.size(); j++) {
                    if (users.get(j).getUsername().equals(input.getCommands().get(i)
                            .getUsername())) {
                        if ((users.get(j).ratingMovies(users.get(j), input.getCommands().get(i)
                                .getTitle(), input.getCommands().get(i).getGrade(), movies)) == 1) {
                            JSONObject obj = fileWriter.writeFile(input.getCommands().get(i)
                                    .getActionId(), "success -> "
                                    + input.getCommands().get(i).getTitle() + " was rated with "
                                    + input.getCommands().get(i).getGrade() + " by "
                                    + input.getCommands().get(i).getUsername());
                            arrayResult.add(obj);
                        } else if ((users.get(j).ratingMovies(users.get(j), input.getCommands()
                                .get(i).getTitle(), input.getCommands().get(i).getGrade(), movies))
                                == 0) {
                            JSONObject obj = fileWriter.writeFile(input.getCommands().get(i)
                                    .getActionId(), "error -> "
                                    + input.getCommands().get(i).getTitle()
                                    + " has been already rated");
                            arrayResult.add(obj);
                        } else {
                            JSONObject obj = fileWriter.writeFile(input.getCommands().get(i)
                                    .getActionId(), "error -> "
                                    + input.getCommands().get(i).getTitle() + " is not seen");
                            arrayResult.add(obj);
                        }
                    }
                }
            } else {       //SERIALE
                if (input.getCommands().get(i).getActionType().equals("command")
                        && input.getCommands().get(i).getType().equals("rating")
                        && (input.getCommands().get(i).getSeasonNumber() != 0)) {
                    for (int j = 0; j < users.size(); j++) {
                        if (users.get(j).getUsername().equals(input.getCommands().get(i)
                                .getUsername())) {
                            if (users.get(j).ratingSeason(users.get(j), input.getCommands().get(i)
                                            .getSeasonNumber(), input.getCommands().get(i)
                                            .getTitle(),
                                    input.getCommands().get(i).getGrade(), serials) == 1) {
                                JSONObject obj = fileWriter.writeFile(input.getCommands().get(i)
                                        .getActionId(), "success -> "
                                        + input.getCommands().get(i).getTitle() + " was rated with "
                                        + input.getCommands().get(i).getGrade() + " by "
                                        + input.getCommands().get(i).getUsername());
                                arrayResult.add(obj);
                            } else if (users.get(j).ratingSeason(users.get(j), input.getCommands()
                                            .get(i).getSeasonNumber(), input.getCommands().get(i)
                                            .getTitle(),
                                    input.getCommands().get(i).getGrade(), serials) == 0) {
                                JSONObject obj = fileWriter.writeFile(input.getCommands().get(i)
                                        .getActionId(), "error -> "
                                        + input.getCommands().get(i).getTitle()
                                        + " has been already rated");
                                arrayResult.add(obj);
                            } else {
                                JSONObject obj = fileWriter.writeFile(input.getCommands().get(i)
                                        .getActionId(), "error -> "
                                        + input.getCommands().get(i).getTitle() + " is not seen");
                                arrayResult.add(obj);
                            }
                        }
                    }
                }
            }

            // MARK AS SEEN
            if (input.getCommands().get(i).getActionType().equals("command")
                    && input.getCommands().get(i).getType().equals("view")) {
                for (int j = 0; j < users.size(); j++) {
                    if (users.get(j).getUsername().equals(input.getCommands().get(i)
                            .getUsername())) {
                        users.get(j).markSeen(users.get(j), input.getCommands().get(i).getTitle());
                        JSONObject obj = fileWriter.writeFile(input.getCommands().get(i)
                                .getActionId(), "success -> "
                                + input.getCommands().get(i).getTitle()
                                + " was viewed with total views of "
                                + users.get(j).getHistory().get(input.getCommands().get(i)
                                .getTitle()));
                        arrayResult.add(obj);
                    }
                }
            }

            // QUERY AVERAGE ACTORS
            Query query = new Query(movies, actors, serials, seasons);

            if (input.getCommands().get(i).getActionType().equals("query") && input.getCommands()
                    .get(i).getCriteria().equals("average")) {
                Map<String, Double> actorsMoviesRating = AverageActors.averageActorMovie(actors,
                        movies);
                Map<String, Double> actorsSerialsRating = AverageActors.averageActorSerial(actors,
                        serials);
                Map<String, Double> averageActor = AverageActors.averageActor(actors,
                        actorsMoviesRating, actorsSerialsRating);
                if (input.getCommands().get(i).getSortType().equals("asc")) {
                    List<String> sortedActors = query.sortAsc(averageActor);
                    for (int k = 0; k < sortedActors.size() - 1; k++) {
                        for (int j = k + 1; j < sortedActors.size(); j++) {
                            //daca au aceeasi val
                            if (averageActor.get(sortedActors.get(k))
                                    .equals(averageActor.get(sortedActors.get(j)))) {
                                //daca john e dupa anna fac swipe
                                if (sortedActors.get(k).compareTo(sortedActors.get(j)) > 0) {
                                    Collections.swap(sortedActors, k, j);
                                }
                            }
                        }
                    }
                    List<String> sortedActorsfinal = query.sortLimit(sortedActors,
                            input.getCommands().get(i).getNumber());
                    JSONObject obj = fileWriter.writeFile(input.getCommands().get(i)
                            .getActionId(), "Query result: "
                            + sortedActorsfinal);
                    arrayResult.add(obj);
                } else if (input.getCommands().get(i).getSortType().equals("desc")) {
                    List<String> sortedActors = query.sortDesc(averageActor);
                    for (int k = 0; k < sortedActors.size() - 1; k++) {
                        for (int j = k + 1; j < sortedActors.size(); j++) {
                            //daca au aceeasi val
                            if (averageActor.get(sortedActors.get(k))
                                    .equals(averageActor.get(sortedActors.get(j)))) {
                                //daca john e dupa anna fac swipe
                                if (sortedActors.get(k).compareTo(sortedActors.get(j)) < 0) {
                                    Collections.swap(sortedActors, k, j);
                                }
                            }
                        }
                    }
                    List<String> sortedActorsfinal = query.sortLimit(sortedActors,
                            input.getCommands().get(i).getNumber());
                    JSONObject obj = fileWriter.writeFile(input.getCommands().get(i).getActionId(),
                            "Query result: "
                            + sortedActorsfinal);
                    arrayResult.add(obj);
                }
            }

            // QUERY FAV VIDEOS
            // QUERY FAV MOVIES
            if (input.getCommands().get(i).getActionType().equals("query")
                    && input.getCommands().get(i).getObjectType().equals("movies")) {
                if (input.getCommands().get(i).getCriteria().equals("favorite")) {
                    List<Movie> filteredMovies = query.moviesFiltered(input.getCommands()
                            .get(i).getFilters());
                    List<String> filteredMovies1 = filteredMovies.stream()
                            .map(Video::getTitle)
                            .collect(Collectors.toList());
                    Map<String, Integer> favoriteMovies = FavoriteVideos.favoriteMovies(movies,
                            users);
                    // l a sortat fie asc, fie desc
                    List<String> favoriteMoviesSorted = query.sortVideos(favoriteMovies,
                            input.getCommands().get(i).getSortType());
                    favoriteMoviesSorted.retainAll(filteredMovies1);
                    List<String> favoriteMoviesFinal = query.sortLimit(favoriteMoviesSorted,
                            input.getCommands().get(i).getNumber());
                    JSONObject obj = fileWriter.writeFile(input.getCommands().get(i).getActionId(),
                            "Query result: "
                            + favoriteMoviesFinal);
                    arrayResult.add(obj);
                }
            }

            // QUERY FAV SHOWS
            if (input.getCommands().get(i).getActionType().equals("query") && input.getCommands()
                    .get(i).getObjectType().equals("shows")) {
                if (input.getCommands().get(i).getCriteria().equals("favorite")) {
                    List<Serial> filteredShows = query.serialsFiltered(serials, input.getCommands()
                            .get(i).getFilters());
                    List<String> filteredShows1 = filteredShows.stream()
                            .map(Video::getTitle)
                            .collect(Collectors.toList());
                    Map<String, Integer> favoriteShows = FavoriteVideos.favoriteShows(movies,
                            users);
                    List<String> favoriteShowsSorted = query.sortVideos(favoriteShows,
                            input.getCommands().get(i).getSortType());
                    favoriteShowsSorted.retainAll(filteredShows1);
                    List<String> favoriteShowsFinal = query.sortLimit(favoriteShowsSorted,
                            input.getCommands().get(i).getNumber());
                    JSONObject obj = fileWriter.writeFile(input.getCommands().get(i).getActionId(),
                            "Query result: "
                            + favoriteShowsFinal);
                    arrayResult.add(obj);
                }
            }

            // QUERY ACTORS AWARDS
            if (input.getCommands().get(i).getActionType().equals("query") && input.getCommands()
                    .get(i).getObjectType().equals("actors")) {
                if (input.getCommands().get(i).getCriteria().equals("awards")) {
                    int awardsIdx = 3;
                    List<Actor> actorsAwards = ActorsAwards.sortActorsWithAwards(actors,
                            input.getCommands()
                            .get(i).getFilters().get(awardsIdx));
                    Map<String, Integer> actorsNumberAwards = ActorsAwards.awards(actorsAwards);
                    List<String> actorsFinal = query.sortVideos(actorsNumberAwards,
                            input.getCommands().get(i).getSortType());
                        JSONObject obj = fileWriter.writeFile(input.getCommands().get(i)
                                .getActionId(), "Query result: "
                                + actorsFinal);
                        arrayResult.add(obj);
                }
            }

            // QUERY FILTER DESCRIPTION
            if (input.getCommands().get(i).getActionType().equals("query") && input.getCommands()
                    .get(i).getObjectType().equals("actors")) {
                if (input.getCommands().get(i).getCriteria().equals("filter_description")) {
                    int descriptionIdx = 2;
                    List<String> actorsDescription = FilterDescription.sortActorsDescription(actors,
                            input.getCommands().get(i).getFilters().get(descriptionIdx));
                    List<String> actorsDescriptionSorted =
                            FilterDescription.descriptionCriteria(actorsDescription,
                                    input.getCommands().get(i).getSortType());
                    JSONObject obj = fileWriter.writeFile(input.getCommands().get(i).getActionId(),
                            "Query result: "
                            + actorsDescriptionSorted);
                    arrayResult.add(obj);
                }
            }

            // QUERY LONGEST MOVIES
            if (input.getCommands().get(i).getActionType().equals("query")
                    && input.getCommands().get(i).getObjectType().equals("movies")) {
                if (input.getCommands().get(i).getCriteria().equals("longest")) {
                    List<Movie> moviesFiltered = query.moviesFiltered(input.getCommands()
                            .get(i).getFilters());
                    List<Movie> moviesDuration = LongestVideos.sortDuration(moviesFiltered,
                            input.getCommands().get(i).getSortType());
                    List<String> movies1 = moviesDuration.stream().map(Video::getTitle)
                            .collect(Collectors.toList());
                    List<String> moviesFinal = query.sortLimit(movies1, input.getCommands().get(i)
                            .getNumber());
                    JSONObject obj = fileWriter.writeFile(input.getCommands().get(i).getActionId(),
                            "Query result: "
                            + moviesFinal);
                    arrayResult.add(obj);

                }
            }

            // QUERY LONGEST SHOWS
            if (input.getCommands().get(i).getActionType().equals("query")
                    && input.getCommands().get(i).getObjectType().equals("shows")) {
                if (input.getCommands().get(i).getCriteria().equals("longest")) {
                    query.calculateSerialsDuration(serials);
                    List<Serial> serialsFiltered = query.serialsFiltered(serials,
                            input.getCommands().get(i).getFilters());
                    List<Serial> serialsDuration =
                            LongestVideos.sortDurationSerials(serialsFiltered,
                            input.getCommands().get(i).getSortType());
                    List<String> serials1 = serialsDuration.stream()
                            .map(Video::getTitle)
                            .collect(Collectors.toList());
                    List<String> serialsFinal = query.sortLimit(serials1, input.getCommands()
                            .get(i).getNumber());
                    JSONObject obj = fileWriter.writeFile(input.getCommands().get(i).getActionId(),
                            "Query result: "
                            + serialsFinal);
                    arrayResult.add(obj);

                }
            }

            // QUERY MOST VIEWED MOVIE
            if (input.getCommands().get(i).getActionType().equals("query")
                    && input.getCommands().get(i).getObjectType().equals("movies")) {
                if (input.getCommands().get(i).getCriteria().equals("most_viewed")) {
                    List<Movie> moviesFiltered = query.moviesFiltered(input.getCommands()
                            .get(i).getFilters());
                    Map<String, Integer> movieViews =
                            MostViewedVideo.calculateMovieViews(moviesFiltered, users);
                    List<String> mostViewedMovies = query.sortVideos(movieViews,
                            input.getCommands().get(i).getSortType());
                    mostViewedMovies = query.sortLimit(mostViewedMovies, input.getCommands()
                            .get(i).getNumber());
                    JSONObject obj = fileWriter.writeFile(input.getCommands().get(i).getActionId(),
                            "Query result: "
                            + mostViewedMovies);
                    arrayResult.add(obj);
                }
            }

            //QUERY MOST VIEWED SHOWS
            if (input.getCommands().get(i).getActionType().equals("query")
                    && input.getCommands().get(i).getObjectType().equals("shows")) {
                if (input.getCommands().get(i).getCriteria().equals("most_viewed")) {
                    Map<String, Integer> serialViews = MostViewedVideo.calculateShowViews(serials,
                            users);
                    List<Serial> serialsFiltered = query.serialsFiltered(serials,
                            input.getCommands().get(i).getFilters());
                    List<String> mostViewedSerials = query.sortVideos(serialViews,
                            input.getCommands().get(i).getSortType());
                    List<String> serialsFiltered1 = serialsFiltered.stream()
                            .map(Video::getTitle).collect(Collectors.toList());
                    mostViewedSerials.retainAll(serialsFiltered1);
                    mostViewedSerials = query.sortLimit(mostViewedSerials, input.getCommands()
                            .get(i).getNumber());
                    JSONObject obj = fileWriter.writeFile(input.getCommands().get(i).getActionId(),
                            "Query result: "
                            + mostViewedSerials);
                    arrayResult.add(obj);
                }
            }

            //QUERY RATING MOVIE
            if (input.getCommands().get(i).getActionType().equals("query")
                    && input.getCommands().get(i).getObjectType().equals("movies")) {
                if (input.getCommands().get(i).getCriteria().equals("ratings")) {
                    List<Movie> filteredMovies = query.moviesFiltered(input.getCommands()
                            .get(i).getFilters());
                    Map<String, Double> ratedMovies = RatingVideos.calculateMovieRatings(movies,
                            users);
                    List<String> sortedMovies = query.sortVideosDouble(ratedMovies,
                            input.getCommands().get(i).getSortType());
                    List<String> filteredMovies1 = filteredMovies.stream()
                            .map(Video::getTitle).collect(Collectors.toList());
                    sortedMovies.retainAll(filteredMovies1);
                    sortedMovies = query.sortLimit(sortedMovies, input.getCommands().get(i)
                            .getNumber());
                    JSONObject obj = fileWriter.writeFile(input.getCommands().get(i).getActionId(),
                            "Query result: "
                            + sortedMovies);
                    arrayResult.add(obj);
                }
            }

            // QUERY RATING SHOWS
            if (input.getCommands().get(i).getActionType().equals("query")
                    && input.getCommands().get(i).getObjectType().equals("shows")) {
                if (input.getCommands().get(i).getCriteria().equals("ratings")) {
                    List<Serial> filteredShows = query.serialsFiltered(serials, input.getCommands().
                            get(i).getFilters());
                    Map<String, Double> ratedShows = RatingVideos.calculateShowRatings(serials,
                            users);
                    List<String> sortedShows = query.sortVideosDouble(ratedShows,
                            input.getCommands().get(i).getSortType());
                    List<String> filteredShows1 = filteredShows.stream()
                            .map(Video::getTitle).collect(Collectors.toList());
                    sortedShows.retainAll(filteredShows1);
                    sortedShows = query.sortLimit(sortedShows, input.getCommands().get(i)
                            .getNumber());
                    JSONObject obj = fileWriter.writeFile(input.getCommands().get(i).getActionId(),
                            "Query result: "
                            + sortedShows);
                    arrayResult.add(obj);
                }
            }

            // QUERY USERS NUMBER OF RATINGS
            if (input.getCommands().get(i).getActionType().equals("query")
                    && input.getCommands().get(i).getObjectType().equals("users")) {
                if (input.getCommands().get(i).getCriteria().equals("num_ratings")) {
                    Map<String, Integer> userRatings = UsersRatings.userRatings(users);
                    List<String> sortedRatings = query.sortVideos(userRatings, input.getCommands()
                            .get(i).getSortType());
                    sortedRatings = query.sortLimit(sortedRatings, input.getCommands().get(i)
                            .getNumber());
                    JSONObject obj = fileWriter.writeFile(input.getCommands().get(i).getActionId(),
                            "Query result: "
                            + sortedRatings);
                    arrayResult.add(obj);
                }
            }

            //RECOMANDARI

            // STANDARD
            if (input.getCommands().get(i).getActionType().equals("recommendation")
                    && input.getCommands().get(i).getType().equals("standard")) {
                for (int j = 0; j < users.size(); j++) {
                    if (users.get(j).getUsername().equals(input.getCommands().get(i)
                            .getUsername())) {
                        String firstUnseen = Standard.firstUnseenVideo(users.get(j), videos);
                        if (firstUnseen != null) {
                            JSONObject obj = fileWriter.writeFile(input.getCommands().get(i)
                                    .getActionId(), "StandardRecommendation result: "
                                    + firstUnseen);
                            arrayResult.add(obj);
                        } else {
                            JSONObject obj = fileWriter.writeFile(input.getCommands().get(i)
                                    .getActionId(),
                                    "StandardRecommendation cannot be applied!");
                            arrayResult.add(obj);
                        }
                    }
                }
            }

            // BEST UNSEEN
            if (input.getCommands().get(i).getActionType().equals("recommendation")
                    && input.getCommands().get(i).getType().equals("best_unseen")) {
                for (int j = 0; j < users.size(); j++) {
                    if (users.get(j).getUsername().equals(input.getCommands().get(i)
                            .getUsername())) {
                        Map<String, Double> ratedMovies =
                                BestUnseen.calculateMovieRatings(movies, users);
                        Map<String, Double> ratedShows =
                                BestUnseen.calculateShowRatings(serials, users);
                        Map<String, Double> ratedVideos = new HashMap<>();
                        ratedVideos.putAll(ratedMovies);
                        ratedVideos.putAll(ratedShows);
                        List<String> descRatedVideos = query.sortVideosDouble(ratedVideos,
                                "desc");
                        String bestUnseen = BestUnseen.bestUnseen(movies, descRatedVideos,
                                users.get(j));
                        if (bestUnseen != null) {
                            JSONObject obj = fileWriter.writeFile(input.getCommands().get(i)
                                    .getActionId(), "BestRatedUnseenRecommendation result: "
                                    + bestUnseen);
                            arrayResult.add(obj);
                        } else {
                            JSONObject obj = fileWriter.writeFile(input.getCommands().get(i)
                                    .getActionId(),
                                    "BestRatedUnseenRecommendation cannot be applied!");
                            arrayResult.add(obj);
                        }
                    }
                }
            }

            // PREMIUM SEARCH
            if (input.getCommands().get(i).getActionType().equals("recommendation")
                    && input.getCommands().get(i).getType().equals("search")) {
                for (int j = 0; j < users.size(); j++) {
                    if (users.get(j).getUsername().equals(input.getCommands().get(i)
                            .getUsername())) {
                        List<String> unseenVideos = Search.unseenVideos(movies, serials,
                                users.get(j), users, input.getCommands().get(i).getGenre());
                        if (!unseenVideos.isEmpty()) {
                            JSONObject obj = fileWriter.writeFile(input.getCommands().get(i)
                                    .getActionId(), "SearchRecommendation result: "
                                    + unseenVideos);
                            arrayResult.add(obj);
                        } else {
                            JSONObject obj = fileWriter.writeFile(input.getCommands().get(i)
                                    .getActionId(),
                                    "SearchRecommendation cannot be applied!");
                            arrayResult.add(obj);
                        }
                    }
                }
            }

            // PREMIUM FAVORITE
            if (input.getCommands().get(i).getActionType().equals("recommendation")
                    && input.getCommands().get(i).getType().equals("favorite")) {
                for (int j = 0; j < users.size(); j++) {
                    if (users.get(j).getUsername().equals(input.getCommands().get(i)
                            .getUsername())) {
                        Map<String, Long> favoriteMovies =
                                Favorite.contorfavoriteVideos(users, videos);
                        List<String> sortedFavVideos = Favorite.sortFavVideos(favoriteMovies);
                        String favVideo =
                                Favorite.verifyHistory(sortedFavVideos, users.get(j), videos);
                        if (favVideo != null) {
                            JSONObject obj = fileWriter.writeFile(input.getCommands().get(i)
                                    .getActionId(), "FavoriteRecommendation result: "
                                    + favVideo);
                            arrayResult.add(obj);

                        } else {
                            JSONObject obj = fileWriter.writeFile(input.getCommands().get(i)
                                    .getActionId(),
                                    "FavoriteRecommendation cannot be applied!");
                            arrayResult.add(obj);
                        }
                    }
                }
            }

            // PREMIUM POPULAR
            if (input.getCommands().get(i).getActionType().equals("recommendation")
                    && input.getCommands().get(i).getType().equals("popular")) {
                for (int j = 0; j < users.size(); j++) {
                    if (users.get(j).getUsername().equals(input.getCommands().get(i).getUsername())
                            && users.get(j).getSubscriptionType().equals("PREMIUM")) {
                        Map<String, Long> popularGenres =
                                Popular.contorPopularGenres(videos);
                        List<String> sortedPopularGenres =
                                Popular.sortPopularGenres(popularGenres);
                        String popularVideo = Popular.popularGenre(sortedPopularGenres,
                                videos, users.get(j));
                        if (popularVideo == null) {
                            JSONObject obj = fileWriter.writeFile(input.getCommands().get(i)
                                    .getActionId(),
                                    "PopularRecommendation cannot be applied!");
                            arrayResult.add(obj);
                        } else {
                            JSONObject obj = fileWriter.writeFile(input.getCommands().get(i)
                                    .getActionId(), "PopularRecommendation result: "
                                    + popularVideo);
                            arrayResult.add(obj);
                        }
                    } else if (users.get(j).getUsername().equals(input.getCommands().get(i)
                            .getUsername())
                            && users.get(j).getSubscriptionType().equals("BASIC")) {
                        JSONObject obj = fileWriter.writeFile(input.getCommands().get(i)
                                .getActionId(), "PopularRecommendation cannot be applied!");
                        arrayResult.add(obj);
                    }
                }
            }


        }
        fileWriter.closeJSON(arrayResult);
    }
}
