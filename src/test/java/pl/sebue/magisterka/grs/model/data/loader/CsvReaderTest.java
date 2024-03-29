package pl.sebue.magisterka.grs.model.data.loader;

import org.hibernate.Session;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import pl.sebue.magisterka.grs.model.HibernateFactory;
import pl.sebue.magisterka.grs.model.data.dto.Game;
import pl.sebue.magisterka.grs.model.data.dto.GameStatistic;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Ignore
public class CsvReaderTest {
    private static CsvReader csvReader;
    @BeforeClass
    public static void setUp() {
        File gamesCsv = new File(CsvReaderTest.class.getClassLoader().getResource("games.csv").getFile());
        File gamesStatisticCsv = new File(CsvReaderTest.class.getClassLoader().getResource("players.csv").getFile());
        csvReader = new CsvReader(gamesCsv.getPath(), gamesStatisticCsv.getPath());
    }

    @Test
    public void shouldGatherAllGames() {
        var games = csvReader.gatherAllGames();

        assertEquals(13096, games.size());
    }

    @Test
    public void shouldGatherAllGameStatistics() {
        var games = csvReader.gatherAllGames();

        var gameStatisticList = csvReader.gatherAllGameStatistics(games);

        assertEquals(66977, gameStatisticList.size());
    }

    @Test
    public void shouldHasAllDataOnDb(){
        csvReader.gatherRecommendationInputData();
        Session session = HibernateFactory.INSTANCE.getSessionFactory().openSession();
        List<Game> gamesOnDb = session.createQuery("from Game", Game.class).list();
        List<GameStatistic> gameStatisticsOnDb = session.createQuery("from GameStatistic", GameStatistic.class).list();

        assertEquals(13096, gamesOnDb.size());
        assertEquals(66977, gameStatisticsOnDb.size());
    }
}
