package pl.sebue.magisterka.grs.model.algorithm.contentbased;

import com.google.common.collect.Lists;
import org.grouplens.lenskit.cursors.Cursor;
import org.grouplens.lenskit.cursors.Cursors;
import org.grouplens.lenskit.data.dao.EventCollectionDAO;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.SortOrder;
import org.grouplens.lenskit.data.event.Event;
import org.grouplens.lenskit.data.event.Rating;
import org.grouplens.lenskit.data.event.RatingBuilder;
import org.hibernate.Session;
import pl.sebue.magisterka.grs.model.HibernateFactory;
import pl.sebue.magisterka.grs.model.data.dto.GameStatistic;

import java.util.List;

public class GameRatingDAO implements EventDAO {
    private transient volatile EventCollectionDAO cache;

    public GameRatingDAO() {
    }

    private void ensureRatingCache() {
        if (cache == null) {
            Session session = HibernateFactory.INSTANCE.getSessionFactory().openSession();
            List<GameStatistic> gameStatisticList = session.createQuery("from GameStatistic", GameStatistic.class).list();
//            List<GameStatistic> gameStatisticList = CrossValidationProvider.getTrainData();
            List<Rating> ratings = Lists.newArrayList();
            for (GameStatistic gs : gameStatisticList) {
                Rating rating = new RatingBuilder()
                        .setItemId(gs.getGame().getGameId())
                        .setUserId(gs.getUserId())
                        .setRating(gs.getPlayedHours())
                        .build();
                ratings.add(rating);
            }

            cache = new EventCollectionDAO(Cursors.makeList(new OwnCursor(ratings)));
        }
    }

    @Override
    public Cursor<Event> streamEvents() {
        ensureRatingCache();
        return cache.streamEvents();
    }

    @Override
    public <E extends Event> Cursor<E> streamEvents(Class<E> type) {
        ensureRatingCache();
        return cache.streamEvents(type);
    }

    @Override
    public <E extends Event> Cursor<E> streamEvents(Class<E> type, SortOrder sortOrder) {
        ensureRatingCache();
        return cache.streamEvents(type, sortOrder);
    }
}
