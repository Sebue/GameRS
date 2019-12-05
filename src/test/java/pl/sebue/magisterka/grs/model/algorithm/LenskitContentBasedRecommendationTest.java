package pl.sebue.magisterka.grs.model.algorithm;

import com.google.common.collect.Lists;
import org.grouplens.lenskit.RecommenderBuildException;
import org.junit.Ignore;
import org.junit.Test;
import pl.sebue.magisterka.grs.model.data.crossvalidation.CrossValidationProvider;

import java.util.List;

@Ignore
public class LenskitContentBasedRecommendationTest {
    @Test
    public void sth() {
        try {
            LenskitContentBasedRecommendation.predict();
        } catch (RecommenderBuildException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cv() {
        List<String> results = Lists.newArrayList();
        for (int j = 0; j < 10; j++) {
            System.out.println(j);
            CrossValidationProvider.cleanData();
            try {
                results.add(LenskitContentBasedRecommendation.predict());
            } catch (RecommenderBuildException e) {
                e.printStackTrace();
            }
        }
        for(String result : results){
            System.out.println(result);
        }
    }
}