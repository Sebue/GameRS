package pl.sebue.magisterka.grs.model.algorithm;

import com.google.common.collect.Lists;
import org.hibernate.Session;
import org.junit.Test;
import pl.sebue.magisterka.grs.model.HibernateFactory;
import pl.sebue.magisterka.grs.model.data.crossvalidation.SlopeCV;
import pl.sebue.magisterka.grs.model.data.dto.GameStatistic;

import java.util.List;
import java.util.Map;
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

        SlopeCV slopecv = new SlopeCV();
        Map<Integer, List<GameStatistic>> sth = slopecv.getSplittedSets();
        List<GameStatistic> dataToTrain = Lists.newArrayList();
        for (int i = 0; i < 9; i++) {
            dataToTrain.addAll(sth.get(i));
        }
        SlopeOneCollaborativeRecomendation.slopeOne(dataToTrain, Optional.of(sth.get(9)));
    }

}