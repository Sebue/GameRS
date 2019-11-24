package pl.sebue.magisterka.grs.model.algorithm.contentbased;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.longs.LongSet;
import org.grouplens.lenskit.core.Transient;
import org.grouplens.lenskit.util.TopNScoredItemAccumulator;
import org.grouplens.lenskit.vectors.ImmutableSparseVector;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import pl.sebue.magisterka.grs.model.data.dto.Game;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

public class CBFModelBuilder implements Provider<CBModel> {
    private final GameDAO dao;

    @Inject
    public CBFModelBuilder(@Transient GameDAO dao) {
        this.dao = dao;
    }

    private Map<String, Long> buildTagIdMap() {
        Set<String> tags = dao.getTagVocabulary();
        Map<String, Long> tagIds = Maps.newHashMap();

        // Map each tag to a new number.
        for (String tag : tags) {
            tagIds.put(tag, tagIds.size() + 1L);
        }
        return tagIds;
    }

    @Override
    public CBModel get() {
        // Get the transposed rating matrix
        // This gives us a map of item IDs to those items' rating vectors
        Map<Long, ImmutableSparseVector> itemVectors = getItemVectors();

        LongSet games = dao.getItemIds();
        // Map items to vectors of item similarities
        Map<Long,SparseVector> itemSimilarities = new HashMap<Long, SparseVector>();
//        Map<Long, List<ScoredId>> neighborhoods = new HashMap<Long, List<ScoredId>>();


        // Computing the similarities between each pair of items
        for (Iterator outerIter = games.iterator(); outerIter.hasNext(); ) {
            Long gameId = (Long) outerIter.next();

            TopNScoredItemAccumulator accumulator = new TopNScoredItemAccumulator(games.size() - 1);

            // Calculate similiarity with other item one by one and
            for (Iterator innerIter = games.iterator(); innerIter.hasNext(); ) {
                Long comparingGameId = (Long) innerIter.next();
                if (gameId.equals(comparingGameId)) continue;

                // cosine similarity
                double similarity = 1.0 - (Math.abs(itemVectors.get(gameId).get(1l) - itemVectors.get(comparingGameId).get(1l)) /100.0);

                //accumulate
                if (similarity > 0) {
                    accumulator.put(comparingGameId, similarity);
                }
            }
            MutableSparseVector vectorEntries = accumulator.finishVector();
            itemSimilarities.put(gameId, vectorEntries);
//            List<ScoredId> similarities = accumulator.finish();
//            neighborhoods.put(gameId, similarities);
        }
        return new CBModel(Lists.newArrayList(itemVectors.keySet()), itemSimilarities);
//        return new CBModel(buildTagIdMap(), neighborhoods);
    }

    private Map<Long, ImmutableSparseVector> getItemVectors() {
        List<Game> games = dao.getItems();
        Map<Long, ImmutableSparseVector> itemVectors = new HashMap<Long, ImmutableSparseVector>();
        for (Game game : games) {
            Map<Long, Double> result = Maps.newHashMap();
            result.put(1l, (double)game.getMetacriticScore());
//            result.put(2l, game.isControllerSupported() ? 1.0 : 0.0);
            MutableSparseVector vec = MutableSparseVector.create(result);
            itemVectors.put(game.getGameId(), vec.immutable());
        }
        return itemVectors;
    }
}
