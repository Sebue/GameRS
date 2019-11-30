package pl.sebue.magisterka.grs.model.algorithm.contentbased;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.longs.LongSet;
import org.grouplens.lenskit.core.Transient;
import org.grouplens.lenskit.util.TopNScoredItemAccumulator;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import pl.sebue.magisterka.grs.model.data.dto.Game;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CBFModelBuilder implements Provider<CBModel> {
    private final GameDAO dao;

    @Inject
    public CBFModelBuilder(@Transient GameDAO dao) {
        this.dao = dao;
    }

    @Override
    public CBModel get() {
        // Get the transposed rating matrix
        // This gives us a map of item IDs to those items' rating vectors
        Map<Long, Map<Integer, Double>> itemVectors = getItemVectors();

        LongSet games = dao.getItemIds();
        // Map items to vectors of item similarities
        Map<Long, SparseVector> itemSimilarities = new HashMap<Long, SparseVector>();
//        Map<Long, List<ScoredId>> neighborhoods = new HashMap<Long, List<ScoredId>>();


        // Computing the similarities between each pair of items
        for (Iterator outerIter = games.iterator(); outerIter.hasNext(); ) {
            Long gameId = (Long) outerIter.next();

            TopNScoredItemAccumulator accumulator = new TopNScoredItemAccumulator(games.size() - 1);

            Map<Integer, Double> gameData = itemVectors.get(gameId);
            // Calculate similiarity with other item one by one and
            for (Iterator innerIter = games.iterator(); innerIter.hasNext(); ) {
                Long comparingGameId = (Long) innerIter.next();
                if (gameId.equals(comparingGameId)) continue;

                Map<Integer, Double> comparingGameData = itemVectors.get(comparingGameId);
                double similarity = 1.0 - (Math.abs(gameData.get(0) - comparingGameData.get(0)) / 100.0);   //metacritic
                similarity += 1.0 - (Math.abs(gameData.get(1) - comparingGameData.get(1)));                 //controllerSupport
                similarity += 1.0 - (Math.abs(gameData.get(2) - comparingGameData.get(2)) / 20.0);         //releaseYear
                similarity += 1.0 - (Math.abs(gameData.get(9) - comparingGameData.get(9)));         //requiredAge
                similarity += 1.0 - (Math.abs(gameData.get(10) - comparingGameData.get(10)));               //demoVersion

                similarity += 1.0 - (Math.abs(gameData.get(3) - comparingGameData.get(3)));                 //ownerCount
                similarity += 1.0 - (Math.abs(gameData.get(4) - comparingGameData.get(4)));         //achievementCount
                similarity += 1.0 - (Math.abs(gameData.get(5) - comparingGameData.get(5)));         //dlcCount
                similarity += 1.0 - (Math.abs(gameData.get(6) - comparingGameData.get(6)));         //initialPrice
                similarity += 1.0 - (Math.abs(gameData.get(7) - comparingGameData.get(7)));         //finalPrice
                similarity += 1.0 - (Math.abs(gameData.get(8) - comparingGameData.get(8)));         //recommendationCount
                similarity = similarity / gameData.size();

//                similarity = similarity / 5;
//                if(similarity < 0.5) {
//                    System.out.println(similarity);
//                }

                //accumulate
                if (similarity > 0) {
                    accumulator.put(comparingGameId, similarity);
                }
            }
            MutableSparseVector vectorEntries = accumulator.finishVector();
            itemSimilarities.put(gameId, vectorEntries);
        }
        return new CBModel(Lists.newArrayList(itemVectors.keySet()), itemSimilarities);
    }

    private Map<Long, Map<Integer, Double>> getItemVectors() {
        List<Game> games = dao.getItems();
        Map<Long, Map<Integer, Double>> itemVectors = new HashMap<Long, Map<Integer, Double>>();
        for (Game game : games) {
            Map<Integer, Double> result = Maps.newHashMap();
            result.put(0, (double) game.getMetacriticScore());
            result.put(1, game.isControllerSupported() ? 1.0 : 0.0);
            result.put(2, (double) game.getReleaseYear());
            result.put(3, (double) game.getOwnerCount() / 90687580.0);
            result.put(4, (double) game.getAchievementCount() / 1629.0);
            result.put(5, (double) game.getDlcCount() / 630.0);
            result.put(6, (double) game.getInitialPrice() / 449.99);
            result.put(7, (double) game.getFinalPrice() / 499.99);
            result.put(8, (double) game.getRecommendationCount() / 1427633.0);
            result.put(9, (double) game.getRequiredAge() / 18.0);
            result.put(10, game.isHasDemoVersion() ? 1.0 : 0.0);
            itemVectors.put(game.getGameId(), result);
        }
        return itemVectors;
    }
}
