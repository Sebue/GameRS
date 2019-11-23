package pl.sebue.magisterka.grs.model.data.crossvalidation;

import com.google.common.collect.Lists;
import org.hibernate.Session;
import pl.sebue.magisterka.grs.model.HibernateFactory;
import pl.sebue.magisterka.grs.model.data.dto.GameStatistic;

import java.util.*;
import java.util.stream.Collectors;

public class SlopeCV {

    public Map<Integer, List<GameStatistic>> sth() {
        Session session = HibernateFactory.INSTANCE.getSessionFactory().openSession();
        List<GameStatistic> gameStatisticsOnDb = session.createQuery("from GameStatistic", GameStatistic.class).list();
        gameStatisticsOnDb = si(gameStatisticsOnDb); //toremove
        Collections.shuffle(gameStatisticsOnDb);
        Map<Integer, List<GameStatistic>> datas = new HashMap<Integer, List<GameStatistic>>();
        for (int i = 0; i < 10; i++) {
            datas.put(i, Lists.newArrayList());
        }
        for (int i = 0; i < gameStatisticsOnDb.size(); i++) {
            datas.get(i % 10).add(gameStatisticsOnDb.get(i));
        }
        return datas;
    }

    private List<GameStatistic> si(List<GameStatistic> gameStatisticsOnDb) {
        Set<Long> users = gameStatisticsOnDb.stream().map(GameStatistic::getUserId).collect(Collectors.toSet());
        List<GameStatistic> filtered = Lists.newArrayList();
        for (Long userId : users) {
            List<GameStatistic> count = gameStatisticsOnDb.stream().filter(gs -> userId.equals(gs.getUserId())).collect(Collectors.toList());
            if(count.size() > 4){
                filtered.addAll(count);
            }
        }
        return filtered;
    }
}
