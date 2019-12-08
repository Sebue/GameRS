package pl.sebue.magisterka.grs.controller.phaseone;

import com.google.common.io.Files;
import pl.sebue.magisterka.grs.model.data.loader.CsvReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

@MultipartConfig
@WebServlet("/uploadData")
public class UploadDataServlet extends HttpServlet {

    public static final String GAME_STATISTICS = "gameStatistics";
    public static final String GAMES = "games";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Part filePartGameStatistics = null;
        Part filePartGames = null;
        try{
            filePartGameStatistics = request.getPart(GAME_STATISTICS);
            filePartGames = request.getPart(GAMES);
        } catch (Exception e){
        }
        CsvReader csvReader;
        if (filePartGames != null && filePartGameStatistics != null) {
            csvReader = getCsvReaderFromUser(filePartGameStatistics, filePartGames);
        } else {
            File gamesCsv = new File(getClass().getClassLoader().getResource("games.csv").getFile());
            File gamesStatisticCsv = new File(getClass().getClassLoader().getResource("players.csv").getFile());
            csvReader = new CsvReader(gamesCsv.getPath(), gamesStatisticCsv.getPath());
        }

        csvReader.gatherRecommendationInputData();

        PrintWriter writer = response.getWriter();
        response.setContentType("text/html");
        writer.println("<h2>Data uploaded!</h2><br>");
    }

    private CsvReader getCsvReaderFromUser(Part filePartGameStatistics, Part filePartGames) throws IOException {
        InputStream gamesFile = filePartGames.getInputStream();
        InputStream gameStatisticsFile = filePartGameStatistics.getInputStream();
        byte[] buffer = new byte[gamesFile.available()];
        gamesFile.read(buffer);

        File targetFile = new File("src/main/resources/games.tmp");
        Files.write(buffer, targetFile);

        byte[] bufferGs = new byte[gameStatisticsFile.available()];
        gameStatisticsFile.read(buffer);

        File targetFileGs = new File("src/main/resources/gameStatistics.tmp");
        Files.write(bufferGs, targetFileGs);
        return new CsvReader(targetFile.getPath(), targetFileGs.getPath());
    }
}
