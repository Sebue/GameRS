package pl.sebue.magisterka.grs.model.algorithm.contentbased;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import org.grouplens.lenskit.data.dao.ItemDAO;
import org.hibernate.Session;
import pl.sebue.magisterka.grs.model.HibernateFactory;
import pl.sebue.magisterka.grs.model.data.dto.Game;

import java.util.List;

public class GameDAO implements ItemDAO {
    private transient volatile LongSet itemIds;
    private transient volatile List<Game> gamesOnDb;

    public GameDAO() {

    }

    private void ensureTagCache() {
        if (itemIds == null) {
            loadCaches();
        }
    }

    private void loadCaches() {
        Session session = HibernateFactory.INSTANCE.getSessionFactory().openSession();
        gamesOnDb = session.createQuery("from Game", Game.class).list();
        Long2ObjectOpenHashMap<List<String>> tagCache = new Long2ObjectOpenHashMap<List<String>>();
        for (Game game : gamesOnDb) {
            tagCache.put(game.getGameId(), Lists.newArrayList());
        }
        itemIds = LongSets.unmodifiable(tagCache.keySet());
    }

    @Override
    public LongSet getItemIds() {
        ensureTagCache();
        return itemIds;
    }

    public List<Game> getItems(){
        ensureTagCache();
        return gamesOnDb;
    }
}
