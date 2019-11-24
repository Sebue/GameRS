package pl.sebue.magisterka.grs.model.algorithm.contentbased;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import org.grouplens.lenskit.data.dao.ItemDAO;
import org.hibernate.Session;
import pl.sebue.magisterka.grs.model.HibernateFactory;
import pl.sebue.magisterka.grs.model.data.dto.Game;

import java.util.List;
import java.util.Set;

public class GameDAO implements ItemDAO {
    private transient volatile Long2ObjectMap<List<String>> tagCache;
    private transient volatile Set<String> vocabCache;
    private transient volatile List<Game> gamesOnDb;

    public GameDAO() {

    }

    private void ensureTagCache() {
        if (tagCache == null) {
            loadCaches();
        }
    }

    private void loadCaches() {
        Session session = HibernateFactory.INSTANCE.getSessionFactory().openSession();
        gamesOnDb = session.createQuery("from Game", Game.class).list();
        Long2ObjectMap<List<String>> cacheTags = new Long2ObjectOpenHashMap<List<String>>();
        vocabCache = Sets.newHashSet();
        for (Game game : gamesOnDb) {
            cacheTags.put(game.getGameId(), Lists.newArrayList(game.getMetacriticScore() + ""));
            vocabCache.add(game.getMetacriticScore() + "");
        }
        tagCache = cacheTags;
    }

    @Override
    public LongSet getItemIds() {
        ensureTagCache();
        return LongSets.unmodifiable(tagCache.keySet());
    }


    public List<String> getItemTags(long item) {
        ensureTagCache();
        return tagCache.get(item);
    }

    public Set<String> getTagVocabulary() {
        ensureTagCache();
        return vocabCache;
    }

    public List<Game> getItems(){
        ensureTagCache();
        return gamesOnDb;
    }
}
