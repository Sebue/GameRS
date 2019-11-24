package pl.sebue.magisterka.grs.model.algorithm;

import org.grouplens.lenskit.RecommenderBuildException;
import org.junit.Test;

public class LenskitContentBasedRecommendationTest {
    @Test
    public void sth() {
        try {
            LenskitContentBasedRecommendation.sth();
        } catch (RecommenderBuildException e) {
            e.printStackTrace();
        }
    }
}