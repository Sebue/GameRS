package pl.sebue.magisterka.grs.model.algorithm;

import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.sebue.magisterka.grs.model.HibernateFactory;
import pl.sebue.magisterka.grs.model.data.dto.Game;
import pl.sebue.magisterka.grs.model.data.dto.GameStatistic;
import pl.sebue.magisterka.grs.model.data.dto.Recommendation;
import pl.sebue.magisterka.grs.model.data.dto.RecommendationType;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;

public class SlopeOneCollaborativeRecomendation {
    // https://github.com/eugenp/tutorials/blob/master/algorithms-miscellaneous-2/src/main/java/com/baeldung/algorithms/slope_one/SlopeOne.java
    // https://www.baeldung.com/java-collaborative-filtering-recommendations

    private static final Logger logger = Logger.getLogger(SlopeOneCollaborativeRecomendation.class.getSimpleName());

    private static Map<Game, Map<Game, Integer>> differences = new HashMap<>();
    private static Map<Game, Map<Game, Integer>> frequencies = new HashMap<>();

    public static String slopeOne(List<GameStatistic> gameStatistics, Optional<List<GameStatistic>> dataToPredict) {
        differences = new HashMap<>();
        frequencies = new HashMap<>();
        Map<Long, Map<Game, Integer>> inputData = getInputData(gameStatistics); //userId, game, rating

        buildDifferencesMatrix(inputData);

        return predict(inputData, dataToPredict);
    }

    private static Map<Long, Map<Game, Integer>> getInputData(List<GameStatistic> gameStatistics) {
        Map<Long, Map<Game, Integer>> inputData = new HashMap<>();
        Set<Long> userIds = gameStatistics.stream().map(GameStatistic::getUserId).collect(Collectors.toSet());
        for (Long userId : userIds) {
            Map<Game, Integer> gamesWithPlayedHours = getGamesWithPlayedHoursForSpecificUser(gameStatistics, userId);
            inputData.put(userId, gamesWithPlayedHours);
        }
        return inputData;
    }

    private static Map<Game, Integer> getGamesWithPlayedHoursForSpecificUser(List<GameStatistic> gameStatistics, Long userId) {
        float maxValue = highestPlayedHours(gameStatistics, userId);
        return gameStatistics.stream()
                .filter(gs -> gs.getUserId().equals(userId))
                .collect(Collectors.toMap(GameStatistic::getGame, gs -> normalizeRating(maxValue, gs.getPlayedHours()), (v1, v2) -> {
                    return v1 > v2 ? v1 : v2;
                }));
    }

    private static float highestPlayedHours(List<GameStatistic> gameStatisticsOnDb, Long userId) {
        return gameStatisticsOnDb.stream()
                .filter(gs -> gs.getUserId().equals(userId))
                .map(GameStatistic::getPlayedHours)
                .max(Float::compare)
                .orElse(0.0f);
    }

    private static int normalizeRating(float maxValue, float actualValue) {
        float result = (actualValue / maxValue) * 5.0f;
        return Math.round(result);
    }

    /**
     * Based on the available data, calculate the relationships between the
     * items and number of occurences
     */
    private static void buildDifferencesMatrix(Map<Long, Map<Game, Integer>> data) {
        for (Map<Game, Integer> gameStatistics : data.values()) {
            for (Map.Entry<Game, Integer> gameWithRating : gameStatistics.entrySet()) {
                populateMapsIfNotExistsYet(gameWithRating);

                for (Map.Entry<Game, Integer> comparingGame : gameStatistics.entrySet()) {
                    int oldCount = 0;
                    if (frequencies.get(gameWithRating.getKey()).containsKey(comparingGame.getKey())) {
                        oldCount = frequencies.get(gameWithRating.getKey()).get(comparingGame.getKey());
                    }
                    int oldDiff = 0;
                    if (differences.get(gameWithRating.getKey()).containsKey(comparingGame.getKey())) {
                        oldDiff = differences.get(gameWithRating.getKey()).get(comparingGame.getKey());
                    }
                    int currentDiff = gameWithRating.getValue() - comparingGame.getValue();
                    frequencies.get(gameWithRating.getKey()).put(comparingGame.getKey(), oldCount + 1);
                    differences.get(gameWithRating.getKey()).put(comparingGame.getKey(), oldDiff + currentDiff);
                }
            }
        }

        normalizeDifferences();
    }

    private static void normalizeDifferences() {
        for (Game currentGame : differences.keySet()) {
            for (Game comparedGame : differences.get(currentGame).keySet()) {
                int oldValue = differences.get(currentGame).get(comparedGame);
                int count = frequencies.get(currentGame).get(comparedGame);
                differences.get(currentGame).put(comparedGame, divide(oldValue, count));
            }
        }
    }

    private static void populateMapsIfNotExistsYet(Map.Entry<Game, Integer> e) {
        if (!differences.containsKey(e.getKey())) {
            differences.put(e.getKey(), new HashMap<Game, Integer>());
            frequencies.put(e.getKey(), new HashMap<Game, Integer>());
        }
    }

    /**
     * Based on existing data predict all missing ratings. If prediction is not
     * possible, the value will be equal to -1
     */
    private static String predict(Map<Long, Map<Game, Integer>> inputData, Optional<List<GameStatistic>> testSet) {
        Session session = HibernateFactory.INSTANCE.getSessionFactory().openSession();

        HashMap<Game, Integer> userPredictions = new HashMap<Game, Integer>();
        HashMap<Game, Integer> userFrequencies = new HashMap<Game, Integer>();
        for (Game j : differences.keySet()) {
            userFrequencies.put(j, 0);
            userPredictions.put(j, 0);
        }


        int allTestCount = 0;
        int correctCount = 0;
        for (Map.Entry<Long, Map<Game, Integer>> userWithGameAndRating : inputData.entrySet()) {

            for (Game ratedByUserGame : userWithGameAndRating.getValue().keySet()) {
                for (Game gameToPredict : differences.keySet()) {
                    try {
                        int predictedValue = differences.get(gameToPredict).get(ratedByUserGame) // differences between games (0 is the best)
                                + userWithGameAndRating.getValue().get(ratedByUserGame); // rating for that game from this users
                        int finalValue = predictedValue * frequencies.get(gameToPredict).get(ratedByUserGame);
                        userPredictions.put(gameToPredict, userPredictions.get(gameToPredict) + finalValue);
                        userFrequencies.put(gameToPredict, userFrequencies.get(gameToPredict) + frequencies.get(gameToPredict).get(ratedByUserGame));
                    } catch (NullPointerException e1) {
                    }
                }
            }

            Map<Game, Integer> clean = new HashMap<Game, Integer>();
            for (Game j : userPredictions.keySet()) {
                if (userFrequencies.get(j) > 0) {
                    clean.put(j, divide(userPredictions.get(j), userFrequencies.get(j)));
                }
            }

            if (testSet.isPresent()) {
                float maxValue = highestPlayedHours(testSet.get(), userWithGameAndRating.getKey());
                List<GameStatistic> gameForUser = testSet.get().stream().filter(gs -> gs.getUserId().equals(userWithGameAndRating.getKey())).collect(Collectors.toList());
                for (GameStatistic gs : gameForUser) {
                    Game game = gs.getGame();
                    if (clean.containsKey(game)) {
                        Integer prediction = clean.get(game);
                        int actualValue = normalizeRating(maxValue, gs.getPlayedHours());
                        float accuracy = actualValue != 0 ? prediction / (float) actualValue : 1.0f;
//                        System.out.println("UserId: " + gs.getUserId() + "; playedHours: " + gs.getPlayedHours());
//                        System.out.println("Accuracy: " + accuracy + "; prediction: " + prediction + "; actualValue: " + actualValue);
                        if (accuracy < 1.1f && accuracy > 0.9f) {
                            correctCount++;
                        }
                        allTestCount++;
                    }
                }
            } else {
                LinkedHashMap<Game, Integer> sorted = clean.entrySet().stream()
                        .sorted(Collections.reverseOrder(comparingByValue()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
                Transaction transaction = session.beginTransaction();
                int i = 0;
                for (Map.Entry<Game, Integer> sth : sorted.entrySet()) {
                    Recommendation r = new Recommendation(userWithGameAndRating.getKey(), sth.getKey(), sth.getValue(), RecommendationType.COLLABORATIVE);
                    session.save(r);
                    if (++i > 9) {
                        break;
                    }
                }
                transaction.commit();
            }
        }
        logger.info("Success: " + correctCount + ", all tests: " + allTestCount + " and prediction rate: " + (correctCount/ (float) allTestCount) * 100 + "%");
        return (correctCount/ (float) allTestCount) * 100 + "%";
    }

    private static Integer divide(int a, int b){
        return Math.round(a / (float) b);
    }
}
