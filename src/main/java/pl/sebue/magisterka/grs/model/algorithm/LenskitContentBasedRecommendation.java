package pl.sebue.magisterka.grs.model.algorithm;

import org.grouplens.lenskit.ItemRecommender;
import org.grouplens.lenskit.ItemScorer;
import org.grouplens.lenskit.Recommender;
import org.grouplens.lenskit.RecommenderBuildException;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.ItemDAO;
import org.grouplens.lenskit.data.dao.UserDAO;
import org.grouplens.lenskit.scored.ScoredId;
import org.hibernate.Session;
import pl.sebue.magisterka.grs.model.HibernateFactory;
import pl.sebue.magisterka.grs.model.algorithm.contentbased.GameDAO;
import pl.sebue.magisterka.grs.model.algorithm.contentbased.GameRatingDAO;
import pl.sebue.magisterka.grs.model.data.dto.GameStatistic;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LenskitContentBasedRecommendation {
    private static final Logger logger = Logger.getLogger(LenskitContentBasedRecommendation.class.getSimpleName());

    public static void sth() throws RecommenderBuildException {
        LenskitConfiguration config = configureRecommender();

        logger.info("building recommender");
        Recommender rec = LenskitRecommender.build(config);

        // we automatically get a useful recommender since we have a scorer
        ItemRecommender irec = rec.getItemRecommender();

        Session session = HibernateFactory.INSTANCE.getSessionFactory().openSession();
        List<GameStatistic> gameStatisticList = session.createQuery("from GameStatistic", GameStatistic.class).list();
        Set<Long> userIds = gameStatisticList.stream().map(GameStatistic::getUserId).collect(Collectors.toSet());

        // Generate 5 recommendations for each user
        for (Long uid : userIds) {
            logger.info("searching for recommendations for user " + uid);
            List<ScoredId> recs = irec.recommend(uid, 5);
            if (recs.isEmpty()) {
                logger.warning("no recommendations for user " + uid + ", do they exist?");
            }
            System.out.format("recommendations for user %d:\n", uid);
            for (ScoredId id : recs) {
                System.out.format("  %d: %.4f\n", id.getId(), id.getScore());
            }
        }
    }

    // LensKit configuration API generates some unchecked warnings, turn them off
    @SuppressWarnings("unchecked")
    private static LenskitConfiguration configureRecommender() {
        LenskitConfiguration config = new LenskitConfiguration();
        config.bind(EventDAO.class)
                .to(GameRatingDAO.class);

        config.bind(ItemDAO.class)
                .to(GameDAO.class);

        config.bind(UserDAO.class)
                .to(pl.sebue.magisterka.grs.model.algorithm.contentbased.UserDAO.class);

        config.bind(ItemScorer.class)
                .to(pl.sebue.magisterka.grs.model.algorithm.contentbased.ItemScorer.class);
        return config;
    }
}