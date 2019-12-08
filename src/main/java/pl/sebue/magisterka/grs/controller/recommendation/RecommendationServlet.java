package pl.sebue.magisterka.grs.controller.recommendation;

import org.hibernate.Session;
import pl.sebue.magisterka.grs.model.HibernateFactory;
import pl.sebue.magisterka.grs.model.data.dto.Game;
import pl.sebue.magisterka.grs.model.data.dto.GameStatistic;
import pl.sebue.magisterka.grs.model.data.dto.Recommendation;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/recommendation")
public class RecommendationServlet extends HttpServlet {
    private static final String PARAMETER = "userId";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userId = request.getParameter(PARAMETER);
        long parsedUserId = Long.parseLong(userId);

        PrintWriter writer = response.getWriter();
        response.setContentType("text/html");
        writer.println("<body style=\"background-color:#858AE3;color:#32292F\">");
        writer.println("<h2>UserID: " + userId + "</h2><br>");
        writer.println("<h3>Recommendation:</h3><br>");
        writer.println("<table border=\"1\"><tr><th>Game name</th><th>Count in game statistic</th><th>Recommendation type</th></tr>");

        Session hibernateSession = HibernateFactory.INSTANCE.getSessionFactory().openSession();
        List<GameStatistic> gameStatisticList = hibernateSession.createQuery("from GameStatistic", GameStatistic.class).list();
        List<Recommendation> recommendationList = hibernateSession.createQuery("from Recommendation", Recommendation.class).list();
        recommendationList.stream()
                .filter(r -> r.getUserId().equals(parsedUserId))
                .forEach(r -> printEntity(writer, gameStatisticList, r));
        writer.println("</table><br><br>");
        writer.println("<h3>Game statistic for user:</h3><br>");
        writer.println("<table border=\"1\"><tr><th>Game name</th><th>Played hours</th></tr>");
        gameStatisticList.stream()
                .filter(gs -> gs.getUserId().equals(parsedUserId))
                .forEach(gs -> writer.println("<tr><td><a href=\"/GameRecommendationSystem/game?gameId=" + gs.getGame().getGameId() + "\">" + gs.getGame().getName() + "</a></td><td>" + gs.getPlayedHours() + "</td></tr>"));
        writer.println("</table><br><br><a href=\"/GameRecommendationSystem/index.jsp\">Go to main page</a>");
    }

    private void printEntity(PrintWriter writer, List<GameStatistic> gameStatisticList, Recommendation r) {
        Game game = r.getGame();
        long countInGameStatistic = gameStatisticList.stream().filter(gs -> gs.getGame().getGameId().equals(game.getGameId())).count();
        writer.println("<tr><td><a href=\"/GameRecommendationSystem/game?gameId=" + game.getGameId() + "\">" + game.getName() + "</a></td><td>" + countInGameStatistic + "</td><td>" + r.getRecommendationType() + "</td></tr>");
    }
}
