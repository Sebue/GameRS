package pl.sebue.magisterka.grs.data.loader;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.sebue.magisterka.grs.data.HibernateFactory;
import pl.sebue.magisterka.grs.data.dto.Game;
import pl.sebue.magisterka.grs.data.dto.GameStatistic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

public class CsvReader {
    private static final Logger logger = Logger.getLogger(CsvReader.class.getSimpleName());
    private final String gamesDataFilePath;
    private final String gamesStatisticDataFilePath;

    public CsvReader(String gamesDataFilePath, String gamesStatisticDataFilePath) {
        this.gamesDataFilePath = gamesDataFilePath;
        this.gamesStatisticDataFilePath = gamesStatisticDataFilePath;
    }

    public void gatherRecommendationInputData() {
        List<Game> games = gatherAllGames();
        List<GameStatistic> gameStatisticList = gatherAllGameStatistics(games);
        HibernateFactory factory = new HibernateFactory();
        Session session = factory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        games.forEach(session::save);
        gameStatisticList.forEach(session::save);
        transaction.commit();
    }


    protected List<Game> gatherAllGames() {
        List<Game> games = Lists.newArrayList();
        try {
            BufferedReader br = new BufferedReader(new FileReader(gamesDataFilePath));
            String line;
            while ((line = br.readLine()) != null) {
                //TODO + validation for split (count) etc.
                String[] gameDatas = line.split(",");
                Optional<Game> game = new Game.Builder()
                        .setName(gameDatas[0])
                        .setReleaseYear(gameDatas[1])
                        .setRequiredAge(gameDatas[2])
                        .setHasDemoVersion(gameDatas[3])
                        .setDlcCount(gameDatas[4])
                        .setMetacriticScore(gameDatas[5])
                        .setIsControllerSupported(gameDatas[6])
                        .setRecommendationCount(gameDatas[7])
                        .setAchievementCount(gameDatas[8])
                        .setInitialPrice(gameDatas[9])
                        .setFinalPrice(gameDatas[10])
                        .setOwnerCount(gameDatas[11])
                        .build();
                game.ifPresent(games::add);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //TODO logowanie
        }
        return games;
    }

    protected List<GameStatistic> gatherAllGameStatistics(List<Game> games) {
        List<GameStatistic> gameStatistics = Lists.newArrayList();
        Set<String> notSupported = Sets.newHashSet();
        try {
            BufferedReader br = new BufferedReader(new FileReader(gamesStatisticDataFilePath));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("play")) { //TODO + validation for split (count) etc.
                    String[] steamUserDatas = getPreparedGameStatisticLine(line);
                    long userId = Long.parseLong(steamUserDatas[0]);
                    String gameName = steamUserDatas[1].replaceAll("\"", "");
                    float playedHours = Float.parseFloat(steamUserDatas[3]);
                    Game currentGame = null;
                    for (Game game : games) {
                        if (game.getName().equals(gameName)) {
                            currentGame = game;
                            break;
                        }
                    }
                    if (currentGame != null) {
                        GameStatistic statistic = new GameStatistic(userId, currentGame, playedHours);
                        gameStatistics.add(statistic);
                    } else {
                        logger.warning(gameName + " has no match in recommendation system's data");
                        notSupported.add(gameName);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //TODO logowanie
        }
        return gameStatistics;
    }

    private String[] getPreparedGameStatisticLine(String line) {
        String[] split = line.split("\"");
        if (split.length == 3) {
            if (split[1].contains(",")) { //checker if name has comma in name - prevent corrupting data
                split[1] = split[1].replaceAll(",", "");
                line = String.join("\"", split);
            }
        }
        return line.split(",");
    }
}
