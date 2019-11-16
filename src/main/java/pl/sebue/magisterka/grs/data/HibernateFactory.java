package pl.sebue.magisterka.grs.data;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import pl.sebue.magisterka.grs.data.dto.Game;
import pl.sebue.magisterka.grs.data.dto.GameStatistic;

public class HibernateFactory {

    public SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(Game.class);
        configuration.addAnnotatedClass(GameStatistic.class);
        StandardServiceRegistryBuilder registryBuilder =
                new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration.buildSessionFactory(registryBuilder.build());
        return sessionFactory;
    }
}
