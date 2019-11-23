package pl.sebue.magisterka.grs.model.algorithm;

import com.google.common.base.Throwables;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import java.io.File;
import java.util.List;

public class MahoutUserBasedRecommendation {
    //TODO extract interface

    public void sth() {
        try {
            DataModel model = new FileDataModel(new File(MahoutUserBasedRecommendation.class.getClassLoader().getResource("gameFromDb.csv").getFile()));
            ItemSimilarity similarity = new TanimotoCoefficientSimilarity(model);
            ItemBasedRecommender irecommender = new GenericItemBasedRecommender(model, similarity);
            List<RecommendedItem> irecommendations = irecommender.mostSimilarItems(1l, 5);

            // The First argument is the userID and the Second parameter is 'HOW MANY'
//            List<RecommendedItem> recommendations = recommender.recommend(2, 2);

            for (RecommendedItem recommendation : irecommendations) {
                System.out.println(recommendation);
            }
        } catch (Exception e) {
            System.out.println(Throwables.getStackTraceAsString(e));
        }
    }
}
