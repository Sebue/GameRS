package pl.sebue.magisterka.grs.controller.recommendation;

import org.hibernate.Session;
import pl.sebue.magisterka.grs.model.HibernateFactory;
import pl.sebue.magisterka.grs.model.data.dto.Game;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/game")
public class GameServlet extends HttpServlet {
    private static final String PARAMETER = "gameId";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String gameId = request.getParameter(PARAMETER);
        long parsedGameId = Long.parseLong(gameId);

        Session hibernateSession = HibernateFactory.INSTANCE.getSessionFactory().openSession();
        Game choosenGame = hibernateSession.createQuery("from Game", Game.class).list().stream()
                .filter(game -> game.getGameId().equals(parsedGameId))
                .findAny()
                .orElseThrow(IllegalStateException::new);

        PrintWriter writer = response.getWriter();
        response.setContentType("text/html");
        writer.println("<h1>GameId: " + gameId + "</h1>");
        writer.println("<h1>Name: " + choosenGame.getName() + "</h1>");
        writer.println("<h2>Required age: " + choosenGame.getRequiredAge() + "</h2>");
        writer.println("<h2>Owner count: " + choosenGame.getOwnerCount() + "</h2>");
        writer.println("<h2>Achievement count: " + choosenGame.getAchievementCount() + "</h2>");
        writer.println("<h2>Recommendation count: " + choosenGame.getRecommendationCount() + "</h2>");
        writer.println("<h2>DLC count: " + choosenGame.getDlcCount() + "</h2>");
        writer.println("<h2>Initial price: " + choosenGame.getInitialPrice() + "</h2>");
        writer.println("<h2>Final price: " + choosenGame.getFinalPrice() + "</h2>");
        writer.println("<h2>Release year: " + choosenGame.getReleaseYear() + "</h2>");
        writer.println("<h2>Metacritic score: " + choosenGame.getMetacriticScore() + "</h2>");
        writer.println("<br><br><a href=\"/GameRecommendationSystem/index.jsp\">Go to main page</a>");
    }
}
