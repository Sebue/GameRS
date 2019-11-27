package pl.sebue.magisterka.grs.model.algorithm;

import com.google.common.collect.Lists;
import org.hibernate.Session;
import org.junit.Test;
import pl.sebue.magisterka.grs.model.HibernateFactory;
import pl.sebue.magisterka.grs.model.data.crossvalidation.CrossValidationProvider;
import pl.sebue.magisterka.grs.model.data.dto.GameStatistic;

import java.util.List;
import java.util.Optional;

public class SlopeOneCollaborativeRecomendationTest {

    @Test
    public void sth() {
        Session session = HibernateFactory.INSTANCE.getSessionFactory().openSession();
        List<GameStatistic> gameStatisticsOnDb = session.createQuery("from GameStatistic", GameStatistic.class).list();
        SlopeOneCollaborativeRecomendation.slopeOne(gameStatisticsOnDb, Optional.empty());
    }

    @Test
    public void cv() {
        List<String> results = Lists.newArrayList();
        for (int j = 0; j < 10; j++) {
            List<GameStatistic> sth = CrossValidationProvider.getTrainData();
            results.add(SlopeOneCollaborativeRecomendation.slopeOne(sth, Optional.of(CrossValidationProvider.getTestData())));
        }
        for(String result : results){
            System.out.println(result);
        }
    }

}