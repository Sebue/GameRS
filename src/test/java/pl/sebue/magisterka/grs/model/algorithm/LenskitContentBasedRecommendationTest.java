package pl.sebue.magisterka.grs.model.algorithm;

import com.google.common.collect.Lists;
import org.grouplens.lenskit.RecommenderBuildException;
import org.junit.Test;
import pl.sebue.magisterka.grs.model.data.crossvalidation.CrossValidationProvider;
import pl.sebue.magisterka.grs.model.data.dto.GameStatistic;

import java.util.List;
import java.util.Optional;

public class LenskitContentBasedRecommendationTest {
    @Test
    public void sth() {
        try {
            LenskitContentBasedRecommendation.sth();
        } catch (RecommenderBuildException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cv() {
        List<String> results = Lists.newArrayList();
        for (int j = 0; j < 10; j++) {
            CrossValidationProvider.cleanData();
            List<GameStatistic> sth = CrossValidationProvider.getTrainData();
            results.add(SlopeOneCollaborativeRecomendation.slopeOne(sth, Optional.of(CrossValidationProvider.getTestData())));
        }
        for(String result : results){
            System.out.println(result);
        }
    }
}