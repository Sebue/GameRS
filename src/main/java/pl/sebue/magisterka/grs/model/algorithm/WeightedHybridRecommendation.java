package pl.sebue.magisterka.grs.model.algorithm;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.sebue.magisterka.grs.model.HibernateFactory;
import pl.sebue.magisterka.grs.model.data.dto.Recommendation;
import pl.sebue.magisterka.grs.model.data.dto.RecommendationType;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class WeightedHybridRecommendation {
    private static float COLLABORATIVE_MULTIPLY = 0.2f;
    private static float CONTENT_BASED_MULTIPLY = 0.8f;

    public void predict() {
        Session hibernateSession = HibernateFactory.INSTANCE.getSessionFactory().openSession();
        List<Recommendation> recommendationList = hibernateSession.createQuery("from Recommendation", Recommendation.class).list();
        Map<Long, List<Recommendation>> userRecommendationMap = Maps.newHashMap();

        for (Recommendation r : recommendationList) {
            if (userRecommendationMap.containsKey(r.getUserId())) {
                userRecommendationMap.get(r.getUserId()).add(r);
            } else {
                userRecommendationMap.put(r.getUserId(), Lists.newArrayList(r));
            }
        }

        Transaction transaction = hibernateSession.beginTransaction();
        for (var userRecommendation : userRecommendationMap.entrySet()) {
            List<Recommendation> hybridRecommendations = getHybridRecommendations(userRecommendation.getValue());
            for(int i = 0; i < 10; i++){
                hibernateSession.save(hybridRecommendations.get(i));
            }
        }
        transaction.commit();
    }

    private List<Recommendation> getHybridRecommendations(List<Recommendation> anotherAlghoritmsRecommendations) {
        List<Recommendation> results = Lists.newArrayList();
        Set<Long> gameIds = anotherAlghoritmsRecommendations.stream()
                .map(r -> r.getGame().getGameId())
                .collect(Collectors.toSet());

        for (Long gameId : gameIds) {
            List<Recommendation> oneGameRecommendation = anotherAlghoritmsRecommendations.stream()
                    .filter(r -> gameId.equals(r.getGame().getGameId()))
                    .collect(Collectors.toList());

            float probability = 0.0f;
            for (Recommendation r : oneGameRecommendation) {
                probability += r.getRecommendationType().equals(RecommendationType.COLLABORATIVE)
                        ? r.getProbability() * COLLABORATIVE_MULTIPLY
                        : r.getProbability() * CONTENT_BASED_MULTIPLY;
            }

            Recommendation r = oneGameRecommendation.get(0);
            Recommendation hybridRecommendation = new Recommendation(r.getUserId(), r.getGame(), probability, RecommendationType.HYBRID);
            results.add(hybridRecommendation);
        }

        Collections.sort(results, (r1, r2) -> r1.getProbability() > r2.getProbability() ? 1 : -1);
        return results;
    }
}
