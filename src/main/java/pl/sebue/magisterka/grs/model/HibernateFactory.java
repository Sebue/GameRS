package pl.sebue.magisterka.grs.model;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import pl.sebue.magisterka.grs.model.data.dto.Game;
import pl.sebue.magisterka.grs.model.data.dto.GameStatistic;

public enum HibernateFactory {
    INSTANCE;

    public SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(Game.class);
        configuration.addAnnotatedClass(GameStatistic.class);

        StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration.buildSessionFactory(registryBuilder.build());
        return sessionFactory;
    }
}
