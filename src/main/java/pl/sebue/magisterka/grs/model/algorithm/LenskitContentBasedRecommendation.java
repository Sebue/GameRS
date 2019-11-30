package pl.sebue.magisterka.grs.model.algorithm;

import org.grouplens.lenskit.ItemRecommender;
import org.grouplens.lenskit.ItemScorer;
import org.grouplens.lenskit.Recommender;
import org.grouplens.lenskit.RecommenderBuildException;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.ItemDAO;
import org.grouplens.lenskit.scored.ScoredId;
import pl.sebue.magisterka.grs.model.algorithm.contentbased.GameDAO;
import pl.sebue.magisterka.grs.model.algorithm.contentbased.GameRatingDAO;
import pl.sebue.magisterka.grs.model.data.crossvalidation.CrossValidationProvider;
import pl.sebue.magisterka.grs.model.data.dto.GameStatistic;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LenskitContentBasedRecommendation {
    private static final Logger logger = Logger.getLogger(LenskitContentBasedRecommendation.class.getSimpleName());

    public static String predict() throws RecommenderBuildException {
        LenskitConfiguration config = configureRecommender();

        logger.info("building recommender");
        Recommender rec = LenskitRecommender.build(config);

        // we automatically get a useful recommender since we have a scorer
        ItemRecommender irec = rec.getItemRecommender();

        Set<Long> userIds = CrossValidationProvider.getTrainData().stream().map(GameStatistic::getUserId).collect(Collectors.toSet());

        List<GameStatistic> testData = CrossValidationProvider.getTestData();

        int allTestCount = 0;
        int correctCount = 0;
        for (Long uid : userIds) {
//            logger.info("searching for recommendations for user " + uid);
//            List<ScoredId> recs = irec.recommend(uid, 10);
//            if (recs.isEmpty()) {
//                logger.warning("no recommendations for user " + uid + ", do they exist?");
//            }
            Float max = testData.stream()
                    .filter(gs -> gs.getUserId().equals(uid))
                    .map(gs -> gs.getPlayedHours())
                    .max(Float::compare)
                    .orElse(0.0f);
            Set<Long> gameIds = testData.stream()
                    .filter(gs -> gs.getUserId().equals(uid))
                    .filter(gs -> gs.getPlayedHours() / max >= 0.5f)
                    .map(gs -> gs.getGame().getGameId()).collect(Collectors.toSet());
            List<ScoredId> recs = irec.recommend(uid, gameIds);
            allTestCount += gameIds.size();
            correctCount += recs.stream().filter(recommendation -> recommendation.getScore() > 0).filter(recommendation -> gameIds.contains(recommendation.getId())).count();
//            System.out.format("recommendations for user %d:\n", uid);
//            for (ScoredId id : recs) {
//                System.out.format("  %d: %.4f\n", id.getId(), id.getScore());
//            }
        }

        logger.info("Success: " + correctCount + ", all tests: " + allTestCount + " and prediction rate: " + (correctCount / (float) allTestCount) * 100 + "%");
        return (correctCount/ (float) allTestCount) * 100 + "%";
    }

    // LensKit configuration API generates some unchecked warnings, turn them off
    @SuppressWarnings("unchecked")
    private static LenskitConfiguration configureRecommender() {
        LenskitConfiguration config = new LenskitConfiguration();
        config.bind(EventDAO.class)
                .to(GameRatingDAO.class);

        config.bind(ItemDAO.class)
                .to(GameDAO.class);

        config.bind(ItemScorer.class)
                .to(pl.sebue.magisterka.grs.model.algorithm.contentbased.ItemScorer.class);
        return config;
    }
}