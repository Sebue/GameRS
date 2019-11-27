package pl.sebue.magisterka.grs.model.data.crossvalidation;

import com.google.common.collect.Lists;
import org.hibernate.Session;
import pl.sebue.magisterka.grs.model.HibernateFactory;
import pl.sebue.magisterka.grs.model.data.dto.GameStatistic;

import java.util.*;
import java.util.stream.Collectors;

public class CrossValidationProvider {
    private static List<GameStatistic> trainData;
    private static List<GameStatistic> testData;

    public static void cleanData() {
        trainData = null;
        testData = null;
    }

    public static List<GameStatistic> getTrainData() {
        if (trainData == null) {
            Map<Integer, List<GameStatistic>> data = getSplittedAndRandomizedData();
            trainData = Lists.newArrayList();
            for (int i = 0; i < 9; i++) {
                trainData.addAll(data.get(i));
            }
            testData = data.get(9);
        }
        return trainData;
    }

    public static List<GameStatistic> getTestData() {
        if (trainData == null) {
            getTrainData();
        }
        return testData;
    }

    private static Map<Integer, List<GameStatistic>> getSplittedAndRandomizedData() {
        Session session = HibernateFactory.INSTANCE.getSessionFactory().openSession();
        List<GameStatistic> gameStatisticsOnDb = session.createQuery("from GameStatistic", GameStatistic.class).list();
        List<GameStatistic> gamesForRecommendation = narrowResults(gameStatisticsOnDb);
        Collections.shuffle(gamesForRecommendation);

        Map<Integer, List<GameStatistic>> datas = new HashMap<Integer, List<GameStatistic>>();
        for (int i = 0; i < 10; i++) {
            datas.put(i, Lists.newArrayList());
        }
        for (int i = 0; i < gamesForRecommendation.size(); i++) {
            datas.get(i % 10).add(gamesForRecommendation.get(i));
        }
        return datas;
    }

    private static List<GameStatistic> narrowResults(List<GameStatistic> gameStatisticsOnDb) {
        Set<Long> users = gameStatisticsOnDb.stream().map(GameStatistic::getUserId).collect(Collectors.toSet());
        List<GameStatistic> filtered = Lists.newArrayList();
        for (Long userId : users) {
            List<GameStatistic> count = gameStatisticsOnDb.stream().filter(gs -> userId.equals(gs.getUserId())).collect(Collectors.toList());
            if (hasMinimumGames(count.size())) {
                filtered.addAll(count);
            }
        }
        return filtered;
    }

    private static boolean hasMinimumGames(int count) {
        return count >= 5;
    }
}