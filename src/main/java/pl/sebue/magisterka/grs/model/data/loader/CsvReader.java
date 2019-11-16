package pl.sebue.magisterka.grs.model.data.loader;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.sebue.magisterka.grs.model.HibernateFactory;
import pl.sebue.magisterka.grs.model.data.dto.Game;
import pl.sebue.magisterka.grs.model.data.dto.GameStatistic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class CsvReader {
    private static final Logger logger = Logger.getLogger(CsvReader.class.getSimpleName());
    private static final String ONLY_LETTERS_AND_NUMBERS_REGEX = "[^A-Z0-9]";
    private static final String DOUBLE_QUOTE_MARK = "\"";
    private static final String COMMA = ",";
    public static final String SIGNIFICANT_LINE_MARK = "play";
    private final String gamesDataFilePath;
    private final String gamesStatisticDataFilePath;

    public CsvReader(String gamesDataFilePath, String gamesStatisticDataFilePath) {
        this.gamesDataFilePath = gamesDataFilePath;
        this.gamesStatisticDataFilePath = gamesStatisticDataFilePath;
    }

    public void gatherRecommendationInputData() {
        List<Game> games = gatherAllGames();
        List<GameStatistic> gameStatisticList = gatherAllGameStatistics(games);

        Session session = HibernateFactory.INSTANCE.getSessionFactory().openSession();
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
                Optional<Game> game = getGame(line);
                game.ifPresent(games::add);
            }
        } catch (Exception e) {
            logger.warning(Throwables.getStackTraceAsString(e));
        }
        return games;
    }

    private Optional<Game> getGame(String line) {
        String[] gameDatas = getPreparedLine(line);
        return new Game.Builder()
                .setComparableName(getPreparedToComparisionGameName(gameDatas[0]))
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
    }

    protected List<GameStatistic> gatherAllGameStatistics(List<Game> games) {
        List<GameStatistic> gameStatistics = Lists.newArrayList();
        try {
            BufferedReader br = new BufferedReader(new FileReader(gamesStatisticDataFilePath));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(SIGNIFICANT_LINE_MARK)) {
                    String[] statisticData = getPreparedLine(line);
                    long userId = Long.parseLong(statisticData[0]);
                    String statisticGameName = statisticData[1];
                    String preparedToComparisionGameName = getPreparedToComparisionGameName(statisticGameName);
                    float playedHours = Float.parseFloat(statisticData[3]);

                    getGame(games, preparedToComparisionGameName)
                            .ifPresentOrElse(game -> gameStatistics.add(new GameStatistic(userId, game, playedHours)),
                                    () -> logger.warning(statisticGameName + " has no match in recommendation system's data"));
                }
            }
        } catch (Exception e) {
            logger.warning(Throwables.getStackTraceAsString(e));
        }
        return gameStatistics;
    }

    private Optional<Game> getGame(List<Game> games, String preparedToComparisionGameName) {
        return games.stream().filter(game -> game.getComparableName().equals(preparedToComparisionGameName)).findAny();
    }

    private String getPreparedToComparisionGameName(String statisticGameName) {
        return statisticGameName.toUpperCase().replaceAll(ONLY_LETTERS_AND_NUMBERS_REGEX, "");
    }

    private String[] getPreparedLine(String line) {
        String[] split = line.split(DOUBLE_QUOTE_MARK);
        if (split.length == 3 && split[1].contains(COMMA)) { //checker if line has comma between double quote mark - prevent corrupting data
            split[1] = split[1].replaceAll(COMMA, "");
            line = String.join("", split);
        }
        return line.split(COMMA);
    }
}
